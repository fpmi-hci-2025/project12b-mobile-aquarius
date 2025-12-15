package com.example.bookstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.model.CartResponse
import com.example.bookstore.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CartUiState>(CartUiState.Idle)
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _bookImagesCache = mutableMapOf<String, String>()

    fun loadCart() {
        viewModelScope.launch {
            _uiState.value = CartUiState.Loading
            try {
                val cart = cartRepository.getCart()

                // Просто используем корзину как есть, не перезаписывая base64CoverImage
                _uiState.value = CartUiState.Success(cart)
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(
                    message = when {
                        e.message?.contains("401") == true -> "Please login to view your cart"
                        e.message?.contains("network", ignoreCase = true) == true ->
                            "Network error. Please check your internet connection."
                        else -> e.message ?: "Failed to load cart"
                    }
                )
            }
        }
    }

    fun addBookToCart(bookId: String, quantity: Int = 1) {
        viewModelScope.launch {
            try {
                val result = cartRepository.addBookToCart(bookId, quantity)
                result.onSuccess {
                    loadCart() // Reload cart after adding
                }.onFailure { e ->
                    _uiState.value = CartUiState.Error(
                        message = e.message ?: "Failed to add book to cart"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(
                    message = e.message ?: "Failed to add book to cart"
                )
            }
        }
    }

    fun removeBookFromCart(bookId: String) {
        viewModelScope.launch {
            try {
                val result = cartRepository.removeBookFromCart(bookId)
                result.onSuccess {
                    loadCart() // Reload cart after removing
                }.onFailure { e ->
                    _uiState.value = CartUiState.Error(
                        message = e.message ?: "Failed to remove book from cart"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = CartUiState.Error(
                    message = e.message ?: "Failed to remove book from cart"
                )
            }
        }
    }
}

sealed class CartUiState {
    data object Idle : CartUiState()
    data object Loading : CartUiState()
    data class Success(val cart: CartResponse) : CartUiState()
    data class Error(val message: String) : CartUiState()
}
