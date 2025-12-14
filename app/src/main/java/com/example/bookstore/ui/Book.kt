package com.example.bookstore.ui

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val price: Double,
    val imageUrl: String = "",
    val description: String? = ""
)
