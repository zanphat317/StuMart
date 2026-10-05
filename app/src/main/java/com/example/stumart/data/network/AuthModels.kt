package com.example.stumart.data.network

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest(
    val email: String
)

data class ResetPasswordRequest(
    val email: String,
    @SerializedName("otp_code")
    val otp: String,
    @SerializedName("new_password")
    val newPassword: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: String,
    val message: String,
    val user: UserData? = null
)

data class UserData(
    val id: String,
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("student_id")
    val studentId: String?,
    val university: String?,
    val phone: String?
)
