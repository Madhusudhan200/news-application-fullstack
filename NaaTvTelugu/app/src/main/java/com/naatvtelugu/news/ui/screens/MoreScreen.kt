package com.naatvtelugu.news.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naatvtelugu.news.R
import com.naatvtelugu.news.data.AppStrings
import com.naatvtelugu.news.data.Brand
import com.naatvtelugu.news.data.UserRole
import com.naatvtelugu.news.ui.NewsViewModel

/**
 * =====================================================================
 * MORE / PROFILE SCREEN (Matches Reference Image 3: media_1794832152624_10295a91.jpg)
 * =====================================================================
 * - Top Bar: Settings gear icon, Search icon, right "న్యూస్ >" button (jumps to news watching)
 * - User Card: Avatar circle, "Guest" / user name, "Click here to select email id"
 * - Language Row: Globe, "మీ భాషను ఎంచుకోండి" / "తెలుగు Telugu >"
 * - Location Row: Location Pin, "మీరు ఎంచుకున్న స్థానం" / "ఆంధ్రప్రదేశ్ / హైదరాబాద్ (Change) >"
 * - Categories Horizontal Row: Circular icons for "తాజా వార్తలు", "వార్తలు", "రాజకీయాలు", "వ్యాపారం", "క్రీడలు", "సినిమాలు"
 * - Menu Items: Fact Check (CHECK NOW), Refer (REFER NOW), Status Videos, NAA TV Exclusive, Settings, Feedback, Contact Us
 * - Copyright: "© 2018 NAA TV Telugu. All Rights Reserved."
 */
@Composable
fun MoreScreen(
    vm: NewsViewModel,
    onNavigateNews: () -> Unit = {},
    onNavigateAdmin: () -> Unit = {},
    onNavigateEmployee: () -> Unit = {},
    onNavigateAuth: () -> Unit = {},
) {
    val uri = LocalUriHandler.current
    val context = LocalContext.current
    val currentUser by vm.currentUser.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var showReferralDialog by remember { mutableStateOf(false) }

    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = vm.currentLanguage,
            onLanguageSelected = { vm.setLanguage(it) },
            onDismiss = { showLanguageDialog = false },
        )
    }

    if (showLocationDialog) {
        LocationPickerDialog(
            vm = vm,
            onDismiss = { showLocationDialog = false },
        )
    }

    if (showReferralDialog) {
        ReferralDialog(
            currentUser = currentUser,
            onDismiss = { showReferralDialog = false },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- TOP BAR (Matches Image 3) ---
        Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                    IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

                // Right: "న్యూస్ >" (Quick jump to news reading screen)
                TextButton(
                    onClick = onNavigateNews,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp),
                ) {
                    Text(
                        text = "న్యూస్",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.width(2.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }

        // --- SCROLLABLE BODY ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            // 1. USER ACCOUNT CARD (Matches Image 3)
            GroupCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(30.dp),
                        )
                    }

                    Spacer(Modifier.width(14.dp))

                    Column(Modifier.weight(1f)) {
                        Text(
                            text = currentUser?.name ?: "Guest",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = currentUser?.email ?: "Click here to select email id",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { onNavigateAuth() },
                        )
                    }

                    // Role Badge
                    val (roleText, roleBg, roleColor) = when (currentUser?.role) {
                        UserRole.ADMIN -> Triple("👑 Admin", MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), MaterialTheme.colorScheme.primary)
                        UserRole.EMPLOYEE -> Triple("✍️ Reporter", Color(0xFF10B981).copy(alpha = 0.15f), Color(0xFF10B981))
                        UserRole.USER -> Triple("👤 User", Color(0xFF3B82F6).copy(alpha = 0.15f), Color(0xFF3B82F6))
                        UserRole.GUEST, null -> Triple("Guest", MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Text(
                        text = roleText,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = roleColor,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(roleBg)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // 2. LANGUAGE PICKER ROW (Matches Image 3)
            GroupCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLanguageDialog = true }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Filled.Language, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "మీ భాషను ఎంచుకోండి",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "${vm.currentLanguage.nativeName} ${vm.currentLanguage.englishName}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // 3. LOCATION PICKER ROW (Matches Image 3)
            GroupCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLocationDialog = true }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color(0xFFE5A93C), modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "మీరు ఎంచుకున్న స్థానం",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "${vm.userLocation} (Change)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // 4. CATEGORIES HORIZONTAL ICONS ROW (Matches Image 3)
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                vm.categories.forEachIndexed { index, cat ->
                    val isSelected = index == vm.selected
                    val icon = when (cat.slug) {
                        null -> Icons.Filled.Newspaper
                        "sports" -> Icons.Filled.EmojiEvents // Sports trophy!
                        "cinema" -> Icons.Filled.Movie
                        "politics" -> Icons.Filled.AccountBalance
                        "business" -> Icons.Filled.TrendingUp
                        else -> Icons.Filled.Newspaper
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable {
                                vm.selectCategory(index)
                                onNavigateNews()
                            }
                            .padding(4.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = vm.getCategoryLabel(cat.slug),
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            // 5. QUICK MENU LINKS (Matches Image 3)
            GroupCard {
                // Fact Check
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.CheckCircle, null, tint = Color(0xFF10B981), modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(14.dp))
                    Text("Fact Check", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    TextButton(onClick = { uri.openUri(Brand.SITE) }) {
                        Text("CHECK NOW", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                // Refer (Dedicated Dialog with Unique Referral Code & Rewards)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showReferralDialog = true }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Share, null, tint = Color(0xFF3B82F6), modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(14.dp))
                    Text("రెఫరర్ (Refer & Earn)", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    TextButton(onClick = { showReferralDialog = true }) {
                        Text("REFER NOW", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                // Status Videos
                ActionRow(Icons.Outlined.VideoLibrary, "స్టేటస్ వీడియోలు") { uri.openUri(Brand.YOUTUBE) }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                // NAA TV Exclusive
                ActionRow(Icons.Filled.Newspaper, "నా టీవీ ఎక్స్‌క్లూజివ్") {
                    vm.selectCategory(0)
                    onNavigateNews()
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                // Admin Dashboard & Customization (Easy Access for Admin)
                ActionRow(Icons.Filled.AdminPanelSettings, "అడ్మిన్ ప్యానెల్ & కస్టమైజేషన్ (Admin Panel)") {
                    onNavigateAdmin()
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                // Settings (Dark Mode Toggle)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Filled.DarkMode, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(14.dp))
                    Text(AppStrings.darkMode(vm.currentLanguage), fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    Switch(checked = vm.darkTheme, onCheckedChange = { vm.toggleTheme() })
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                // Feedback
                ActionRow(Icons.Outlined.Feedback, "మీ స్పందన (Feedback)") { uri.openUri(Brand.CONTACT) }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                // Contact Us
                ActionRow(Icons.Filled.Mail, "సంప్రదించండి (Contact Us)") { uri.openUri(Brand.CONTACT) }
            }

            Spacer(Modifier.height(14.dp))

            // Admin / Reporter shortcuts if privileged
            if (currentUser?.role == UserRole.ADMIN) {
                Button(
                    onClick = onNavigateAdmin,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                ) {
                    Icon(Icons.Filled.AdminPanelSettings, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("అడ్మిన్ డాష్‌బోర్డ్ (Admin Panel)", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))
            } else if (currentUser?.role == UserRole.EMPLOYEE) {
                Button(
                    onClick = onNavigateEmployee,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                ) {
                    Icon(Icons.Filled.Create, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("వార్తను పోస్ట్ చేయండి (Reporter Desk)", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))
            }

            // Logout or Switch Account
            OutlinedButton(
                onClick = {
                    vm.logout()
                    onNavigateAuth()
                },
                modifier = Modifier.fillMaxWidth().height(42.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (currentUser?.role == UserRole.GUEST || currentUser == null) "ఖాతా మార్చండి / సైన్ ఇన్ చేయండి" else AppStrings.logout(vm.currentLanguage),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(Modifier.height(20.dp))

            // Copyright 2018 Footer (Matches Request)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = AppStrings.copyright(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Official Portal: naatvtelugu.com",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun GroupCard(content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 1.dp,
        shadowElevation = 1.dp,
    ) {
        Column { content() }
    }
}

@Composable
private fun ActionRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), fontSize = 13.5.sp)
        Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun ReferralDialog(
    currentUser: com.naatvtelugu.news.data.AppUser?,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val referralCode = remember(currentUser) {
        val namePart = (currentUser?.name ?: "USER").filter { it.isLetterOrDigit() }.take(4).uppercase()
        val numPart = kotlin.math.abs((currentUser?.email ?: "naatv").hashCode() % 9000 + 1000)
        "NAA-$namePart-$numPart"
    }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.CardGiftcard, contentDescription = null, tint = Color(0xFFE5A93C), modifier = Modifier.size(26.dp))
                Spacer(Modifier.width(8.dp))
                Text("మిత్రులను ఆహ్వానించండి", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "మీ స్నేహితులకు NAA TV తెలుగు న్యూస్ యాప్‌ను పరిచయం చేసి రివార్డులు పొందండి!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(14.dp))

                // Unique Code Container
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE5A93C)),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "మీ ప్రత్యేక రెఫరల్ కోడ్ (Unique Referral Code)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = referralCode,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFE5A93C),
                            letterSpacing = 2.sp,
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Stats row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("ఆహ్వానించిన మిత్రులు: 0", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("పాయింట్లు: 50 NAA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                }

                Spacer(Modifier.height(14.dp))

                // Copy Code Button
                OutlinedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("NAA TV Referral Code", referralCode)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "రెఫరల్ కోడ్ కాపీ చేయబడింది: $referralCode", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Icon(Icons.Filled.ContentCopy, contentDescription = null, Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("కోడ్ కాపీ చేయండి (Copy Code)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(8.dp))

                // Share to WhatsApp Button
                Button(
                    onClick = {
                        val shareMessage = "🙏 నమస్కారం! NAA TV Telugu న్యూస్ యాప్‌ను ఇప్పుడే డౌన్‌లోడ్ చేసుకోండి. నిజమైన, నిష్పాక్షికమైన వార్తలు & తాజా అప్‌డేట్స్ పొందండి!\n\nనా రెఫరల్ కోడ్: $referralCode\nయాప్ లింక్: https://naatvtelugu.com/app"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareMessage)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            setPackage("com.whatsapp")
                        }
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            val chooser = Intent.createChooser(
                                Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                                },
                                "షేర్ చేయండి (Share Invite)"
                            ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                            context.startActivity(chooser)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                ) {
                    Icon(Icons.Filled.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("వాట్సాప్‌లో ఆహ్వానించండి", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("ముగించు (Close)", fontWeight = FontWeight.Bold)
            }
        },
    )
}
