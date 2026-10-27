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
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.drawscope.Stroke
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.stumart.data.supabase.Post
import com.example.stumart.data.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import com.example.stumart.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostBottomSheetContent(onDismiss: () -> Unit) {
    var productName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("Mới") }
    var description by remember { mutableStateOf("") }
    
    var selectedType by remember { mutableStateOf("Bán thường") }
    var isLoading by remember { mutableStateOf(false) }
    
    // Quản lý danh sách ảnh (Uris)
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    
    // Launcher mở thư viện ảnh chọn nhiều
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris -> 
            imageUris = imageUris + uris 
        }
    )

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

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
            item { PostTypeChip(icon = "🏷️", label = "Bán thường", isSelected = selectedType == "Bán thường", onClick = { selectedType = "Bán thường" }) }
            item { PostTypeChip(icon = "⚡", label = "Giá rẻ", isSelected = selectedType == "Giá rẻ", onClick = { selectedType = "Giá rẻ" }) }
            item { PostTypeChip(icon = "🎁", label = "Cho tặng", isSelected = selectedType == "Cho tặng", onClick = { selectedType = "Cho tặng" }) }
            item { PostTypeChip(icon = "🔨", label = "Đấu giá", isSelected = selectedType == "Đấu giá", onClick = { selectedType = "Đấu giá" }) }
            item { PostTypeChip(icon = "❓", label = "Túi mù", isSelected = selectedType == "Túi mù", onClick = { selectedType = "Túi mù" }) }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Ảnh sản phẩm
        Text("Ảnh sản phẩm", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = StuMartTextDark)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Nút thêm ảnh (Viền nét đứt)
            item {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .clickable { multiplePhotoPickerLauncher.launch("image/*") }
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
            }

            // Danh sách ảnh đã chọn
            items(imageUris.size) { index ->
                SelectedImageItem(
                    uri = imageUris[index],
                    onRemove = { 
                        imageUris = imageUris.toMutableList().apply { removeAt(index) } 
                    }
                )
            }
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
            onClick = {
                if (productName.isBlank() || price.isBlank() || description.isBlank()) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val priceValue = price.toDoubleOrNull()
                if (priceValue == null) {
                    Toast.makeText(context, "Giá trị không hợp lệ", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true
                coroutineScope.launch {
                    try {
                        val newPost = Post(
                            title = productName,
                            type = selectedType,
                            price = priceValue,
                            condition = condition,
                            description = description
                        )
                        SupabaseClient.client.postgrest["posts"].insert(newPost)
                        
                        Toast.makeText(context, "Đăng tin thành công!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Lỗi kết nối: ${e.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = StuMartGreen),
            shape = RoundedCornerShape(16.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Đăng tin ngay", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp)) // Padding dưỡi cùng
    }
}

@Composable
fun PostTypeChip(icon: String, label: String, isSelected: Boolean, onClick: () -> Unit = {}) {
    val borderColor = if (isSelected) StuMartGreen else Color.Transparent
    val textColor = if (isSelected) StuMartGreen else StuMartTextGray
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { onClick() }
    ) {
        Text(icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 12.sp, color = textColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SelectedImageItem(uri: Uri, onRemove: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "Selected Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Mock remove button
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Text("×", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
