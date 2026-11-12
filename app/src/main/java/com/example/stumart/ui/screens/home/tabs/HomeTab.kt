package com.example.stumart.ui.screens.home.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.stumart.data.repository.PostRepository
import com.example.stumart.data.supabase.Post
import com.example.stumart.ui.theme.*

@Composable
fun HomeTab(userUniversity: String = "ĐH Bách Khoa Hà Nội") {
    val posts by PostRepository.posts.collectAsState()

    LaunchedEffect(Unit) {
        PostRepository.fetchPosts()
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

        // --- CATEGORIES SECTION ---
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Danh mục",
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

        // --- DEAL HỜI HÔM NAY (LƯỚI 2 CỘT DÀI XUỐNG KIỂU SHOPEE) ---
        Spacer(modifier = Modifier.height(28.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚡", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Deal hời hôm nay",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = StuMartTextDark
                )
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

        // Xen kẽ các sản phẩm "Bán thường" và "Giá rẻ"
        val normalList = posts.filter { it.type == "Bán thường" }
        val discountList = posts.filter { it.type == "Giá rẻ" }
        
        val mixedPosts = remember(posts) {
            val list = mutableListOf<Post>()
            val maxSize = maxOf(normalList.size, discountList.size)
            for (i in 0 until maxSize) {
                if (i < normalList.size) list.add(normalList[i])
                if (i < discountList.size) list.add(discountList[i])
            }
            // Nếu không có đủ 2 loại thì lấy tất cả sản phẩm
            if (list.isNotEmpty()) list else posts
        }

        if (mixedPosts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🦆", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Chưa có sản phẩm nào.",
                        color = StuMartTextGray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            val chunkedPosts = mixedPosts.chunked(2)
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                chunkedPosts.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        for (post in rowItems) {
                            Box(modifier = Modifier.weight(1f)) {
                                GridPostCard(post)
                            }
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(100.dp)) // padding cho Bottom Nav
    }
}

@Composable
fun GridPostCard(post: Post) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val formattedPrice = if (post.price >= 1000000) {
        "${(post.price / 1000000).toString().removeSuffix(".0")} triệu đ"
    } else {
        "${post.price.toInt()}đ"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Color(0xFFFFF9E6)),
                contentAlignment = Alignment.Center
            ) {
                val isValidImage = !post.imageUrl.isNullOrBlank() && 
                    (post.imageUrl.startsWith("http://") || post.imageUrl.startsWith("https://"))

                if (isValidImage) {
                    AsyncImage(
                        model = coil.request.ImageRequest.Builder(context)
                            .data(post.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = post.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("🦆", fontSize = 48.sp)

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE53935)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("!", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }

                // Nhãn phân loại loại tin đăng
                val tagColor = if (post.type == "Giá rẻ") StuMartOrange else StuMartGreen
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(bottomEnd = 8.dp))
                        .background(tagColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(post.type, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = post.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = StuMartTextDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray.copy(alpha = 0.4f))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(post.condition, fontSize = 9.sp, color = StuMartTextGray)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = post.sellerName ?: "Sinh viên",
                        fontSize = 10.sp,
                        color = StuMartTextGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // GIÁ TIỀN MỌI MÓN HÀNG ĐỀU MÀU CAM (StuMartOrange)
                Text(
                    text = formattedPrice,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = StuMartOrange
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
