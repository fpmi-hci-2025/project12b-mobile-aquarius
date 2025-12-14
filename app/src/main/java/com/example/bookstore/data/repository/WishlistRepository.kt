package com.example.bookstore.data.repository

import com.example.bookstore.data.api.WishlistApiService
import com.example.bookstore.data.model.WishlistItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WishlistRepository @Inject constructor(
    private val wishlistApiService: WishlistApiService
) {
    suspend fun getWishlist(): List<WishlistItem> {
        return wishlistApiService.getWishlist()
    }
    
    suspend fun addToWishlist(bookId: String): Result<Unit> {
        return try {
            val response = wishlistApiService.addToWishlist(bookId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to add to wishlist: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun removeFromWishlist(bookId: String): Result<Unit> {
        return try {
            val response = wishlistApiService.removeFromWishlist(bookId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to remove from wishlist: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
