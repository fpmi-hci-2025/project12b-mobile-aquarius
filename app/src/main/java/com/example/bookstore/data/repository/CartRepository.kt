package com.example.bookstore.data.repository

import com.example.bookstore.data.api.CartApiService
import com.example.bookstore.data.model.CartResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
    private val cartApiService: CartApiService
) {
    suspend fun getCart(): CartResponse {
        return cartApiService.getCart()
    }
    
    suspend fun addBookToCart(bookId: String, quantity: Int = 1): Result<Unit> {
        return try {
            val response = cartApiService.addBookToCart(bookId, quantity)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to add book to cart: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun removeBookFromCart(bookId: String): Result<Unit> {
        return try {
            val response = cartApiService.removeBookFromCart(bookId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to remove book from cart: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
