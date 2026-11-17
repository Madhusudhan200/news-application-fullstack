package com.naatvtelugu.news.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class PostStatus {
    APPROVED,
    PENDING,
    REJECTED,
}

data class CustomPost(
    val id: Int = (System.currentTimeMillis() % 100000000).toInt(),
    val title: String,
    val summary: String,
    val imageUrl: String?,
    val link: String = Brand.SITE,
    val date: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH)),
    val timeAgo: String = "ఇప్పుడే",
    val category: String,
    val contentHtml: String = "",
    val authorName: String,
    val authorEmail: String,
    val authorRole: UserRole,
    val status: PostStatus = PostStatus.PENDING,
) {
    fun toNewsItem(): NewsItem = NewsItem(
        id = id,
        title = title,
        summary = summary,
        imageUrl = imageUrl,
        link = link,
        date = date,
        timeAgo = timeAgo,
        category = category,
        contentHtml = if (contentHtml.isBlank()) "<p>$summary</p>" else contentHtml,
    )
}

class CustomPostStore(context: Context) {
    private val prefs = context.getSharedPreferences("custom_posts", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val postListType = object : TypeToken<List<CustomPost>>() {}.type

    private val _posts = MutableStateFlow<List<CustomPost>>(load())
    val posts: StateFlow<List<CustomPost>> = _posts.asStateFlow()

    private fun load(): List<CustomPost> {
        val json = prefs.getString("posts_json", null) ?: return emptyList()
        return try {
            gson.fromJson<List<CustomPost>>(json, postListType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun save(list: List<CustomPost>) {
        _posts.value = list
        prefs.edit().putString("posts_json", gson.toJson(list)).apply()
    }

    fun submitPost(
        title: String,
        summary: String,
        imageUrl: String?,
        category: String,
        contentHtml: String,
        author: AppUser,
    ): CustomPost {
        // Admin posts are approved immediately; Employee posts require admin verification
        val initialStatus = if (author.role == UserRole.ADMIN) PostStatus.APPROVED else PostStatus.PENDING
        val post = CustomPost(
            title = title.trim(),
            summary = summary.trim(),
            imageUrl = imageUrl?.trim()?.ifBlank { null },
            category = category.trim().ifBlank { "వార్తలు" },
            contentHtml = contentHtml.trim(),
            authorName = author.name,
            authorEmail = author.email,
            authorRole = author.role,
            status = initialStatus,
        )
        val updated = listOf(post) + _posts.value
        save(updated)
        return post
    }

    fun approvePost(postId: Int) {
        val updated = _posts.value.map {
            if (it.id == postId) it.copy(status = PostStatus.APPROVED) else it
        }
        save(updated)
    }

    fun rejectPost(postId: Int) {
        val updated = _posts.value.map {
            if (it.id == postId) it.copy(status = PostStatus.REJECTED) else it
        }
        save(updated)
    }

    fun getApprovedNewsItems(categorySlug: String? = null): List<NewsItem> {
        val approved = _posts.value.filter { it.status == PostStatus.APPROVED }
        if (categorySlug == null) {
            return approved.map { it.toNewsItem() }
        }
        // Match category slug to category Telugu or English name
        val targetCat = Categories.list.firstOrNull { it.slug == categorySlug }?.label.orEmpty()
        return approved.filter {
            it.category.equals(targetCat, ignoreCase = true) ||
            it.category.contains(categorySlug, ignoreCase = true)
        }.map { it.toNewsItem() }
    }

    fun getPendingPosts(): List<CustomPost> =
        _posts.value.filter { it.status == PostStatus.PENDING }

    fun getPostsByAuthor(email: String): List<CustomPost> =
        _posts.value.filter { it.authorEmail.equals(email, ignoreCase = true) }
}
