package com.example.bookstore.data.api

import com.example.bookstore.data.model.CartResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CartApiService {
    @GET("api/carts")
    suspend fun getCart(): CartResponse
    
    @POST("api/carts/{bookId}")
    suspend fun addBookToCart(
        @Path("bookId") bookId: String,
        @Query("quantity") quantity: Int = 1
    ): retrofit2.Response<Void>
    
    @DELETE("api/carts/{bookId}")
    suspend fun removeBookFromCart(
        @Path("bookId") bookId: String
    ): retrofit2.Response<Void>
}
