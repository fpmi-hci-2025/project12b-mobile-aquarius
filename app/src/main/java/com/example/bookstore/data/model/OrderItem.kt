package com.example.bookstore.data.model

data class OrderItem(
    val bookId: String,
    val bookTitle: String,
    val quantity: Int,
    val unitPrice: Double
)
