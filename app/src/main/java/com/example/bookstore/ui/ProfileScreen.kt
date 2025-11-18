package com.example.bookstore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit = {},
    onSavedClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onCartClick: () -> Unit = {}, // Добавляем обработчик корзины
    onLogoutClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(3) } // Profile tab selected

    Scaffold(
        topBar = {
            CommonAppBar(
                title = "Profile",
                onNavigationClick = onBackClick,
                onProfileClick = onProfileClick
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    when (tab) {
                        0 -> onHomeClick()
                        1 -> onCartClick() // Добавляем обработку корзины
                        2 -> onSavedClick()
                        3 -> onProfileClick()
                    }
                },
                onProfileClick = onProfileClick,
                onCartClick = onCartClick, // Передаем обработчик
                onSavedClick = onSavedClick
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ProfileContent()

            // Добавляем кнопку выхода внизу
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2231AA)
                )
            ) {
                Text(
                    text = "Log Out",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        // User Info Section
        UserInfoSection()

        Spacer(modifier = Modifier.height(32.dp))

        // My Orders Title
        Text(
            text = "My Orders",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF49454F),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Order Card
        OrderCard()

    }
}

@Composable
fun UserInfoSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar and Info
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(43.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF5360CD)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "U",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // User Details
                Column {
                    Text(
                        text = "Настя Аладко",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF49454F),
                        letterSpacing = 0.15.sp
                    )
                    Text(
                        text = "kypitKnigi@gmail.com",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF49454F),
                        letterSpacing = 0.25.sp
                    )
                }
            }

            // Edit Button
            OutlinedButton(
                onClick = { /* Handle edit profile */ },
                modifier = Modifier
                    .height(40.dp),
                shape = RoundedCornerShape(100.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.dp
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF49454F)
                )
            ) {
                Text(
                    text = "Edit",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF49454F)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Divider
        Divider(
            color = Color(0xFF2231AA),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun OrderCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(228.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3F3F3)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Order Title
            Text(
                text = "Recent Order",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1D1B20),
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Order Details
            Text(
                text = "Order #12345 • Completed\n" +
                        "3 items • $142.96\n" +
                        "Delivered on Oct 15, 2023",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1D1B20),
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Supporting Text
            Text(
                text = "Your order has been successfully delivered. Thank you for shopping with us!",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF49454F),
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp
            )
        }
    }
}

