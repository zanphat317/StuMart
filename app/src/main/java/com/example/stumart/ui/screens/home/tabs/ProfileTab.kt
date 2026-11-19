package com.example.stumart.ui.screens.home.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import android.widget.Toast
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.stumart.data.repository.PostRepository
import com.example.stumart.data.supabase.Post
import com.example.stumart.data.supabase.SupabaseClient
import com.example.stumart.data.supabase.UserProfile
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import com.example.stumart.ui.theme.*

@Composable
fun ProfileTab(
    userName: String,
    userUniversity: String,
    onProfileChange: (name: String, university: String) -> Unit = { _, _ -> },
    onPostClick: (Post) -> Unit = {},
    onEditPostClick: (Post) -> Unit = {},
    onNewPostClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }
    val posts by PostRepository.posts.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val currentUserId = remember { SupabaseClient.client.auth.currentUserOrNull()?.id }
    val myPosts = remember(posts, currentUserId, userName) {
        if (!currentUserId.isNullOrBlank()) {
            posts.filter { it.userId == currentUserId }
        } else {
            posts.filter { it.userId == null || it.sellerName == userName }
        }
    }

    if (showEditProfileDialog) {
        EditProfileDialog(
            currentName = userName,
            currentUniversity = userUniversity,
            onDismiss = { showEditProfileDialog = false },
            onSave = { newName, newUni, newStudentId, newPhone ->
                coroutineScope.launch {
                    try {
                        val currentUser = SupabaseClient.client.auth.currentUserOrNull()
                        if (currentUser != null) {
                            val updatedProfile = UserProfile(
                                id = currentUser.id,
                                email = currentUser.email,
                                fullName = newName,
                                studentId = newStudentId,
                                university = newUni,
                                phone = newPhone
                            )
                            SupabaseClient.client.postgrest["profiles"].upsert(updatedProfile)
                            PostRepository.updateSellerInfoForUser(currentUser.id, newName, newUni)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    onProfileChange(newName, newUni)
                    Toast.makeText(context, "Cập nhật hồ sơ cá nhân thành công!", Toast.LENGTH_SHORT).show()
                    showEditProfileDialog = false
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StuMartLightBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = StuMartGreen,
                            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                        )
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 72.dp)
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
                                .background(Color.White.copy(alpha = 0.2f))
                                .clickable { showEditProfileDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            // 1. Thẻ thông tin cá nhân
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-40).dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(Color.LightGray)
                                        .border(2.dp, StuMartYellow, CircleShape)
                                ) {
                                    Box(modifier = Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.3f)))
                                }
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
                                Text(userName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = StuMartTextDark)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(userUniversity, fontSize = 12.sp, color = StuMartTextGray)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("⭐⭐⭐⭐⭐", fontSize = 10.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("4.9 (32 đánh giá)", fontSize = 12.sp, color = StuMartTextGray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatBox(icon = "📦", number = "${myPosts.size}", label = "Đã bán")
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
                            Text("Tin đăng của tôi (${myPosts.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StuMartTextDark)
                        }
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(StuMartGreen)
                                .clickable { onNewPostClick() }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("+ Đăng mới", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // 4. Lưới sản phẩm (Tin đăng của tôi)
            val chunkedProducts = myPosts.chunked(2)
            
            items(chunkedProducts.size) { rowIndex ->
                val rowItems = chunkedProducts[rowIndex]
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).offset(y = (-40).dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    for (post in rowItems) {
                        Box(modifier = Modifier.weight(1f)) {
                            MyProductCard(
                                post = post,
                                onClick = { onPostClick(post) },
                                onEditClick = { onEditPostClick(post) }
                            )
                        }
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 5. Nút Đăng xuất
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp).offset(y = (-40).dp)) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Đăng xuất", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    currentName: String,
    currentUniversity: String,
    onDismiss: () -> Unit,
    onSave: (name: String, university: String, studentId: String, phone: String) -> Unit
) {
    var fullName by remember { mutableStateOf(currentName) }
    var university by remember { mutableStateOf(currentUniversity) }
    var studentId by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val universities = listOf(
        "ĐH Bách Khoa TP.HCM (ĐHQG-HCM)",
        "ĐH Khoa học Tự nhiên TP.HCM (ĐHQG-HCM)",
        "Trường Đại học Công nghệ Thông tin (ĐHQG-HCM)",
        "ĐH Khoa học Xã hội và Nhân văn TP.HCM (ĐHQG-HCM)",
        "ĐH Kinh tế - Luật TP.HCM (ĐHQG-HCM)",
        "Trường Đại học Quốc tế (ĐHQG-HCM)",
        "ĐH An Giang (ĐHQG-HCM)"
    )

    // Tự động tải hồ sơ cá nhân hiện tại từ Supabase nếu có
    LaunchedEffect(Unit) {
        try {
            val currentUser = SupabaseClient.client.auth.currentUserOrNull()
            if (currentUser != null) {
                val profile = SupabaseClient.client.postgrest["profiles"]
                    .select { filter { eq("id", currentUser.id) } }
                    .decodeSingleOrNull<UserProfile>()
                if (profile != null) {
                    if (!profile.fullName.isNullOrBlank()) fullName = profile.fullName
                    if (!profile.university.isNullOrBlank()) university = profile.university
                    if (!profile.studentId.isNullOrBlank()) studentId = profile.studentId
                    if (!profile.phone.isNullOrBlank()) phone = profile.phone
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⚙️", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Chỉnh sửa trang cá nhân", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = StuMartGreen)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Cập nhật thông tin tài khoản và thông tin sinh viên", fontSize = 12.sp, color = StuMartTextGray)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. Họ và tên
                Text(text = "Họ và tên", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = StuMartTextDark)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = { Text("Nhập họ và tên sinh viên") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = StuMartGreen
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 2. Trường học / Khu vực
                Text(text = "Trường đại học / Khu vực", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = StuMartTextDark)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = university,
                        onValueChange = { university = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        readOnly = false,
                        placeholder = { Text("Chọn hoặc nhập trường") },
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                            focusedBorderColor = StuMartGreen
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.heightIn(max = 200.dp)
                    ) {
                        val filteredList = if (university.isBlank()) universities else universities.filter { it.contains(university, ignoreCase = true) }
                        filteredList.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    university = selectionOption
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 3. Mã số sinh viên (MSSV)
                Text(text = "Mã số sinh viên (MSSV)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = StuMartTextDark)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = studentId,
                    onValueChange = { studentId = it },
                    placeholder = { Text("VD: 20216001") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = StuMartGreen
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 4. Số điện thoại
                Text(text = "Số điện thoại liên hệ", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = StuMartTextDark)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = { Text("VD: 0987654321") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = StuMartGreen
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (fullName.isBlank() || university.isBlank()) {
                        Toast.makeText(context, "Vui lòng nhập Họ tên và Trường học", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    onSave(fullName.trim(), university.trim(), studentId.trim(), phone.trim())
                },
                colors = ButtonDefaults.buttonColors(containerColor = StuMartGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Lưu thay đổi", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color.Gray)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun MyProductCard(
    post: Post,
    onClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    val formattedPrice = if (post.price >= 1000000) "${post.price / 1000000} triệu đ" else "${post.price.toInt()}đ"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFFFF9E6)),
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
                    Text("🦆", fontSize = 36.sp)

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE53935)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("!", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = post.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StuMartTextDark,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "👤 ${if (!post.sellerName.isNullOrBlank()) post.sellerName else "Sinh viên"}",
                        fontSize = 10.sp,
                        color = StuMartTextGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = formattedPrice,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = StuMartGreen
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🏷️ ${post.type}", fontSize = 10.sp, color = StuMartTextGray)

                    Surface(
                        onClick = onEditClick,
                        shape = RoundedCornerShape(8.dp),
                        color = StuMartGreenContainer,
                        modifier = Modifier.height(26.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("✏️", fontSize = 10.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Sửa", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StuMartGreen)
                        }
                    }
                }
            }
        }
    }
}
