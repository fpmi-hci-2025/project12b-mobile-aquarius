package com.example.bookstore

import androidx.compose.runtime.*
import com.example.bookstore.ui.Book
import com.example.bookstore.ui.BookDetailScreen
import com.example.bookstore.ui.CartScreen
import com.example.bookstore.ui.LoginScreen
import com.example.bookstore.ui.MainScreen
import com.example.bookstore.ui.PaymentScreen
import com.example.bookstore.ui.ProfileScreen
import com.example.bookstore.ui.SavedScreen
import com.example.bookstore.ui.SignUpScreen

class NavigationController {
    var currentScreen by mutableStateOf("main")
        private set
    var isUserLoggedIn by mutableStateOf(false) // Добавляем состояние авторизации

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
        isUserLoggedIn = true
        currentScreen = "main"
    }

    fun logout() {
        isUserLoggedIn = false
        currentScreen = "main"
    }
}

@Composable
fun AppNavigation() {
    val navigationController = remember { NavigationController() }
    var selectedBook by remember { mutableStateOf<Book?>(null) }

    // Обработчик для профиля с проверкой авторизации
    val onProfileClick: () -> Unit = {
        if (navigationController.isUserLoggedIn) {
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
            onHomeClick = { navigationController.navigateTo("main") }, // Добавляем Home
            onCheckoutClick = { navigationController.navigateTo("payment") }
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
        "profile" -> if (navigationController.isUserLoggedIn) {
            ProfileScreen(
                onBackClick = { navigationController.navigateBack() },
                onProfileClick = onProfileClick,
                onSavedClick = { navigationController.navigateTo("saved") },
                onHomeClick = { navigationController.navigateTo("main") },
                onCartClick = { navigationController.navigateTo("cart") },
                onLogoutClick = { navigationController.logout() }
            )
        } else {
            navigationController.navigateTo("login")
        }
        "payment" -> PaymentScreen(
            onBackClick = { navigationController.navigateBack() },
            onProfileClick = onProfileClick,
            onSavedClick = { navigationController.navigateTo("saved") },
            onHomeClick = { navigationController.navigateTo("main") }, // Добавляем Home
            onPaymentComplete = {
                navigationController.navigateTo("main")
            }
        )
    }
}