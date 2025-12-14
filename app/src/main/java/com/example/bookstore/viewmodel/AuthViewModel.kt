package com.example.bookstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.local.UserManager
import com.example.bookstore.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userManager: UserManager
) : ViewModel() {

    private val _signOutState = MutableStateFlow<SignOutUiState>(SignOutUiState.Idle)
    val signOutState: StateFlow<SignOutUiState> = _signOutState.asStateFlow()

    fun signOut() {
        viewModelScope.launch {
            _signOutState.value = SignOutUiState.Loading

            try {
                authRepository.signOut()
                // Очищаем данные пользователя
                userManager.clearUser()
                _signOutState.value = SignOutUiState.Success
            } catch (e: Exception) {
                // Даже если API вызов не удался, очищаем локальные данные
                userManager.clearUser()
                _signOutState.value = SignOutUiState.Success
            }
        }
    }

    fun getUserEmail(): String? = userManager.getUserEmail()
    fun getUserFullName(): String = userManager.getFullName()
    fun isLoggedIn(): Boolean = userManager.isLoggedIn()
    fun isLoggedInFlow() = userManager.isLoggedInFlow
}

sealed class SignOutUiState {
    data object Idle : SignOutUiState()
    data object Loading : SignOutUiState()
    data object Success : SignOutUiState()
    data class Error(val message: String) : SignOutUiState()
}

