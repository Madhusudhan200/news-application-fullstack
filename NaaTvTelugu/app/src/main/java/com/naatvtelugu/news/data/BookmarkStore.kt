package com.naatvtelugu.news.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Saves bookmarked stories on the phone so they can be read offline. */
class BookmarkStore(context: Context) {
    private val prefs = context.getSharedPreferences("bookmarks", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val type = object : TypeToken<List<NewsItem>>() {}.type

    private val _items = MutableStateFlow(load())
    val items: StateFlow<List<NewsItem>> = _items.asStateFlow()

    private fun load(): List<NewsItem> = try {
        gson.fromJson<List<NewsItem>>(prefs.getString("items", "[]"), type) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }

    fun toggle(item: NewsItem) {
        val current = _items.value
        val updated = if (current.any { it.id == item.id }) current.filterNot { it.id == item.id }
        else listOf(item) + current
        _items.value = updated
        prefs.edit().putString("items", gson.toJson(updated)).apply()
    }
}
