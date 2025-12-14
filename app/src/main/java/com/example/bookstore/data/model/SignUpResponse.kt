package com.example.bookstore.data.model

data class SignUpResponse(
    val message: String,
    val userId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    // Дополнительные поля, если API их возвращает
    val accessToken: String? = null,
    val refreshToken: String? = null
)
