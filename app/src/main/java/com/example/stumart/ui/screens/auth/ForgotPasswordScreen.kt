package com.example.stumart.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.stumart.data.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.OtpType
import com.example.stumart.ui.theme.StuMartGreen
import com.example.stumart.ui.theme.StuMartLightBackground
import com.example.stumart.ui.theme.StuMartOrange
import com.example.stumart.ui.theme.StuMartTextDark
import com.example.stumart.ui.theme.StuMartTextGray
import com.example.stumart.ui.theme.StuMartYellow

enum class ForgotPasswordStep {
    EMAIL, OTP, NEW_PASSWORD
}

@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    onSubmit: () -> Unit
) {
    var currentStep by rememberSaveable { mutableStateOf(ForgotPasswordStep.EMAIL) }
    var email by rememberSaveable { mutableStateOf("") }
    var otp by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmPasswordError by rememberSaveable { mutableStateOf<String?>(null) }
    
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
                .padding(24.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        when (currentStep) {
                            ForgotPasswordStep.EMAIL -> onBackToLogin()
                            ForgotPasswordStep.OTP -> currentStep = ForgotPasswordStep.EMAIL
                            ForgotPasswordStep.NEW_PASSWORD -> currentStep = ForgotPasswordStep.OTP
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
                    ForgotPasswordStep.EMAIL -> "2/4"
                    ForgotPasswordStep.OTP -> "3/4"
                    ForgotPasswordStep.NEW_PASSWORD -> "4/4"
                }
                Text(
                    text = stepText,
                    color = StuMartTextGray,
                    fontSize = 14.sp
                )
            }

            when (currentStep) {
                ForgotPasswordStep.EMAIL -> {
                    EmailStepContent(
                        email = email,
                        onEmailChange = { email = it },
                        isLoading = isLoading,
                        onNext = {
                            if (email.isBlank() || !email.contains("@")) {
                                Toast.makeText(context, "Vui lòng nhập email hợp lệ", Toast.LENGTH_SHORT).show()
                                return@EmailStepContent
                            }
                            
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    SupabaseClient.client.auth.resetPasswordForEmail(email = email.trim())
                                    Toast.makeText(context, "Đã gửi mã xác thực về email!", Toast.LENGTH_SHORT).show()
                                    currentStep = ForgotPasswordStep.OTP
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Lỗi gửi mail: ${e.message}", Toast.LENGTH_LONG).show()
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    )
                }
                ForgotPasswordStep.OTP -> {
                    OtpStepContent(
                        email = email,
                        otp = otp,
                        onOtpChange = { otp = it },
                        onNext = {
                            if (otp.length < 6) {
                                Toast.makeText(context, "Vui lòng nhập đủ 6 số OTP", Toast.LENGTH_SHORT).show()
                                return@OtpStepContent
                            }
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    SupabaseClient.client.auth.verifyEmailOtp(
                                        type = OtpType.Email.RECOVERY,
                                        email = email.trim(),
                                        token = otp.trim()
                                    )
                                    Toast.makeText(context, "Mã OTP hợp lệ!", Toast.LENGTH_SHORT).show()
                                    currentStep = ForgotPasswordStep.NEW_PASSWORD
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Mã OTP không đúng hoặc đã hết hạn: ${e.message}", Toast.LENGTH_LONG).show()
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        onResend = {
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    SupabaseClient.client.auth.resetPasswordForEmail(email = email.trim())
                                    Toast.makeText(context, "Đã gửi lại mã xác thực về email!", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Lỗi kết nối: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    )
                }
                ForgotPasswordStep.NEW_PASSWORD -> {
                    NewPasswordStepContent(
                        newPassword = newPassword,
                        onNewPasswordChange = { 
                            newPassword = it
                            passwordError = null
                        },
                        confirmPassword = confirmPassword,
                        onConfirmPasswordChange = { 
                            confirmPassword = it
                            confirmPasswordError = null
                        },
                        passwordError = passwordError,
                        confirmPasswordError = confirmPasswordError,
                        isLoading = isLoading,
                        onSubmit = {
                            passwordError = null
                            confirmPasswordError = null

                            if (newPassword.isBlank() || confirmPassword.isBlank() || email.isBlank() || otp.isBlank()) {
                                Toast.makeText(context, "Vui lòng nhập đầy đủ email, otp và mật khẩu mới!", Toast.LENGTH_SHORT).show()
                                return@NewPasswordStepContent
                            }
                            
                            val hasMinLength = newPassword.length >= 8
                            val hasUpper = newPassword.any { it.isUpperCase() }
                            val hasDigit = newPassword.any { it.isDigit() }
                            val hasSpecial = newPassword.any { !it.isLetterOrDigit() }
                            
                            if (!hasMinLength || !hasUpper || !hasDigit || !hasSpecial) {
                                passwordError = "Mật khẩu phải có ít nhất 8 ký tự, gồm chữ hoa, số và ký tự đặc biệt"
                                return@NewPasswordStepContent
                            }

                            if (newPassword != confirmPassword) {
                                confirmPasswordError = "Mật khẩu xác nhận không khớp"
                                return@NewPasswordStepContent
                            }
                            
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    SupabaseClient.client.auth.updateUser {
                                        password = newPassword
                                    }
                                    Toast.makeText(context, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                                    onSubmit() // Gọi callback quay về trang đăng nhập
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Lỗi cập nhật mật khẩu: ${e.message}", Toast.LENGTH_LONG).show()
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

@Composable
fun DecorativeIllustration(
    backgroundColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer decorative circle
        Box(
            modifier = Modifier
                .size(160.dp)
                .background(Color(0xFFF0EAE1), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(140.dp)
                .border(1.dp, Color.LightGray, CircleShape)
        )

        // Center Box
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(backgroundColor, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            content()
        }

        // Floating elements
        Box(
            modifier = Modifier
                .offset(x = 90.dp, y = (-50).dp)
                .size(16.dp)
                .background(StuMartOrange, CircleShape)
        )
        Box(
            modifier = Modifier
                .offset(x = (-80).dp, y = 60.dp)
                .size(12.dp)
                .background(StuMartGreen, CircleShape)
        )
        Box(
            modifier = Modifier
                .offset(x = 80.dp, y = 60.dp)
                .size(16.dp)
                .background(StuMartYellow) // Diamond shape can be simplified to square rotated, but box is fine
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailStepContent(
    email: String,
    onEmailChange: (String) -> Unit,
    isLoading: Boolean,
    onNext: () -> Unit
) {
    Column {
        Text(
            text = "Nhập email của bạn",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = StuMartTextDark
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Nhập email sinh viên đã liên kết với StuMart để nhận mã xác thực.",
            fontSize = 15.sp,
            color = StuMartTextGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        DecorativeIllustration(backgroundColor = StuMartGreen) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Email", fontWeight = FontWeight.Bold, color = StuMartTextDark)
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedBorderColor = StuMartGreen,
                focusedBorderColor = StuMartGreen
            ),
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null, tint = StuMartGreen)
            },
            trailingIcon = {
                if (email.contains("@")) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StuMartGreen)
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Mã OTP gồm 6 số sẽ được gửi đến email này.",
            fontSize = 13.sp,
            color = StuMartTextGray
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = StuMartGreen),
            shape = RoundedCornerShape(16.dp),
            enabled = email.isNotBlank() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Gửi mã xác thực", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                append("Bạn không còn dùng email này? ")
                withStyle(style = SpanStyle(color = StuMartGreen, fontWeight = FontWeight.Bold)) {
                    append("Liên hệ hỗ trợ")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = StuMartTextGray
        )
    }
}

@Composable
fun OtpStepContent(
    email: String,
    otp: String,
    onOtpChange: (String) -> Unit,
    onNext: () -> Unit,
    onResend: () -> Unit
) {
    var timeLeft by remember { mutableStateOf(300) } // 300 giây = 5 phút

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            kotlinx.coroutines.delay(1000L)
            timeLeft -= 1
        }
    }

    val formattedTime = java.lang.String.format(java.util.Locale.getDefault(), "%02d:%02d", timeLeft / 60, timeLeft % 60)

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Nhập mã xác thực",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = StuMartTextDark
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = buildAnnotatedString {
                append("Mã gồm 6 số đã được gửi tới\n")
                withStyle(style = SpanStyle(color = StuMartTextDark, fontWeight = FontWeight.Bold)) {
                    append(email)
                }
            },
            fontSize = 15.sp,
            color = StuMartTextGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        DecorativeIllustration(backgroundColor = StuMartOrange) {
            Text(
                text = "123",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // OTP Input
        BasicTextField(
            value = otp,
            onValueChange = { if (it.length <= 6) onOtpChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            decorationBox = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(6) { index ->
                        val char = when {
                            index >= otp.length -> ""
                            else -> otp[index].toString()
                        }
                        val isFocused = otp.length == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(0.8f)
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .border(
                                    1.dp,
                                    if (isFocused) StuMartOrange else Color.LightGray,
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = StuMartTextDark
                            )
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Mã có hiệu lực trong ")
                    withStyle(style = SpanStyle(color = StuMartOrange, fontWeight = FontWeight.Bold)) {
                        append(formattedTime)
                    }
                },
                fontSize = 13.sp,
                color = StuMartTextGray
            )

            Text(
                text = "Gửi lại mã",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (timeLeft == 0) StuMartGreen else Color.Gray,
                modifier = Modifier
                    .clickable(enabled = timeLeft == 0) {
                        timeLeft = 300 // Reset lại thời gian 5 phút
                        onResend()
                    }
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = StuMartGreen),
            shape = RoundedCornerShape(16.dp),
            enabled = otp.length == 6
        ) {
            Text("Xác nhận mã OTP", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Không nhận được mã? Kiểm tra thư mục spam nhé.",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = StuMartTextGray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPasswordStepContent(
    newPassword: String,
    onNewPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    passwordError: String?,
    confirmPasswordError: String?,
    isLoading: Boolean,
    onSubmit: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Tạo mật khẩu mới",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = StuMartTextDark
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Mật khẩu mới nên khác mật khẩu cũ và đủ mạnh để bảo vệ tài khoản.",
            fontSize = 15.sp,
            color = StuMartTextGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        DecorativeIllustration(backgroundColor = StuMartYellow) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = StuMartGreen,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Mật khẩu mới", fontWeight = FontWeight.Bold, color = StuMartTextDark)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newPassword,
            onValueChange = onNewPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = StuMartGreen
            ),
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = StuMartGreen)
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = passwordError != null,
            supportingText = {
                if (passwordError != null) {
                    Text(passwordError, color = Color.Red)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Nhập lại mật khẩu", fontWeight = FontWeight.Bold, color = StuMartTextDark)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = StuMartGreen
            ),
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = StuMartGreen)
            },
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = confirmPasswordError != null,
            supportingText = {
                if (confirmPasswordError != null) {
                    Text(confirmPasswordError, color = Color.Red)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = StuMartGreen,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ít nhất 8 ký tự, gồm chữ hoa, số và ký tự đặc biệt",
                fontSize = 13.sp,
                color = StuMartTextGray
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onSubmit,
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
                Text("Đổi mật khẩu", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
