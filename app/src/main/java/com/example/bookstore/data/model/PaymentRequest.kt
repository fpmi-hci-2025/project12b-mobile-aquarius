package com.example.bookstore.data.model

data class PaymentRequest(
    val paymentMethod: String,
    val orderId: String,
    val amount: Double
)
