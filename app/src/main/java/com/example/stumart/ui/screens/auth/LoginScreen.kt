package com.example.stumart.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.stumart.data.supabase.SupabaseClient
import com.example.stumart.data.supabase.UserProfile
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import com.example.stumart.ui.theme.StuMartGreen
import com.example.stumart.ui.theme.StuMartLightBackground
import com.example.stumart.ui.theme.StuMartOrange
import com.example.stumart.ui.theme.StuMartTextDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (name: String, university: String) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var emailInput by rememberSaveable { mutableStateOf("") }
    var passwordInput by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = StuMartLightBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Đăng Nhập",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = StuMartGreen
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Chào mừng bạn trở lại StuMart! 🦆",
                fontSize = 16.sp,
                color = StuMartTextDark
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                value = emailInput,
                onValueChange = { emailInput = it },
                label = { Text("Email sinh viên") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text("Mật khẩu") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                            tint = Color.Gray
                        )
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Quên mật khẩu?",
                    color = StuMartOrange,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onNavigateToForgotPassword() }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    if (emailInput.isBlank() || passwordInput.isBlank()) {
                        Toast.makeText(context, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    isLoading = true
                    coroutineScope.launch {
                        try {
                            SupabaseClient.client.auth.signInWith(Email) {
                                this.email = emailInput.trim()
                                this.password = passwordInput
                            }
                            
                            var fetchedName = "Sinh viên StuMart"
                            var fetchedUni = "Trường Đại học Công nghệ Thông tin (ĐHQG-HCM)"
                            
                            try {
                                val userEmail = emailInput.trim()
                                val profile = SupabaseClient.client.postgrest["profiles"]
                                    .select { filter { eq("email", userEmail) } }
                                    .decodeSingleOrNull<UserProfile>()
                                    
                                if (profile != null) {
                                    if (!profile.fullName.isNullOrBlank()) fetchedName = profile.fullName
                                    if (!profile.university.isNullOrBlank()) fetchedUni = profile.university
                                }
                            } catch (e: Exception) {
                                val currentUser = SupabaseClient.client.auth.currentUserOrNull()
                                val metadata = currentUser?.userMetadata
                                val metaName = metadata?.get("full_name")?.jsonPrimitive?.contentOrNull
                                val metaUni = metadata?.get("university")?.jsonPrimitive?.contentOrNull
                                if (!metaName.isNullOrBlank()) fetchedName = metaName
                                if (!metaUni.isNullOrBlank()) fetchedUni = metaUni
                            }
                            
                            Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess(fetchedName, fetchedUni)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Lỗi đăng nhập: ${e.message}", Toast.LENGTH_LONG).show()
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
                    Text("Đăng Nhập", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Chưa có tài khoản? ", color = StuMartTextDark)
                Text(
                    text = "Đăng ký ngay",
                    color = StuMartGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}
