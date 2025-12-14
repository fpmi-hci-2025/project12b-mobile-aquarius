package com.example.bookstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.local.UserManager
import com.example.bookstore.data.model.LoginRequest
import com.example.bookstore.data.model.LoginResponse
import com.example.bookstore.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            try {
                val request = LoginRequest(
                    email = email,
                    passwordHash = password
                )

                val response = authRepository.login(request)
                // Сохраняем данные из ответа (теперь email и имя есть в userDetails)
                userManager.saveLoginData(response, response.userDetails.email)
                _uiState.value = LoginUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(
                    message = when {
                        e.message?.contains("401") == true ||
                                e.message?.contains("Unauthorized") == true ->
                            "Invalid email or password. Please try again."
                        e.message?.contains("400") == true ||
                                e.message?.contains("Bad Request") == true ->
                            "Invalid data. Please check your information."
                        e.message?.contains("network", ignoreCase = true) == true ||
                                e.message?.contains("socket", ignoreCase = true) == true ||
                                e.message?.contains("timeout", ignoreCase = true)!! ->
                            "Network error. Please check your internet connection."
                        else -> e.message ?: "Login failed. Please try again."
                    }
                )
            }
        }
    }

    fun setError(message: String) {
        _uiState.value = LoginUiState.Error(message)
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data class Success(val response: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

