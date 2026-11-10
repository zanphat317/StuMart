package com.example.stumart.ui.screens.home.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.stumart.data.repository.PostRepository
import com.example.stumart.data.supabase.Post
import com.example.stumart.ui.theme.*

@Composable
fun DiscountTab() {
    val posts by PostRepository.posts.collectAsState()
    var selectedCategory by remember { mutableStateOf("Tất cả") }

    // Lọc bài đăng theo type == "Giá rẻ"
    val discountPosts = posts.filter { 
        it.type == "Giá rẻ" || (selectedCategory == "Tất cả" && it.type == "Giá rẻ") 
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(StuMartLightBackground),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // --- HEADER TỔNG (Màu Cam) ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = StuMartOrange,
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⚡", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Góc Giá Rẻ",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Text(
                        text = "Giảm sốc mỗi ngày – số lượng có hạn!",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    // Flash sale dark box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF2A1600))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🔥", fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Flash Sale đang diễn ra",
                                        color = StuMartYellow,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Kết thúc sau:",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "01",
                                    color = StuMartYellow,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(":", color = StuMartYellow, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "48",
                                    color = StuMartYellow,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(":", color = StuMartYellow, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "22",
                                    color = StuMartYellow,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- BỘ LỌC CATEGORY (Tabs) ---
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { CategoryFilterChip(text = "Tất cả", isSelected = selectedCategory == "Tất cả", onClick = { selectedCategory = "Tất cả" }) }
                item { CategoryFilterChip(text = "Sách", isSelected = selectedCategory == "Sách", onClick = { selectedCategory = "Sách" }) }
                item { CategoryFilterChip(text = "Điện tử", isSelected = selectedCategory == "Điện tử", onClick = { selectedCategory = "Điện tử" }) }
                item { CategoryFilterChip(text = "Thời trang", isSelected = selectedCategory == "Thời trang", onClick = { selectedCategory = "Thời trang" }) }
                item { CategoryFilterChip(text = "Đồ dùng", isSelected = selectedCategory == "Đồ dùng", onClick = { selectedCategory = "Đồ dùng" }) }
            }
        }

        // --- DANH SÁCH SẢN PHẨM (Items) ---
        items(discountPosts) { post ->
            DiscountProductCard(post)
        }
    }
}

@Composable
fun CategoryFilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) StuMartOrange else Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else StuMartTextDark,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun DiscountProductCard(post: Post) {
    val formattedPrice = if (post.price >= 1000000) "${post.price / 1000000} triệu đ" else "${post.price.toInt()}đ"
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(140.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .fillMaxHeight()
                    .background(Color.LightGray)
            ) {
                if (!post.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = post.imageUrl,
                        contentDescription = post.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomEnd = 16.dp))
                        .background(StuMartOrange)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = post.type,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Text(
                    text = post.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = StuMartTextDark,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray.copy(alpha = 0.5f))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = post.condition,
                            fontSize = 10.sp,
                            color = StuMartTextGray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = " • ${post.location ?: "Hà Nội"}",
                        fontSize = 11.sp,
                        color = StuMartTextGray
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = formattedPrice,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StuMartOrange
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = post.sellerName ?: "Sinh viên", fontSize = 12.sp, color = StuMartTextGray)
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🤍", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(StuMartOrange)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("Mua", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
