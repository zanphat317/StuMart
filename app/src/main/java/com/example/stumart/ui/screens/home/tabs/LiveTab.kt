package com.example.stumart.ui.screens.home.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.stumart.data.repository.PostRepository
import com.example.stumart.data.supabase.Post
import com.example.stumart.ui.theme.*

@Composable
fun LiveTab() {
    var selectedTab by remember { mutableStateOf("Đấu giá") }
    val posts by PostRepository.posts.collectAsState()

    val auctionPosts = posts.filter { it.type == "Đấu giá" }
    val blindBoxPosts = posts.filter { it.type == "Túi mù" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(StuMartLightBackground)
    ) {
        // --- HEADER TỔNG (Màu Xanh lá) ---
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
            AuctionContent(auctionPosts)
        } else {
            BlindBoxContent(blindBoxPosts)
        }
    }
}

@Composable
fun ToggleButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
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

fun LazyListScope.AuctionContent(posts: List<Post>) {
    item { Spacer(modifier = Modifier.height(16.dp)) }
    
    if (posts.isEmpty()) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Chưa có phiên đấu giá nào.", color = StuMartTextGray, fontSize = 14.sp)
            }
        }
    } else {
        items(posts) { post ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                AuctionCard(post)
            }
        }
    }
    
    item { Spacer(modifier = Modifier.height(80.dp)) }
}

@Composable
fun AuctionCard(post: Post) {
    val formattedPrice = if (post.price >= 1000000) "${post.price / 1000000} triệu đ" else "${post.price.toInt()}đ"
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
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
                        .padding(8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(StuMartGreen)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("● LIVE • Đang đấu giá", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(post.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = StuMartTextDark)
            
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text("Giá hiện tại  ", fontSize = 12.sp, color = StuMartTextGray)
                Text(formattedPrice, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = StuMartGreen)
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Tình trạng: ", fontSize = 12.sp, color = StuMartTextGray)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(StuMartOrange)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(post.condition, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
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

fun LazyListScope.BlindBoxContent(posts: List<Post>) {
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

    if (posts.isEmpty()) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Chưa có túi mù nào.", color = StuMartTextGray, fontSize = 14.sp)
            }
        }
    } else {
        val chunkedBoxes = posts.chunked(2)
        items(chunkedBoxes) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (post in rowItems) {
                    Box(modifier = Modifier.weight(1f)) {
                        BlindBoxPostCard(post)
                    }
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
    
    item { Spacer(modifier = Modifier.height(80.dp)) }
}

@Composable
fun BlindBoxPostCard(post: Post) {
    val formattedPrice = if (post.price >= 1000000) "${post.price / 1000000} triệu đ" else "${post.price.toInt()}đ"
    
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
                    .background(StuMartYellow),
                contentAlignment = Alignment.Center
            ) {
                if (!post.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = post.imageUrl,
                        contentDescription = post.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("🎁", fontSize = 40.sp)
                }
            }
            
            Column(modifier = Modifier.padding(12.dp)) {
                Text(post.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = StuMartTextDark)
                Spacer(modifier = Modifier.height(4.dp))
                Text(post.description, fontSize = 10.sp, color = StuMartTextGray, maxLines = 2)
                
                Spacer(modifier = Modifier.weight(1f))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(formattedPrice, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = StuMartGreen)
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
