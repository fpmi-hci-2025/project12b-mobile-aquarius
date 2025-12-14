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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookstore.data.mapper.BookMapper
import com.example.bookstore.viewmodel.WishlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    onBookClick: (Book) -> Unit,
    onSavedClick: () -> Unit,
    onHomeClick: () -> Unit,
    viewModel: WishlistViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(2) } // По умолчанию выбрана вкладка "Saved"
    
    val uiState by viewModel.uiState.collectAsState()
    
    // Загружаем wishlist при первом запуске
    LaunchedEffect(Unit) {
        viewModel.loadWishlist()
    }
    
    // Конвертируем WishlistItem в Book
    val currentState = uiState
    val books = when (currentState) {
        is com.example.bookstore.viewmodel.WishlistUiState.Success -> {
            currentState.wishlist.map { wishlistItem ->
                Book(
                    id = wishlistItem.bookId,
                    title = wishlistItem.bookTitle,
                    author = wishlistItem.authors.joinToString(", ").ifEmpty { "Unknown Author" },
                    price = wishlistItem.price,
                    imageUrl = wishlistItem.coverImage ?: "",
                    description = wishlistItem.description
                )
            }
        }
        else -> emptyList()
    }

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
        when (currentState) {
            is com.example.bookstore.viewmodel.WishlistUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is com.example.bookstore.viewmodel.WishlistUiState.Error -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(
                        text = currentState.message,
                        color = Color.Red
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (books.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = androidx.compose.ui.Alignment.Center
                            ) {
                                Text(
                                    text = "Your wishlist is empty",
                                    color = Color.Gray
                                )
                            }
                        }
                    } else {
                        items(books) { book ->
                            BookCardItem(
                                book = book,
                                onAddToCart = { /* Handle add to cart */ },
                                onBookmark = {
                                    viewModel.removeFromWishlist(book.id)
                                },
                                onBookClick = { onBookClick(book) }
                            )
                        }
                    }
                }
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




