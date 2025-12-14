package com.example.bookstore.data.model

data class CartItem(
    val bookId: String,
    val bookTitle: String,
    val bookPrice: Double,
    val quantity: Int,
    val totalPrice: Double
)
