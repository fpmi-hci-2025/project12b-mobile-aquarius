package com.example.bookstore

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookstore.ui.Book
import com.example.bookstore.ui.BookDetailScreen
import com.example.bookstore.ui.CartScreen
import com.example.bookstore.ui.LoginScreen
import com.example.bookstore.ui.MainScreen
import com.example.bookstore.ui.PaymentScreen
import com.example.bookstore.ui.ProfileScreen
import com.example.bookstore.ui.SavedScreen
import com.example.bookstore.ui.SignUpScreen
import com.example.bookstore.viewmodel.AuthViewModel

class NavigationController {
    var currentScreen by mutableStateOf("main")
        private set

    fun navigateTo(screen: String) {
        currentScreen = screen
    }

    fun navigateBack() {
        when (currentScreen) {
            "login", "signup" -> currentScreen = "main"
            "profile", "cart", "saved", "bookDetail", "payment" -> currentScreen = "main"
            else -> currentScreen = "main"
        }
    }

    fun login() {
        currentScreen = "main"
    }

    fun logout() {
        currentScreen = "main"
    }
}

@Composable
fun AppNavigation() {
    val navigationController = remember { NavigationController() }
    var selectedBook by remember { mutableStateOf<Book?>(null) }
    val authViewModel: AuthViewModel = hiltViewModel()
    
    // Реактивно отслеживаем статус входа через Flow
    val isUserLoggedIn by authViewModel.isLoggedInFlow().collectAsState()

    // Обработчик для профиля с проверкой авторизации
    val onProfileClick: () -> Unit = {
        if (isUserLoggedIn) {
            navigationController.navigateTo("profile")
        } else {
            navigationController.navigateTo("login")
        }
    }

    when (navigationController.currentScreen) {
        "login" -> LoginScreen(
            onBackClick = { navigationController.navigateBack() },
            onSignUpClick = { navigationController.navigateTo("signup") },
            onLoginSuccess = { navigationController.login() }
        )
        "signup" -> SignUpScreen(
            onBackClick = { navigationController.navigateBack() },
            onLoginClick = { navigationController.navigateTo("login") },
            onSignUpSuccess = { navigationController.login() }
        )
        "cart" -> CartScreen(
            onBackClick = { navigationController.navigateBack() },
            onProfileClick = onProfileClick,
            onSavedClick = { navigationController.navigateTo("saved") },
            onHomeClick = { navigationController.navigateTo("main") },
            onCheckoutClick = { /* Обрабатывается внутри CartScreen */ }
        )
        "main" -> MainScreen(
            onBackClick = { navigationController.navigateBack() },
            onProfileClick = onProfileClick,
            onCartClick = { navigationController.navigateTo("cart") },
            onBookClick = { book ->
                selectedBook = book
                navigationController.navigateTo("bookDetail")
            },
            onSavedClick = { navigationController.navigateTo("saved") }
        )
        "saved" -> SavedScreen(
            onBackClick = { navigationController.navigateBack() },
            onProfileClick = onProfileClick,
            onCartClick = { navigationController.navigateTo("cart") },
            onBookClick = { book ->
                selectedBook = book
                navigationController.navigateTo("bookDetail")
            },
            onSavedClick = { navigationController.navigateTo("saved") },
            onHomeClick = { navigationController.navigateTo("main") } // Добавляем Home
        )
        "bookDetail" -> selectedBook?.let { book ->
            BookDetailScreen(
                book = book,
                onBackClick = { navigationController.navigateBack() },
                onProfileClick = onProfileClick,
                onCartClick = { navigationController.navigateTo("cart") },
                onAddToCart = {
                    navigationController.navigateTo("cart")
                },
                onSavedClick = { navigationController.navigateTo("saved") }
            )
        } ?: run {
            navigationController.navigateTo("main")
        }
        "profile" -> {
            // Используем LaunchedEffect для реактивной проверки состояния входа
            LaunchedEffect(isUserLoggedIn, navigationController.currentScreen) {
                if (navigationController.currentScreen == "profile" && !isUserLoggedIn) {
                    navigationController.navigateTo("login")
                }
            }
            
            if (isUserLoggedIn) {
                ProfileScreen(
                    onBackClick = { navigationController.navigateBack() },
                    onProfileClick = onProfileClick,
                    onSavedClick = { navigationController.navigateTo("saved") },
                    onHomeClick = { navigationController.navigateTo("main") },
                    onCartClick = { navigationController.navigateTo("cart") },
                    onLogoutClick = { 
                        // Выход обрабатывается в ProfileScreen через AuthViewModel
                        navigationController.logout() 
                    }
                )
            } else {
                // Если не авторизован, перенаправляем на логин
                LaunchedEffect(Unit) {
                    navigationController.navigateTo("login")
                }
            }
        }
    }
}