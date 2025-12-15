package com.example.bookstore.data.model

data class CreateOrderResponse(
    val id: String,
    val status: String,
    val totalAmount: Double,
    val createdAt: String,
    val customerNotes: String?,
    val deliveryAddress: String?,
    val items: List<OrderItem>
)
