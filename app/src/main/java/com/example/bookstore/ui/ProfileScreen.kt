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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookstore.data.model.Order
import com.example.bookstore.viewmodel.AuthViewModel
import com.example.bookstore.viewmodel.OrderViewModel
import com.example.bookstore.viewmodel.SignOutUiState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit = {},
    onSavedClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    authViewModel: AuthViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(3) } // Profile tab selected
    val coroutineScope = rememberCoroutineScope()
    val signOutState by authViewModel.signOutState.collectAsState()
    val ordersState by orderViewModel.ordersState.collectAsState()
    
    // Получаем данные пользователя
    val userFullName = authViewModel.getUserFullName()
    val userEmail = authViewModel.getUserEmail() ?: ""

    // Загружаем заказы при первом запуске
    LaunchedEffect(Unit) {
        orderViewModel.loadOrders()
    }

    // Обработка успешного выхода
    LaunchedEffect(signOutState) {
        val currentSignOutState = signOutState
        if (currentSignOutState is SignOutUiState.Success) {
            onLogoutClick()
        }
    }

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
            ProfileContent(
                userFullName = userFullName,
                userEmail = userEmail,
                ordersState = ordersState
            )

            // Добавляем кнопку выхода внизу
            Spacer(modifier = Modifier.weight(1f))
            val currentSignOutState = signOutState
            Button(
                onClick = {
                    coroutineScope.launch {
                        authViewModel.signOut()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = currentSignOutState !is SignOutUiState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2231AA)
                )
            ) {
                if (currentSignOutState is SignOutUiState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Log Out",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileContent(
    userFullName: String,
    userEmail: String,
    ordersState: com.example.bookstore.viewmodel.OrdersUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        // User Info Section
        UserInfoSection(
            userFullName = userFullName,
            userEmail = userEmail
        )

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

        // Orders List
        when (val currentState = ordersState) {
            is com.example.bookstore.viewmodel.OrdersUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is com.example.bookstore.viewmodel.OrdersUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentState.message,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }
            is com.example.bookstore.viewmodel.OrdersUiState.Success -> {
                if (currentState.orders.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No orders yet",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(currentState.orders) { order ->
                            OrderCard(order = order)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

@Composable
fun UserInfoSection(
    userFullName: String,
    userEmail: String
) {
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
                        text = userFullName.take(1).uppercase().ifEmpty { "U" },
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // User Details
                Column {
                    Text(
                        text = userFullName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF49454F),
                        letterSpacing = 0.15.sp
                    )
                    Text(
                        text = userEmail,
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
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3F3F3)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Order Status and Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order • ${order.status}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1D1B20),
                    letterSpacing = 0.5.sp
                )
                
                // Format date
                val formattedDate = try {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    val date = inputFormat.parse(order.createdAt)
                    date?.let { outputFormat.format(it) } ?: order.createdAt
                } catch (e: Exception) {
                    order.createdAt
                }
                
                Text(
                    text = formattedDate,
                    fontSize = 12.sp,
                    color = Color(0xFF49454F)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Order Items Count and Total
            Text(
                text = "${order.items.size} items • $${String.format("%.2f", order.totalAmount)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1D1B20),
                lineHeight = 20.sp
            )

            // Delivery Address if available
            if (!order.deliveryAddress.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Delivery: ${order.deliveryAddress}",
                    fontSize = 12.sp,
                    color = Color(0xFF49454F),
                    lineHeight = 16.sp
                )
            }

            // Customer Notes if available
            if (!order.customerNotes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Notes: ${order.customerNotes}",
                    fontSize = 12.sp,
                    color = Color(0xFF49454F),
                    lineHeight = 16.sp,
                    maxLines = 2
                )
            }

            // Order Items Preview
            if (order.items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(
                    color = Color(0xFF49454F).copy(alpha = 0.2f),
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    order.items.take(3).forEach { item ->
                        Text(
                            text = "• ${item.bookTitle} x${item.quantity}",
                            fontSize = 12.sp,
                            color = Color(0xFF49454F),
                            lineHeight = 16.sp
                        )
                    }
                    if (order.items.size > 3) {
                        Text(
                            text = "... and ${order.items.size - 3} more",
                            fontSize = 12.sp,
                            color = Color(0xFF49454F),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}

