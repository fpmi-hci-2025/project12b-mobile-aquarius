package com.example.bookstore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    onBookClick: (Book) -> Unit,
    onSavedClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    // Фильтрация книг по поисковому запросу
    val filteredBooks = remember(searchQuery, getSampleProducts()) {
        if (searchQuery.isBlank()) {
            getSampleProducts()
        } else {
            getSampleProducts().filter { book ->
                book.title.contains(searchQuery, ignoreCase = true) ||
                        book.author.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CommonAppBar(
            title = "Book Store",
            onNavigationClick = onBackClick,
            onProfileClick = onProfileClick
        )

        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth()
        )

        // Content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (filteredBooks.isEmpty() && searchQuery.isNotEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No books found for \"$searchQuery\"",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                items(filteredBooks) { product ->
                    BookCardItem(
                        book = product,
                        onAddToCart = { /* Handle add to cart */ },
                        onBookmark = { /* Handle bookmark */ },
                        onBookClick = { onBookClick(product) }
                    )
                }
            }
        }

        // Bottom Navigation
        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { tab ->
                selectedTab = tab
            },
            onProfileClick = onProfileClick,
            onCartClick = onCartClick,
            onSavedClick = onSavedClick
        )
    }
}

fun getSampleProducts(): List<Book> {
    return listOf(
        Book(
            id = "1",
            title = "GUI Programming 2nd edition",
            author = "John Doe",
            price = 29.99
        ),
        Book(
            id = "2",
            title = "Advanced Android Development",
            author = "Jane Smith",
            price = 34.99
        ),
        Book(
            id = "3",
            title = "Kotlin Coroutines Guide",
            author = "Mike Johnson",
            price = 24.99
        )
    )
}