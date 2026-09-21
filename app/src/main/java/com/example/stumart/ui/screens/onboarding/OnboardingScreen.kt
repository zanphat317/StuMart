package com.example.stumart.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stumart.ui.theme.*
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val buttonText: String,
    val buttonColor: Color,
    val emoji: String
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "Mua bán trong tầm tay",
        description = "Hàng ngàn món đồ từ sinh viên quanh bạn - sách vở, điện tử, thời trang. Tìm kiếm siêu nhanh!",
        buttonText = "Tiếp theo →",
        buttonColor = StuMartYellow,
        emoji = "🛍️🦆"
    ),
    OnboardingPage(
        title = "Đấu giá & Túi mù cực vui",
        description = "Đặt giá để sở hữu món hời, hoặc thử vận may với túi bí ẩn - phiêu lưu như vịt con! 🦆",
        buttonText = "Tiếp theo →",
        buttonColor = StuMartGreen,
        emoji = "🔨🦆"
    ),
    OnboardingPage(
        title = "Cho tặng & Trao đổi đồ",
        description = "Đồ không dùng nữa? Cho tặng bạn bè hoặc đổi lấy thứ mình cần. Circular economy thật sự!",
        buttonText = "Bắt đầu ngay! 🦆",
        buttonColor = StuMartOrange,
        emoji = "🎁🦆"
    )
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = StuMartLightBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Bỏ qua",
                    color = StuMartTextGray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable { onFinish() }
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                val currentPage = onboardingPages[page]
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Image placeholder
                    Box(
                        modifier = Modifier
                            .size(250.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = currentPage.emoji, fontSize = 100.sp)
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = currentPage.title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = StuMartTextDark,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = currentPage.description,
                        fontSize = 15.sp,
                        color = StuMartTextGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Bottom section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Page Indicator
                Row(
                    modifier = Modifier.padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(onboardingPages.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) {
                            onboardingPages[pagerState.currentPage].buttonColor
                        } else {
                            Color.LightGray
                        }
                        
                        val width = if (pagerState.currentPage == iteration) 24.dp else 10.dp
                        
                        Box(
                            modifier = Modifier
                                .height(10.dp)
                                .width(width)
                                .clip(RoundedCornerShape(5.dp))
                                .background(color)
                        )
                    }
                }

                // Action Button
                val currentButtonColor = onboardingPages[pagerState.currentPage].buttonColor
                val currentButtonText = onboardingPages[pagerState.currentPage].buttonText
                val textColor = if (currentButtonColor == StuMartYellow) Color.White else Color.White
                
                Button(
                    onClick = {
                        if (pagerState.currentPage < onboardingPages.size - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onFinish()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = currentButtonColor),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = currentButtonText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
