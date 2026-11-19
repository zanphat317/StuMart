package com.example.stumart.ui.screens.home.tabs

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.stumart.data.repository.PostRepository
import com.example.stumart.data.supabase.Post
import com.example.stumart.ui.theme.*

/**
 * Giao diện HomeTab - "Campus Story & Bento Stream"
 * Thiết kế phá cách, giàu năng lượng cộng đồng sinh viên:
 * - Lời chào thân thiện cá nhân hóa theo buổi
 * - Khối Bento tiện ích (Góc 0đ Cho Tặng & Săn Deal KTX)
 * - Thanh tìm kiếm & bộ lọc chip trượt ngang trong tầm ngón cái (Thumb-zone)
 * - Lưới sản phẩm mở, tuyệt đối không lồng card-trong-card
 */
@Composable
fun HomeTab(
    userName: String = "Khoa Mai Dang",
    userUniversity: String = "ĐH Bách Khoa Hà Nội",
    onSearchClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onPostClick: (Post) -> Unit = {},
    onChatClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onBannerClick: () -> Unit = {}
) {
    val posts by PostRepository.posts.collectAsState()
    var selectedCategory by remember { mutableStateOf("Tất cả") }
    var searchQuery by remember { mutableStateOf("") }
    var showAISearchDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        PostRepository.fetchPosts()
    }

    // Lọc bài đăng theo danh mục và từ khóa tìm kiếm AI
    val filteredPosts = remember(posts, selectedCategory, searchQuery) {
        posts.filter { post ->
            val matchesCategory = if (selectedCategory == "Tất cả") {
                true
            } else if (selectedCategory == "🎁 Đồ 0đ") {
                post.type == "Cho tặng" || post.price <= 0
            } else if (selectedCategory == "⚡ Deal rẻ") {
                post.type == "Giá rẻ"
            } else {
                val cat = post.category ?: "Khác"
                cat.contains(selectedCategory.replace(Regex("^[\\p{So}\\p{Cn}]\\s*"), ""), ignoreCase = true) ||
                        post.title.contains(selectedCategory.replace(Regex("^[\\p{So}\\p{Cn}]\\s*"), ""), ignoreCase = true)
            }

            val matchesSearch = if (searchQuery.isBlank()) {
                true
            } else {
                val q = searchQuery.trim().lowercase()
                post.title.lowercase().contains(q) ||
                        post.description.lowercase().contains(q) ||
                        (post.category ?: "").lowercase().contains(q)
            }

            matchesCategory && matchesSearch
        }
    }

    // Chia bài đăng thành 2 cột cho LazyColumn
    val chunkedPosts = remember(filteredPosts) { filteredPosts.chunked(2) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(StuMartLightBackground),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- 1. CAMPUS GREETING BAR (Thanh chào đón & Vị trí) ---
            item(key = "home_greeting") {
                CampusGreetingSection(
                    userName = userName,
                    userUniversity = userUniversity,
                    onChatClick = onChatClick,
                    onNotificationClick = onNotificationClick
                )
            }

            // --- 2. BENTO QUICK HUB (Cụm Bento khám phá sinh viên) ---
            item(key = "home_bento_hub") {
                BentoQuickHub(
                    onFreeGiveawayClick = {
                        selectedCategory = "🎁 Đồ 0đ"
                        onCategoryClick("Cho tặng")
                    },
                    onDealsClick = {
                        selectedCategory = "⚡ Deal rẻ"
                        onCategoryClick("Giá rẻ")
                    }
                )
            }

            // --- 3. SMART AI SEARCH & CHIP FILTER BAR (Thumb-zone) ---
            item(key = "home_search_filters") {
                SearchAndFilterSection(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    selectedCategory = selectedCategory,
                    onCategorySelect = { category ->
                        selectedCategory = category
                        onCategoryClick(category)
                    },
                    onOpenAISearch = { showAISearchDialog = true }
                )
            }

            // --- 4. FEED HEADER ---
            item(key = "home_feed_header") {
                FeedHeaderSection(
                    totalItems = filteredPosts.size,
                    selectedCategory = selectedCategory
                )
            }

            // --- 5. PRODUCTS GRID OR EMPTY STATE ---
            if (filteredPosts.isEmpty()) {
                item(key = "home_empty_state") {
                    HomeEmptyState(
                        onResetFilter = {
                            selectedCategory = "Tất cả"
                            searchQuery = ""
                        }
                    )
                }
            } else {
                items(
                    items = chunkedPosts,
                    key = { row -> row.firstOrNull()?.id ?: row.hashCode() }
                ) { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        for (post in rowItems) {
                            Box(modifier = Modifier.weight(1f)) {
                                GridPostCard(
                                    post = post,
                                    onClick = { onPostClick(post) }
                                )
                            }
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        if (showAISearchDialog) {
            AISearchDialog(
                onDismiss = { showAISearchDialog = false },
                onAISearchSubmit = { _, promptQuery ->
                    showAISearchDialog = false
                    if (promptQuery.isNotBlank()) {
                        searchQuery = promptQuery
                    }
                }
            )
        }
    }
}

/**
 * 1. Thanh Chào Đón & Định Vị Giảng Đường
 */
@Composable
private fun CampusGreetingSection(
    userName: String,
    userUniversity: String,
    onChatClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    val shortName = remember(userName) {
        userName.trim().split(' ').lastOrNull() ?: "Bạn"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Tên thương hiệu & Lời chào thân thiện
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "StuMart",
                        color = StuMartGreen,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "🦆", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Vị trí trường đại học dạng chip gọn gàng
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(StuMartGreenContainer)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = StuMartGreen,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = userUniversity,
                        color = StuMartGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Các nút hành động Chat & Thông báo (Đạt chuẩn 48dp touch target)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                // Chat Icon Button
                IconButton(
                    onClick = onChatClick,
                    modifier = Modifier
                        .size(48.dp)
                        .semantics {
                            contentDescription = "Tin nhắn"
                            role = Role.Button
                        }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 1.dp,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.ChatBubbleOutline,
                                    contentDescription = null,
                                    tint = StuMartTextDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        // Badge tin nhắn mới
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 2.dp, y = (-2).dp)
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(StuMartBadgeRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "3",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Notification Icon Button
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier
                        .size(48.dp)
                        .semantics {
                            contentDescription = "Thông báo"
                            role = Role.Button
                        }
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 1.dp,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.NotificationsNone,
                                contentDescription = null,
                                tint = StuMartTextDark,
                                modifier = Modifier.size(21.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Câu chào hóm hỉnh sinh viên
        Text(
            text = "Chào $shortName! Hôm nay trường mình có gì hot?",
            color = StuMartTextDark,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * 2. Cụm Bento Khám Phá Nhanh (Bento Quick Hub)
 * Thiết kế 2 khối bất đối xứng: Góc 0đ & Săn Deal KTX
 */
@Composable
private fun BentoQuickHub(
    onFreeGiveawayClick: () -> Unit,
    onDealsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Khối Bento 1: Góc 0đ Cho Tặng
        Surface(
            onClick = onFreeGiveawayClick,
            shape = RoundedCornerShape(18.dp),
            color = StuMartGreenContainer,
            border = BorderStroke(1.dp, StuMartGreen.copy(alpha = 0.15f)),
            modifier = Modifier
                .weight(1.15f)
                .height(115.dp)
                .semantics {
                    role = Role.Button
                    contentDescription = "Góc Cho Tặng 0đ: Nhận giáo trình và đồ dùng miễn phí"
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(StuMartGreen)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "🎁 GÓC 0Đ",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Text(text = "📚", fontSize = 16.sp)
                }

                Column {
                    Text(
                        text = "Tủ đồ KTX Cho Tặng",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = StuMartTextDark,
                        lineHeight = 18.sp
                    )
                    Text(
                        text = "Đồ dùng sinh viên miễn phí →",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = StuMartGreen
                    )
                }
            }
        }

        // Khối Bento 2: Săn Deal KTX Giảm tới 70%
        Surface(
            onClick = onDealsClick,
            shape = RoundedCornerShape(18.dp),
            color = StuMartCream,
            border = BorderStroke(1.dp, StuMartOrange.copy(alpha = 0.25f)),
            modifier = Modifier
                .weight(0.95f)
                .height(115.dp)
                .semantics {
                    role = Role.Button
                    contentDescription = "Săn Deal KTX: Giảm giá tới 70%"
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(StuMartOrange)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "⚡ HOT DEAL",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Text(text = "🔥", fontSize = 16.sp)
                }

                Column {
                    Text(
                        text = "Deal KTX -70%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = StuMartTextDark
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = StuMartOrange,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Kết thúc trong ngày",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = StuMartOrange
                        )
                    }
                }
            }
        }
    }
}

/**
 * 3. Thanh Tìm Kiếm Thông Minh AI & Hàng Chip Gợi Ý / Danh Mục
 */
@Composable
private fun SearchAndFilterSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    onOpenAISearch: () -> Unit
) {
    val categories = listOf(
        "Tất cả",
        "📚 Sách",
        "💻 Điện tử",
        "👕 Thời trang",
        "🛋️ KTX",
        "🎁 Đồ 0đ",
        "⚡ Deal rẻ",
        "🎮 Giải trí",
        "📦 Khác"
    )

    val aiRecommendations = listOf(
        "📚 Sách C++ / Giải tích",
        "💻 Laptop sinh viên < 5tr",
        "🛋️ Đồ KTX giá hời",
        "🎧 Tai nghe Bluetooth",
        "👕 Áo Blouse Bách Khoa"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // --- Thanh Tìm Kiếm Thông Minh AI ---
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            border = BorderStroke(1.5.dp, StuMartGreen.copy(alpha = 0.6f)),
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search Icon",
                    tint = StuMartGreen,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Input nhập liệu tìm kiếm
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            text = "✨ Tìm AI / Sách, Laptop, Đồ KTX...",
                            color = StuMartTextGray,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = StuMartTextDark,
                        unfocusedTextColor = StuMartTextDark
                    ),
                    modifier = Modifier.weight(1f)
                )

                // Nút xóa từ khóa nếu đã nhập
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { onSearchQueryChange("") },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search",
                            tint = StuMartTextGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }

                // Nút "AI Search / Chụp Ảnh AI"
                Surface(
                    onClick = onOpenAISearch,
                    shape = RoundedCornerShape(16.dp),
                    color = StuMartGreen,
                    modifier = Modifier.height(36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = "AI Camera Search",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "AI Search",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- Hàng Gợi Ý Thông Minh AI (Smart AI Tagging Chips) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = StuMartYellow.copy(alpha = 0.25f),
                border = BorderStroke(1.dp, StuMartYellow)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Recommendation",
                        tint = StuMartOrange,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "AI Gợi ý:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StuMartOrange
                    )
                }
            }

            aiRecommendations.forEach { tag ->
                Surface(
                    onClick = {
                        val keyword = tag.replace(Regex("^[\\p{So}\\p{Cn}]\\s*"), "").trim()
                        onSearchQueryChange(keyword)
                    },
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier.height(28.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tag,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = StuMartTextDark
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Dòng Chip Danh Mục Trượt Ngang (Single-Row Horizontal Scroll)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = category == selectedCategory
                Surface(
                    onClick = { onCategorySelect(category) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) StuMartGreen else Color.White,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) StuMartGreen else Color(0xFFE5E7EB)
                    ),
                    modifier = Modifier.height(34.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else StuMartTextDark
                        )
                    }
                }
            }
        }
    }
}

/**
 * Dialog Tìm kiếm bằng AI & Hình ảnh (Smart Recommendation & AI Tagging)
 * Sẵn sàng kết nối với API AI Image / Prompt Search của bạn sau này.
 */
@Composable
fun AISearchDialog(
    onDismiss: () -> Unit,
    onAISearchSubmit: (selectedUri: Uri?, prompt: String) -> Unit
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var promptQuery by remember { mutableStateOf("") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> selectedImageUri = uri }
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🤖", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AI Visual Search & Tagging",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = StuMartGreen
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Tải ảnh sản phẩm/giáo trình hoặc nhập yêu cầu để AI phân tích & gợi ý món đồ phù hợp nhất.",
                    fontSize = 13.sp,
                    color = StuMartTextGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Khung tải ảnh bằng AI
                Text("1. Chọn ảnh món đồ cần tìm", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StuMartTextDark)
                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(StuMartLightBackground)
                        .clickable { photoPickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Search Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                                .clickable { selectedImageUri = null },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("×", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Canvas(modifier = Modifier.matchParentSize()) {
                            drawRoundRect(
                                color = StuMartGreen,
                                style = Stroke(
                                    width = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                                ),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Outlined.PhotoCamera,
                                contentDescription = null,
                                tint = StuMartGreen,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Bấm để chọn ảnh từ thư viện",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = StuMartGreen
                            )
                            Text(
                                text = "AI sẽ quét & tự động phân tích tag",
                                fontSize = 10.sp,
                                color = StuMartTextGray
                            )
                        }
                    }
                }

                if (selectedImageUri != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    // Mô phỏng AI Tagging
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = StuMartGreenContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("✨ AI Tagging:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StuMartGreen)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("#Giaotrinh #BachKhoa #DienTu", fontSize = 11.sp, color = StuMartGreen)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ô nhập câu hỏi / AI Prompt
                Text("2. Nhập mô tả / Câu hỏi cho AI", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StuMartTextDark)
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = promptQuery,
                    onValueChange = { promptQuery = it },
                    placeholder = {
                        Text(
                            text = "VD: Tìm sách đúng tái bản mới nhất, giá rẻ dưới 100k...",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = StuMartGreen
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "💡 Chú thích: Bạn có thể đưa API AI Visual Search của mình vào callback khi sẵn sàng.",
                    fontSize = 10.sp,
                    color = StuMartTextGray
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedImageUri == null && promptQuery.isBlank()) {
                        Toast.makeText(context, "Vui lòng chọn ảnh hoặc nhập mô tả", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    /*
                     * =========================================================================
                     * TODO API: ĐÂY LÀ NƠI BẠN TÍCH HỢP GỌI API AI SEARCH & SMART RECOMMENDATION
                     * Ví dụ:
                     * val result = AIService.searchByImageAndPrompt(selectedImageUri, promptQuery)
                     * =========================================================================
                     */
                    Toast.makeText(context, "Đã quét AI thành công!", Toast.LENGTH_SHORT).show()
                    onAISearchSubmit(selectedImageUri, promptQuery)
                },
                colors = ButtonDefaults.buttonColors(containerColor = StuMartGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("🚀 Phân tích & Tìm kiếm", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color.Gray)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}

/**
 * 4. Tiêu Đề Khu Vực Danh Sách Món Đồ
 */
@Composable
private fun FeedHeaderSection(
    totalItems: Int,
    selectedCategory: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = if (selectedCategory == "Tất cả") "Mới lên kệ tại trường" else selectedCategory,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = StuMartTextDark
            )
            Text(
                text = "$totalItems bài đăng quanh bạn",
                fontSize = 12.sp,
                color = StuMartTextGray
            )
        }

        // Tag sinh viên xác thực
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(StuMartGreenContainer)
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(text = "🛡️ 100% Sinh viên", fontSize = 11.sp, color = StuMartGreen, fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * 5. Thẻ Sản Phẩm Mở (Bỏ hoàn toàn Card-lồng-Card)
 */
@Composable
fun GridPostCard(
    post: Post,
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val formattedPrice = remember(post.price, post.type) {
        when {
            post.type == "Cho tặng" || post.price <= 0 -> "Miễn phí (0đ)"
            post.price >= 1_000_000 -> "${(post.price / 1_000_000).toString().removeSuffix(".0")} triệu đ"
            else -> "%,d đ".format(post.price.toInt()).replace(',', '.')
        }
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = BorderStroke(0.5.dp, Color(0xFFE5E7EB)),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                role = Role.Button
                contentDescription = "${post.title}, giá $formattedPrice, tình trạng ${post.condition}"
            }
    ) {
        Column {
            // Khung ảnh sản phẩm tràn viền mép trên
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp)
                    .background(StuMartCream),
                contentAlignment = Alignment.Center
            ) {
                val isValidImage = !post.imageUrl.isNullOrBlank() &&
                        (post.imageUrl.startsWith("http://") || post.imageUrl.startsWith("https://"))

                if (isValidImage) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(post.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = post.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Fallback Mascot
                    Text("🦆", fontSize = 44.sp)
                }

                // Tag Loại bài đăng (Top-Start)
                val badgeBg = when (post.type) {
                    "Giá rẻ" -> StuMartOrange
                    "Cho tặng" -> StuMartGreen
                    "Trao đổi" -> Color(0xFF6750A4)
                    else -> StuMartGreen
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(bottomEnd = 8.dp))
                        .background(badgeBg)
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = post.type,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                // Tag Tình trạng (Top-End)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White.copy(alpha = 0.90f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = post.condition,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = StuMartTextDark
                    )
                }
            }

            // Phần thân hiển thị thông tin sản phẩm (Dòng chảy liền mạch, không hộp lồng hộp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Tiêu đề
                Text(
                    text = post.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = StuMartTextDark,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Dòng người bán & danh mục
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = StuMartTextGray,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = post.sellerName ?: "Sinh viên",
                            fontSize = 11.sp,
                            color = StuMartTextGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(StuMartGreenContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = post.category ?: "Khác",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = StuMartGreen,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Mức giá nổi bật
                Text(
                    text = formattedPrice,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = if (post.type == "Cho tặng" || post.price <= 0) StuMartGreen else StuMartOrange
                )
            }
        }
    }
}

/**
 * Trạng thái trống (Empty State)
 */
@Composable
private fun HomeEmptyState(
    onResetFilter: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🦆", fontSize = 54.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Chưa có món đồ nào trong mục này!",
                color = StuMartTextDark,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Hãy thử chọn danh mục khác hoặc là người đầu tiên đăng bán nhé.",
                color = StuMartTextGray,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedButton(
                onClick = onResetFilter,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, StuMartGreen),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = StuMartGreen)
            ) {
                Text("Xem tất cả bài đăng", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}
