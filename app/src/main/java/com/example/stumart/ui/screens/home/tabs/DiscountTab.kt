package com.example.stumart.ui.screens.home.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stumart.ui.theme.*

data class DiscountProduct(
    val title: String,
    val discount: String,
    val newPrice: String,
    val oldPrice: String,
    val conditionAndLocation: String,
    val sellerName: String
)

val mockDiscountProducts = listOf(
    DiscountProduct(
        title = "Giáo trình Giải tích A1 – Bách Khoa HN",
        discount = "-56%",
        newPrice = "35.000đ",
        oldPrice = "80.000đ",
        conditionAndLocation = "Cũ • Đống Đa, HN",
        sellerName = "Minh Tuấn"
    ),
    DiscountProduct(
        title = "Laptop Dell XPS 13 – Core i5, RAM 8GB",
        discount = "-29%",
        newPrice = "8.5 triệu đ",
        oldPrice = "12 triệu đ",
        conditionAndLocation = "Như mới • Cầu Giấy, HN",
        sellerName = "Thu Hà"
    ),
    DiscountProduct(
        title = "Balo laptop Samsonite 15.6 inch",
        discount = "-50%",
        newPrice = "450.000đ",
        oldPrice = "900.000đ",
        conditionAndLocation = "Cũ • Hoàng Mai, HN",
        sellerName = "Lan Anh"
    ),
    DiscountProduct(
        title = "Máy tính Casio FX-580VN X – còn bảo hành",
        discount = "-57%",
        newPrice = "400.000đ",
        oldPrice = "650.000đ",
        conditionAndLocation = "Mới • Hai Bà Trưng, HN",
        sellerName = "Tuấn Tú"
    )
)

@Composable
fun DiscountTab() {
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
                            .background(Color(0xFF2A1600)) // Nâu đen
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
                            // Timer
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
                item { CategoryFilterChip(text = "Tất cả", isSelected = true) }
                item { CategoryFilterChip(text = "Sách", isSelected = false) }
                item { CategoryFilterChip(text = "Điện tử", isSelected = false) }
                item { CategoryFilterChip(text = "Thời trang", isSelected = false) }
                item { CategoryFilterChip(text = "Đồ dùng", isSelected = false) }
            }
        }

        // --- DANH SÁCH SẢN PHẨM (Items) ---
        items(mockDiscountProducts) { product ->
            DiscountProductCard(product)
        }
    }
}

@Composable
fun CategoryFilterChip(text: String, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) StuMartOrange else Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
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
fun DiscountProductCard(product: DiscountProduct) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(140.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Ảnh sản phẩm + Tag giảm giá
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .fillMaxHeight()
                    .background(Color.LightGray)
            ) {
                // Label % giảm giá
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomEnd = 16.dp))
                        .background(StuMartOrange)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = product.discount,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            // Thông tin chi tiết
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Text(
                    text = product.title,
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
                            text = product.conditionAndLocation.substringBefore(" •"),
                            fontSize = 10.sp,
                            color = StuMartTextGray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = " • " + product.conditionAndLocation.substringAfter("• "),
                        fontSize = 11.sp,
                        color = StuMartTextGray
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = product.newPrice,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StuMartOrange
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = product.oldPrice,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // User & Button
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
                        Text(text = product.sellerName, fontSize = 12.sp, color = StuMartTextGray)
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
