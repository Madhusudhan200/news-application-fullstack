package com.naatvtelugu.news.data

import androidx.core.text.HtmlCompat
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

interface WpApi {
    @GET("wp-json/wp/v2/posts")
    suspend fun posts(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 15,
        @Query("categories") categories: Int? = null,
        @Query("_embed") embed: Int = 1,
    ): List<WpPost>

    @GET("wp-json/wp/v2/posts/{id}")
    suspend fun post(@Path("id") id: Int, @Query("_embed") embed: Int = 1): WpPost

    @GET("wp-json/wp/v2/categories")
    suspend fun categoriesBySlug(@Query("slug") slug: String): List<WpCategory>
}

class NewsRepository {

    private val api: WpApi = Retrofit.Builder()
        .baseUrl(Brand.SITE)
        .client(
            OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    chain.proceed(
                        chain.request().newBuilder()
                            .header("User-Agent", "NaaTvTeluguAndroid/1.0")
                            .build()
                    )
                }
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WpApi::class.java)

    private val categoryIds = mutableMapOf<String, Int>()

    suspend fun latest(slug: String?, page: Int): List<NewsItem> {
        val catId = slug?.let { categoryId(it) ?: return emptyList() }
        return api.posts(page = page, categories = catId).map { it.toItem() }
    }

    suspend fun post(id: Int): NewsItem = api.post(id).toItem()

    private suspend fun categoryId(slug: String): Int? =
        categoryIds[slug] ?: api.categoriesBySlug(slug).firstOrNull()?.id?.also { categoryIds[slug] = it }
}

/* ---------- mapping helpers ---------- */

private fun String?.stripHtml(): String =
    if (this.isNullOrBlank()) "" else HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
        .toString().replace(' ', ' ').trim()

/** Short summary: about 60 words. */
private fun shortSummary(text: String, words: Int = 60): String {
    val clean = text.replace(Regex("\\[(…|&hellip;|\\.\\.\\.)\\]"), "")
        .replace(Regex("(?i)read more.*$"), "")
        .replace(Regex("\\s+"), " ").trim()
    val parts = clean.split(" ")
    return if (parts.size <= words) clean else parts.take(words).joinToString(" ") + "…"
}

private val outFmt = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a", Locale.ENGLISH)

private fun prettyDate(raw: String?): String = try {
    LocalDateTime.parse(raw).format(outFmt)
} catch (e: Exception) {
    raw.orEmpty()
}

/** Relative time: e.g. "4m ago", "2h ago", "1d ago" */
private fun formatTimeAgo(raw: String?): String {
    if (raw.isNullOrBlank()) return "ఇటీవల"
    return try {
        val postTime = LocalDateTime.parse(raw)
        val now = LocalDateTime.now()
        val duration = java.time.Duration.between(postTime, now)
        val minutes = duration.toMinutes().coerceAtLeast(1)
        val hours = duration.toHours()
        val days = duration.toDays()
        when {
            minutes < 60 -> "${minutes}m ago"
            hours < 24 -> "${hours}h ago"
            days < 7 -> "${days}d ago"
            else -> postTime.format(DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH))
        }
    } catch (e: Exception) {
        "ఇటీవల"
    }
}

fun WpPost.toItem(): NewsItem {
    val contentText = content?.rendered.stripHtml()
    val excerptText = excerpt?.rendered.stripHtml().ifBlank { contentText }
    return NewsItem(
        id = id,
        title = title?.rendered.stripHtml(),
        summary = shortSummary(excerptText),
        imageUrl = embedded?.media?.firstOrNull()?.sourceUrl,
        link = link ?: Brand.SITE,
        date = prettyDate(date),
        timeAgo = formatTimeAgo(date),
        category = embedded?.terms?.flatten()?.firstOrNull { it.taxonomy == "category" }?.name.stripHtml(),
        contentHtml = content?.rendered.orEmpty(),
    )
}
