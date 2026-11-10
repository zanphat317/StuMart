package com.example.stumart.data.repository

import com.example.stumart.data.supabase.Post
import com.example.stumart.data.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object PostRepository {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    init {
        // Dữ liệu mẫu ban đầu
        _posts.value = listOf(
            Post(
                id = "1",
                title = "Giáo trình Giải tích A1 – Bách Khoa HN",
                type = "Giá rẻ",
                price = 35000.0,
                condition = "Cũ",
                description = "Sách còn mới 90%, đủ bài tập.",
                sellerName = "Minh Tuấn",
                location = "Đống Đa, HN"
            ),
            Post(
                id = "2",
                title = "Laptop Dell XPS 13 – Core i5, RAM 8GB",
                type = "Giá rẻ",
                price = 8500000.0,
                condition = "Như mới",
                description = "Máy dùng lướt, pin khoẻ.",
                sellerName = "Thu Hà",
                location = "Cầu Giấy, HN"
            ),
            Post(
                id = "3",
                title = "Balo laptop Samsonite 15.6 inch",
                type = "Bán thường",
                price = 450000.0,
                condition = "Cũ",
                description = "Balo chắc chắn, nhiều ngăn.",
                sellerName = "Lan Anh",
                location = "Hoàng Mai, HN"
            ),
            Post(
                id = "4",
                title = "iPhone 13 Pro 256GB - Deep Purple",
                type = "Đấu giá",
                price = 12500000.0,
                condition = "Như mới",
                description = "Hàng chính hãng VN/A.",
                sellerName = "Đạt Nguyễn",
                location = "Bách Khoa, HN"
            ),
            Post(
                id = "5",
                title = "Túi Vịt Con - Quà bí ẩn sinh viên",
                type = "Túi mù",
                price = 50000.0,
                condition = "Mới",
                description = "Túi quà ngẫu nhiên gồm phụ kiện học tập.",
                sellerName = "StuMart Store",
                location = "Bách Khoa, HN"
            )
        )
    }

    suspend fun fetchPosts() {
        try {
            val remotePosts = SupabaseClient.client.postgrest["posts"]
                .select()
                .decodeList<Post>()
            if (remotePosts.isNotEmpty()) {
                val combined = (remotePosts + _posts.value).distinctBy { it.id ?: it.title }
                _posts.value = combined
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun createPost(post: Post) {
        // Thêm vào danh sách địa phương ngay lập tức để UI cập nhật mượt mà
        _posts.value = listOf(post) + _posts.value

        // Ghi vào Supabase
        try {
            SupabaseClient.client.postgrest["posts"].insert(post)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
