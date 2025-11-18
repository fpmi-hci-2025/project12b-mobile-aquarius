package com.example.bookstore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    book: Book,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    onAddToCart: () -> Unit,
    onSavedClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // App Bar
        CommonAppBar(
            title = "Book",
            onNavigationClick = onBackClick, // Используем onBackClick
            onProfileClick = { /* Можно оставить пустым */ },
            showBackArrow = true // Показываем стрелку назад
        )

        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // Book Cover Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(197.dp)
                    .padding(vertical = 16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Book Cover",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Book Title and Author
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = book.title,
                    color = Color(0xFF1D1B20),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = book.author,
                    color = Color(0xFF313037),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Book Description
            Text(
                text = "Популярный язык программирования C используется в самых разных приложениях: от миниатюрных микроконтроллеров, используемых в тостерах и часах, до полноценных операционных систем. Первая половина этой книги представляет собой введение в C и охватывает основы написания простых программ командной строки. Во второй половине книги показано, как использовать набор инструментов пользовательского интерфейса GTK с C для создания многофункциональных графических приложений, которые можно запускать на настольном компьютере.",
                color = Color.Black,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Outline Button 1
                OutlinedButton(
                    onClick = { /* Handle first action */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = CircleShape,
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF2231AA)
                    )
                ) {
                    Text(
                        text = "Добавить в избранное",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.1.sp,
                        color = Color(0xFF1D1B20)
                    )
                }

                // Tonal Button
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2231AA)
                    )
                ) {
                    Text(
                        text = "Добавить в корзину",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.1.sp
                    )
                }

            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Bottom Navigation
        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            onProfileClick = onProfileClick,
            onCartClick = onCartClick,
            onSavedClick = onSavedClick
        )
    }
}