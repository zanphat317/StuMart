package com.example.stumart.data.repository

import android.util.Log
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
        // Xóa hoàn toàn tất cả sản phẩm tĩnh (Mock data)
        _posts.value = emptyList()
    }

    suspend fun fetchPosts() {
        try {
            val remotePosts = SupabaseClient.client.postgrest["posts"]
                .select()
                .decodeList<Post>()
            _posts.value = remotePosts
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun createPost(post: Post) {
        try {
            // Đăng bài đăng trực tiếp lên Supabase
            SupabaseClient.client.postgrest["posts"].insert(post)
            // Tải lại danh sách bài đăng từ Supabase về ứng dụng
            fetchPosts()
        } catch (e: Exception) {
            Log.e("POST_REPO", "Lỗi khi đăng bài lên Supabase: ${e.message}", e)
            // Không lưu ảo vào RAM nữa nếu Supabase từ chối để tránh hiểu nhầm
            throw e
        }
    }
}
