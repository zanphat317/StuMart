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
        // Khởi tạo danh sách mặc định nếu chưa lấy được dữ liệu mạng
        _posts.value = emptyList()
    }

    suspend fun fetchPosts() {
        try {
            val remotePosts = SupabaseClient.client.postgrest["posts"]
                .select()
                .decodeList<Post>()
            
            // Sắp xếp bài đăng MỚI NHẤT lên đầu tiên (ID giảm dần)
            val sortedPosts = remotePosts.sortedByDescending { it.id ?: Long.MAX_VALUE }
            _posts.value = sortedPosts
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun createPost(post: Post) {
        // Đặt ngay bài vừa tạo lên đầu danh sách địa phương
        val currentList = _posts.value.filter { it.title != post.title }
        _posts.value = listOf(post) + currentList

        try {
            SupabaseClient.client.postgrest["posts"].insert(post)
            // Tải lại và sắp xếp lại bài từ Supabase
            fetchPosts()
        } catch (e: Exception) {
            Log.e("POST_REPO", "Lỗi khi đăng bài lên Supabase: ${e.message}", e)
        }
    }

    suspend fun updatePost(post: Post) {
        // Cập nhật ngay bài vừa sửa trong danh sách địa phương
        val updatedList = _posts.value.map { existing ->
            if (existing.id != null && existing.id == post.id) post else existing
        }
        _posts.value = updatedList

        try {
            if (post.id != null) {
                SupabaseClient.client.postgrest["posts"].update(post) {
                    filter { eq("id", post.id) }
                }
            }
            fetchPosts()
        } catch (e: Exception) {
            Log.e("POST_REPO", "Lỗi khi cập nhật bài đăng trên Supabase: ${e.message}", e)
        }
    }

    suspend fun deletePost(postId: Long) {
        _posts.value = _posts.value.filter { it.id != postId }
        try {
            SupabaseClient.client.postgrest["posts"].delete {
                filter { eq("id", postId) }
            }
            fetchPosts()
        } catch (e: Exception) {
            Log.e("POST_REPO", "Lỗi khi xóa bài đăng trên Supabase: ${e.message}", e)
        }
    }
}
