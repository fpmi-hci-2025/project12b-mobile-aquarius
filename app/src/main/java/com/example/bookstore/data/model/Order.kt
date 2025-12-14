package com.example.bookstore.data.model

import java.util.Date

data class Order(
    val status: String,
    val totalAmount: Double,
    val createdAt: String, // ISO 8601 date string
    val customerNotes: String?,
    val deliveryAddress: String?,
    val items: List<OrderItem>
)
