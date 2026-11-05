package com.example.stumart.ui.screens.home.tabs

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stumart.data.supabase.Post
import com.example.stumart.data.supabase.SupabaseClient
import com.example.stumart.data.supabase.UserProfile
import com.example.stumart.ui.screens.home.InterestsSurveyDialog
import com.example.stumart.ui.theme.StuMartGreen
import com.example.stumart.ui.theme.StuMartLightBackground
import com.example.stumart.ui.theme.StuMartTextDark
import com.example.stumart.ui.theme.StuMartTextGray
import com.example.stumart.ui.theme.StuMartYellow
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTab(userUniversity: String = "ĐH Bách Khoa Hà Nội") {
    var showSurveyDialog by remember { mutableStateOf(false) }
    var userInterests by remember { mutableStateOf<List<String>>(emptyList()) }
    var postsList by remember { mutableStateOf<List<Post>>(emptyList()) }
    var selectedCategoryFilter by remember { mutableStateOf("Tất cả") }
    var isLoadingPosts by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Tải thông tin Profile & Posts từ Supabase khi mở HomeTab
    LaunchedEffect(Unit) {
        val currentUser = SupabaseClient.client.auth.currentUserOrNull()
        if (currentUser != null) {
            try {
                val profile = SupabaseClient.client.postgrest["profiles"]
                    .select { filter { eq("id", currentUser.id) } }
                    .decodeSingleOrNull<UserProfile>()
                
                if (profile != null) {
                    if (profile.interests.isNullOrEmpty()) {
                        showSurveyDialog = true
                    } else {
                        userInterests = profile.interests
                    }
                } else {
                    showSurveyDialog = true
                }
            } catch (e: Exception) {
                Log.e("HOME_TAB", "Lỗi lấy profile: ${e.message}")
            }
        }

        // Tải danh sách bài đăng từ Supabase
        try {
            val posts = SupabaseClient.client.postgrest["posts"]
                .select()
                .decodeList<Post>()
            postsList = posts
        } catch (e: Exception) {
            Log.e("HOME_TAB", "Lỗi lấy bài đăng: ${e.message}")
        } finally {
            isLoadingPosts = false
        }
    }

    // Hiển thị Dialog Khảo sát Sở thích nếu interests bị RỖNG
    if (showSurveyDialog) {
        InterestsSurveyDialog(
            onDismiss = { showSurveyDialog = false },
            onSaveInterests = { selected ->
                userInterests = selected
                showSurveyDialog = false
                
                // Cập nhật sở thích lên Supabase Profiles
                val currentUser = SupabaseClient.client.auth.currentUserOrNull()
                if (currentUser != null) {
                    coroutineScope.launch {
                        try {
                            SupabaseClient.client.postgrest["profiles"].update({
                                set("interests", selected)
                            }) {
                                filter { eq("id", currentUser.id) }
                            }
                            Toast.makeText(context, "Cập nhật sở thích thành công!", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Log.e("HOME_TAB", "Lỗi lưu sở thích: ${e.message}")
                        }
                    }
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StuMartLightBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // --- HEADER SECTION (Green Area) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = StuMartGreen,
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                )
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
        ) {
            Column {
                // Top Row: Location & Icons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📍 ", fontSize = 14.sp)
                            Text(
                                text = userUniversity,
                                color = StuMartYellow,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text(
                            text = "StuMart",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Chat Icon with badge
                        Box {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("💬", fontSize = 16.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("3", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        // Notification Icon
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🔔", fontSize = 16.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color.White)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🔍", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tìm sách, laptop, quần áo...",
                        color = StuMartTextGray,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(StuMartLightBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🦆", fontSize = 16.sp)
                    }
                }
            }
        }

        // --- BANNER SECTION ---
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(StuMartGreen)
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
                Text("Flash Sale 🔥", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Giảm đến 70%", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                Text("Chỉ hôm nay!", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Xem ngay →", color = StuMartGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // --- BỘ LỌC THEO SỞ THÍCH NGHỆ THUẬT (PERSONALIZED FEED) ---
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Dành riêng cho bạn",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = StuMartTextDark
                )
            }

            // Nút mở lại Khảo sát sở thích
            Text(
                text = "Tùy chỉnh 🎯",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = StuMartGreen,
                modifier = Modifier.clickable { showSurveyDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Dãy Chip danh mục lọc (Lấy danh sách từ userInterests + "Tất cả")
        val filterCategories = remember(userInterests) {
            val list = mutableListOf("Tất cả")
            if (userInterests.isNotEmpty()) {
                list.addAll(userInterests)
            } else {
                list.addAll(listOf("Sách/Giáo trình", "Đồ điện tử", "Thời trang/Phụ kiện", "Đồ gia dụng"))
            }
            list.distinct()
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filterCategories) { category ->
                FilterChip(
                    selected = selectedCategoryFilter == category,
                    onClick = { selectedCategoryFilter = category },
                    label = { Text(category, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StuMartGreen,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = StuMartTextDark
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        // --- DANH SÁCH BÀI ĐĂNG TỪ SUPABASE (LỌC THEO CATEGORY) ---
        Spacer(modifier = Modifier.height(16.dp))

        // Lọc bài đăng
        val filteredPosts = remember(postsList, selectedCategoryFilter, userInterests) {
            if (selectedCategoryFilter == "Tất cả") {
                // Sắp xếp ưu tiên bài đăng nằm trong sở thích của user lên đầu
                if (userInterests.isNotEmpty()) {
                    postsList.sortedByDescending { post ->
                        post.category != null && userInterests.contains(post.category)
                    }
                } else {
                    postsList
                }
            } else {
                postsList.filter { post ->
                    post.category?.contains(selectedCategoryFilter, ignoreCase = true) == true
                }
            }
        }

        if (isLoadingPosts) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = StuMartGreen)
            }
        } else if (filteredPosts.isEmpty()) {
            // Mẫu bài đăng mặc định khi chưa có bài đăng thật trên Supabase
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { ProductCard("-56%", "Giáo trình Giải tích A1...", "35.000đ") }
                item { ProductCard("-29%", "Laptop Dell XPS 13 - i5", "8.5 triệu đ") }
                item { ProductCard("-50%", "Balo laptop Samsonite", "450.000đ") }
            }
        } else {
            // Hiển thị danh sách bài đăng thật lấy từ Supabase
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredPosts) { post ->
                    RealPostCard(post = post)
                }
            }
        }

        // --- CATEGORIES SECTION (DANH MỤC TRUYỀN THỐNG) ---
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "Khám phá danh mục",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = StuMartTextDark,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                CategoryItem("📚", "Sách", Modifier.weight(1f))
                CategoryItem("💻", "Điện tử", Modifier.weight(1f))
                CategoryItem("👕", "Thời trang", Modifier.weight(1f))
                CategoryItem("🪑", "Nội thất", Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                CategoryItem("🎮", "Giải trí", Modifier.weight(1f))
                CategoryItem("🧴", "Làm đẹp", Modifier.weight(1f))
                CategoryItem("🚲", "Xe cộ", Modifier.weight(1f))
                CategoryItem("📦", "Khác", Modifier.weight(1f))
            }
        }

        // --- DEAL HỜI HÔM NAY ---
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚡", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Deal hời hôm nay", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = StuMartTextDark)
            }
            
            // Countdown timer
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TimeBox("01")
                Text(":", fontWeight = FontWeight.Bold)
                TimeBox("48")
                Text(":", fontWeight = FontWeight.Bold)
                TimeBox("22")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { ProductCard("-56%", "Giáo trình Giải tích A1...", "35.000đ") }
            item { ProductCard("-29%", "Laptop Dell XPS 13 - i5", "8.5 triệu đ") }
            item { ProductCard("-50%", "Balo laptop Samsonite", "450.000đ") }
        }
        
        Spacer(modifier = Modifier.height(100.dp)) // padding cho Bottom Nav
    }
}

@Composable
fun RealPostCard(post: Post) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(220.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (post.category) {
                        "Sách/Giáo trình" -> "📚"
                        "Đồ điện tử" -> "💻"
                        "Thời trang/Phụ kiện" -> "👕"
                        "Đồ gia dụng" -> "🏠"
                        "Nội thất" -> "🪑"
                        "Xe cộ" -> "🚲"
                        else -> "📦"
                    },
                    fontSize = 40.sp
                )

                // Category tag
                if (!post.category.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clip(RoundedCornerShape(bottomEnd = 8.dp))
                            .background(StuMartGreen)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(post.category, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = post.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    color = StuMartTextDark
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                val formattedPrice = if (post.price >= 1_000_000) {
                    "${(post.price / 1_000_000).toString().removeSuffix(".0")} triệu đ"
                } else {
                    "${java.text.NumberFormat.getIntegerInstance().format(post.price.toLong())}đ"
                }

                Text(
                    text = formattedPrice,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = StuMartGreen
                )
            }
        }
    }
}

@Composable
fun CategoryItem(emoji: String, title: String, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 28.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, fontSize = 12.sp, color = StuMartTextDark, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun TimeBox(time: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.Black)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Text(time, color = StuMartYellow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}

@Composable
fun ProductCard(discount: String, title: String, price: String) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.LightGray)
            ) {
                        // Discount tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomEnd = 8.dp))
                        .background(Color(0xFFFF7A45))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(discount, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    color = StuMartTextDark
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = StuMartGreen
                )
            }
        }
    }
}
