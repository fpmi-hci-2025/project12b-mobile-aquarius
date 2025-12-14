package com.example.bookstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.model.WishlistItem
import com.example.bookstore.data.repository.WishlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WishlistUiState>(WishlistUiState.Idle)
    val uiState: StateFlow<WishlistUiState> = _uiState.asStateFlow()

    fun loadWishlist() {
        viewModelScope.launch {
            _uiState.value = WishlistUiState.Loading
            try {
                val wishlist = wishlistRepository.getWishlist()
                _uiState.value = WishlistUiState.Success(wishlist)
            } catch (e: Exception) {
                _uiState.value = WishlistUiState.Error(
                    message = when {
                        e.message?.contains("401") == true -> "Please login to view your wishlist"
                        e.message?.contains("network", ignoreCase = true) == true -> 
                            "Network error. Please check your internet connection."
                        else -> e.message ?: "Failed to load wishlist"
                    }
                )
            }
        }
    }

    fun addToWishlist(bookId: String) {
        viewModelScope.launch {
            try {
                val result = wishlistRepository.addToWishlist(bookId)
                result.onSuccess {
                    loadWishlist() // Reload wishlist after adding
                }.onFailure { e ->
                    _uiState.value = WishlistUiState.Error(
                        message = e.message ?: "Failed to add to wishlist"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = WishlistUiState.Error(
                    message = e.message ?: "Failed to add to wishlist"
                )
            }
        }
    }

    fun removeFromWishlist(bookId: String) {
        viewModelScope.launch {
            try {
                val result = wishlistRepository.removeFromWishlist(bookId)
                result.onSuccess {
                    loadWishlist() // Reload wishlist after removing
                }.onFailure { e ->
                    _uiState.value = WishlistUiState.Error(
                        message = e.message ?: "Failed to remove from wishlist"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = WishlistUiState.Error(
                    message = e.message ?: "Failed to remove from wishlist"
                )
            }
        }
    }
}

sealed class WishlistUiState {
    data object Idle : WishlistUiState()
    data object Loading : WishlistUiState()
    data class Success(val wishlist: List<WishlistItem>) : WishlistUiState()
    data class Error(val message: String) : WishlistUiState()
}
