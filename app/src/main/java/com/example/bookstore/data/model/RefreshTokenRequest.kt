package com.example.bookstore.data.model

data class RefreshTokenRequest(
    val accessToken: String,
    val refreshToken: String
)

