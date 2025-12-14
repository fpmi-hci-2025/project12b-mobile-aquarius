package com.example.bookstore.data.repository

import com.example.bookstore.data.api.AuthApiService
import com.example.bookstore.data.model.LoginRequest
import com.example.bookstore.data.model.LoginResponse
import com.example.bookstore.data.model.RefreshTokenRequest
import com.example.bookstore.data.model.RefreshTokenResponse
import com.example.bookstore.data.model.SignUpRequest
import com.example.bookstore.data.model.SignUpResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService
) {
    suspend fun signUp(request: SignUpRequest): SignUpResponse {
        return authApiService.register(request)
    }

    suspend fun login(request: LoginRequest): LoginResponse {
        return authApiService.login(request)
    }

    suspend fun signOut() {
        authApiService.signOut()
    }

    suspend fun refresh(accessToken: String, refreshToken: String): RefreshTokenResponse {
        val request = RefreshTokenRequest(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
        return authApiService.refresh(request)
    }

    // Здесь можно добавить кэширование, обработку ошибок и т.д.
}