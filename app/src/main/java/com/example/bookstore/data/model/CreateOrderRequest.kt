package com.example.bookstore.data.model

data class CreateOrderRequest(
    val customerNotes: String? = null,
    val deliveryAddress: String? = null,
    val orderItems: List<OrderItemRequest> = emptyList()
)

data class OrderItemRequest(
    val bookId: String,
    val count: Int
)
