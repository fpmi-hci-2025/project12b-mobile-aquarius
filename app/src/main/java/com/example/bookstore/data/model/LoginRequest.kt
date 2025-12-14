package com.example.bookstore.data.model

data class LoginRequest(
    val email: String,
    val passwordHash: String
)

