package com.example.bookstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.model.BookResponse
import com.example.bookstore.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookUiState>(BookUiState.Idle)
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    fun loadBooks(
        title: String? = null,
        pageNumber: Int = 1,
        pageSize: Int = 20
    ) {
        viewModelScope.launch {
            _uiState.value = BookUiState.Loading

            try {
                val books = bookRepository.searchBooks(
                    title = title,
                    pageNumber = pageNumber,
                    pageSize = pageSize
                )
                _uiState.value = BookUiState.Success(books)
            } catch (e: Exception) {
                _uiState.value = BookUiState.Error(
                    message = when {
                        e.message?.contains("404") == true -> "Books not found"
                        e.message?.contains("network", ignoreCase = true) == true ||
                                e.message?.contains("socket", ignoreCase = true) == true ||
                                e.message?.contains("timeout", ignoreCase = true) == true ->
                            "Network error. Please check your internet connection."
                        else -> e.message ?: "Failed to load books"
                    }
                )
            }
        }
    }

    fun searchBooks(query: String) {
        loadBooks(title = query.ifBlank { null })
    }
}

sealed class BookUiState {
    data object Idle : BookUiState()
    data object Loading : BookUiState()
    data class Success(val books: List<BookResponse>) : BookUiState()
    data class Error(val message: String) : BookUiState()
}

