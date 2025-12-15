package com.example.bookstore.data.repository

import com.example.bookstore.data.api.OrderApiService
import com.example.bookstore.data.model.CreateOrderRequest
import com.example.bookstore.data.model.Order
import com.example.bookstore.data.model.PaymentRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderApiService: OrderApiService
) {
    suspend fun getOrders(pageNumber: Int? = null, pageSize: Int? = null): List<Order> {
        return orderApiService.getOrders(pageNumber, pageSize)
    }
    
    suspend fun createOrder(request: CreateOrderRequest): Result<Pair<String, Double>> {  // Возвращаем orderId и totalAmount
        return try {
            val response = orderApiService.createOrder(request)
            // Извлекаем orderId и totalAmount из тела ответа
            Result.success(Pair(response.id, response.totalAmount))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getOrderStatus(orderId: String): String {
        return orderApiService.getOrderStatus(orderId)
    }
    
    suspend fun payOrder(orderId: String, request: PaymentRequest): Result<Unit> {
        return try {
            val response = orderApiService.payOrder(orderId, request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to pay order: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
