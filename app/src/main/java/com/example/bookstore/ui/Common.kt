package com.example.bookstore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonAppBar(
    title: String,
    onNavigationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    showBackArrow: Boolean = false
) {
    Surface(
        shadowElevation = 4.dp,
        modifier = modifier
    ) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            navigationIcon = {
                if (showBackArrow) {
                    IconButton(
                        onClick = onNavigationClick,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color(0xFF5360CD), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(28.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                } else {
                    // Пустое место для выравнивания, когда кнопки нет
                    Spacer(modifier = Modifier.size(56.dp))
                }
            },
            actions = {
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier
                        .size(56.dp)
                        .padding(end = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFF5360CD), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF2231AA),
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
                navigationIconContentColor = Color.White
            ),
            modifier = Modifier.height(72.dp)
        )
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFFF3F3F3), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
                color = Color(0xFF1D1B20)
            ),
            decorationBox = { innerTextField ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    // Label
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        letterSpacing = 0.4.sp,
                        color = Color(0xFF1D1B20)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Input field
                    innerTextField()
                }
            }
        )

        // Active indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFF1D1B20))
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2231AA)
        )
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.1.sp
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = CircleShape,
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFF2231AA)
        )
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.1.sp,
            color = Color(0xFF1D1B20)
        )
    }
}