package com.example.stumart.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stumart.data.supabase.Post
import com.example.stumart.ui.screens.ProductDetailScreen
import com.example.stumart.ui.screens.home.tabs.DiscountTab
import com.example.stumart.ui.screens.home.tabs.HomeTab
import com.example.stumart.ui.screens.home.tabs.LiveTab
import com.example.stumart.ui.screens.home.tabs.ProfileTab
import com.example.stumart.ui.theme.StuMartGreen
import com.example.stumart.ui.theme.StuMartLightBackground
import com.example.stumart.ui.theme.StuMartYellow

enum class HomeRoute {
    Home, Discount, Live, Profile
}

@Composable
fun HomeScreen(
    userName: String = "Khoa Mai Dang",
    userUniversity: String = "ĐH Bách Khoa Hà Nội",
    shouldShowSurvey: Boolean = false,
    onSurveyDismissed: () -> Unit = {},
    onUniversityChange: (String) -> Unit = {},
    onLogout: () -> Unit
) {
    var currentRoute by remember { mutableStateOf(HomeRoute.Home) }
    var showPostBottomSheet by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<Post?>(null) }

    if (selectedPost != null) {
        ProductDetailScreen(
            post = selectedPost!!,
            onBackClick = { selectedPost = null }
        )
        return
    }

    Scaffold(
        bottomBar = {
            CustomBottomNavigation(
                currentRoute = currentRoute,
                onRouteSelected = { currentRoute = it },
                onPostClick = { showPostBottomSheet = true }
            )
        },
        containerColor = StuMartLightBackground
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentRoute) {
                HomeRoute.Home -> HomeTab(
                    userName = userName,
                    userUniversity = userUniversity,
                    onPostClick = { selectedPost = it }
                )
                HomeRoute.Discount -> DiscountTab()
                HomeRoute.Live -> LiveTab()
                HomeRoute.Profile -> ProfileTab(
                    userName = userName,
                    userUniversity = userUniversity,
                    onUniversityChange = onUniversityChange,
                    onLogout = onLogout
                )
            }
        }
    }

    if (showPostBottomSheet) {
        // Hiển thị giao diện Đăng bán (Bottom Sheet)
        @OptIn(ExperimentalMaterial3Api::class)
        ModalBottomSheet(
            onDismissRequest = { showPostBottomSheet = false },
            containerColor = StuMartLightBackground,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            PostBottomSheetContent(onDismiss = { showPostBottomSheet = false })
        }
    }

    if (shouldShowSurvey) {
        InterestsSurveyDialog(
            onDismiss = onSurveyDismissed,
            onSaveInterests = { selectedCategories ->
                // Đóng dialog khảo sát danh mục yêu thích sau khi bấm lưu/bắt đầu
                onSurveyDismissed()
            }
        )
    }
}

@Composable
fun CustomBottomNavigation(
    currentRoute: HomeRoute,
    onRouteSelected: (HomeRoute) -> Unit,
    onPostClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp)
                    .height(60.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    icon = Icons.Default.Storefront,
                    label = "Trang chủ",
                    isSelected = currentRoute == HomeRoute.Home,
                    onClick = { onRouteSelected(HomeRoute.Home) },
                    modifier = Modifier.weight(1f)
                )
                BottomNavItem(
                    icon = Icons.Outlined.LocalOffer,
                    label = "Giá rẻ",
                    isSelected = currentRoute == HomeRoute.Discount,
                    onClick = { onRouteSelected(HomeRoute.Discount) },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.weight(1.1f))

                BottomNavItem(
                    icon = Icons.Default.FlashOn,
                    label = "Live",
                    isSelected = currentRoute == HomeRoute.Live,
                    onClick = { onRouteSelected(HomeRoute.Live) },
                    modifier = Modifier.weight(1f)
                )
                BottomNavItem(
                    icon = Icons.Default.Person,
                    label = "Cá nhân",
                    isSelected = currentRoute == HomeRoute.Profile,
                    onClick = { onRouteSelected(HomeRoute.Profile) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Hero Voltage Duck Floating Action Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .navigationBarsPadding()
                .offset(y = (-14).dp)
                .clickable { onPostClick() }
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .shadow(8.dp, CircleShape, spotColor = Color(0x66FFD21E))
                    .clip(CircleShape)
                    .background(StuMartYellow)
                    .padding(3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🦆", fontSize = 28.sp)
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Đăng bán",
                fontSize = 11.sp,
                color = StuMartGreen,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (isSelected) StuMartGreen else Color(0xFF6B7280)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick() }
            .padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(23.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = color,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
        if (isSelected) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(StuMartGreen)
            )
        }
    }
}
