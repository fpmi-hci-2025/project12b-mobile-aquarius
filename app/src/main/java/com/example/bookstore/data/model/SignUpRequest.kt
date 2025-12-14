package com.example.bookstore.data.model

data class SignUpRequest(
    val email: String,
    val passwordHash: String,
    val firstName: String,
    val lastName: String,
    val phone: String? = null,
    val dateOfBirth: String? = null
)
