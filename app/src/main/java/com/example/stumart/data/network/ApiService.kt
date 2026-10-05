package com.example.stumart.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.ResponseBody

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<ResponseBody>

    @POST("forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ResponseBody>

    @POST("reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResponseBody>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
