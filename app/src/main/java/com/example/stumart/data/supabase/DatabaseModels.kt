package com.example.stumart.data.supabase

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String? = null,
    val email: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("student_id") val studentId: String? = null,
    val university: String? = null,
    val phone: String? = null
)

@Serializable
data class Post(
    val id: String? = null, // UUID của bài đăng do DB tự sinh
    val title: String,
    val type: String, // VD: "Bán thường", "Giá rẻ"...
    val price: Double,
    val condition: String,
    val description: String,
    @SerialName("user_id") val userId: String? = null // Liên kết đến người đăng
)