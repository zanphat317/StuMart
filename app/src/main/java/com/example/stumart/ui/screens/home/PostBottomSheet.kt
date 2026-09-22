package com.example.stumart.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stumart.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostBottomSheetContent(onDismiss: () -> Unit) {
    var productName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("Mới") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f) // Chiếm 90% chiều cao màn hình
            .background(StuMartLightBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📦", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Đăng tin mới", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = StuMartTextDark)
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = StuMartTextGray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Loại tin đăng
        Text("Loại tin đăng", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = StuMartTextDark)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            item { PostTypeChip(icon = "🏷️", label = "Bán thường", isSelected = true) }
            item { PostTypeChip(icon = "⚡", label = "Giá rẻ", isSelected = false) }
            item { PostTypeChip(icon = "🎁", label = "Cho tặng", isSelected = false) }
            item { PostTypeChip(icon = "🔨", label = "Đấu giá", isSelected = false) }
            item { PostTypeChip(icon = "❓", label = "Túi mù", isSelected = false) }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Ảnh sản phẩm
        Text("Ảnh sản phẩm", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = StuMartTextDark)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Nút thêm ảnh (Viền nét đứt)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .clickable { /* Mở thư viện ảnh */ }
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.matchParentSize()) {
                    drawRoundRect(
                        color = StuMartGreen,
                        style = Stroke(
                            width = 3.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                        ),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
                    )
                }
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("+", fontSize = 24.sp, color = StuMartGreen, fontWeight = FontWeight.Bold)
                    Text("Thêm ảnh", fontSize = 10.sp, color = StuMartGreen, fontWeight = FontWeight.Medium)
                }
            }

            // Ảnh Demo 1
            MockImageWithRemoveBtn()
            // Ảnh Demo 2
            MockImageWithRemoveBtn()
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Form điền thông tin
        Text("Tên sản phẩm", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = StuMartTextDark)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            placeholder = { Text("VD: Laptop Dell i5, còn bảo hành...", fontSize = 14.sp, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = StuMartGreen
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Giá (đ)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = StuMartTextDark)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    placeholder = { Text("0", fontSize = 14.sp, color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = StuMartGreen
                    )
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Tình trạng", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = StuMartTextDark)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = condition,
                    onValueChange = { condition = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = StuMartGreen
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Mô tả", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = StuMartTextDark)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Mô tả chi tiết tình trạng, lý do bán...", fontSize = 14.sp, color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = StuMartGreen
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Nút đăng tin
        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = StuMartGreen),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Đăng tin ngay", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(32.dp)) // Padding dưỡi cùng
    }
}

@Composable
fun PostTypeChip(icon: String, label: String, isSelected: Boolean) {
    val borderColor = if (isSelected) StuMartGreen else Color.Transparent
    val textColor = if (isSelected) StuMartGreen else StuMartTextGray
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { /* Change type */ }
    ) {
        Text(icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 12.sp, color = textColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun MockImageWithRemoveBtn() {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
    ) {
        // Mock remove button
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Text("×", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
