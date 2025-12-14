package com.example.bookstore.data.api

import com.example.bookstore.data.model.LoginRequest
import com.example.bookstore.data.model.LoginResponse
import com.example.bookstore.data.model.RefreshTokenRequest
import com.example.bookstore.data.model.RefreshTokenResponse
import com.example.bookstore.data.model.SignUpRequest
import com.example.bookstore.data.model.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/sign-up")
    suspend fun register(@Body request: SignUpRequest): SignUpResponse
    
    @POST("api/auth/sign-in")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    
    @POST("api/auth/sign-out")
    suspend fun signOut(): retrofit2.Response<Void>
    
    @POST("api/auth/refresh")
    suspend fun refresh(@Body request: RefreshTokenRequest): RefreshTokenResponse
}
