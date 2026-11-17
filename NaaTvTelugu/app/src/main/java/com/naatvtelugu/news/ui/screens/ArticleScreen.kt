package com.naatvtelugu.news.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.naatvtelugu.news.data.Brand
import com.naatvtelugu.news.data.NewsItem
import com.naatvtelugu.news.ui.NewsViewModel

/**
 * Native Jetpack Compose Full Story Reader:
 * - Loads instantly with zero delay
 * - Hero image with category badge & watermark
 * - Large bold Telugu headline styled with Admin font size
 * - Crisp native body paragraphs with comfortable line spacing
 * - Quick action bar: Like, Dislike, Bookmark, WhatsApp Share, Web Link
 * - Related Stories section
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    vm: NewsViewModel,
    id: Int,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val saved by vm.saved.collectAsState()
    val adminSettings by vm.adminSettings.collectAsState()
    val item by produceState(initialValue = vm.find(id), id) {
        if (value == null || value?.contentHtml.isNullOrBlank()) {
            value = vm.fetch(id) ?: value
        }
    }
    val isSaved = saved.any { it.id == id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = item?.category?.ifBlank { "వార్తలు" } ?: "పూర్తి వార్త",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    item?.let { story ->
                        IconButton(onClick = { vm.toggleSave(story) }) {
                            Icon(
                                if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                                contentDescription = "Save",
                                tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        IconButton(onClick = { shareStory(context, story) }) {
                            Icon(Icons.Outlined.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        IconButton(onClick = {
                            val link = if (story.link.isNotBlank()) story.link else Brand.SITE
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                        }) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = "Browser", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
            )
        },
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            val story = item
            if (story == null) {
                CircularProgressIndicator(Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary)
            } else {
                val scrollState = rememberScrollState()
                val isLiked = story.id in vm.likedStories
                val isDisliked = story.id in vm.dislikedStories
                val hasVideo = vm.isVideoStory(story)

                // Clean plain text paragraphs from HTML
                val cleanBody = if (story.contentHtml.isNotBlank()) {
                    HtmlCompat.fromHtml(story.contentHtml, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
                } else {
                    story.summary
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(bottom = 32.dp),
                ) {
                    // 1. HERO IMAGE
                    if (!story.imageUrl.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .background(Color.Black),
                        ) {
                            AsyncImage(
                                model = story.imageUrl,
                                contentDescription = story.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )

                            // Show play button ONLY if the story has a video
                            if (hasVideo) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x77000000))
                                        .align(Alignment.Center),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        Icons.Filled.PlayArrow,
                                        contentDescription = "Play Video",
                                        tint = Color.White,
                                        modifier = Modifier.size(34.dp),
                                    )
                                }
                            }

                            // Watermark pill
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0x99000000))
                                    .padding(horizontal = 8.dp, vertical = 3.dp),
                            ) {
                                Text("naatvtelugu", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // 2. ARTICLE HEADER & METADATA
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        // Category pill
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        ) {
                            Text(
                                text = story.category.ifBlank { "వార్తలు" }.uppercase(),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        // Full Headline (Styled with Admin custom headline font size)
                        Text(
                            text = story.title,
                            fontSize = adminSettings.headlineFontSize.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = vm.adminSettingsStore.getFontFamily(),
                            lineHeight = (adminSettings.headlineFontSize + 8).sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        Spacer(Modifier.height(10.dp))

                        // Published Date Row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.CalendarToday,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.size(13.dp),
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "${story.date} • ${story.timeAgo.ifBlank { "ఇప్పుడే" }}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // Interactive Action Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            // Like
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { vm.toggleLike(story.id) },
                            ) {
                                Icon(
                                    if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                    contentDescription = null,
                                    tint = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp),
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    "${vm.getLikesCount(story.id)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }

                            // Dislike
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { vm.toggleDislike(story.id) },
                            ) {
                                Icon(
                                    if (isDisliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                                    contentDescription = null,
                                    tint = if (isDisliked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp),
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    "${vm.getDislikesCount(story.id)}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }

                            // Bookmark
                            IconButton(onClick = { vm.toggleSave(story) }, modifier = Modifier.size(32.dp)) {
                                Icon(
                                    if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = null,
                                    tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(19.dp),
                                )
                            }

                            // Share on WhatsApp
                            IconButton(onClick = { shareStory(context, story) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Outlined.Share, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(19.dp))
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Full Article Body Paragraphs (Styled with Admin custom body font size)
                        cleanBody.split("\n\n").forEach { paragraph ->
                            val trimmed = paragraph.trim()
                            if (trimmed.isNotBlank()) {
                                Text(
                                    text = trimmed,
                                    fontSize = adminSettings.bodyFontSize.sp,
                                    lineHeight = (adminSettings.bodyFontSize + 9).sp,
                                    fontFamily = vm.adminSettingsStore.getFontFamily(),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                )
                                Spacer(Modifier.height(12.dp))
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                        Spacer(Modifier.height(16.dp))

                        // 3. RELATED STORIES SECTION
                        Text(
                            text = "సంబంధిత వార్తలు (Related Stories)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        Spacer(Modifier.height(12.dp))

                        val related = vm.items.filter { it.id != story.id }.take(5)
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(related) { rel ->
                                Card(
                                    modifier = Modifier
                                        .width(180.dp)
                                        .clickable {
                                            // Open the tapped article
                                            onBack()
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                ) {
                                    Column(Modifier.padding(8.dp)) {
                                        if (!rel.imageUrl.isNullOrBlank()) {
                                            AsyncImage(
                                                model = rel.imageUrl,
                                                contentDescription = rel.title,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(95.dp)
                                                    .clip(RoundedCornerShape(8.dp)),
                                            )
                                            Spacer(Modifier.height(6.dp))
                                        }
                                        Text(
                                            text = rel.title,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
