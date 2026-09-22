package com.example.stumart.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.ResponseBody

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<ResponseBody>
}
