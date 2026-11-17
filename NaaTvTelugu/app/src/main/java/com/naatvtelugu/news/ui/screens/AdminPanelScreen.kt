package com.naatvtelugu.news.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.naatvtelugu.news.R
import com.naatvtelugu.news.data.AdminSettings
import com.naatvtelugu.news.data.AppUser
import com.naatvtelugu.news.data.CustomPost
import com.naatvtelugu.news.data.NewsItem
import com.naatvtelugu.news.data.PostStatus
import com.naatvtelugu.news.data.UserRole
import com.naatvtelugu.news.ui.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    vm: NewsViewModel,
    onBack: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val allCustomPosts by vm.customPosts.collectAsState()
    val allUsers by vm.allUsers.collectAsState()

    val pendingPosts = allCustomPosts.filter { it.status == PostStatus.PENDING }
    val employees = allUsers.filter { it.role == UserRole.EMPLOYEE }

    var showAddEmployeeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Verified, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("అడ్మిన్ ప్యానెల్ (Admin Panel)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 12.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
            ) {
                // Tab 0: Approvals
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "పరిశీలన (${pendingPosts.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp,
                        )
                    },
                )
                // Tab 1: Post News
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("వార్త రాయండి", fontWeight = FontWeight.Bold, fontSize = 12.5.sp) },
                )
                // Tab 2: Edit News Posts
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("వార్తల సవరణ", fontWeight = FontWeight.Bold, fontSize = 12.5.sp) },
                )
                // Tab 3: App Customization
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("కస్టమైజేషన్", fontWeight = FontWeight.Bold, fontSize = 12.5.sp) },
                )
                // Tab 4: Users & Staff Directory
                Tab(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    text = {
                        Text(
                            "యూజర్లు & సిబ్బంది (${allUsers.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp,
                        )
                    },
                )
            }

            Box(Modifier.weight(1f).fillMaxWidth()) {
                when (selectedTab) {
                    0 -> PendingApprovalsList(pendingPosts, onApprove = vm::approveNews, onReject = vm::rejectNews)
                    1 -> PostNewsForm(vm, isAdmin = true)
                    2 -> EditNewsTab(vm)
                    3 -> AppCustomizationTab(vm)
                    4 -> UsersAndStaffTab(vm, onAddEmployeeClick = { showAddEmployeeDialog = true })
                }
            }
        }
    }

    if (showAddEmployeeDialog) {
        AddEmployeeDialog(
            onDismiss = { showAddEmployeeDialog = false },
            onAdd = { name, email, pass ->
                val res = vm.addEmployee(name, email, pass)
                if (res.isSuccess) showAddEmployeeDialog = false
                res
            },
        )
    }
}

/* =====================================================================
 * TAB 0: PENDING APPROVALS
 * ===================================================================== */
@Composable
private fun PendingApprovalsList(
    pendingPosts: List<CustomPost>,
    onApprove: (Int) -> Unit,
    onReject: (Int) -> Unit,
) {
    if (pendingPosts.isEmpty()) {
        Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Filled.Check, contentDescription = null, Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(12.dp))
                Text(
                    "పరిశీలించాల్సిన వార్తలు ఏవీ లేవు.\nఉద్యోగులు పోస్ట్ చేసినప్పుడు ఇక్కడ కనిపిస్తాయి.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(pendingPosts, key = { it.id }) { post ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(Modifier.padding(16.dp)) {
                    // Author banner
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                    ) {
                        Icon(Icons.Filled.Badge, contentDescription = null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "రిపోర్టర్: ${post.authorName} (${post.authorEmail})",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Text(
                        post.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        post.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(Modifier.height(6.dp))
                    Text(
                        post.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 4,
                    )

                    Spacer(Modifier.height(14.dp))

                    // Approve & Reject Buttons
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        OutlinedButton(
                            onClick = { onReject(post.id) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        ) {
                            Icon(Icons.Filled.Close, contentDescription = null, Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("తిరస్కరించండి")
                        }

                        Spacer(Modifier.width(10.dp))

                        Button(
                            onClick = { onApprove(post.id) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                        ) {
                            Icon(Icons.Filled.Check, contentDescription = null, Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("ఆమోదించండి (Approve)")
                        }
                    }
                }
            }
        }
    }
}

/* =====================================================================
 * TAB 1: POST NEWS FORM
 * ===================================================================== */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostNewsForm(
    vm: NewsViewModel,
    isAdmin: Boolean,
    onComplete: () -> Unit = {},
) {
    var title by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var contentHtml by remember { mutableStateOf("") }
    var expandedCategory by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("వార్తలు") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }

    val categoryOptions = listOf("వార్తలు", "సినిమా", "రాజకీయాలు", "వ్యాపారం", "క్రీడలు", "ఆరోగ్యం")

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        // Status banner
        AnimatedVisibility(visible = statusMessage != null) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isError) MaterialTheme.colorScheme.error.copy(alpha = 0.12f) else Color(0xFF10B981).copy(alpha = 0.12f))
                    .padding(12.dp),
            ) {
                Text(
                    text = statusMessage.orEmpty(),
                    color = if (isError) MaterialTheme.colorScheme.error else Color(0xFF10B981),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                )
            }
        }

        // Role notice
        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp),
        ) {
            Text(
                text = if (isAdmin) {
                    "👑 అడ్మిన్ హోదా: మీరు పోస్ట్ చేసే వార్త వెంటనే ఆమోదించబడి లైవ్ ఫీడ్‌లో ప్రచురించబడుతుంది."
                } else {
                    "✍️ ఉద్యోగి / రిపోర్టర్: మీ వార్తను అడ్మిన్ పరిశీలించి ఆమోదించిన తర్వాత లైవ్ ఫీడ్‌లో ప్రచురిస్తారు."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.height(14.dp))

        // Title
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("వార్త శీర్షిక (Headline)*") },
            placeholder = { Text("ముఖ్యమైన వార్తా శీర్షిక...") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
        )

        // Category dropdown
        ExposedDropdownMenuBox(
            expanded = expandedCategory,
            onExpandedChange = { expandedCategory = !expandedCategory },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        ) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("విభాగం (Category)*") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                shape = RoundedCornerShape(12.dp),
            )
            ExposedDropdownMenu(
                expanded = expandedCategory,
                onDismissRequest = { expandedCategory = false },
            ) {
                categoryOptions.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat) },
                        onClick = {
                            selectedCategory = cat
                            expandedCategory = false
                        },
                    )
                }
            }
        }

        // Summary (~60 words)
        OutlinedTextField(
            value = summary,
            onValueChange = { summary = it },
            label = { Text("సంక్షిప్త వార్త (~60 పదాలు - Summary)*") },
            placeholder = { Text("సంక్షిప్త వివరణ (~60 పదాలు)...") },
            minLines = 4,
            maxLines = 6,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
        )

        // Image URL
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("ఫోటో లింక్ (Image URL - ఐచ్ఛికం)") },
            placeholder = { Text("https://example.com/image.jpg") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
        )

        // Content
        OutlinedTextField(
            value = contentHtml,
            onValueChange = { contentHtml = it },
            label = { Text("పూర్తి వార్త వివరాలు (Full Article Details)") },
            placeholder = { Text("పూర్తి కథనం రాయండి...") },
            minLines = 4,
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            shape = RoundedCornerShape(12.dp),
        )

        // Publish / Submit button
        Button(
            onClick = {
                if (title.isBlank() || summary.isBlank()) {
                    isError = true
                    statusMessage = "దయచేసి శీర్షిక మరియు సంక్షిప్త వార్తను నమోదు చేయండి"
                    return@Button
                }
                val result = vm.submitNews(
                    title = title,
                    summary = summary,
                    imageUrl = imageUrl.ifBlank { null },
                    category = selectedCategory,
                    contentHtml = contentHtml,
                )
                if (result.isSuccess) {
                    isError = false
                    statusMessage = if (isAdmin) {
                        "వార్త విజయవంతంగా ప్రచురించబడింది!"
                    } else {
                        "వార్త అడ్మిన్ ఆమోదం కోసం విజయవంతంగా పంపబడింది!"
                    }
                    title = ""
                    summary = ""
                    imageUrl = ""
                    contentHtml = ""
                    onComplete()
                } else {
                    isError = true
                    statusMessage = result.exceptionOrNull()?.message ?: "లోపం సంభవించింది"
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Icon(Icons.Filled.Newspaper, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                if (isAdmin) "తక్షణమే ప్రచురించండి (Publish Live)" else "ఆమోదం కోసం పంపండి (Submit for Approval)",
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

/* =====================================================================
 * TAB 2: EDIT NEWS TAB (Admin can edit any story headline, summary, text)
 * ===================================================================== */
@Composable
private fun EditNewsTab(vm: NewsViewModel) {
    val items = vm.items
    var searchQuery by remember { mutableStateOf("") }
    var storyToEdit by remember { mutableStateOf<NewsItem?>(null) }

    val filteredItems = if (searchQuery.isBlank()) {
        items
    } else {
        items.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.summary.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("వార్తను వెతకండి (Search story to edit)...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, Modifier.size(20.dp)) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
        )

        Text(
            text = "మొత్తం వార్తలు: ${filteredItems.size} (ఏదైనా వార్తను సవరించడానికి 'సవరించండి' నొక్కండి)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 10.dp),
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
        ) {
            items(filteredItems, key = { it.id }) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = item.category.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Button(
                                onClick = { storyToEdit = item },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp),
                            ) {
                                Icon(Icons.Filled.Edit, contentDescription = null, Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("సవరించండి", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = item.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = item.summary,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }

    if (storyToEdit != null) {
        EditStoryDialog(
            story = storyToEdit!!,
            onDismiss = { storyToEdit = null },
            onSave = { id, title, summary, category, content ->
                vm.editStoryText(id, title, summary, category, content)
                storyToEdit = null
            },
        )
    }
}

@Composable
private fun EditStoryDialog(
    story: NewsItem,
    onDismiss: () -> Unit,
    onSave: (id: Int, title: String, summary: String, category: String, content: String) -> Unit,
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf(story.title) }
    var summary by remember { mutableStateOf(story.summary) }
    var category by remember { mutableStateOf(story.category) }
    var contentHtml by remember { mutableStateOf(story.contentHtml ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("వార్తను సవరించండి (Edit Story)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("శీర్షిక (Headline)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    shape = RoundedCornerShape(10.dp),
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("విభాగం (Category)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                )

                OutlinedTextField(
                    value = summary,
                    onValueChange = { summary = it },
                    label = { Text("సంక్షిప్త వార్త (Summary)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    minLines = 3,
                    shape = RoundedCornerShape(10.dp),
                )

                OutlinedTextField(
                    value = contentHtml,
                    onValueChange = { contentHtml = it },
                    label = { Text("పూర్తి కథనం (Full Article Body)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    shape = RoundedCornerShape(10.dp),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(story.id, title, summary, category, contentHtml)
                    Toast.makeText(context, "వార్త విజయవంతంగా సవరించబడింది!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Text("సేవ్ చేయండి (Save)")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("రద్దు") }
        },
    )
}

/* =====================================================================
 * TAB 3: APP CUSTOMIZATION (Typography, Logo & Image Size, Guest & Employee Modules)
 * ===================================================================== */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppCustomizationTab(vm: NewsViewModel) {
    val context = LocalContext.current
    val currentSettings by vm.adminSettings.collectAsState()

    var headlineSize by remember(currentSettings) { mutableIntStateOf(currentSettings.headlineFontSize) }
    var bodySize by remember(currentSettings) { mutableIntStateOf(currentSettings.bodyFontSize) }
    var fontFamilyChoice by remember(currentSettings) { mutableStateOf(currentSettings.fontFamilyChoice) }
    var logoHeight by remember(currentSettings) { mutableIntStateOf(currentSettings.logoHeightDp) }
    var imageRatio by remember(currentSettings) { mutableIntStateOf(currentSettings.imageRatioPercent) }
    var guestMode by remember(currentSettings) { mutableStateOf(currentSettings.guestModeEnabled) }
    var guestBookmark by remember(currentSettings) { mutableStateOf(currentSettings.guestCanBookmark) }
    var guestShare by remember(currentSettings) { mutableStateOf(currentSettings.guestCanShare) }
    var empRequireApproval by remember(currentSettings) { mutableStateOf(currentSettings.employeeRequireApproval) }
    var empAllowBreaking by remember(currentSettings) { mutableStateOf(currentSettings.employeeAllowBreaking) }

    var expandedFontDropdown by remember { mutableStateOf(false) }
    val fontOptions = listOf("Default", "Serif", "SansSerif", "Monospace")

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        // Section 1: Typography
        Text(
            text = "1. టెక్స్ట్ & ఫాంట్ కస్టమైజేషన్ (Typography)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(Modifier.padding(14.dp)) {
                // Headline font size
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("శీర్షిక అక్షరాల సైజు (Headline Size)", fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp)
                    Text("${headlineSize} sp", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Slider(
                    value = headlineSize.toFloat(),
                    onValueChange = { headlineSize = it.toInt() },
                    valueRange = 14f..28f,
                    steps = 13,
                )

                Spacer(Modifier.height(8.dp))

                // Body font size
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("వార్త వివరాల సైజు (Body Size)", fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp)
                    Text("${bodySize} sp", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Slider(
                    value = bodySize.toFloat(),
                    onValueChange = { bodySize = it.toInt() },
                    valueRange = 12f..22f,
                    steps = 9,
                )

                Spacer(Modifier.height(8.dp))

                // Font Family Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedFontDropdown,
                    onExpandedChange = { expandedFontDropdown = !expandedFontDropdown },
                ) {
                    OutlinedTextField(
                        value = fontFamilyChoice,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("ఫాంట్ స్టైల్ (Font Family)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFontDropdown) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(10.dp),
                    )
                    ExposedDropdownMenu(
                        expanded = expandedFontDropdown,
                        onDismissRequest = { expandedFontDropdown = false },
                    ) {
                        fontOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    fontFamilyChoice = opt
                                    expandedFontDropdown = false
                                },
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // Section 2: Logo and Images
        Text(
            text = "2. లోగో & ఫోటో సైజు (Logo & Media Size)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(Modifier.padding(14.dp)) {
                // Logo Height Slider
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("హెడర్ లోగో ఎత్తు (Logo Height)", fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp)
                    Text("${logoHeight} dp", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Slider(
                    value = logoHeight.toFloat(),
                    onValueChange = { logoHeight = it.toInt() },
                    valueRange = 22f..48f,
                    steps = 12,
                )

                // Logo Live Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(if (vm.darkTheme) R.drawable.naa_tv_logo_white else R.drawable.naa_tv_logo),
                        contentDescription = "Logo Preview",
                        modifier = Modifier.height(logoHeight.dp),
                    )
                }

                Spacer(Modifier.height(14.dp))

                // Story Image Ratio
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("వార్తా కార్డులో ఫోటో నిష్పత్తి (Image Ratio)", fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp)
                    Text("${imageRatio} %", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Slider(
                    value = imageRatio.toFloat(),
                    onValueChange = { imageRatio = it.toInt() },
                    valueRange = 30f..55f,
                    steps = 5,
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        // Section 3: Guest Modules
        Text(
            text = "3. గెస్ట్ మాడ్యూల్స్ (Guest Permissions)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(Modifier.padding(14.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("లాగిన్ లేకుండా చదవడం (Guest Browsing)", Modifier.weight(1f), fontSize = 13.5.sp)
                    Switch(checked = guestMode, onCheckedChange = { guestMode = it })
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("గెస్ట్‌లు సేవ్ చేసుకోవచ్చు (Guest Bookmarking)", Modifier.weight(1f), fontSize = 13.5.sp)
                    Switch(checked = guestBookmark, onCheckedChange = { guestBookmark = it })
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("గెస్ట్‌లు షేర్ చేసుకోవచ్చు (Guest Sharing)", Modifier.weight(1f), fontSize = 13.5.sp)
                    Switch(checked = guestShare, onCheckedChange = { guestShare = it })
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // Section 4: Employee Modules
        Text(
            text = "4. ఉద్యోగుల మాడ్యూల్స్ (Employee Rules)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(Modifier.padding(14.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("రిపోర్టర్ల వార్తలకు అడ్మిన్ ఆమోదం తప్పనిసరి (Require Approval)", Modifier.weight(1f), fontSize = 13.5.sp)
                    Switch(checked = empRequireApproval, onCheckedChange = { empRequireApproval = it })
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("బ్రేకింగ్ న్యూస్ ట్యాగ్ అనుమతించండి (Allow Breaking)", Modifier.weight(1f), fontSize = 13.5.sp)
                    Switch(checked = empAllowBreaking, onCheckedChange = { empAllowBreaking = it })
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Action Buttons: Save & Reset
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = {
                    headlineSize = 18
                    bodySize = 15
                    fontFamilyChoice = "Default"
                    logoHeight = 28
                    imageRatio = 42
                    guestMode = true
                    guestBookmark = true
                    guestShare = true
                    empRequireApproval = true
                    empAllowBreaking = true
                    Toast.makeText(context, "సెట్టింగ్స్ రీసెట్ చేయబడ్డాయి", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f).height(46.dp),
                shape = RoundedCornerShape(10.dp),
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = null, Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("రీసెట్ (Reset)", fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = {
                    vm.updateAdminSettings(
                        AdminSettings(
                            headlineFontSize = headlineSize,
                            bodyFontSize = bodySize,
                            fontFamilyChoice = fontFamilyChoice,
                            logoHeightDp = logoHeight,
                            imageRatioPercent = imageRatio,
                            guestModeEnabled = guestMode,
                            guestCanBookmark = guestBookmark,
                            guestCanShare = guestShare,
                            employeeRequireApproval = empRequireApproval,
                            employeeAllowBreaking = empAllowBreaking,
                        )
                    )
                    Toast.makeText(context, "కస్టమైజేషన్ సెట్టింగ్స్ విజయవంతంగా సేవ్ అయ్యాయి!", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.weight(1.5f).height(46.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Icon(Icons.Filled.Check, contentDescription = null, Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("సేవ్ చేయండి (Save)", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

/* =====================================================================
 * TAB 4: USERS & STAFF DIRECTORY
 * Displays all registered users, employees, work status, news counts, and status switchers
 * ===================================================================== */
@Composable
private fun UsersAndStaffTab(
    vm: NewsViewModel,
    onAddEmployeeClick: () -> Unit,
) {
    val context = LocalContext.current
    val allUsers by vm.allUsers.collectAsState()
    var filterType by remember { mutableStateOf("ALL") } // "ALL", "EMPLOYEES", "USERS"

    val displayedUsers = when (filterType) {
        "EMPLOYEES" -> allUsers.filter { it.role == UserRole.EMPLOYEE }
        "USERS" -> allUsers.filter { it.role == UserRole.USER }
        else -> allUsers
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        // Header
        Row(
            Modifier.fillMaxWidth().padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    "వినియోగదారులు & ఉద్యోగులు",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "మొత్తం: ${allUsers.size} మంది | ఉద్యోగులు: ${allUsers.count { it.role == UserRole.EMPLOYEE }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Button(
                onClick = onAddEmployeeClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Icon(Icons.Filled.PersonAdd, contentDescription = null, Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("ఉద్యోగిని చేర్చండి", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Filter chips
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(
                selected = filterType == "ALL",
                onClick = { filterType = "ALL" },
                label = { Text("అందరూ (${allUsers.size})") },
            )
            FilterChip(
                selected = filterType == "EMPLOYEES",
                onClick = { filterType = "EMPLOYEES" },
                label = { Text("ఉద్యోగులు (${allUsers.count { it.role == UserRole.EMPLOYEE }})") },
            )
            FilterChip(
                selected = filterType == "USERS",
                onClick = { filterType = "USERS" },
                label = { Text("సాధారణ యూజర్లు (${allUsers.count { it.role == UserRole.USER }})") },
            )
        }

        if (displayedUsers.isEmpty()) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    "ఈ విభాగంలో ఎవరూ లేరు.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(displayedUsers, key = { it.id }) { user ->
                    val stats = vm.getEmployeeStats(user.email)
                    UserStaffCard(
                        user = user,
                        stats = stats,
                        onStatusChange = { newStatus ->
                            vm.updateEmployeeWorkStatus(user.email, newStatus)
                            Toast.makeText(context, "${user.name} పని స్థితి: $newStatus కు మార్చబడింది", Toast.LENGTH_SHORT).show()
                        },
                        onRoleChange = { newRole ->
                            vm.updateUserRole(user.email, newRole)
                            Toast.makeText(context, "${user.name} హోదా: $newRole కు మార్చబడింది", Toast.LENGTH_SHORT).show()
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun UserStaffCard(
    user: AppUser,
    stats: com.naatvtelugu.news.ui.EmployeeStats,
    onStatusChange: (String) -> Unit,
    onRoleChange: (UserRole) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(14.dp)) {
            // Top row: Avatar + Name + Role Badge
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp),
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(user.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(Modifier.width(6.dp))
                        // Work Status Badge
                        val (statusBg, statusFg) = when (user.workStatus) {
                            "Active" -> Color(0xFF10B981).copy(alpha = 0.15f) to Color(0xFF10B981)
                            "On Leave" -> Color(0xFFF59E0B).copy(alpha = 0.15f) to Color(0xFFF59E0B)
                            else -> Color.Gray.copy(alpha = 0.15f) to Color.Gray
                        }
                        Text(
                            text = when (user.workStatus) {
                                "Active" -> "యాక్టివ్"
                                "On Leave" -> "సెలవులో"
                                else -> "నిష్క్రియ"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusFg,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(statusBg)
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                        )
                    }
                    Text(
                        user.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                // Role Pill
                val (roleLabel, roleColor) = when (user.role) {
                    UserRole.ADMIN -> "👑 అడ్మిన్" to Color(0xFFE5A93C)
                    UserRole.EMPLOYEE -> "✍️ రిపోర్టర్" to Color(0xFF10B981)
                    UserRole.USER -> "👤 యూజర్" to Color(0xFF3B82F6)
                    UserRole.GUEST -> "గెస్ట్" to Color.Gray
                }
                Text(
                    text = roleLabel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = roleColor,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(roleColor.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                )
            }

            Spacer(Modifier.height(10.dp))

            // Details info row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "📍 స్థానం: ${user.location}",
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "📅 చేరిన తేదీ: ${user.joinedDate}",
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(Modifier.height(8.dp))

            // Stats row (News posted statistics)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("మొత్తం పోస్ట్‌లు", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${stats.totalPosted}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("ఆమోదించినవి", fontSize = 10.sp, color = Color(0xFF10B981))
                    Text("${stats.approved}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF10B981))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("పెండింగ్", fontSize = 10.sp, color = Color(0xFFF59E0B))
                    Text("${stats.pending}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFF59E0B))
                }
            }

            Spacer(Modifier.height(10.dp))

            // Action Switchers: Change Work Status & Change Role
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Status buttons
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("స్థితి:", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterVertically))
                    WorkStatusButton("యాక్టివ్", active = user.workStatus == "Active", activeColor = Color(0xFF10B981)) {
                        onStatusChange("Active")
                    }
                    WorkStatusButton("సెలవు", active = user.workStatus == "On Leave", activeColor = Color(0xFFF59E0B)) {
                        onStatusChange("On Leave")
                    }
                    WorkStatusButton("నిష్క్రియ", active = user.workStatus == "Inactive", activeColor = Color.Gray) {
                        onStatusChange("Inactive")
                    }
                }

                // Promote / Demote Role toggle
                if (user.role != UserRole.ADMIN) {
                    TextButton(
                        onClick = {
                            val newRole = if (user.role == UserRole.EMPLOYEE) UserRole.USER else UserRole.EMPLOYEE
                            onRoleChange(newRole)
                        },
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp),
                    ) {
                        Text(
                            text = if (user.role == UserRole.EMPLOYEE) "యూజర్‌గా మార్చు" else "రిపోర్టర్‌గా మార్చు",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkStatusButton(
    label: String,
    active: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(26.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (active) activeColor else MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick)
            .padding(horizontal = 7.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            fontSize = 10.5.sp,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
            color = if (active) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/* =====================================================================
 * ADD EMPLOYEE DIALOG
 * ===================================================================== */
@Composable
private fun AddEmployeeDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, email: String, pass: String) -> Result<AppUser>,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("కొత్త ఉద్యోగిని చేర్చండి", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    "ఉద్యోగికి ఖాతా వివరాలు ఇవ్వండి. వారు ఈ వివరాలతో లాగిన్ అయి వార్తలు సమర్పించవచ్చు.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 12.dp),
                )

                if (error != null) {
                    Text(
                        error.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("ఉద్యోగి పేరు (Name)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("ఇమెయిల్ (Email ID)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("పాస్‌వర్డ్ (Password)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val res = onAdd(name, email, password)
                    if (res.isFailure) error = res.exceptionOrNull()?.message
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) { Text("జోడించండి (Add)") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("రద్దు చేయండి") }
        },
    )
}
