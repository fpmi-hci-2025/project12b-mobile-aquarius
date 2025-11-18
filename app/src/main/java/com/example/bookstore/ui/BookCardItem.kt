package com.example.bookstore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BookCardItem(
    book: Book,
    onAddToCart: () -> Unit,
    onBookmark: () -> Unit,
    onBookClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onBookClick() }, // Делаем всю карточку кликабельной
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3F3F3)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Media/Image section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(188.dp)
                    .background(Color.Gray), // Placeholder for image
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Book Cover",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            // Text content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Title and subtitle
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

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$${book.price}",
                        color = Color(0xFF2231AA),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Bookmark button
                    IconButton(
                        onClick = onBookmark,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .border(
                                width = 1.dp,
                                color = Color(0xFF2231AA),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Bookmark",
                            tint = Color(0xFF2231AA),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Add to cart button
                    Button(
                        onClick = onAddToCart,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2231AA)
                        )
                    ) {
                        Text(
                            text = "Add to Cart",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}