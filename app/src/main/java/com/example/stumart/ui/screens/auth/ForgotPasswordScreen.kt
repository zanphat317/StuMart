package com.example.stumart.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stumart.ui.theme.StuMartGreen
import com.example.stumart.ui.theme.StuMartLightBackground
import com.example.stumart.ui.theme.StuMartTextDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    onSubmit: () -> Unit
) {
    var email by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = StuMartLightBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "← Trở lại",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = StuMartGreen,
                modifier = Modifier
                    .clickable { onBackToLogin() }
                    .padding(vertical = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Quên Mật Khẩu",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = StuMartGreen
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Vui lòng nhập địa chỉ email đã đăng ký. Chúng tôi sẽ gửi cho bạn hướng dẫn để đặt lại mật khẩu.",
                fontSize = 16.sp,
                color = StuMartTextDark
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StuMartGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Gửi Yêu Cầu", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
