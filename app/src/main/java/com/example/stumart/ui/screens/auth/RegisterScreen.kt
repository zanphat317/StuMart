package com.example.stumart.ui.screens.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stumart.data.supabase.SupabaseClient
import com.example.stumart.data.supabase.UserProfile
import com.example.stumart.ui.theme.StuMartGreen
import com.example.stumart.ui.theme.StuMartLightBackground
import com.example.stumart.ui.theme.StuMartTextDark
import com.example.stumart.ui.theme.StuMartTextGray
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import androidx.activity.compose.BackHandler
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

enum class RegisterStep {
    FORM, OTP
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: (name: String, university: String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var currentStep by rememberSaveable { mutableStateOf(RegisterStep.FORM) }
    var inputName by rememberSaveable { mutableStateOf("") }
    var inputEmail by rememberSaveable { mutableStateOf("") }
    var inputStudentId by rememberSaveable { mutableStateOf("") }
    var inputUniversity by rememberSaveable { mutableStateOf("") }
    var inputPhone by rememberSaveable { mutableStateOf("") }
    var inputPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var inputConfirmPassword by rememberSaveable { mutableStateOf("") }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmPasswordError by rememberSaveable { mutableStateOf<String?>(null) }
    var otp by rememberSaveable { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = currentStep == RegisterStep.OTP) {
        currentStep = RegisterStep.FORM
    }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (currentStep == RegisterStep.FORM) 16.dp else 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        when (currentStep) {
                            RegisterStep.FORM -> onNavigateToLogin()
                            RegisterStep.OTP -> currentStep = RegisterStep.FORM
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = StuMartGreen
                    )
                }

                Text(
                    text = "StuMart",
                    color = StuMartGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                val stepText = when (currentStep) {
                    RegisterStep.FORM -> "1/2"
                    RegisterStep.OTP -> "2/2"
                }
                Text(
                    text = stepText,
                    color = StuMartTextGray,
                    fontSize = 14.sp
                )
            }

            when (currentStep) {
                RegisterStep.FORM -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Đăng Ký",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = StuMartGreen
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Tham gia cộng đồng StuMart ngay! 🛍️",
                            fontSize = 16.sp,
                            color = StuMartTextDark
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            value = inputName,
                            onValueChange = { inputName = it },
                            label = { Text("Họ và tên") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputEmail,
                            onValueChange = { inputEmail = it },
                            label = { Text("Email sinh viên (@edu.vn)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputStudentId,
                            onValueChange = { inputStudentId = it },
                            label = { Text("Mã sinh viên") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputUniversity,
                            onValueChange = { inputUniversity = it },
                            label = { Text("Trường đại học") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputPhone,
                            onValueChange = { inputPhone = it },
                            label = { Text("Số điện thoại") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputPassword,
                            onValueChange = {
                                inputPassword = it
                                passwordError = null
                            },
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
                            },
                            isError = passwordError != null,
                            supportingText = {
                                if (passwordError != null) {
                                    Text(passwordError!!, color = Color.Red)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputConfirmPassword,
                            onValueChange = {
                                inputConfirmPassword = it
                                confirmPasswordError = null
                            },
                            label = { Text("Xác nhận mật khẩu") },
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(
                                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = if (confirmPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                                        tint = Color.Gray
                                    )
                                }
                            },
                            isError = confirmPasswordError != null,
                            supportingText = {
                                if (confirmPasswordError != null) {
                                    Text(confirmPasswordError!!, color = Color.Red)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                if (inputName.isBlank() || inputEmail.isBlank() || inputPassword.isBlank() || inputStudentId.isBlank() || inputUniversity.isBlank() || inputPhone.isBlank()) {
                                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                if (!inputEmail.contains("@")) {
                                    Toast.makeText(context, "Vui lòng nhập email hợp lệ", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                passwordError = null
                                confirmPasswordError = null

                                val hasMinLength = inputPassword.length >= 8
                                val hasUpper = inputPassword.any { it.isUpperCase() }
                                val hasDigit = inputPassword.any { it.isDigit() }
                                val hasSpecial = inputPassword.any { !it.isLetterOrDigit() }

                                if (!hasMinLength || !hasUpper || !hasDigit || !hasSpecial) {
                                    passwordError = "Mật khẩu phải có ít nhất 8 ký tự, gồm chữ hoa, số và ký tự đặc biệt"
                                    return@Button
                                }

                                if (inputPassword != inputConfirmPassword) {
                                    confirmPasswordError = "Mật khẩu xác nhận không khớp"
                                    return@Button
                                }

                                isLoading = true
                                coroutineScope.launch {
                                    try {
                                        val userEmail = inputEmail.trim().lowercase()
                                        val userPassword = inputPassword

                                        Log.d("AUTH_CHECK", "Đang gửi đăng ký: $userEmail")

                                        // Gọi hàm signUpWith của Supabase
                                        SupabaseClient.client.auth.signUpWith(Email) {
                                            this.email = userEmail
                                            this.password = userPassword
                                            this.data = buildJsonObject {
                                                put("full_name", inputName.trim())
                                                put("student_id", inputStudentId.trim())
                                                put("university", inputUniversity.trim())
                                                put("phone", inputPhone.trim())
                                            }
                                        }

                                        Toast.makeText(context, "Đã gửi mã xác thực về email!", Toast.LENGTH_SHORT).show()
                                        otp = ""
                                        currentStep = RegisterStep.OTP
                                    } catch (e: Exception) {
                                        Log.e("AUTH_CHECK", "Lỗi đăng ký: ${e.message}", e)
                                        val msg = e.message ?: ""
                                        val userFriendlyMsg = when {
                                            msg.contains("User already registered", ignoreCase = true) -> {
                                                otp = ""
                                                currentStep = RegisterStep.OTP
                                                "Email này đã được đăng ký! Vui lòng kiểm tra email để lấy OTP hoặc Đăng nhập."
                                            }
                                            msg.contains("Unable to resolve host", ignoreCase = true) || msg.contains("ConnectException", ignoreCase = true) ->
                                                "Lỗi kết nối mạng. Vui lòng kiểm tra Internet!"
                                            else -> "Lỗi đăng ký: $msg"
                                        }
                                        Toast.makeText(context, userFriendlyMsg, Toast.LENGTH_LONG).show()
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
                                Text("Tạo Tài Khoản", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Đã có tài khoản? ", color = StuMartTextDark)
                            Text(
                                text = "Đăng nhập",
                                color = StuMartGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable(enabled = !isLoading) { onNavigateToLogin() }
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
                RegisterStep.OTP -> {
                    OtpStepContent(
                        email = inputEmail.trim(),
                        otp = otp,
                        onOtpChange = { otp = it },
                        isLoading = isLoading,
                        buttonText = "Xác nhận & Hoàn tất",
                        onNext = {
                            if (otp.length < 6) {
                                Toast.makeText(context, "Vui lòng nhập đủ 6 số OTP", Toast.LENGTH_SHORT).show()
                                return@OtpStepContent
                            }
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    val userEmail = inputEmail.trim().lowercase()
                                    val cleanOtp = otp.trim()
                                    Log.d("AUTH_CHECK", "Xác thực OTP đăng ký cho: $userEmail")

                                    // Xác nhận mã OTP đăng ký với Supabase
                                    SupabaseClient.client.auth.verifyEmailOtp(
                                        type = OtpType.Email.SIGNUP,
                                        email = userEmail,
                                        token = cleanOtp
                                    )

                                    // Lưu thông tin sinh viên vào bảng profiles trên Supabase
                                    val currentUser = SupabaseClient.client.auth.currentUserOrNull()
                                    if (currentUser == null) {
                                        Toast.makeText(context, "Không tìm thấy phiên đăng nhập. Vui lòng đăng nhập lại!", Toast.LENGTH_LONG).show()
                                        onNavigateToLogin()
                                        return@launch
                                    }

                                    try {
                                        val profile = UserProfile(
                                            id = currentUser.id,
                                            email = userEmail,
                                            fullName = inputName.trim(),
                                            studentId = inputStudentId.trim(),
                                            university = inputUniversity.trim(),
                                            phone = inputPhone.trim()
                                        )
                                        SupabaseClient.client.postgrest["profiles"].upsert(profile)
                                        Log.d("AUTH_CHECK", "Đã ghi thông tin vào bảng profiles thành công")
                                    } catch (dbEx: Exception) {
                                        Log.e("AUTH_CHECK", "Lỗi ghi bảng profiles: ${dbEx.message}", dbEx)
                                        Toast.makeText(context, "Lỗi cập nhật hồ sơ sinh viên: ${dbEx.message}", Toast.LENGTH_LONG).show()
                                        return@launch
                                    }

                                    Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                                    onRegisterSuccess(inputName.trim(), inputUniversity.trim())
                                } catch (e: Exception) {
                                    Log.e("AUTH_CHECK", "Lỗi xác thực OTP: ${e.message}", e)
                                    val msg = e.message ?: ""
                                    val userFriendlyMsg = when {
                                        msg.contains("Token has expired or is invalid", ignoreCase = true) ||
                                        msg.contains("invalid", ignoreCase = true) ||
                                        msg.contains("expired", ignoreCase = true) ->
                                            "Mã OTP không đúng hoặc đã hết hạn!"
                                        else -> "Xác thực thất bại: $msg"
                                    }
                                    Toast.makeText(context, userFriendlyMsg, Toast.LENGTH_LONG).show()
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        onResend = {
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    val userEmail = inputEmail.trim().lowercase()
                                    SupabaseClient.client.auth.resendEmail(
                                        type = OtpType.Email.SIGNUP,
                                        email = userEmail
                                    )
                                    Toast.makeText(context, "Đã gửi lại mã xác thực về email!", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Log.e("AUTH_CHECK", "Lỗi gửi lại mã: ${e.message}", e)
                                    Toast.makeText(context, "Lỗi kết nối: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
