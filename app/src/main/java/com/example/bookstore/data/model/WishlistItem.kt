package com.example.bookstore.data.model

data class WishlistItem(
    val bookId: String,
    val bookTitle: String,
    val coverImage: String?,
    val price: Double,
    val description: String,
    val authors: List<String>,
    val genres: List<String>
)
