package com.naatvtelugu.news.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.FileProvider
import com.naatvtelugu.news.data.Brand
import com.naatvtelugu.news.data.NewsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Reliable story sharing:
 * - Shares bold heading + website link + official app link (omits full story body)
 * - Attempts to attach story image if available; gracefully falls back to text/plain if network is slow/offline
 * - Always opens the share dialog without crashing
 */
fun shareStory(context: Context, item: NewsItem) {
    val heading = item.title
    val websiteLink = if (item.link.isNotBlank()) item.link else Brand.SITE
    val appLink = Brand.SITE

    val shareText = buildString {
        append(heading)
        append("\n\n")
        append("🌐 పూర్తి వార్త వెబ్‌సైట్: ")
        append(websiteLink)
        append("\n")
        append("📲 NAA TV Telugu యాప్: ")
        append(appLink)
    }

    CoroutineScope(Dispatchers.IO).launch {
        var imageUri: Uri? = null
        val imageUrl = item.imageUrl

        if (!imageUrl.isNullOrBlank()) {
            try {
                val shareDir = File(context.cacheDir, "share").apply { mkdirs() }
                val imageFile = File(shareDir, "story_${item.id}.jpg")
                if (!imageFile.exists() || imageFile.length() == 0L) {
                    val client = OkHttpClient.Builder()
                        .connectTimeout(2, TimeUnit.SECONDS)
                        .readTimeout(3, TimeUnit.SECONDS)
                        .build()
                    val req = Request.Builder().url(imageUrl).build()
                    client.newCall(req).execute().use { resp ->
                        if (resp.isSuccessful) {
                            resp.body?.bytes()?.let { bytes ->
                                imageFile.writeBytes(bytes)
                            }
                        }
                    }
                }
                if (imageFile.exists() && imageFile.length() > 0) {
                    imageUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        imageFile
                    )
                }
            } catch (e: Exception) {
                // Fall back gracefully to text share
                e.printStackTrace()
            }
        }

        withContext(Dispatchers.Main) {
            try {
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    if (imageUri != null) {
                        type = "image/*"
                        putExtra(Intent.EXTRA_STREAM, imageUri)
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    } else {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    putExtra(Intent.EXTRA_SUBJECT, heading)
                }

                val chooser = Intent.createChooser(sendIntent, "షేర్ చేయండి (Share Story)").apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(chooser)
            } catch (e: Exception) {
                Toast.makeText(context, "షేరింగ్ విండో తెరవబడలేదు: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

/** Story image, or a branded block when the post has no featured image. */
@Composable
fun StoryImage(url: String?, modifier: Modifier = Modifier) {
    if (url.isNullOrBlank()) {
        Box(modifier.background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
            Text("NAA TV", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black)
        }
    } else {
        coil.compose.AsyncImage(
            model = url,
            contentDescription = null,
            modifier = modifier,
            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
        )
    }
}
