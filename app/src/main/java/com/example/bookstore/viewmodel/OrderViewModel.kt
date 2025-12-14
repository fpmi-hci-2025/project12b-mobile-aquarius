package com.example.bookstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.model.CreateOrderRequest
import com.example.bookstore.data.model.Order
import com.example.bookstore.data.model.PaymentRequest
import com.example.bookstore.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _ordersState = MutableStateFlow<OrdersUiState>(OrdersUiState.Idle)
    val ordersState: StateFlow<OrdersUiState> = _ordersState.asStateFlow()

    private val _createOrderState = MutableStateFlow<CreateOrderUiState>(CreateOrderUiState.Idle)
    val createOrderState: StateFlow<CreateOrderUiState> = _createOrderState.asStateFlow()

    fun loadOrders(pageNumber: Int? = null, pageSize: Int? = null) {
        viewModelScope.launch {
            _ordersState.value = OrdersUiState.Loading
            try {
                val orders = orderRepository.getOrders(pageNumber, pageSize)
                _ordersState.value = OrdersUiState.Success(orders)
            } catch (e: Exception) {
                _ordersState.value = OrdersUiState.Error(
                    message = e.message ?: "Failed to load orders"
                )
            }
        }
    }

    fun createOrder(request: CreateOrderRequest) {
        viewModelScope.launch {
            _createOrderState.value = CreateOrderUiState.Loading
            try {
                val result = orderRepository.createOrder(request)
                result.onSuccess {
                    _createOrderState.value = CreateOrderUiState.Success
                    loadOrders() // Reload orders after creating
                }.onFailure { e ->
                    _createOrderState.value = CreateOrderUiState.Error(
                        message = e.message ?: "Failed to create order"
                    )
                }
            } catch (e: Exception) {
                _createOrderState.value = CreateOrderUiState.Error(
                    message = e.message ?: "Failed to create order"
                )
            }
        }
    }

    fun getOrderStatus(orderId: String) {
        viewModelScope.launch {
            try {
                val status = orderRepository.getOrderStatus(orderId)
                // Handle status update if needed
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun payOrder(orderId: String, paymentMethod: String, amount: Double) {
        viewModelScope.launch {
            try {
                val request = PaymentRequest(
                    paymentMethod = paymentMethod,
                    orderId = orderId,
                    amount = amount
                )
                val result = orderRepository.payOrder(orderId, request)
                result.onSuccess {
                    loadOrders() // Reload orders after payment
                }.onFailure { e ->
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

sealed class OrdersUiState {
    data object Idle : OrdersUiState()
    data object Loading : OrdersUiState()
    data class Success(val orders: List<Order>) : OrdersUiState()
    data class Error(val message: String) : OrdersUiState()
}

sealed class CreateOrderUiState {
    data object Idle : CreateOrderUiState()
    data object Loading : CreateOrderUiState()
    data object Success : CreateOrderUiState()
    data class Error(val message: String) : CreateOrderUiState()
}
