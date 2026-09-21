package com.example.stumart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import com.example.stumart.ui.screens.auth.ForgotPasswordScreen
import com.example.stumart.ui.screens.auth.LoginScreen
import com.example.stumart.ui.screens.auth.RegisterScreen
import com.example.stumart.ui.screens.onboarding.OnboardingScreen
import com.example.stumart.ui.screens.splash.SplashScreen
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
            StuMartTheme {
                var currentScreen by remember { mutableStateOf(Screen.Splash) }

                when (currentScreen) {
                    Screen.Splash -> SplashScreen(onSplashFinished = { currentScreen = Screen.Onboarding })
                    Screen.Onboarding -> OnboardingScreen(onFinish = { currentScreen = Screen.Login })
                    Screen.Login -> LoginScreen(
                        onLoginSuccess = { currentScreen = Screen.Home },
                        onNavigateToRegister = { currentScreen = Screen.Register },
                        onNavigateToForgotPassword = { currentScreen = Screen.ForgotPassword }
                    )
                    Screen.Register -> RegisterScreen(
                        onRegisterSuccess = { currentScreen = Screen.Home },
                        onNavigateToLogin = { currentScreen = Screen.Login }
                    )
                    Screen.ForgotPassword -> ForgotPasswordScreen(
                        onBackToLogin = { currentScreen = Screen.Login },
                        onSubmit = { currentScreen = Screen.Login }
                    )
                    Screen.Home -> {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.White
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Home Screen (Coming Soon)")
                            }
                        }
                    }
                }
            }
        }
    }
}
