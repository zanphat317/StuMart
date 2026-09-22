package com.example.stumart.ui.screens.home.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stumart.ui.theme.*

@Composable
fun ProfileTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StuMartLightBackground)
    ) {
        // --- NỘI DUNG CHÍNH (Được kéo lên đè lên Header) ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header được đẩy vào trong LazyColumn để trôi theo cuộn
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = StuMartGreen,
                            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                        )
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 72.dp) // Tăng bottom padding cho khối Card trồi lên đè
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Cá nhân", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("👤", fontSize = 24.sp)
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            // 1. Thẻ thông tin cá nhân (Card đè lên Header xanh)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-40).dp), // Kéo Card lên đè 40dp
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Avatar
                            Box {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(Color.LightGray)
                                        .border(2.dp, StuMartYellow, CircleShape)
                                ) {
                                    // Thay bằng painterResource của ảnh thực tế nếu có
                                    Box(modifier = Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.3f)))
                                }
                                // Tích xanh / Vàng chứng nhận
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(StuMartYellow)
                                        .border(1.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Column {
                                Text("Nguyễn Minh Tuấn", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = StuMartTextDark)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("ĐH Bách Khoa HN • K68", fontSize = 12.sp, color = StuMartTextGray)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("⭐⭐⭐⭐⭐", fontSize = 10.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("4.9 (32 đánh giá)", fontSize = 12.sp, color = StuMartTextGray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Thống kê (Đã bán, Đã mua, Uy tín)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatBox(icon = "📦", number = "28", label = "Đã bán")
                            StatBox(icon = "🛒", number = "14", label = "Đã mua")
                            StatBox(icon = "🏆", number = "98%", label = "Uy tín")
                        }
                    }
                }
            }

            // 2. Huy hiệu thành tích
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp).offset(y = (-40).dp)) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🦆", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Huy hiệu thành tích", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StuMartTextDark)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BadgeItem("⭐", "Top Seller", StuMartYellow)
                        BadgeItem("🤝", "Tin cậy", StuMartGreen)
                        BadgeItem("🔥", "Deal Hời", StuMartOrange)
                        BadgeItem("🎁", "Từ thiện", Color(0xFF9C27B0))
                    }
                }
            }

            // 3. Tin đăng của tôi
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp).offset(y = (-40).dp)) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📋", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Tin đăng của tôi", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StuMartTextDark)
                        }
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(StuMartGreen)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("+ Đăng mới", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // 4. Lưới sản phẩm (Tin đăng của tôi)
            val myProducts = listOf(
                MyProduct("Giáo trình Giải tích A1 - ...", "35.000đ", "34", "12"),
                MyProduct("Laptop Dell XPS 13 - Core...", "8.5 triệu đ", "34", "45"),
                MyProduct("Tai nghe chụp tai Sony...", "200.000đ", "12", "5"),
                MyProduct("Balo laptop nam đen...", "150.000đ", "45", "10")
            )
            
            val chunkedProducts = myProducts.chunked(2)
            
            items(chunkedProducts.size) { rowIndex ->
                val rowItems = chunkedProducts[rowIndex]
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).offset(y = (-40).dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    for (product in rowItems) {
                        Box(modifier = Modifier.weight(1f)) {
                            MyProductCard(product)
                        }
                    }
                    // Nếu hàng cuối bị lẻ 1 item thì thêm ô trống để lấp chỗ
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StatBox(icon: String, number: String, label: String) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(StuMartLightBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 20.sp)
            Text(number, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StuMartGreen)
            Text(label, fontSize = 12.sp, color = StuMartTextGray)
        }
    }
}

@Composable
fun BadgeItem(emoji: String, label: String, borderColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(2.dp, borderColor, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 28.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 12.sp, color = StuMartTextDark)
    }
}

data class MyProduct(val title: String, val price: String, val views: String, val likes: String)

@Composable
fun MyProductCard(product: MyProduct) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.LightGray)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StuMartTextDark,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = StuMartGreen
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("👁️ ${product.views}", fontSize = 10.sp, color = StuMartTextGray)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("❤️ ${product.likes}", fontSize = 10.sp, color = StuMartTextGray)
                }
            }
        }
    }
}
