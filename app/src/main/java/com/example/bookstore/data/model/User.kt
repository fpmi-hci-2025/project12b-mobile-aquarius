package com.example.bookstore.data.model

data class User(
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String?,
    val dateOfBirth: String?,
    val profileImage: String? = null,
)
