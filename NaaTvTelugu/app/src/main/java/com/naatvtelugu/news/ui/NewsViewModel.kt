package com.naatvtelugu.news.ui

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.naatvtelugu.news.data.AdminSettings
import com.naatvtelugu.news.data.AdminSettingsStore
import com.naatvtelugu.news.data.AppLanguage
import com.naatvtelugu.news.data.AppStrings
import com.naatvtelugu.news.data.AppUser
import com.naatvtelugu.news.data.AuthStore
import com.naatvtelugu.news.data.BookmarkStore
import com.naatvtelugu.news.data.Categories
import com.naatvtelugu.news.data.CustomPost
import com.naatvtelugu.news.data.CustomPostStore
import com.naatvtelugu.news.data.EditedStory
import com.naatvtelugu.news.data.LocationHelper
import com.naatvtelugu.news.data.NewsItem
import com.naatvtelugu.news.data.NewsRepository
import com.naatvtelugu.news.data.PostStatus
import com.naatvtelugu.news.data.TranslationService
import com.naatvtelugu.news.data.UserRole
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class EmployeeStats(
    val totalPosted: Int,
    val approved: Int,
    val pending: Int,
)

class NewsViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = NewsRepository()
    private val store = BookmarkStore(app)
    private val authStore = AuthStore(app)
    private val postStore = CustomPostStore(app)
    val adminSettingsStore = AdminSettingsStore(app)
    private val settings = app.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val intListType = object : TypeToken<List<Int>>() {}.type

    val categories = Categories.list
    val saved: StateFlow<List<NewsItem>> = store.items
    val currentUser: StateFlow<AppUser?> = authStore.currentUser
    val allUsers: StateFlow<List<AppUser>> = authStore.allUsers
    val customPosts: StateFlow<List<CustomPost>> = postStore.posts
    val adminSettings: StateFlow<AdminSettings> = adminSettingsStore.settings
    val editedStories: StateFlow<Map<Int, EditedStory>> = adminSettingsStore.editedStories

    // Location
    var userLocation by mutableStateOf(
        settings.getString("user_location", "పొన్నమండ, కోనసీమ") ?: "పొన్నమండ, కోనసీమ"
    )

    fun updateUserLocation(loc: String) {
        userLocation = loc
        settings.edit().putString("user_location", loc).apply()
    }

    fun detectLocation(context: Context) {
        viewModelScope.launch {
            val loc = LocationHelper.detectGpsLocation(context)
            if (loc != null) {
                updateUserLocation(loc.displayLabel)
            }
        }
    }

    // Unread ("చదవనివి") stories tracking
    val readStoryIds = mutableStateListOf<Int>()
    var showOnlyUnread by mutableStateOf(false)

    val unreadCount: Int
        get() = items.count { it.id !in readStoryIds }

    fun markStoryRead(id: Int) {
        if (id !in readStoryIds) {
            readStoryIds.add(id)
            settings.edit().putString("read_stories_json", gson.toJson(readStoryIds.toList())).apply()
        }
    }

    var selected by mutableIntStateOf(0); private set
    private val rawItems = mutableStateListOf<NewsItem>()
    val items = mutableStateListOf<NewsItem>()

    private val _scrollToTopTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val scrollToTopTrigger: SharedFlow<Unit> = _scrollToTopTrigger.asSharedFlow()

    var currentStoryPage by mutableIntStateOf(0)

    fun scrollToHomeStory() {
        if (selected != 0) {
            selectCategory(0)
        }
        _scrollToTopTrigger.tryEmit(Unit)
    }

    private var translationJob: Job? = null
    var loading by mutableStateOf(false); private set
    var error by mutableStateOf<String?>(null); private set
    var darkTheme by mutableStateOf(settings.getBoolean("dark", false)); private set
    var searchQuery by mutableStateOf("")

    var currentLanguage by mutableStateOf(
        AppLanguage.fromCode(settings.getString("language", AppLanguage.TELUGU.code) ?: AppLanguage.TELUGU.code)
    ); private set

    fun setLanguage(lang: AppLanguage) {
        if (currentLanguage == lang) return
        currentLanguage = lang
        settings.edit().putString("language", lang.code).apply()
        applyTranslation()
    }

    private fun applyTranslation() {
        val lang = currentLanguage
        translationJob?.cancel()
        if (lang == AppLanguage.TELUGU) {
            items.clear()
            items.addAll(rawItems)
            return
        }

        translationJob = viewModelScope.launch(Dispatchers.IO) {
            val listCopy = rawItems.toList()
            val translatedList = listCopy.map { raw ->
                async { TranslationService.translateNewsItem(raw, lang) }
            }.awaitAll()

            if (currentLanguage == lang) {
                withContext(Dispatchers.Main) {
                    items.clear()
                    items.addAll(translatedList)
                }
            }
        }
    }

    fun getCategoryLabel(slug: String?): String = when (slug) {
        null -> AppStrings.forYou(currentLanguage)
        "news" -> AppStrings.news(currentLanguage)
        "sports" -> AppStrings.sports(currentLanguage)
        "cinema" -> AppStrings.cinema(currentLanguage)
        "politics" -> AppStrings.politics(currentLanguage)
        "business" -> AppStrings.business(currentLanguage)
        "health-fitness" -> AppStrings.health(currentLanguage)
        "ayur_vedam" -> AppStrings.ayurveda(currentLanguage)
        else -> slug
    }

    val likedStories = mutableStateListOf<Int>()
    val dislikedStories = mutableStateListOf<Int>()

    val filteredItems: List<NewsItem>
        get() {
            val currentEdits = editedStories.value
            // Apply admin edits if any
            val mapped = items.map { item ->
                val edit = currentEdits[item.id]
                if (edit != null) {
                    item.copy(
                        title = edit.title,
                        summary = edit.summary,
                        category = edit.category,
                        contentHtml = edit.contentHtml.ifBlank { item.contentHtml },
                    )
                } else item
            }

            // Apply unread filter if active
            val base = if (showOnlyUnread) mapped.filter { it.id !in readStoryIds } else mapped

            val q = searchQuery.trim()
            if (q.isBlank()) return base
            return base.filter {
                it.title.contains(q, ignoreCase = true) ||
                it.summary.contains(q, ignoreCase = true) ||
                it.category.contains(q, ignoreCase = true)
            }
        }

    fun isVideoStory(item: NewsItem): Boolean {
        return item.contentHtml.contains("youtube.com", ignoreCase = true) ||
               item.contentHtml.contains("youtu.be", ignoreCase = true) ||
               item.contentHtml.contains("<video", ignoreCase = true) ||
               item.contentHtml.contains(".mp4", ignoreCase = true) ||
               item.category.contains("వీడియో", ignoreCase = true) ||
               item.category.contains("రీల్స్", ignoreCase = true) ||
               item.category.contains("video", ignoreCase = true) ||
               item.title.contains("వీడియో", ignoreCase = true) ||
               item.title.contains("Video", ignoreCase = true)
    }

    fun toggleLike(id: Int) {
        if (id in likedStories) {
            likedStories.remove(id)
        } else {
            likedStories.add(id)
            dislikedStories.remove(id)
        }
    }

    fun toggleDislike(id: Int) {
        if (id in dislikedStories) {
            dislikedStories.remove(id)
        } else {
            dislikedStories.add(id)
            likedStories.remove(id)
        }
    }

    fun getLikesCount(id: Int): Int {
        val base = (id * 17) % 350 + 50
        return if (id in likedStories) base + 1 else base
    }

    fun getDislikesCount(id: Int): Int {
        val base = (id * 3) % 25 + 2
        return if (id in dislikedStories) base + 1 else base
    }

    private var page = 1
    private var endReached = false
    private var job: Job? = null
    private var requestId = 0

    init {
        // Load read story IDs
        val readJson = settings.getString("read_stories_json", null)
        if (!readJson.isNullOrBlank()) {
            try {
                val list = gson.fromJson<List<Int>>(readJson, intListType)
                if (list != null) readStoryIds.addAll(list)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        refresh()
    }

    fun selectCategory(index: Int) {
        if (index == selected && items.isNotEmpty()) return
        selected = index
        showOnlyUnread = false
        refresh()
    }

    fun refresh() {
        job?.cancel()
        translationJob?.cancel()
        rawItems.clear()
        items.clear()
        page = 1
        endReached = false
        error = null
        loading = false
        loadMore()
    }

    fun loadMore() {
        if (loading || endReached) return
        val myId = ++requestId
        val slug = categories[selected].slug
        val lang = currentLanguage
        loading = true
        job = viewModelScope.launch {
            try {
                val newIncoming = mutableListOf<NewsItem>()

                // On first page, prepend local approved custom posts
                if (page == 1) {
                    val approvedCustom = postStore.getApprovedNewsItems(slug)
                    newIncoming.addAll(approvedCustom)
                }

                // Fetch remote posts
                val remote = repo.latest(slug, page)
                if (remote.isEmpty() && newIncoming.isEmpty()) {
                    endReached = true
                } else {
                    newIncoming.addAll(remote)
                    page++
                }

                if (myId != requestId) return@launch

                val filtered = newIncoming.filter { incoming ->
                    rawItems.none { it.id == incoming.id }
                }

                rawItems.addAll(filtered)

                if (lang == AppLanguage.TELUGU) {
                    items.addAll(filtered)
                } else {
                    val translated = filtered.map { raw ->
                        async(Dispatchers.IO) { TranslationService.translateNewsItem(raw, lang) }
                    }.awaitAll()
                    if (myId == requestId) {
                        items.addAll(translated)
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (rawItems.isEmpty()) {
                    error = "వార్తలను లోడ్ చేయడంలో సమస్య వచ్చింది. దయచేసి ఇంటర్నెట్ సరిచూసుకోండి."
                }
            } finally {
                loading = false
            }
        }
    }

    fun toggleTheme() {
        darkTheme = !darkTheme
        settings.edit().putBoolean("dark", darkTheme).apply()
    }

    fun toggleSave(item: NewsItem) {
        viewModelScope.launch { store.toggle(item) }
    }

    fun find(id: Int): NewsItem? =
        items.firstOrNull { it.id == id } ?: saved.value.firstOrNull { it.id == id }

    suspend fun fetch(id: Int): NewsItem? = try {
        find(id) ?: repo.post(id)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        null
    }

    /* ---------- Admin Customizations ---------- */

    fun updateAdminSettings(newSettings: AdminSettings) {
        adminSettingsStore.updateSettings(newSettings)
    }

    fun editStoryText(id: Int, title: String, summary: String, category: String, contentHtml: String = "") {
        adminSettingsStore.saveEditedStory(
            EditedStory(
                id = id,
                title = title.trim(),
                summary = summary.trim(),
                category = category.trim(),
                contentHtml = contentHtml.trim(),
            )
        )
    }

    fun updateEmployeeWorkStatus(email: String, status: String) {
        authStore.updateWorkStatus(email, status)
    }

    fun updateUserRole(email: String, role: UserRole) {
        authStore.updateUserRole(email, role)
    }

    fun getEmployeeStats(email: String): EmployeeStats {
        val authorPosts = customPosts.value.filter { it.authorEmail.equals(email, ignoreCase = true) }
        return EmployeeStats(
            totalPosted = authorPosts.size,
            approved = authorPosts.count { it.status == PostStatus.APPROVED },
            pending = authorPosts.count { it.status == PostStatus.PENDING },
        )
    }

    /* ---------- Authentication ---------- */

    fun login(email: String, pass: String): Result<AppUser> {
        val result = authStore.login(email, pass)
        if (result.isSuccess) refresh()
        return result
    }

    fun signUp(name: String, email: String, pass: String): Result<AppUser> {
        val result = authStore.signUp(name, email, pass)
        if (result.isSuccess) refresh()
        return result
    }

    fun loginAsGuest(): AppUser {
        val user = authStore.loginAsGuest()
        refresh()
        return user
    }

    fun logout() {
        authStore.logout()
    }

    fun addEmployee(name: String, email: String, pass: String): Result<AppUser> =
        authStore.addEmployee(name, email, pass)

    fun getEmployees(): List<AppUser> = authStore.getEmployees()

    /* ---------- News Creation & Verification ---------- */

    fun submitNews(
        title: String,
        summary: String,
        imageUrl: String?,
        category: String,
        contentHtml: String,
    ): Result<CustomPost> {
        val user = currentUser.value ?: return Result.failure(Exception("వార్త పోస్ట్ చేయడానికి లాగిన్ అవ్వండి"))
        if (user.role != UserRole.ADMIN && user.role != UserRole.EMPLOYEE) {
            return Result.failure(Exception("కేవలం అడ్మిన్ లేదా ఉద్యోగులు మాత్రమే వార్తలు పోస్ట్ చేయగలరు"))
        }

        val requireApproval = adminSettings.value.employeeRequireApproval
        val autoApprove = (user.role == UserRole.ADMIN) || (!requireApproval)

        val post = postStore.submitPost(title, summary, imageUrl, category, contentHtml, user)
        if (autoApprove && post.status == PostStatus.PENDING) {
            postStore.approvePost(post.id)
        }
        if (autoApprove) {
            refresh()
        }
        return Result.success(post)
    }

    fun approveNews(postId: Int) {
        postStore.approvePost(postId)
        refresh()
    }

    fun rejectNews(postId: Int) {
        postStore.rejectPost(postId)
    }

    fun getPendingPosts(): List<CustomPost> = postStore.getPendingPosts()

    fun getMyPosts(): List<CustomPost> {
        val email = currentUser.value?.email ?: return emptyList()
        return postStore.getPostsByAuthor(email)
    }
}
