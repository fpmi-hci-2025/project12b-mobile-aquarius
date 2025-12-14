package com.example.bookstore.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.bookstore.data.model.LoginResponse
import com.example.bookstore.data.model.SignUpResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "user_prefs",
        Context.MODE_PRIVATE
    )

    private val KEY_USER_ID = "user_id"
    private val KEY_EMAIL = "email"
    private val KEY_FIRST_NAME = "first_name"
    private val KEY_LAST_NAME = "last_name"
    private val KEY_ACCESS_TOKEN = "access_token"
    private val KEY_REFRESH_TOKEN = "refresh_token"
    private val KEY_IS_LOGGED_IN = "is_logged_in"
    
    // Flow для реактивного отслеживания состояния авторизации
    private val _isLoggedInFlow = MutableStateFlow(prefs.getBoolean(KEY_IS_LOGGED_IN, false))
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedInFlow.asStateFlow()

    fun saveLoginData(loginResponse: LoginResponse, email: String) {
        prefs.edit().apply {
            putString(KEY_EMAIL, loginResponse.userDetails.email)
            putString(KEY_ACCESS_TOKEN, loginResponse.accessToken)
            putString(KEY_REFRESH_TOKEN, loginResponse.refreshToken)
            putString(KEY_FIRST_NAME, loginResponse.userDetails.firstName)
            putString(KEY_LAST_NAME, loginResponse.userDetails.lastName)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
        _isLoggedInFlow.value = true
    }
    
    // Декодирование email из JWT токена
    private fun decodeEmailFromToken(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null
            
            val payload = parts[1]
            // Добавляем padding если нужно
            val paddedPayload = payload + "=".repeat((4 - payload.length % 4) % 4)
            val decodedBytes = android.util.Base64.decode(paddedPayload, android.util.Base64.URL_SAFE)
            val jsonString = String(decodedBytes, Charsets.UTF_8)
            
            // Ищем email по ключу из схемы claims
            val emailKey = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress"
            val emailRegex = "\"${emailKey.replace(".", "\\.")}\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            emailRegex.find(jsonString)?.groupValues?.get(1)
        } catch (e: Exception) {
            null
        }
    }

    fun saveUser(signUpResponse: SignUpResponse) {
        prefs.edit().apply {
            putString(KEY_USER_ID, signUpResponse.userId)
            putString(KEY_EMAIL, signUpResponse.email)
            putString(KEY_FIRST_NAME, signUpResponse.firstName)
            putString(KEY_LAST_NAME, signUpResponse.lastName)
            signUpResponse.accessToken?.let { putString(KEY_ACCESS_TOKEN, it) }
            signUpResponse.refreshToken?.let { putString(KEY_REFRESH_TOKEN, it) }
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
        _isLoggedInFlow.value = true
    }

    fun getUserEmail(): String? = prefs.getString(KEY_EMAIL, null)
    fun getUserFirstName(): String? = prefs.getString(KEY_FIRST_NAME, null)
    fun getUserLastName(): String? = prefs.getString(KEY_LAST_NAME, null)
    fun getFullName(): String {
        val firstName = getUserFirstName() ?: ""
        val lastName = getUserLastName() ?: ""
        return "$firstName $lastName".trim().ifEmpty { "User" }
    }
    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)
    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    
    fun updateTokens(accessToken: String, refreshToken: String) {
        prefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            apply()
        }
    }

    fun clearUser() {
        prefs.edit().apply {
            remove(KEY_USER_ID)
            remove(KEY_EMAIL)
            remove(KEY_FIRST_NAME)
            remove(KEY_LAST_NAME)
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            putBoolean(KEY_IS_LOGGED_IN, false)
            apply()
        }
        _isLoggedInFlow.value = false
    }
}

