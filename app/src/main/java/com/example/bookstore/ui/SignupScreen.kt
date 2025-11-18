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
fun SignUpScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // App Bar
        CommonAppBar(
            title = "Sign Up",
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

            // Name Text Field
            CustomTextField(
                value = name,
                onValueChange = {
                    name = it
                    errorMessage = null
                },
                label = "Full Name",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email Text Field
            CustomTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
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
                    errorMessage = null
                },
                label = "Password",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Confirm Password Text Field
            CustomTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    errorMessage = null
                },
                label = "Confirm Password",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Button
            PrimaryButton(
                text = if (isLoading) "Creating Account..." else "Create Account",
                onClick = {
                    if (isLoading) return@PrimaryButton

                    // Validation
                    if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                        errorMessage = "Please fill in all fields"
                        return@PrimaryButton
                    }

                    if (!isValidEmail(email)) {
                        errorMessage = "Please enter a valid email address"
                        return@PrimaryButton
                    }

                    if (password.length < 6) {
                        errorMessage = "Password must be at least 6 characters"
                        return@PrimaryButton
                    }

                    if (password != confirmPassword) {
                        errorMessage = "Passwords do not match"
                        return@PrimaryButton
                    }

                    // Simulate signup process
                    isLoading = true
                    errorMessage = null

                    simulateSignUp(name, email, password) { success ->
                        isLoading = false
                        if (success) {
                            onSignUpSuccess()
                        } else {
                            errorMessage = "Email already exists or registration failed"
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Login Button
            SecondaryButton(
                text = "Already have an account? Login",
                onClick = {
                    if (!isLoading) {
                        onLoginClick()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Helper function to simulate signup
private fun simulateSignUp(name: String, email: String, password: String, callback: (Boolean) -> Unit) {
    // Simulate network delay
    Handler(Looper.getMainLooper()).postDelayed({
        // Simple mock validation - in real app, this would be API call
        val isValid = name.isNotEmpty() && email.isNotEmpty() && password.length >= 6
        callback(isValid)
    }, 2000)
}

private fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}