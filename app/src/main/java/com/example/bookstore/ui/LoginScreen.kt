package com.example.bookstore.ui

import android.os.Handler
import android.os.Looper
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // App Bar
        CommonAppBar(
            title = "Log In",
            onNavigationClick = onBackClick, // Используем onBackClick
            onProfileClick = { /* Можно оставить пустым */ },
            showBackArrow = true // Показываем стрелку назад
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Error message
            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            // Email Text Field
            CustomTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null // Clear error when user starts typing
                },
                label = "Email",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Password Text Field
            CustomTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null // Clear error when user starts typing
                },
                label = "Password",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            PrimaryButton(
                text = if (isLoading) "Logging in..." else "Login",
                onClick = {
                    if (isLoading) return@PrimaryButton

                    // Basic validation
                    if (email.isEmpty() || password.isEmpty()) {
                        errorMessage = "Please fill in all fields"
                        return@PrimaryButton
                    }

                    if (!isValidEmail(email)) {
                        errorMessage = "Please enter a valid email address"
                        return@PrimaryButton
                    }

                    // Simulate login process
                    isLoading = true
                    errorMessage = null

                    // In real app, you would call your API here
                    simulateLogin(email, password) { success ->
                        isLoading = false
                        if (success) {
                            onLoginSuccess()
                        } else {
                            errorMessage = "Invalid email or password"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Sign Up Button
            SecondaryButton(
                text = "Create Account",
                onClick = {
                    if (!isLoading) {
                        onSignUpClick()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

// Helper function to simulate login
private fun simulateLogin(email: String, password: String, callback: (Boolean) -> Unit) {
    // Simulate network delay
    Handler(Looper.getMainLooper()).postDelayed({
        // Simple mock validation - in real app, this would be API call
        val isValid = email.isNotEmpty() && password.length >= 1
        callback(isValid)
    }, 1500)
}

// Email validation helper
private fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}