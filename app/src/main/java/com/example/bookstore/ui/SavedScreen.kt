package com.example.bookstore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    onBookClick: (Book) -> Unit,
    onSavedClick: () -> Unit,
    onHomeClick: () -> Unit // Добавляем обработчик для Home
) {
    var selectedTab by remember { mutableStateOf(2) } // По умолчанию выбрана вкладка "Saved"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CommonAppBar(
            title = "Saved",
            onNavigationClick = onBackClick,
            onProfileClick = onProfileClick
        )

        // Content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(getSavedBooks()) { book ->
                BookCardItem(
                    book = book,
                    onAddToCart = { /* Handle add to cart */ },
                    onBookmark = { /* Handle bookmark */ },
                    onBookClick = { onBookClick(book) }
                )
            }
        }

        // Bottom Navigation
        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { tab ->
                selectedTab = tab
                when (tab) {
                    0 -> onHomeClick() // Обрабатываем нажатие на Home
                    1 -> onCartClick()
                    2 -> onSavedClick()
                    3 -> onProfileClick()
                }
            },
            onProfileClick = onProfileClick,
            onCartClick = onCartClick,
            onSavedClick = onSavedClick
        )
    }
}

// Функция для получения сохраненных книг
fun getSavedBooks(): List<Book> {
    return listOf(
        Book(
            id = "1",
            title = "GUI Programming 2nd edition",
            author = "John Doe",
            price = 29.99,
            description = "Популярный язык программирования C используется в самых разных приложениях..."
        )
    )
}



