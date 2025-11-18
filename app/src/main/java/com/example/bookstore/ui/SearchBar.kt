package com.example.bookstore.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF5360CD),
            unfocusedContainerColor = Color(0xFF5360CD),
            disabledContainerColor = Color(0xFF5360CD),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = Color.White,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White
        ),
        placeholder = {
            Text(
                text = "Search books...",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.size(24.dp)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        singleLine = true
    )
}