package com.example.stumart.ui.screens.home.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stumart.ui.theme.StuMartGreen
import com.example.stumart.ui.theme.StuMartLightBackground
import com.example.stumart.ui.theme.StuMartTextDark
import com.example.stumart.ui.theme.StuMartTextGray
import com.example.stumart.ui.theme.StuMartYellow

@Composable
fun HomeTab() {
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
                                text = "ĐH Bách Khoa Hà Nội",
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
                .background(StuMartGreen) // Có thể thêm sọc xéo (stripe) ở đây sau
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
