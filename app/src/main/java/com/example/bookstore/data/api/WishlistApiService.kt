package com.example.bookstore.data.api

import com.example.bookstore.data.model.WishlistItem
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WishlistApiService {
    @GET("api/wishlists")
    suspend fun getWishlist(): List<WishlistItem>
    
    @POST("api/wishlists/{bookId}")
    suspend fun addToWishlist(
        @Path("bookId") bookId: String
    ): retrofit2.Response<Void>
    
    @DELETE("api/wishlists/{bookId}")
    suspend fun removeFromWishlist(
        @Path("bookId") bookId: String
    ): retrofit2.Response<Void>
}
