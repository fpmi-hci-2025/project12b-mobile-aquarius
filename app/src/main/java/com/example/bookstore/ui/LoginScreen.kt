package com.example.bookstore.ui

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookstore.viewmodel.LoginUiState
import com.example.bookstore.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    
    val uiState by viewModel.uiState.collectAsState()
    
    // Сбрасываем состояние при открытии экрана
    LaunchedEffect(Unit) {
        viewModel.resetState()
    }
    
    val currentUiState = uiState

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
            when (currentUiState) {
                is LoginUiState.Error -> {
                    Text(
                        text = currentUiState.message,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.error,
                            lineHeight = 20.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }
                else -> {}
            }

            // Email Text Field
            CustomTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (currentUiState is LoginUiState.Error) viewModel.resetState()
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
                    if (currentUiState is LoginUiState.Error) viewModel.resetState()
                },
                label = "Password",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = {
                    if (currentUiState is LoginUiState.Loading) return@Button

                    // Basic validation
                    if (email.isEmpty() || password.isEmpty()) {
                        viewModel.setError("Please fill in all fields")
                        return@Button
                    }

                    if (!isValidEmail(email)) {
                        viewModel.setError("Please enter a valid email address")
                        return@Button
                    }

                    // Call ViewModel to login
                    coroutineScope.launch {
                        viewModel.login(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = currentUiState !is LoginUiState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D1B20),
                    contentColor = Color.White
                )
            ) {
                if (currentUiState is LoginUiState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Login",
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Sign Up Button
            TextButton(
                onClick = {
                    if (currentUiState !is LoginUiState.Loading) {
                        onSignUpClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = currentUiState !is LoginUiState.Loading
            ) {
                Text(
                    text = "Create Account",
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color(0xFF1D1B20)
                )
            }
        }
    }

    // Handle successful login
    LaunchedEffect(uiState) {
        val currentState = uiState
        when (currentState) {
            is LoginUiState.Success -> {
                // Сбрасываем состояние после успешного входа
                viewModel.resetState()
                onLoginSuccess()
            }
            else -> {}
        }
    }
}

// Email validation helper
private fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}