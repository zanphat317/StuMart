package com.example.stumart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.stumart.ui.screens.auth.ForgotPasswordScreen
import com.example.stumart.ui.screens.auth.LoginScreen
import com.example.stumart.ui.screens.auth.RegisterScreen
import com.example.stumart.ui.screens.onboarding.OnboardingScreen
import com.example.stumart.ui.screens.splash.SplashScreen
import com.example.stumart.ui.theme.StuMartLightBackground
import com.example.stumart.ui.theme.StuMartTheme

enum class Screen {
    Splash,
    Onboarding,
    Login,
    Register,
    ForgotPassword,
    Home
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StuMartTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = StuMartLightBackground
                ) {
                    var currentScreen by rememberSaveable { mutableStateOf(Screen.Splash) }
                    var userName by rememberSaveable { mutableStateOf("Sinh viên StuMart") }
                    var userUniversity by rememberSaveable { mutableStateOf("ĐH Bách Khoa TP.HCM (ĐHQG-HCM)") }
                    var shouldShowSurvey by rememberSaveable { mutableStateOf(false) }

                    when (currentScreen) {
                        Screen.Splash -> SplashScreen(onSplashFinished = { currentScreen = Screen.Onboarding })
                        Screen.Onboarding -> OnboardingScreen(onFinish = { currentScreen = Screen.Login })
                        Screen.Login -> LoginScreen(
                            onLoginSuccess = { name, uni ->
                                userName = name
                                userUniversity = uni
                                shouldShowSurvey = true // Chỉ bật khảo sát khi VỪA ĐĂNG NHẬP THÀNH CÔNG
                                currentScreen = Screen.Home
                            },
                            onNavigateToRegister = { currentScreen = Screen.Register },
                            onNavigateToForgotPassword = { currentScreen = Screen.ForgotPassword }
                        )
                        Screen.Register -> RegisterScreen(
                            onRegisterSuccess = { name, uni ->
                                userName = name
                                userUniversity = uni
                                shouldShowSurvey = true // Chỉ bật khảo sát khi VỪA ĐĂNG KÝ THÀNH CÔNG
                                currentScreen = Screen.Home
                            },
                            onNavigateToLogin = { currentScreen = Screen.Login }
                        )
                        Screen.ForgotPassword -> ForgotPasswordScreen(
                            onBackToLogin = { currentScreen = Screen.Login },
                            onSubmit = { currentScreen = Screen.Login }
                        )
                        Screen.Home -> {
                            com.example.stumart.ui.screens.home.HomeScreen(
                                userName = userName,
                                userUniversity = userUniversity,
                                shouldShowSurvey = shouldShowSurvey,
                                onSurveyDismissed = { shouldShowSurvey = false },
                                onUniversityChange = { userUniversity = it },
                                onLogout = { currentScreen = Screen.Login }
                            )
                        }
                    }
                }
            }
        }
    }
}
