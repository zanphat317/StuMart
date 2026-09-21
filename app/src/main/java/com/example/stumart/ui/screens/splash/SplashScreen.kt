package com.example.stumart.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stumart.ui.theme.StuMartGreen
import com.example.stumart.ui.theme.StuMartYellow
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(2000L)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StuMartYellow),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Placeholder for Logo
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🦆", fontSize = 100.sp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "StuMart",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = StuMartGreen
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "🛍️ Chợ sinh viên - Mua bán dễ dàng",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
        }
        
        // Loading dots placeholder
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(StuMartGreen))
            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(StuMartGreen))
            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(StuMartGreen))
        }
    }
}
