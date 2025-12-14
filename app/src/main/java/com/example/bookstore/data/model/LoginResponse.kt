package com.example.bookstore.data.model

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val userDetails: UserDetails
)

data class UserDetails(
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String
)

