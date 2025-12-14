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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookstore.data.mapper.BookMapper
import com.example.bookstore.viewmodel.BookUiState
import com.example.bookstore.viewmodel.BookViewModel
import com.example.bookstore.viewmodel.CartViewModel
import com.example.bookstore.viewmodel.WishlistViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    onBookClick: (Book) -> Unit,
    onSavedClick: () -> Unit,
    viewModel: BookViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    wishlistViewModel: WishlistViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()
    
    // Загружаем книги при первом запуске
    LaunchedEffect(Unit) {
        viewModel.loadBooks()
    }
    
    // Обработка поиска с задержкой (debounce)
    LaunchedEffect(searchQuery) {
        if (searchQuery.isBlank()) {
            viewModel.loadBooks()
        } else {
            delay(500) // Задержка 500мс перед поиском
            if (searchQuery.isNotBlank()) {
                viewModel.searchBooks(searchQuery)
            }
        }
    }
    
    // Конвертируем BookResponse в Book
    val currentState = uiState
    val books = when (currentState) {
        is BookUiState.Success -> BookMapper.toBookList(currentState.books)
        else -> emptyList()
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
        when (currentState) {
            is BookUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is BookUiState.Error -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = currentState.message,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 16.sp
                        )
                        Button(onClick = { viewModel.loadBooks() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            is BookUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (books.isEmpty() && searchQuery.isNotEmpty()) {
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
                    } else if (books.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No books available",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    } else {
                        items(books) { book ->
                            BookCardItem(
                                book = book,
                                onAddToCart = {
                                    cartViewModel.addBookToCart(book.id, 1)
                                },
                                onBookmark = {
                                    wishlistViewModel.addToWishlist(book.id)
                                },
                                onBookClick = { onBookClick(book) }
                            )
                        }
                    }
                }
            }
            is BookUiState.Idle -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
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