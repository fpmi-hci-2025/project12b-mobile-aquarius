package com.example.bookstore.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSavedClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPaymentComplete: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(1) }
    var showConfirmationDialog by remember { mutableStateOf(false) } // Диалог на экране оплаты

    // Диалог подтверждения оплаты
    if (showConfirmationDialog) {
        PaymentConfirmationDialog(
            onDismiss = { showConfirmationDialog = false },
            onConfirm = {
                showConfirmationDialog = false
                onPaymentComplete() // Завершаем оплату после подтверждения
            }
        )
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = "Payment",
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
                        2 -> onSavedClick()
                        3 -> onProfileClick()
                    }
                },
                onProfileClick = onProfileClick,
                onCartClick = { /* Уже на экране оплаты */ },
                onSavedClick = onSavedClick
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        PaymentContent(
            onPayNowClick = { showConfirmationDialog = true }, // Показываем диалог при нажатии
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}



@Composable
fun PaymentContent(
    onPayNowClick: () -> Unit, // Изменили параметр
    modifier: Modifier = Modifier
) {
    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        // Payment Form
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Card Number Field
            PaymentTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = "Card Number",
                placeholder = "1234 5678 9012 3456",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Card Holder Field
            PaymentTextField(
                value = cardHolder,
                onValueChange = { cardHolder = it },
                label = "Card Holder",
                placeholder = "John Doe",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Expiry Date Field
                PaymentTextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it },
                    label = "Expiry Date",
                    placeholder = "MM/YY",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                // CVV Field
                PaymentTextField(
                    value = cvv,
                    onValueChange = { cvv = it },
                    label = "CVV",
                    placeholder = "123",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Order Summary
            OrderSummarySection(
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Pay Button
        Button(
            onClick = onPayNowClick, // Теперь показывает диалог
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2231AA)
            )
        ) {
            Text(
                text = "Pay Now",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF1D1B20),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFF666666),
                    fontSize = 16.sp
                )
            },
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF3F3F3),
                unfocusedContainerColor = Color(0xFFF3F3F3),
                disabledContainerColor = Color(0xFFF3F3F3),
                focusedIndicatorColor = Color(0xFF1D1B20),
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(4.dp)
        )
    }
}

@Composable
fun OrderSummarySection(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
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
            Text(
                text = "Order Summary",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1D1B20),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Order Items
            OrderItem("Items (3)", "$129.97")
            OrderItem("Shipping", "Free")
            OrderItem("Tax", "$12.99")

            Divider(
                color = Color(0xFF49454F).copy(alpha = 0.2f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OrderItem("Total", "$142.96", isTotal = true)
        }
    }
}

@Composable
fun OrderItem(
    label: String,
    value: String,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = if (isTotal) 16.sp else 14.sp,
            fontWeight = if (isTotal) FontWeight.Medium else FontWeight.Normal,
            color = if (isTotal) Color(0xFF1D1B20) else Color(0xFF49454F)
        )
        Text(
            text = value,
            fontSize = if (isTotal) 16.sp else 14.sp,
            fontWeight = if (isTotal) FontWeight.Medium else FontWeight.Normal,
            color = if (isTotal) Color(0xFF1D1B20) else Color(0xFF49454F)
        )
    }
}
