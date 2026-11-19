package com.example.stumart.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.stumart.data.supabase.Post
import com.example.stumart.data.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import com.example.stumart.ui.theme.*

/**
 * Màn hình Chi tiết bài đăng / sản phẩm StuMart (ProductDetailScreen)
 * Thiết kế chuẩn Android Material 3, tối ưu trải nghiệm mua bán sinh viên trong khuôn viên trường.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    post: Post,
    onBackClick: () -> Unit,
    onContactSeller: (Post) -> Unit = {},
    onEditClick: ((Post) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val currentUserId = remember { SupabaseClient.client.auth.currentUserOrNull()?.id }
    val isOwner = remember(post, currentUserId) {
        if (!currentUserId.isNullOrBlank()) {
            post.userId == currentUserId
        } else {
            post.userId == null
        }
    }

    var isFavorite by remember { mutableStateOf(false) }
    var showOfferSheet by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    // Xử lý nút Back hệ thống Android
    BackHandler {
        onBackClick()
    }

    // Định dạng giá tiền VND
    val formattedPrice = remember(post.price, post.type) {
        when {
            post.type == "Cho tặng" || post.price <= 0 -> "Miễn phí (Cho tặng)"
            post.price >= 1_000_000 -> "${(post.price / 1_000_000).toString().removeSuffix(".0")} triệu đ"
            else -> "%,d đ".format(post.price.toInt()).replace(',', '.')
        }
    }

    // Màu sắc nhãn bài đăng theo loại
    val (badgeBgColor, badgeTextColor) = remember(post.type) {
        when (post.type) {
            "Giá rẻ" -> Pair(StuMartOrange, Color.White)
            "Cho tặng" -> Pair(StuMartGreen, Color.White)
            "Trao đổi" -> Pair(Color(0xFF6750A4), Color.White)
            "Túi mù" -> Pair(StuMartYellow, StuMartTextDark)
            "Đấu giá" -> Pair(Color(0xFFC05621), Color.White)
            else -> Pair(StuMartGreen, Color.White)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = StuMartLightBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = post.category ?: "Chi tiết bài đăng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = StuMartTextDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .semantics { contentDescription = "Quay lại trang trước" }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = StuMartTextDark
                        )
                    }
                },
                actions = {
                    // Nút Yêu thích (Favorite)
                    val favoriteTint by animateColorAsState(
                        targetValue = if (isFavorite) StuMartBadgeRed else StuMartTextDark,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "favoriteColor"
                    )
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            val msg = if (isFavorite) "Đã lưu vào danh sách yêu thích" else "Đã bỏ lưu"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isFavorite) "Bỏ lưu tin" else "Lưu tin",
                            tint = favoriteTint
                        )
                    }

                    // Nút Chia sẻ (Share)
                    IconButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Xem ngay món đồ này trên StuMart: ${post.title} - $formattedPrice"
                                )
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Chia sẻ bài đăng"))
                        },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Chia sẻ bài đăng",
                            tint = StuMartTextDark
                        )
                    }

                    // Nút Báo cáo (Report)
                    IconButton(
                        onClick = { showReportDialog = true },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Flag,
                            contentDescription = "Báo cáo vi phạm",
                            tint = StuMartTextGray
                        )
                    }

                    // Nút Chỉnh sửa bài đăng (Edit - Chỉ hiển thị cho chính chủ bài đăng)
                    if (onEditClick != null && isOwner) {
                        IconButton(
                            onClick = { onEditClick(post) },
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Chỉnh sửa tin",
                                tint = StuMartGreen
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = StuMartTextDark,
                    navigationIconContentColor = StuMartTextDark,
                    actionIconContentColor = StuMartTextDark
                )
            )
        },
        bottomBar = {
            // Thanh hành động cố định ở cạnh dưới (Sticky Bottom Bar)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Nút Thương lượng / Trả giá
                    OutlinedButton(
                        onClick = { showOfferSheet = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, StuMartGreen),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = StuMartGreen
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocalOffer,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Trả giá",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Nút Chat / Nhắn tin trao đổi
                    Button(
                        onClick = {
                            onContactSeller(post)
                            Toast.makeText(
                                context,
                                "Mở hộp thoại chat với ${post.sellerName ?: "người bán"}...",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StuMartGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Chat,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Nhắn tin ngay",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // ================= 1. HÌNH ẢNH SẢN PHẨM (HERO GALLERY) =================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(StuMartCream),
                contentAlignment = Alignment.Center
            ) {
                val isValidImage = !post.imageUrl.isNullOrBlank() &&
                        (post.imageUrl.startsWith("http://") || post.imageUrl.startsWith("https://"))

                if (isValidImage) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(post.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Hình ảnh sản phẩm ${post.title}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Fallback mascot khi chưa có ảnh
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🦆", fontSize = 64.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ảnh thực tế đang được cập nhật",
                            fontSize = 13.sp,
                            color = StuMartTextGray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Nhãn loại bài đăng (Góc trên trái)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(14.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeBgColor)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = post.type,
                        color = badgeTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Huy hiệu tình trạng món đồ (Góc trên phải)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(14.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.92f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(StuMartGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = post.condition,
                            color = StuMartTextDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Chỉ số ảnh thực tế (Góc dưới phải)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(14.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.55f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "1/1 ảnh",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ================= 2. THÔNG TIN GIÁ & TIÊU ĐỀ =================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Dòng giá nổi bật
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formattedPrice,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (post.type == "Cho tặng") StuMartGreen else StuMartOrange
                        )

                        // Chip danh mục
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(StuMartGreenContainer)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = post.category ?: "Khác",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = StuMartGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tiêu đề sản phẩm
                    Text(
                        text = post.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = StuMartTextDark,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Ngày đăng và lượt xem
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = null,
                                tint = StuMartTextGray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Đăng hôm nay",
                                fontSize = 12.sp,
                                color = StuMartTextGray
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Visibility,
                                contentDescription = null,
                                tint = StuMartTextGray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Được quan tâm",
                                fontSize = 12.sp,
                                color = StuMartTextGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ================= 3. KHU VỰC KHUÔN VIÊN TRƯỜNG & AN TOÀN (CAMPUS TRUST ZONE) =================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = StuMartGreenContainer),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = StuMartGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Địa điểm giao dịch đề xuất",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = StuMartGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = post.location ?: "Khuôn viên trường ĐH Bách Khoa (Sảnh H1 / Thư viện)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = StuMartTextDark
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    HorizontalDivider(
                        color = StuMartGreen.copy(alpha = 0.2f),
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "🦆", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Vịt con cam kết: Hẹn gặp trực tiếp tại trường, kiểm tra kỹ hàng trước khi thanh toán. Tuyệt đối không chuyển cọc trước!",
                            fontSize = 12.sp,
                            color = StuMartGreen,
                            lineHeight = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ================= 4. THÔNG TIN NGƯỜI BÁN (SELLER PROFILE CARD) =================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar người bán
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(StuMartYellow),
                            contentAlignment = Alignment.Center
                        ) {
                            val initial = (post.sellerName ?: "S").firstOrNull()?.uppercaseChar() ?: 'S'
                            Text(
                                text = initial.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = StuMartTextDark
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = post.sellerName ?: "Sinh viên Bách Khoa",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StuMartTextDark
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = "Đã xác thực thẻ sinh viên",
                                    tint = StuMartGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Sinh viên đã xác minh thẻ trường",
                                fontSize = 12.sp,
                                color = StuMartTextGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Chỉ số uy tín của sinh viên
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(StuMartLightBackground)
                            .padding(vertical = 10.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "⭐ 4.9/5",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = StuMartTextDark
                            )
                            Text(
                                text = "Đánh giá uy tín",
                                fontSize = 11.sp,
                                color = StuMartTextGray
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(24.dp)
                                .background(Color.LightGray)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "⚡ < 15 phút",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = StuMartGreen
                            )
                            Text(
                                text = "Tốc độ phản hồi",
                                fontSize = 11.sp,
                                color = StuMartTextGray
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(24.dp)
                                .background(Color.LightGray)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "100%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = StuMartTextDark
                            )
                            Text(
                                text = "Giao dịch tại trường",
                                fontSize = 11.sp,
                                color = StuMartTextGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ================= 5. MÔ TẢ CHI TIẾT SẢN PHẨM =================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Mô tả sản phẩm",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = StuMartTextDark
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val descriptionText = if (post.description.isNotBlank()) {
                        post.description
                    } else {
                        "Người bán chưa cập nhật thêm mô tả chi tiết. Bạn có thể bấm Nhắn tin ngay để hỏi thêm thông tin về tình trạng món đồ nhé!"
                    }

                    Text(
                        text = descriptionText,
                        fontSize = 14.sp,
                        color = StuMartTextDark,
                        lineHeight = 22.sp,
                        maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 5,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (descriptionText.length > 180) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isDescriptionExpanded) "Thu gọn ▲" else "Xem thêm ▼",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = StuMartGreen,
                            modifier = Modifier
                                .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                                .padding(vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Bảng thuộc tính sản phẩm
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AttributePill(
                            label = "Tình trạng",
                            value = post.condition,
                            modifier = Modifier.weight(1f)
                        )
                        AttributePill(
                            label = "Danh mục",
                            value = post.category ?: "Khác",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // ================= MODAL BOTTOM SHEET: TRẢ GIÁ / THƯƠNG LƯỢNG =================
    if (showOfferSheet) {
        ModalBottomSheet(
            onDismissRequest = { showOfferSheet = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            OfferBottomSheetContent(
                originalPrice = post.price,
                onDismiss = { showOfferSheet = false },
                onSendOffer = { offerPrice, note ->
                    showOfferSheet = false
                    Toast.makeText(
                        context,
                        "Đã gửi đề nghị giá %,d đ đến người bán!".format(offerPrice.toInt()),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    // ================= DIALOG: BÁO CÁO VI PHẠM =================
    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = {
                Text(
                    text = "Báo cáo bài đăng",
                    fontWeight = FontWeight.Bold,
                    color = StuMartTextDark
                )
            },
            text = {
                Column {
                    Text(
                        text = "Vui lòng chọn lý do báo cáo:",
                        fontSize = 14.sp,
                        color = StuMartTextGray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    listOf(
                        "Hàng không đúng mô tả",
                        "Nghi vấn gian lận / yêu cầu cọc",
                        "Sản phẩm cấm hoặc nguy hiểm",
                        "Đã bán nhưng chưa gỡ bài"
                    ).forEach { reason ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showReportDialog = false
                                    Toast.makeText(
                                        context,
                                        "Cảm ơn bạn. Đội ngũ kiểm duyệt StuMart sẽ xử lý báo cáo này!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ChevronRight,
                                contentDescription = null,
                                tint = StuMartTextGray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = reason,
                                fontSize = 14.sp,
                                color = StuMartTextDark
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("Đóng", color = StuMartGreen, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

/**
 * Chip hiển thị thông số thuộc tính sản phẩm
 */
@Composable
private fun AttributePill(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(StuMartLightBackground)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = StuMartTextGray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = StuMartTextDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Nội dung BottomSheet Trả giá / Thương lượng giá
 */
@Composable
private fun OfferBottomSheetContent(
    originalPrice: Double,
    onDismiss: () -> Unit,
    onSendOffer: (Double, String) -> Unit
) {
    var offerPriceText by remember { mutableStateOf("") }
    var offerNote by remember { mutableStateOf("") }

    val discount5 = remember(originalPrice) { (originalPrice * 0.95).toInt() }
    val discount10 = remember(originalPrice) { (originalPrice * 0.90).toInt() }
    val discount15 = remember(originalPrice) { (originalPrice * 0.85).toInt() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Đề xuất mức giá bạn muốn mua",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = StuMartTextDark
            )
            IconButton(onClick = onDismiss) {
                Icon(Icons.Filled.Close, contentDescription = "Đóng", tint = StuMartTextGray)
            }
        }

        Text(
            text = "Giá gốc của người bán: %,d đ".format(originalPrice.toInt()),
            fontSize = 13.sp,
            color = StuMartTextGray
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Gợi ý mức giảm nhanh
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                Pair("-5%", discount5),
                Pair("-10%", discount10),
                Pair("-15%", discount15)
            ).forEach { (label, value) ->
                SuggestionChip(
                    onClick = { offerPriceText = value.toString() },
                    label = { Text("$label (%,dđ)".format(value), fontSize = 12.sp) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = StuMartGreenContainer,
                        labelColor = StuMartGreen
                    ),
                    border = null
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Ô nhập giá đề xuất
        OutlinedTextField(
            value = offerPriceText,
            onValueChange = { input ->
                if (input.all { it.isDigit() }) {
                    offerPriceText = input
                }
            },
            label = { Text("Số tiền bạn muốn trả (VNĐ)") },
            placeholder = { Text("VD: %,d".format(discount10)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = StuMartGreen,
                focusedLabelColor = StuMartGreen
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Ô lời nhắn gửi người bán
        OutlinedTextField(
            value = offerNote,
            onValueChange = { offerNote = it },
            label = { Text("Lời nhắn thêm (tùy chọn)") },
            placeholder = { Text("VD: Mình ở ký túc xá H1 qua lấy luôn trong chiều...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = StuMartGreen,
                focusedLabelColor = StuMartGreen
            ),
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = {
                val price = offerPriceText.toDoubleOrNull() ?: originalPrice
                onSendOffer(price, offerNote)
            },
            enabled = offerPriceText.isNotBlank() && (offerPriceText.toDoubleOrNull() ?: 0.0) > 0,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = StuMartGreen,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Gửi đề nghị giá",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
