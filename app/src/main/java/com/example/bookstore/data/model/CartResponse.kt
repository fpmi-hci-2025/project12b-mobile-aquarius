package com.example.bookstore.data.model

data class CartResponse(
    val cartItems: List<CartItem>,
    val totalItems: Int,
    val itemsPrice: Double,
    val shippingCost: Double,
    val totalPrice: Double
)
