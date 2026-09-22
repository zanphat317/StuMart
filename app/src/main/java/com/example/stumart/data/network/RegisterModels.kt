package com.example.stumart.data.network

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val email: String,
    val password: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("student_id")
    val studentId: String,
    val university: String,
    val phone: String
)

data class RegisterResponse(
    val message: String,
    @SerializedName("user_id")
    val userId: String? = null
)
