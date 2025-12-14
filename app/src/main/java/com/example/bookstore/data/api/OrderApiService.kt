package com.example.bookstore.data.api

import com.example.bookstore.data.model.CreateOrderRequest
import com.example.bookstore.data.model.Order
import com.example.bookstore.data.model.PaymentRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderApiService {
    @GET("api/orders")
    suspend fun getOrders(
        @Query("PageNumber") pageNumber: Int? = null,
        @Query("PageSize") pageSize: Int? = null
    ): List<Order>
    
    @POST("api/orders")
    suspend fun createOrder(
        @Body request: CreateOrderRequest
    ): retrofit2.Response<Void>
    
    @GET("api/orders/{orderId}/status")
    suspend fun getOrderStatus(
        @Path("orderId") orderId: String
    ): String
    
    @POST("api/orders/{orderId}/pay")
    suspend fun payOrder(
        @Path("orderId") orderId: String,
        @Body request: PaymentRequest
    ): retrofit2.Response<Void>
}
