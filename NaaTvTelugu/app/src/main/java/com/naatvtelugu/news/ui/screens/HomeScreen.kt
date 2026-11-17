package com.naatvtelugu.news.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.AccessTime
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.naatvtelugu.news.R
import com.naatvtelugu.news.data.AppStrings
import com.naatvtelugu.news.data.LocationHelper
import com.naatvtelugu.news.data.NewsItem
import com.naatvtelugu.news.ui.NewsViewModel
import kotlin.math.absoluteValue

// Colors matching Reference Design
private val GoldAccent = Color(0xFFE5A93C)
private val BadgeRed = Color(0xFFEF4444)
private val DarkCardBg = Color(0xFF161B22)

/**
 * =====================================================================
 * 1. HOME EXPLORE FEED (Matches Reference Image 2: media_1794832152624_34d83e19.jpg)
 * =====================================================================
 */
@Composable
fun HomeExploreScreen(
    vm: NewsViewModel,
    onNavigateNews: (storyIndex: Int) -> Unit,
    onNavigateMore: () -> Unit,
    onOpenCategories: () -> Unit,
    onOpenArticle: (id: Int) -> Unit,
    onOpenLanguageDialog: () -> Unit,
    onNavigatePostNews: () -> Unit = {},
) {
    val items = vm.filteredItems
    val scrollState = rememberScrollState()
    val adminSettings by vm.adminSettings.collectAsState()
    val context = LocalContext.current
    var showLocationDialog by remember { mutableStateOf(false) }

    if (showLocationDialog) {
        LocationPickerDialog(
            vm = vm,
            onDismiss = { showLocationDialog = false },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- TOP BAR (Matches Image 2) ---
        Surface(
            modifier = Modifier.fillMaxWidth().height(58.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Left: Profile avatar
                IconButton(onClick = onNavigateMore, modifier = Modifier.size(38.dp)) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "Profile",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

                // Center: Official NAA TV Telugu Logo (with Admin customizable height)
                Image(
                    painter = painterResource(if (vm.darkTheme) R.drawable.naa_tv_logo_white else R.drawable.naa_tv_logo),
                    contentDescription = "NAA TV Telugu",
                    modifier = Modifier.height(adminSettings.logoHeightDp.dp),
                )

                // Right: Create / Post news (+) button (Matches Image 2)
                IconButton(onClick = onNavigatePostNews, modifier = Modifier.size(38.dp)) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.2.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Post News",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(19.dp),
                        )
                    }
                }
            }
        }

        // --- SUB-HEADER: Exact GPS Location Banner ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .clickable { showLocationDialog = true }
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Filled.LocationOn,
                contentDescription = null,
                tint = GoldAccent,
                modifier = Modifier.size(15.dp),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = vm.userLocation,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "• GPS",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF10B981),
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "మార్చు (Change) >",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }

        if (items.isEmpty() && vm.loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 24.dp)
            ) {
                Spacer(Modifier.height(10.dp))

                // --- FEATURED CAROUSEL (Hero Cards matching Image 2) ---
                val topStories = items.take(7)
                if (topStories.isNotEmpty()) {
                    val pagerState = rememberPagerState(pageCount = { topStories.size.coerceAtLeast(1) })

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(310.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        pageSpacing = 12.dp,
                    ) { page ->
                        val story = topStories[page]
                        FeaturedHeroCard(
                            item = story,
                            index = page,
                            totalCount = topStories.size,
                            onClick = { onNavigateNews(page) },
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    // Dots Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        repeat(topStories.size.coerceAtMost(7)) { i ->
                            val isSelected = pagerState.currentPage == i
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .height(4.dp)
                                    .width(if (isSelected) 22.dp else 6.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                    )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

                // --- SECTION: NEWS REELS (Matches Image 2) ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                    Text(
                        text = "NEWS REELS",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                }

                Spacer(Modifier.height(14.dp))

                // Horizontal Reels Row
                val reelStories = items.drop(1).take(10)
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(reelStories) { story ->
                        NewsReelCard(
                            item = story,
                            onClick = { onOpenArticle(story.id) },
                        )
                    }
                }

                Spacer(Modifier.height(22.dp))

                // --- SECTION: CATEGORIES QUICK ACCESS ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "కేటగిరీలు (Categories)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    TextButton(onClick = onOpenCategories) {
                        Text("అన్నీ చూడండి >", fontWeight = FontWeight.SemiBold, fontSize = 12.5.sp)
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Quick Category Chips (Never cut off text)
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(vm.categories) { cat ->
                        Surface(
                            onClick = {
                                val idx = vm.categories.indexOf(cat)
                                vm.selectCategory(idx)
                                onNavigateNews(0)
                            },
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                val icon = when (cat.slug) {
                                    null -> Icons.Filled.Newspaper
                                    "sports" -> Icons.Filled.EmojiEvents
                                    "cinema" -> Icons.Filled.Movie
                                    "politics" -> Icons.Filled.AccountBalance
                                    "business" -> Icons.Filled.TrendingUp
                                    "health-fitness" -> Icons.Filled.FitnessCenter
                                    "ayur_vedam" -> Icons.Filled.Spa
                                    else -> Icons.Filled.Newspaper
                                }
                                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = vm.getCategoryLabel(cat.slug),
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Featured Hero Card in Home Explore Feed
 */
@Composable
private fun FeaturedHeroCard(
    item: NewsItem,
    index: Int,
    totalCount: Int,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(Modifier.fillMaxSize()) {
            if (!item.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            // Dark gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x99000000),
                                Color(0x33000000),
                                Color(0xCC000000),
                                Color(0xF5000000),
                            )
                        )
                    )
            )

            // Top Header: "1 న్యూస్ of 7"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${index + 1}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldAccent,
                    )
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (item.category.isNotBlank()) item.category else "న్యూస్",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Text(
                            text = "of $totalCount",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.7f),
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0x55000000)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            // Bottom Content Overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(14.dp),
            ) {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "News / న్యూస్ కొరకు...",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GoldAccent,
                    )
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

/**
 * News Reel Card in Home Explore Feed
 */
@Composable
private fun NewsReelCard(
    item: NewsItem,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(230.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Box(Modifier.fillMaxSize()) {
            if (!item.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x33000000),
                                Color(0x22000000),
                                Color(0xCC000000),
                                Color(0xF5000000),
                            )
                        )
                    )
            )

            // Play badge
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0x66000000))
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp),
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0x88000000))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(10.dp),
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = item.timeAgo.ifBlank { "తాజా" },
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 9.5.sp,
                    )
                }
            }
        }
    }
}

/**
 * =====================================================================
 * 2. NEWS WATCHING SCREEN (Matches Reference Image 4: media_1794832152624_55ae4043.jpg)
 * =====================================================================
 */
@Composable
fun NewsWatchingScreen(
    vm: NewsViewModel,
    initialStoryIndex: Int = 0,
    onGoHome: () -> Unit,
    onOpenCategories: () -> Unit,
    onOpenArticle: (Int) -> Unit,
    onNavigateMore: () -> Unit,
) {
    val saved by vm.saved.collectAsState()
    val displayItems = vm.filteredItems

    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        when {
            displayItems.isEmpty() && vm.loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary)
            }
            displayItems.isEmpty() && vm.error != null -> {
                ErrorBox(vm.error!!, vm::refresh)
            }
            displayItems.isEmpty() -> {
                ErrorBox(
                    if (vm.showOnlyUnread) "అన్ని వార్తలు చదివారు! (No unread stories)" else "ఈ విభాగంలో ఇంకా వార్తలు లేవు.",
                    { vm.showOnlyUnread = false; vm.refresh() }
                )
            }
            else -> {
                Column(Modifier.fillMaxSize()) {
                    Box(Modifier.weight(1f)) {
                        key(vm.selected, vm.searchQuery, vm.showOnlyUnread) {
                            NewsFeedPager(
                                vm = vm,
                                items = displayItems,
                                initialPage = initialStoryIndex.coerceIn(0, (displayItems.size - 1).coerceAtLeast(0)),
                                savedIds = saved.map { it.id }.toSet(),
                                onOpen = onOpenArticle,
                                onNavigateMore = onNavigateMore,
                            )
                        }
                    }

                    // Bottom Bar while watching news (Requested UX)
                    NewsWatchingBottomBar(
                        vm = vm,
                        onGoHome = onGoHome,
                        onOpenCategories = onOpenCategories,
                        onReload = { vm.refresh() },
                    )
                }
            }
        }
    }
}

/**
 * Vertical Pager with 3D depth flip transitions
 */
@Composable
private fun NewsFeedPager(
    vm: NewsViewModel,
    items: List<NewsItem>,
    initialPage: Int,
    savedIds: Set<Int>,
    onOpen: (Int) -> Unit,
    onNavigateMore: () -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { items.size },
    )

    LaunchedEffect(vm.scrollToTopTrigger) {
        vm.scrollToTopTrigger.collect {
            pagerState.animateScrollToPage(0)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        vm.currentStoryPage = pagerState.currentPage
        items.getOrNull(pagerState.currentPage)?.let {
            vm.markStoryRead(it.id)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page >= items.size - 3) vm.loadMore()
        }
    }

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        pageSpacing = 0.dp,
        beyondViewportPageCount = 1,
    ) { page ->
        val item = items[page]
        val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
        val clamped = pageOffset.coerceIn(-1f, 1f)
        val abs = clamped.absoluteValue

        NewsStoryCard(
            vm = vm,
            item = item,
            pageIndex = page,
            totalPages = items.size,
            isSaved = item.id in savedIds,
            isLiked = item.id in vm.likedStories,
            isDisliked = item.id in vm.dislikedStories,
            likesCount = vm.getLikesCount(item.id),
            dislikesCount = vm.getDislikesCount(item.id),
            readFullArticleLabel = AppStrings.readFullArticle(vm.currentLanguage),
            onLike = { vm.toggleLike(item.id) },
            onDislike = { vm.toggleDislike(item.id) },
            onSave = { vm.toggleSave(item) },
            onOpen = { onOpen(item.id) },
            onNavigateMore = onNavigateMore,
            modifier = Modifier.graphicsLayer {
                rotationX = clamped * -48f
                val scale = 1f - (0.15f * abs)
                scaleX = scale
                scaleY = scale
                alpha = 1f - (0.45f * abs)
                cameraDistance = 10f * density
            },
        )
    }
}

/**
 * Story card layout matching Reference Image 4:
 * - Play button ONLY shown when story has a video
 * - Accurate dynamic unread badge and toggle
 * - Admin customized font size and image ratio
 */
@Composable
private fun NewsStoryCard(
    vm: NewsViewModel,
    item: NewsItem,
    pageIndex: Int,
    totalPages: Int,
    isSaved: Boolean,
    isLiked: Boolean,
    isDisliked: Boolean,
    likesCount: Int,
    dislikesCount: Int,
    readFullArticleLabel: String,
    onLike: () -> Unit,
    onDislike: () -> Unit,
    onSave: () -> Unit,
    onOpen: () -> Unit,
    onNavigateMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val adminSettings by vm.adminSettings.collectAsState()
    val imageWeight = (adminSettings.imageRatioPercent / 100f).coerceIn(0.30f, 0.55f)
    val textWeight = 1f - imageWeight
    val hasVideo = vm.isVideoStory(item)
    val unread = vm.unreadCount

    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.fillMaxSize()) {
            // 1. TOP: Featured Image area (Height ratio customizable by Admin)
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(imageWeight)
                    .background(Color.Black),
            ) {
                if (!item.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Box(
                        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "NAA TV TELUGU",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0x99000000), Color.Transparent)
                            )
                        )
                )

                // --- TOP OVERLAY BAR (Matches Image 4 with ACCURATE Unread toggle) ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    // Profile Circle
                    IconButton(onClick = onNavigateMore, modifier = Modifier.size(36.dp)) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x55000000)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }

                    // Segmented Capsule Toggle: "న్యూస్" (All) | "చదవనివి" (Accurate Unread)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0x66000000),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(2.dp),
                        ) {
                            // "న్యూస్" Pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(if (!vm.showOnlyUnread) GoldAccent else Color.Transparent)
                                    .clickable { vm.showOnlyUnread = false }
                                    .padding(horizontal = 12.dp, vertical = 4.dp),
                            ) {
                                Text(
                                    text = "న్యూస్",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp,
                                    color = if (!vm.showOnlyUnread) Color.Black else Color.White,
                                )
                            }

                            Spacer(Modifier.width(2.dp))

                            // "చదవనివి" Pill (With accurate dynamic unread badge!)
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(if (vm.showOnlyUnread) GoldAccent else Color.Transparent)
                                    .clickable { vm.showOnlyUnread = true }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "చదవనివి",
                                    fontSize = 11.5.sp,
                                    color = if (vm.showOnlyUnread) Color.Black else Color.White,
                                    fontWeight = FontWeight.Medium,
                                )
                                Spacer(Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(BadgeRed)
                                        .padding(horizontal = 5.dp, vertical = 1.dp),
                                ) {
                                    Text(
                                        text = "$unread",
                                        fontSize = 9.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }
                    }

                    // Bookmark Button
                    IconButton(onClick = onSave, modifier = Modifier.size(36.dp)) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x55000000)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (isSaved) GoldAccent else Color.White,
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                }

                // Central media play badge: ONLY RENDERED IF THE NEWS IS ACTUALLY A VIDEO!
                if (hasVideo) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0x66000000))
                            .border(1.5.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                            .align(Alignment.Center)
                            .clickable { onOpen() },
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

                // Branded Watermark on bottom-left of image (Clear NAA TV Logo Icon)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xDD000000))
                        .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.naa_tv_logo),
                        contentDescription = "NAA TV Logo",
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "NAA TV",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                // Drag handle pill at bottom-center of image
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 6.dp)
                        .width(36.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.7f))
                )
            }

            // 2. BOTTOM: Story Content area (Styled with Admin Font & Typography customizations)
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(textWeight)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
            ) {
                // Headline (Styled with Admin custom headline size & font family)
                Text(
                    text = item.title,
                    fontSize = adminSettings.headlineFontSize.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = vm.adminSettingsStore.getFontFamily(),
                    lineHeight = (adminSettings.headlineFontSize + 7).sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { onOpen() },
                )

                Spacer(Modifier.height(6.dp))

                // Summary (Styled with Admin custom body size & font family)
                Text(
                    text = item.summary,
                    fontSize = adminSettings.bodyFontSize.sp,
                    fontFamily = vm.adminSettingsStore.getFontFamily(),
                    lineHeight = (adminSettings.bodyFontSize + 7).sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).clickable { onOpen() },
                )

                Spacer(Modifier.height(4.dp))

                // Metadata row (Time ago, location, green progress indicator, Page counter)
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.size(13.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = item.timeAgo.ifBlank { "తాజా" },
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        fontSize = 11.5.sp,
                    )
                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .width(26.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF22C55E)),
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "${pageIndex + 1} of ${totalPages} Pages",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 11.5.sp,
                    )
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    thickness = 0.8.dp,
                    modifier = Modifier.padding(vertical = 4.dp),
                )

                // Action Bar (Like, Dislike, Bookmark, Share, Full Article button)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    // Like button with counter
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onLike() }
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                    ) {
                        Icon(
                            if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                            contentDescription = "Like",
                            tint = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(19.dp),
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "$likesCount",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    // Dislike button with counter
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onDislike() }
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                    ) {
                        Icon(
                            if (isDisliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                            contentDescription = "Dislike",
                            tint = if (isDisliked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(19.dp),
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "$dislikesCount",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    // Bookmark
                    IconButton(onClick = onSave, modifier = Modifier.size(36.dp)) {
                        Icon(
                            if (isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (isSaved) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    // Share (WhatsApp reliable share)
                    IconButton(onClick = { shareStory(context, item) }, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.Outlined.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    // Full Article ("పూర్తి వార్త →")
                    TextButton(onClick = onOpen, contentPadding = PaddingValues(horizontal = 6.dp)) {
                        Text(
                            readFullArticleLabel,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.5.sp,
                        )
                        Spacer(Modifier.width(2.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp),
                        )
                    }
                }
            }
        }
    }
}

/**
 * Bottom Navigation Bar while watching news stories
 */
@Composable
private fun NewsWatchingBottomBar(
    vm: NewsViewModel,
    onGoHome: () -> Unit,
    onOpenCategories: () -> Unit,
    onReload: () -> Unit,
) {
    val selectedCategory = vm.categories.getOrNull(vm.selected)
    val currentLabel = vm.getCategoryLabel(selectedCategory?.slug)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(56.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Left: Home Button (Always returns to Home page!)
            Surface(
                onClick = onGoHome,
                shape = RoundedCornerShape(20.dp),
                color = Color.Transparent,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "హోమ్",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "హోమ్",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            // Center: Category Selector Pill
            Surface(
                onClick = onOpenCategories,
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = currentLabel,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        Icons.Filled.KeyboardArrowUp,
                        contentDescription = "కేటగిరీలు",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            // Right: Reload Button
            Surface(
                onClick = onReload,
                shape = RoundedCornerShape(20.dp),
                color = Color.Transparent,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = "రీలోడ్",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "రీలోడ్",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

/**
 * =====================================================================
 * 3. CATEGORIES BOTTOM SHEET (Matches Reference Image 1)
 * =====================================================================
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesBottomSheet(
    vm: NewsViewModel,
    onDismiss: () -> Unit,
    onCategorySelected: (Int) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    var showLocationDialog by remember { mutableStateOf(false) }

    if (showLocationDialog) {
        LocationPickerDialog(vm = vm, onDismiss = { showLocationDialog = false })
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(bottom = 32.dp),
        ) {
            // Close (X) button at top (Matches Image 1)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .align(Alignment.Center)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            // Search field (Matches Image 1: trailing search icon and placeholder)
            OutlinedTextField(
                value = vm.searchQuery,
                onValueChange = { vm.searchQuery = it },
                placeholder = { Text("సెర్చ్ చేయండి..", fontSize = 13.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                trailingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                ),
            )

            Spacer(Modifier.height(14.dp))

            // --- LOCAL CATEGORIES Header with dividers (Matches Image 1) ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                Text(
                    text = "LOCAL CATEGORIES",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.padding(horizontal = 10.dp),
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            }

            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Card 1: Favourite Locations (Matches Image 1)
                Card(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(10.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Favourite Locations",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                                .clickable { showLocationDialog = true }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                        ) {
                            Icon(
                                Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(Modifier.width(4.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = vm.userLocation.split(",").firstOrNull()?.trim() ?: "Ponnamanda",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = "Ambedkar Konaseema...",
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Edit Location",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp),
                            )
                        }

                        Surface(
                            onClick = { showLocationDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            modifier = Modifier.fillMaxWidth().height(30.dp),
                        ) {
                            Row(
                                Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "+ Add Location",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }

                // Card 2: Local News card (Matches Image 1)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            vm.selectCategory(0)
                            onCategorySelected(0)
                            onDismiss()
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                ) {
                    Box(Modifier.fillMaxSize()) {
                        val localStory = vm.items.getOrNull(1)
                        if (!localStory?.imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = localStory?.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color(0x88000000), Color(0xDD000000))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier.fillMaxSize().padding(10.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("2", fontSize = 20.sp, fontWeight = FontWeight.Black, color = GoldAccent)
                                Spacer(Modifier.width(4.dp))
                                Text(vm.userLocation.split(",").firstOrNull() ?: "పొన్నమండ", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
                            }
                            Column {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = GoldAccent,
                                    modifier = Modifier.padding(bottom = 2.dp),
                                ) {
                                    Text(
                                        "Local News",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                    )
                                }
                                Text("మీ ఊరి వార్తలకోసం... >", fontSize = 10.5.sp, color = GoldAccent, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            // --- NEWS CATEGORIES Header with dividers (Matches Image 1) ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                Text(
                    text = "NEWS CATEGORIES",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.padding(horizontal = 10.dp),
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            }

            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Left Column: Category Items List inside rounded container (Matches Image 1)
                Card(
                    modifier = Modifier.weight(1.1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(6.dp)) {
                        vm.categories.forEachIndexed { index, cat ->
                            val isSelected = index == vm.selected && !vm.showOnlyUnread
                            val icon: ImageVector = when (cat.slug) {
                                null -> Icons.Filled.Newspaper
                                "sports" -> Icons.Filled.EmojiEvents
                                "cinema" -> Icons.Filled.Movie
                                "politics" -> Icons.Filled.AccountBalance
                                "business" -> Icons.Filled.TrendingUp
                                "health-fitness" -> Icons.Filled.FitnessCenter
                                "ayur_vedam" -> Icons.Filled.Spa
                                else -> Icons.Filled.Newspaper
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                        else Color.Transparent
                                    )
                                    .clickable {
                                        vm.showOnlyUnread = false
                                        vm.selectCategory(index)
                                        onCategorySelected(index)
                                        onDismiss()
                                    }
                                    .padding(horizontal = 8.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(17.dp),
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = vm.getCategoryLabel(cat.slug),
                                    fontSize = 12.5.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f),
                                )
                                Icon(
                                    Icons.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(15.dp),
                                )
                            }

                            if (index == 0) {
                                // "చదవనివి" (Unread item with dynamic unread badge!)
                                val isUnreadActive = vm.showOnlyUnread
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isUnreadActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                            else Color.Transparent
                                        )
                                        .clickable {
                                            vm.showOnlyUnread = true
                                            onCategorySelected(0)
                                            onDismiss()
                                        }
                                        .padding(horizontal = 8.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        Icons.Filled.Visibility,
                                        contentDescription = null,
                                        tint = if (isUnreadActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(17.dp),
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = "చదవనివి",
                                        fontSize = 12.5.sp,
                                        lineHeight = 16.sp,
                                        fontWeight = if (isUnreadActive) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isUnreadActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f),
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(BadgeRed)
                                            .padding(horizontal = 5.dp, vertical = 1.dp),
                                    ) {
                                        Text("${vm.unreadCount}", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(Modifier.width(4.dp))
                                    Icon(
                                        Icons.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                        modifier = Modifier.size(15.dp),
                                    )
                                }

                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                    modifier = Modifier.padding(vertical = 4.dp),
                                )
                            }
                        }
                    }
                }

                // Right Column: Featured Breaking News Card (Matches Image 1)
                Card(
                    modifier = Modifier
                        .weight(0.9f)
                        .height(270.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            vm.showOnlyUnread = false
                            vm.selectCategory(0)
                            onCategorySelected(0)
                            onDismiss()
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                ) {
                    Box(Modifier.fillMaxSize()) {
                        val featured = vm.items.firstOrNull()
                        if (!featured?.imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = featured?.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color(0x88000000), Color(0xDD000000))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier.fillMaxSize().padding(10.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("2", fontSize = 20.sp, fontWeight = FontWeight.Black, color = GoldAccent)
                                Spacer(Modifier.width(4.dp))
                                Text("న్యూస్", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(Modifier.width(4.dp))
                                Text("of 8", fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                            Column {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = GoldAccent,
                                    modifier = Modifier.padding(bottom = 4.dp),
                                ) {
                                    Text(
                                        "Breaking News",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    )
                                }
                                Text("News", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("న్యూస్ కొరకు... >", fontSize = 11.sp, color = GoldAccent, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Location Picker Dialog with GPS Auto-detection & Catalog
 */
@Composable
fun LocationPickerDialog(
    vm: NewsViewModel,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    var isDetecting by remember { mutableStateOf(false) }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = GoldAccent)
                Spacer(Modifier.width(8.dp))
                Text("స్థానాన్ని ఎంచుకోండి (Location)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                // GPS auto-detect button
                Surface(
                    onClick = {
                        isDetecting = true
                        vm.detectLocation(context)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(Icons.Filled.MyLocation, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "📍 GPS ద్వారా గుర్తించండి (Detect GPS)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    "ప్రధాన ప్రాంతాలు (AP & Telangana):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(Modifier.height(8.dp))

                LocationHelper.popularLocations.forEach { loc ->
                    val isSelected = vm.userLocation.contains(loc.name.split(" ").first())
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else Color.Transparent)
                            .clickable {
                                vm.updateUserLocation(loc.displayLabel)
                                onDismiss()
                            }
                            .padding(vertical = 8.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = if (isSelected) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                loc.name,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                "${loc.district} • ${loc.state}",
                                fontSize = 10.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("మూసివేయి (Close)", fontWeight = FontWeight.Bold)
            }
        },
    )
}

@Composable
private fun ErrorBox(message: String, onRetry: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onRetry) {
            Icon(Icons.Filled.Refresh, contentDescription = null, Modifier.size(16.dp))
            Spacer(Modifier.width(8.dp))
            Text("మళ్ళీ ప్రయత్నించండి (Retry)", fontWeight = FontWeight.Bold)
        }
    }
}
