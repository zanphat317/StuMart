package com.example.stumart.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.stumart.ui.theme.StuMartGreen
import com.example.stumart.ui.theme.StuMartLightBackground
import com.example.stumart.ui.theme.StuMartTextDark
import com.example.stumart.ui.theme.StuMartTextGray

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InterestsSurveyDialog(
    onDismiss: () -> Unit,
    onSaveInterests: (List<String>) -> Unit
) {
    val allCategories = listOf(
        "Sách/Giáo trình",
        "Đồ điện tử",
        "Thời trang/Phụ kiện",
        "Đồ gia dụng",
        "Nội thất",
        "Xe cộ",
        "Giải trí/Skincare",
        "Khác"
    )

    val selectedInterests = remember { mutableStateListOf<String>() }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = StuMartLightBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🎯", fontSize = 40.sp)
                
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Bạn đang tìm kiếm gì?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = StuMartTextDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Chọn các chủ đề bạn quan tâm để StuMart gợi ý sản phẩm phù hợp nhất dành cho bạn!",
                    fontSize = 13.sp,
                    color = StuMartTextGray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // FlowRow for multi-select chips
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allCategories.forEach { category ->
                        val isSelected = selectedInterests.contains(category)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) {
                                    selectedInterests.remove(category)
                                } else {
                                    selectedInterests.add(category)
                                }
                            },
                            label = {
                                Text(
                                    text = category,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StuMartGreen,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = StuMartTextDark
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        onSaveInterests(selectedInterests.toList())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StuMartGreen),
                    shape = RoundedCornerShape(16.dp),
                    enabled = selectedInterests.isNotEmpty()
                ) {
                    Text(
                        text = "Bắt đầu khám phá ✨",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = onDismiss) {
                    Text(
                        text = "Bỏ qua",
                        color = StuMartTextGray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
