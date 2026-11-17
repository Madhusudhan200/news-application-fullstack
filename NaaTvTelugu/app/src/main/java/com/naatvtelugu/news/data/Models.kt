package com.naatvtelugu.news.data

import com.google.gson.annotations.SerializedName

/* ---------- Raw WordPress REST API models (naatvtelugu.com/wp-json/wp/v2) ---------- */

data class Rendered(val rendered: String? = null)

data class WpPost(
    val id: Int = 0,
    val date: String? = null,
    val link: String? = null,
    val title: Rendered? = null,
    val excerpt: Rendered? = null,
    val content: Rendered? = null,
    @SerializedName("_embedded") val embedded: WpEmbedded? = null,
)

data class WpEmbedded(
    @SerializedName("wp:featuredmedia") val media: List<WpMedia>? = null,
    @SerializedName("wp:term") val terms: List<List<WpTerm>>? = null,
)

data class WpMedia(@SerializedName("source_url") val sourceUrl: String? = null)

data class WpTerm(val id: Int = 0, val name: String? = null, val taxonomy: String? = null)

data class WpCategory(val id: Int = 0, val name: String? = null, val slug: String? = null)

/* ---------- App model ---------- */

data class NewsItem(
    val id: Int,
    val title: String,
    val summary: String,
    val imageUrl: String?,
    val link: String,
    val date: String,
    val timeAgo: String = "",
    val category: String,
    val contentHtml: String,
)

/** Website menu categories (slugs match naatvtelugu.com URLs). */
data class NewsCategory(val label: String, val slug: String?)

object Categories {
    val list = listOf(
        NewsCategory("తాజా వార్తలు", null),             // Latest / All News (replaced "మీకోసం")
        NewsCategory("వార్తలు", "news"),               // News
        NewsCategory("క్రీడలు", "sports"),             // Sports
        NewsCategory("సినిమా", "cinema"),              // Cinema
        NewsCategory("రాజకీయాలు", "politics"),         // Politics
        NewsCategory("వ్యాపారం", "business"),          // Business
        NewsCategory("ఆరోగ్యం", "health-fitness"),     // Health & Fitness
        NewsCategory("ఆయుర్వేదం", "ayur_vedam"),       // Ayur Vedam
    )
}

object Brand {
    const val SITE = "https://naatvtelugu.com/"
    const val LOGO = "https://naatvtelugu.com/wp-content/uploads/2025/07/naatv_telugu_headder_logo.webp"
    const val LOGO_FOOTER = "https://naatvtelugu.com/wp-content/uploads/2025/07/naatv_telugu_footer_logo.webp"
    const val YOUTUBE = "https://www.youtube.com/@naatvtelugu"
    const val FACEBOOK = "https://www.facebook.com/naatvteluguofficial"
    const val INSTAGRAM = "https://www.instagram.com/naatvteluguoffl/"
    const val X = "https://x.com/NaatvTeluguoffl"
    const val CONTACT = "https://naatvtelugu.com/contact-us/"
}
