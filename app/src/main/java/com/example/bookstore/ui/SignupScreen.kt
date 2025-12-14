package com.example.bookstore.ui

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookstore.data.model.SignUpRequest
import com.example.bookstore.data.model.SignUpResponse
import com.example.bookstore.viewmodel.SignUpUiState
import com.example.bookstore.viewmodel.SignUpViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpSuccess: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    // Состояния полей
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }

    // Состояние UI
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    
    val currentUiState = uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // App Bar
        CommonAppBar(
            title = "Sign Up",
            onNavigationClick = onBackClick,
            onProfileClick = { /* Можно оставить пустым */ },
            showBackArrow = true
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Error message
            when (currentUiState) {
                is SignUpUiState.Error -> {
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

            // Success message (опционально)
            when (currentUiState) {
                is SignUpUiState.Success -> {
                    Text(
                        text = "Registration successful!",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color(0xFF4CAF50), // Green color
                            lineHeight = 20.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }
                else -> {}
            }

            // First Name Text Field
            CustomTextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                    if (currentUiState is SignUpUiState.Error) viewModel.resetState()
                },
                label = "First Name *",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Last Name Text Field
            CustomTextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                    if (currentUiState is SignUpUiState.Error) viewModel.resetState()
                },
                label = "Last Name *",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Text Field
            CustomTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (currentUiState is SignUpUiState.Error) viewModel.resetState()
                },
                label = "Email *",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Text Field (нужно будет обновить CustomTextField для пароля)
            CustomTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (currentUiState is SignUpUiState.Error) viewModel.resetState()
                },
                label = "Password *",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Text Field
            CustomTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    if (currentUiState is SignUpUiState.Error) viewModel.resetState()
                },
                label = "Confirm Password *",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Text Field (optional)
            CustomTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    if (currentUiState is SignUpUiState.Error) viewModel.resetState()
                },
                label = "Phone (optional)",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date of Birth Text Field (optional)
            CustomTextField(
                value = dateOfBirth,
                onValueChange = {
                    dateOfBirth = it
                    if (currentUiState is SignUpUiState.Error) viewModel.resetState()
                },
                label = "Date of Birth (optional) - DD.MM.YYYY",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Примечание о обязательных полях
            Text(
                text = "* - обязательные поля",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    lineHeight = 16.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Sign Up Button
            Button(
                onClick = {
                    if (currentUiState is SignUpUiState.Loading) return@Button

                    // Валидация
                    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                        viewModel.setError("Please fill in all required fields")
                        return@Button
                    }

                    if (!isValidEmail(email)) {
                        viewModel.setError("Please enter a valid email address")
                        return@Button
                    }

                    if (password.length < 6) {
                        viewModel.setError("Password must be at least 6 characters")
                        return@Button
                    }

                    if (password != confirmPassword) {
                        viewModel.setError("Passwords do not match")
                        return@Button
                    }

                    if (dateOfBirth.isNotEmpty() && !isValidDate(dateOfBirth)) {
                        viewModel.setError("Please enter date in format DD.MM.YYYY")
                        return@Button
                    }

                    // Подготовка данных для отправки
                    val formattedDate = if (dateOfBirth.isNotEmpty()) {
                        formatDateForAPI(dateOfBirth)
                    } else {
                        null
                    }

                    // Вызываем ViewModel для регистрации с отдельными параметрами
                    coroutineScope.launch {
                        viewModel.signUp(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password,
                            phone = phone,
                            dateOfBirth = formattedDate
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = currentUiState !is SignUpUiState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D1B20),
                    contentColor = Color.White
                )
            ) {
                if (currentUiState is SignUpUiState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Create Account",
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            TextButton(
                onClick = {
                    if (currentUiState !is SignUpUiState.Loading) {
                        onLoginClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = currentUiState !is SignUpUiState.Loading
            ) {
                Text(
                    text = "Already have an account? Login",
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color(0xFF1D1B20)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Обработка успешной регистрации
    LaunchedEffect(uiState) {
        val currentState = uiState
        when (currentState) {
            is SignUpUiState.Success -> {
                // Успешная регистрация - переходим на главный экран
                onSignUpSuccess()
                // Можно также очистить форму
                firstName = ""
                lastName = ""
                email = ""
                password = ""
                confirmPassword = ""
                phone = ""
                dateOfBirth = ""
            }
            else -> {}
        }
    }
}


@RequiresApi(Build.VERSION_CODES.FROYO)
private fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidDate(date: String): Boolean {
    val regex = Regex("""^\d{2}\.\d{2}\.\d{4}$""")
    if (!regex.matches(date)) return false

    return try {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        sdf.isLenient = false
        sdf.parse(date)
        true
    } catch (e: Exception) {
        false
    }
}

fun formatDateForAPI(date: String): String {
    return try {
        val inputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        outputFormat.format(parsedDate)
    } catch (e: Exception) {
        ""
    }
}

