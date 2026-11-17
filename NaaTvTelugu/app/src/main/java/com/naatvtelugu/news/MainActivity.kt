package com.naatvtelugu.news

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.naatvtelugu.news.data.AppStrings
import com.naatvtelugu.news.data.Brand
import com.naatvtelugu.news.data.UserRole
import com.naatvtelugu.news.ui.NewsViewModel
import com.naatvtelugu.news.ui.screens.AdminPanelScreen
import com.naatvtelugu.news.ui.screens.ArticleScreen
import com.naatvtelugu.news.ui.screens.AuthScreen
import com.naatvtelugu.news.ui.screens.CategoriesBottomSheet
import com.naatvtelugu.news.ui.screens.EmployeePostScreen
import com.naatvtelugu.news.ui.screens.HomeExploreScreen
import com.naatvtelugu.news.ui.screens.LanguageSelectionDialog
import com.naatvtelugu.news.ui.screens.LocationPickerDialog
import com.naatvtelugu.news.ui.screens.MoreScreen
import com.naatvtelugu.news.ui.screens.NewsWatchingScreen
import com.naatvtelugu.news.ui.screens.SavedScreen
import com.naatvtelugu.news.ui.screens.SplashScreen
import com.naatvtelugu.news.ui.theme.DarkScreenBg
import com.naatvtelugu.news.ui.theme.LightScreenBg
import com.naatvtelugu.news.ui.theme.NaaTvTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: NewsViewModel = viewModel()
            val dark = vm.darkTheme
            val currentUser by vm.currentUser.collectAsState()
            var showSplash by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                delay(2000)
                showSplash = false
            }

            LaunchedEffect(dark, showSplash) {
                enableEdgeToEdge(
                    statusBarStyle = if (showSplash || dark) SystemBarStyle.dark(DarkScreenBg.toArgb())
                    else SystemBarStyle.light(LightScreenBg.toArgb(), Color.Black.toArgb()),
                )
            }
            NaaTvTheme(dark = dark) {
                Crossfade(
                    targetState = showSplash,
                    animationSpec = tween(400),
                    label = "splash_crossfade",
                ) { isSplash ->
                    if (isSplash) {
                        SplashScreen(onTimeout = { showSplash = false })
                    } else if (currentUser == null) {
                        AuthScreen(vm = vm, onSuccess = {})
                    } else {
                        AppRoot(vm)
                    }
                }
            }
        }
    }
}

private data class MainTabItem(
    val route: String,
    val icon: ImageVector,
    val isCenter: Boolean = false,
)

private val tabs = listOf(
    MainTabItem("home", Icons.Filled.Home),
    MainTabItem("news", Icons.Filled.Newspaper),
    MainTabItem("categories", Icons.Filled.GridView, isCenter = true),
    MainTabItem("reels", Icons.Filled.PlayCircle),
    MainTabItem("location", Icons.Filled.LocationOn),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppRoot(vm: NewsViewModel) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route ?: "home"
    val showBottomBar = route in listOf("home", "saved", "more")

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showCategoriesSheet by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var lastBackPressTime by remember { mutableLongStateOf(0L) }

    // Intercept Back button: Always navigate to Home page
    BackHandler(enabled = true) {
        if (showCategoriesSheet) {
            showCategoriesSheet = false
        } else if (route != "home") {
            nav.navigate("home") {
                popUpTo("home") { inclusive = false }
                launchSingleTop = true
            }
        } else {
            val now = System.currentTimeMillis()
            if (now - lastBackPressTime < 2000L) {
                (context as? Activity)?.finish()
            } else {
                lastBackPressTime = now
                Toast.makeText(context, "యాప్ నుండి నిష్క్రమించడానికి మళ్ళీ బ్యాక్ నొక్కండి", Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = vm.currentLanguage,
            onLanguageSelected = { vm.setLanguage(it) },
            onDismiss = { showLanguageDialog = false },
        )
    }

    if (showCategoriesSheet) {
        CategoriesBottomSheet(
            vm = vm,
            onDismiss = { showCategoriesSheet = false },
            onCategorySelected = { index ->
                vm.selectCategory(index)
                nav.navigate("news") {
                    popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                }
                showCategoriesSheet = false
            },
        )
    }

    if (showLocationDialog) {
        LocationPickerDialog(
            vm = vm,
            onDismiss = { showLocationDialog = false },
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showBottomBar,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(310.dp).fillMaxHeight(),
                drawerContainerColor = MaterialTheme.colorScheme.surface,
            ) {
                AppDrawerContent(
                    vm = vm,
                    onOpenLanguageDialog = { showLanguageDialog = true },
                    onSelectCategory = { index ->
                        vm.selectCategory(index)
                        nav.navigate("news") {
                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    },
                    onNavigateAdmin = {
                        nav.navigate("admin")
                        scope.launch { drawerState.close() }
                    },
                    onNavigateEmployee = {
                        nav.navigate("employee_post")
                        scope.launch { drawerState.close() }
                    },
                )
            }
        },
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (showBottomBar) {
                    MainBottomBar(
                        nav = nav,
                        currentRoute = route,
                        vm = vm,
                        onOpenCategories = { showCategoriesSheet = true },
                        onOpenLocation = { showLocationDialog = true },
                    )
                }
            },
        ) { padding ->
            NavHost(
                navController = nav,
                startDestination = "home",
                modifier = Modifier.padding(padding).consumeWindowInsets(padding),
            ) {
                // Home Explore Feed (Image 2)
                composable("home") {
                    HomeExploreScreen(
                        vm = vm,
                        onNavigateNews = { storyIndex ->
                            nav.navigate("news") { launchSingleTop = true }
                        },
                        onNavigateMore = {
                            nav.navigate("more") { launchSingleTop = true }
                        },
                        onOpenCategories = {
                            showCategoriesSheet = true
                        },
                        onOpenArticle = { id ->
                            nav.navigate("article/$id")
                        },
                        onOpenLanguageDialog = {
                            showLanguageDialog = true
                        },
                        onNavigatePostNews = {
                            nav.navigate("employee_post")
                        },
                    )
                }

                // Full-screen News Watching Pager (Image 4)
                composable("news") {
                    NewsWatchingScreen(
                        vm = vm,
                        initialStoryIndex = 0,
                        onGoHome = {
                            nav.navigate("home") {
                                popUpTo("home") { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        onOpenCategories = {
                            showCategoriesSheet = true
                        },
                        onOpenArticle = { id ->
                            nav.navigate("article/$id")
                        },
                        onNavigateMore = {
                            nav.navigate("more") { launchSingleTop = true }
                        },
                    )
                }

                // Saved Stories
                composable("saved") {
                    SavedScreen(vm) { id -> nav.navigate("article/$id") }
                }

                // Profile / More Screen (Image 3)
                composable("more") {
                    MoreScreen(
                        vm = vm,
                        onNavigateNews = {
                            nav.navigate("news") { launchSingleTop = true }
                        },
                        onNavigateAdmin = { nav.navigate("admin") },
                        onNavigateEmployee = { nav.navigate("employee_post") },
                        onNavigateAuth = { vm.logout() },
                    )
                }

                // Admin & Reporter Desks
                composable("admin") {
                    AdminPanelScreen(vm = vm, onBack = { nav.popBackStack() })
                }
                composable("employee_post") {
                    EmployeePostScreen(vm = vm, onBack = { nav.popBackStack() })
                }

                // Article Detail
                composable(
                    "article/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType }),
                ) { entry ->
                    ArticleScreen(
                        vm,
                        entry.arguments?.getInt("id") ?: 0,
                        onBack = { nav.popBackStack() },
                    )
                }
            }
        }
    }
}

/**
 * NAA TV Navigation Drawer
 */
@Composable
private fun AppDrawerContent(
    vm: NewsViewModel,
    onOpenLanguageDialog: () -> Unit,
    onSelectCategory: (Int) -> Unit,
    onNavigateAdmin: () -> Unit,
    onNavigateEmployee: () -> Unit,
) {
    val uri = LocalUriHandler.current
    val currentUser by vm.currentUser.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        // Logo & Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        ) {
            Image(
                painter = painterResource(if (vm.darkTheme) R.drawable.naa_tv_logo_white else R.drawable.naa_tv_logo),
                contentDescription = "NAA TV Telugu",
                modifier = Modifier.height(34.dp),
            )
        }

        // Current User Profile Header in Drawer
        currentUser?.let { user ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(user.name, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                    Text(
                        when (user.role) {
                            UserRole.ADMIN -> "👑 అడ్మిన్ (Admin)"
                            UserRole.EMPLOYEE -> "✍️ రిపోర్టర్ (Reporter)"
                            UserRole.USER -> "👤 యూజర్"
                            UserRole.GUEST -> "🌐 గెస్ట్ మోడ్"
                        },
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
        }

        // Search bar
        OutlinedTextField(
            value = vm.searchQuery,
            onValueChange = { vm.searchQuery = it },
            placeholder = { Text("వార్తలను వెతకండి...", fontSize = 12.5.sp) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, Modifier.size(18.dp)) },
            trailingIcon = {
                if (vm.searchQuery.isNotBlank()) {
                    IconButton(onClick = { vm.searchQuery = "" }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear", Modifier.size(16.dp))
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            ),
        )

        Spacer(Modifier.height(14.dp))

        // Role-based Shortcuts
        when (currentUser?.role) {
            UserRole.ADMIN -> {
                DrawerLinkItem(Icons.Filled.AdminPanelSettings, "అడ్మిన్ ప్యానెల్ (Admin Panel)", onNavigateAdmin)
                Spacer(Modifier.height(4.dp))
            }
            UserRole.EMPLOYEE -> {
                DrawerLinkItem(Icons.Filled.Create, "రిపోర్టర్ డెస్క్ (Post Story)", onNavigateEmployee)
                Spacer(Modifier.height(4.dp))
            }
            else -> {}
        }

        Text(
            text = "విభాగాలు (Categories)",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
        )

        // Categories List in Drawer
        vm.categories.forEachIndexed { index, cat ->
            val isSelected = index == vm.selected
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        else Color.Transparent
                    )
                    .clickable { onSelectCategory(index) }
                    .padding(horizontal = 10.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    categoryIcon(cat.slug),
                    contentDescription = null,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = vm.getCategoryLabel(cat.slug),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        Spacer(Modifier.height(14.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
        Spacer(Modifier.height(10.dp))

        // Language dialog opener
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { onOpenLanguageDialog() }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Filled.Language, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(AppStrings.changeLanguage(vm.currentLanguage), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    "${vm.currentLanguage.nativeName} (${vm.currentLanguage.englishName})",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Spacer(Modifier.height(4.dp))

        // Dark mode toggle
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Filled.DarkMode, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Text(AppStrings.darkMode(vm.currentLanguage), Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Switch(checked = vm.darkTheme, onCheckedChange = { vm.toggleTheme() })
        }

        Spacer(Modifier.height(8.dp))

        // Switch / Logout button
        if (currentUser?.role == UserRole.GUEST) {
            OutlinedButton(
                onClick = { vm.logout() },
                modifier = Modifier.fillMaxWidth().height(38.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Icon(Icons.Filled.Login, contentDescription = null, Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("లాగిన్ / ఖాతా తెరవండి", fontSize = 12.sp)
            }
        } else {
            OutlinedButton(
                onClick = { vm.logout() },
                modifier = Modifier.fillMaxWidth().height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(AppStrings.logout(vm.currentLanguage), fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(14.dp))
        Text(
            "Version 1.0.0 • ${AppStrings.copyright()}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = 8.dp),
        )
    }
}

@Composable
private fun DrawerLinkItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}

private fun categoryIcon(slug: String?): ImageVector = when (slug) {
    null -> Icons.Filled.Newspaper        // All / Latest News
    "news" -> Icons.Filled.Newspaper      // వార్తలు
    "sports" -> Icons.Filled.EmojiEvents   // క్రీడలు (Sports trophy)
    "cinema" -> Icons.Filled.Movie        // సినిమా
    "politics" -> Icons.Filled.AccountBalance // రాజకీయాలు
    "business" -> Icons.Filled.TrendingUp  // వ్యాపారం
    "health-fitness" -> Icons.Filled.FitnessCenter // ఆరోగ్యం
    "ayur_vedam" -> Icons.Filled.Spa      // ఆయుర్వేదం
    else -> Icons.Filled.Newspaper
}

/**
 * 5-Tab Bottom Navigation Bar (Matches Reference Image 2):
 * 1. హోమ్ (Home)
 * 2. న్యూస్ (News)
 * 3. కేటగిరీలు (Categories, elevated circular center button)
 * 4. రీల్స్ (Reels)
 * 5. పొన్నమండ (Selected location)
 */
@Composable
private fun MainBottomBar(
    nav: NavHostController,
    currentRoute: String,
    vm: NewsViewModel,
    onOpenCategories: () -> Unit,
    onOpenLocation: () -> Unit,
) {
    val lang = vm.currentLanguage
    val locationLabel = vm.userLocation.split(",").firstOrNull()?.trim() ?: "పొన్నమండ"
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shadowElevation = 12.dp,
    ) {
        Column(modifier = Modifier.fillMaxWidth().navigationBarsPadding()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
            tabs.forEach { tab ->
                val label = when (tab.route) {
                    "home" -> AppStrings.tabHome(lang)
                    "news" -> AppStrings.tabNews(lang)
                    "categories" -> AppStrings.tabCategories(lang)
                    "reels" -> "రీల్స్"
                    "location" -> locationLabel
                    else -> ""
                }

                if (tab.isCenter) {
                    // Center raised circular button for Categories (Matches Image 2)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onOpenCategories() }
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                tab.icon,
                                contentDescription = label,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            softWrap = false,
                        )
                    }
                } else {
                    val isSelected = currentRoute == tab.route
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                when (tab.route) {
                                    "location" -> onOpenLocation()
                                    "reels" -> {
                                        nav.navigate("news") {
                                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                        }
                                    }
                                    else -> {
                                        nav.navigate(tab.route) {
                                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Icon(
                            tab.icon,
                            contentDescription = label,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp),
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            softWrap = false,
                        )
                    }
                }
            }
        }
    }
}
}
