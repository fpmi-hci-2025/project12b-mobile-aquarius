package com.example.bookstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.local.UserManager
import com.example.bookstore.data.model.SignUpRequest
import com.example.bookstore.data.model.SignUpResponse
import com.example.bookstore.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    // Метод принимает отдельные параметры
    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String?,
        dateOfBirth: String?
    ) {
        viewModelScope.launch {
            _uiState.value = SignUpUiState.Loading

            try {
                // Создаем SignUpRequest внутри метода
                val request = SignUpRequest(
                    email = email,
                    passwordHash = password,
                    firstName = firstName,
                    lastName = lastName,
                    phone = phone,
                    dateOfBirth = dateOfBirth
                )

                val response = authRepository.signUp(request)
                _uiState.value = SignUpUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = SignUpUiState.Error(
                    message = when {
                        e.message?.contains("409") == true ||
                                e.message?.contains("Conflict") == true ->
                            "Email already exists. Please use a different email or login."
                        e.message?.contains("400") == true ||
                                e.message?.contains("Bad Request") == true ->
                            "Invalid data. Please check your information."
                        e.message?.contains("network", ignoreCase = true) == true ||
                                e.message?.contains("socket", ignoreCase = true) == true ||
                                e.message?.contains("timeout", ignoreCase = true) == true ->
                            "Network error. Please check your internet connection."
                        else -> e.message ?: "Registration failed. Please try again."
                    }
                )
            }
        }
    }

    // Альтернативный метод для SignUpRequest
    fun signUp(request: SignUpRequest) {
        viewModelScope.launch {
            _uiState.value = SignUpUiState.Loading

            try {
                val response = authRepository.signUp(request)
                // Сохраняем данные пользователя
                userManager.saveUser(response)
                _uiState.value = SignUpUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = SignUpUiState.Error(
                    message = when {
                        e.message?.contains("409") == true -> "Email already exists"
                        e.message?.contains("network", ignoreCase = true) == true ->
                            "Network error. Please check your connection."
                        else -> e.message ?: "Registration failed"
                    }
                )
            }
        }
    }

    fun setError(message: String) {
        _uiState.value = SignUpUiState.Error(message)
    }

    fun resetState() {
        _uiState.value = SignUpUiState.Idle
    }
}

sealed class SignUpUiState {
    data object Idle : SignUpUiState()
    data object Loading : SignUpUiState()
    data class Success(val response: SignUpResponse) : SignUpUiState()
    data class Error(val message: String) : SignUpUiState()
}