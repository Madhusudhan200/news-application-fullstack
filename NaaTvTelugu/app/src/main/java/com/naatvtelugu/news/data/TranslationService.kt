package com.naatvtelugu.news.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.ConcurrentHashMap

object TranslationService {

    private val cache = ConcurrentHashMap<String, String>()

    suspend fun translate(text: String, targetLang: AppLanguage): String {
        if (text.isBlank()) return text
        if (targetLang == AppLanguage.TELUGU) return text

        val cleaned = text
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#8217;", "'")
            .replace("&#8211;", "-")
            .replace("&#8230;", "…")
            .replace("&nbsp;", " ")
            .trim()

        val cacheKey = "${targetLang.code}:$cleaned"
        cache[cacheKey]?.let { return it }

        return withContext(Dispatchers.IO) {
            try {
                val encoded = URLEncoder.encode(cleaned, "UTF-8")
                val urlStr = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=${targetLang.code}&dt=t&q=$encoded"
                val conn = URL(urlStr).openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.connectTimeout = 4000
                conn.readTimeout = 4000
                conn.setRequestProperty("User-Agent", "Mozilla/5.0")

                if (conn.responseCode == 200) {
                    val raw = conn.inputStream.bufferedReader().use { it.readText() }
                    val jsonArray = JSONArray(raw)
                    val sentences = jsonArray.getJSONArray(0)
                    val sb = StringBuilder()
                    for (i in 0 until sentences.length()) {
                        val part = sentences.getJSONArray(i)
                        sb.append(part.getString(0))
                    }
                    val result = sb.toString().trim()
                    if (result.isNotBlank()) {
                        cache[cacheKey] = result
                        return@withContext result
                    }
                }
            } catch (e: Exception) {
                // Fallback to original text on connection failure
            }
            cleaned
        }
    }

    suspend fun translateNewsItem(item: NewsItem, targetLang: AppLanguage): NewsItem {
        if (targetLang == AppLanguage.TELUGU) return item
        val tTitle = translate(item.title, targetLang)
        val tSummary = translate(item.summary, targetLang)
        val tCategory = translate(item.category, targetLang)
        return item.copy(
            title = tTitle,
            summary = tSummary,
            category = tCategory,
        )
    }
}
