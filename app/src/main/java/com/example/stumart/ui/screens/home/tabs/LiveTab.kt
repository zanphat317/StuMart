package com.example.stumart.ui.screens.home.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stumart.ui.theme.*

@Composable
fun LiveTab() {
    var selectedTab by remember { mutableStateOf("Đấu giá") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(StuMartLightBackground)
    ) {
        // --- HEADER TỔNG (Màu Xanh lá) NẰM TRONG LAZYCOLUMN ĐỂ CUỘN THEO ---
        item {
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔨", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Đấu giá & Túi mù",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Toggle Tabs (Đấu giá / Túi mù)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ToggleButton(
                            text = "🔨 Đấu giá", 
                            isSelected = selectedTab == "Đấu giá", 
                            onClick = { selectedTab = "Đấu giá" },
                            modifier = Modifier.weight(1f)
                        )
                        ToggleButton(
                            text = "🎁 Túi mù", 
                            isSelected = selectedTab == "Túi mù", 
                            onClick = { selectedTab = "Túi mù" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // --- CONTENT ---
        if (selectedTab == "Đấu giá") {
            AuctionContent()
        } else {
            BlindBoxContent()
        }
    }
}

@Composable
fun ToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    // Figma shows both buttons as Yellow, but typically we distinguish selected state.
    // Let's use vibrant Yellow for selected, and a slightly dimmer yellow or outlined for unselected.
    val bgColor = if (isSelected) StuMartYellow else StuMartYellow.copy(alpha = 0.5f)
    val textColor = if (isSelected) StuMartTextDark else StuMartTextDark.copy(alpha = 0.7f)
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

// ================= AUCTION =================

data class AuctionItem(
    val title: String,
    val currentPrice: String,
    val startingPrice: String,
    val condition: String,
    val liveCount: String,
    val timeRemaining: String
)

fun LazyListScope.AuctionContent() {
    val mockAuctions = listOf(
        AuctionItem("iPhone 13 Pro 256GB - Deep Purple", "12.5 triệu đ", "8 triệu đ", "Như mới", "7", "02 : 34 : 18"),
        AuctionItem("Xe đạp Giant ATX 610 - 2022", "3.8 triệu đ", "2 triệu đ", "Cũ", "12", "00 : 45 : 30")
    )
    
    // LazyColumn content padding is handled by the parent LazyColumn now.
    // Instead of nesting LazyColumn, we just yield items to the parent.
    item { Spacer(modifier = Modifier.height(16.dp)) }
    items(mockAuctions) { item ->
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            AuctionCard(item)
        }
    }
    item { Spacer(modifier = Modifier.height(80.dp)) } // Bottom nav spacing
}

@Composable
fun AuctionCard(item: AuctionItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Image Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
            ) {
                // Live badge
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(StuMartGreen)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("● LIVE • ${item.liveCount} người đang đấu", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                
                // Timer
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val times = item.timeRemaining.split(" : ")
                    times.forEachIndexed { index, t ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF2A1600).copy(alpha = 0.8f)) // Nâu đen
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(t, color = StuMartYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        if (index < times.size - 1) {
                            Text(":", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = StuMartTextDark)
            
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text("Giá hiện tại  ", fontSize = 12.sp, color = StuMartTextGray)
                Text(item.currentPrice, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = StuMartGreen)
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Giá khởi điểm: ${item.startingPrice} • ", fontSize = 12.sp, color = StuMartTextGray)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(StuMartOrange)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(item.condition, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StuMartOrange),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ra giá 🔨", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

// ================= BLIND BOX =================

data class BlindBoxItem(val name: String, val desc: String, val price: String, val color: Color, val emoji: String)

fun LazyListScope.BlindBoxContent() {
    val mockBoxes = listOf(
        BlindBoxItem("Túi Vịt Con", "Sách • Văn phòng phẩm • Phụ kiện", "50.000đ", StuMartYellow, "🎒"),
        BlindBoxItem("Túi Vịt Vàng", "Điện tử nhỏ • Phụ kiện • Quần áo", "100.000đ", StuMartOrange, "🎁"),
        BlindBoxItem("Túi Vịt Xanh", "Laptop phụ kiện • Điện thoại • Đồ hiệu", "200.000đ", StuMartGreen, "✨"),
        BlindBoxItem("Túi Bí Ẩn", "Siêu bất ngờ – giá trị gấp 3-5x", "500.000đ", Color(0xFF6A1B9A), "🔮")
    )

    // Banner
    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🦆", fontSize = 40.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Túi bí ẩn của vịt 🎉", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = StuMartTextDark)
                    Text("Mỗi túi ẩn chứa đồ ngẫu nhiên từ sinh viên – giá trị có thể gấp nhiều lần!", fontSize = 12.sp, color = StuMartTextGray)
                }
            }
        }
    }

    // Lưới sản phẩm - Chuyển sang dạng chunked List cho LazyColumn
    val chunkedBoxes = mockBoxes.chunked(2)
    items(chunkedBoxes) { rowItems ->
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (box in rowItems) {
                Box(modifier = Modifier.weight(1f)) {
                    BlindBoxCard(box)
                }
            }
            if (rowItems.size == 1) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
    
    item { Spacer(modifier = Modifier.height(80.dp)) }
}

@Composable
fun BlindBoxCard(item: BlindBoxItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(item.color),
                contentAlignment = Alignment.Center
            ) {
                Text(item.emoji, fontSize = 40.sp)
                
                // Sold badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Text("... đã bán", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Column(modifier = Modifier.padding(12.dp)) {
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = StuMartTextDark)
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.desc, fontSize = 10.sp, color = StuMartTextGray, maxLines = 2, lineHeight = 14.sp)
                
                Spacer(modifier = Modifier.weight(1f))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.price, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = StuMartGreen)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(StuMartOrange)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Mua", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
