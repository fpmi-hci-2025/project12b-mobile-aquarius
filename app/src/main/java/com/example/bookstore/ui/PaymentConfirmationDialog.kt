package com.example.bookstore.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFF3F3F3),
        shape = RoundedCornerShape(28.dp),
        title = {
            Text(
                text = "Confirm Payment",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1D1B20)
            )
        },
        text = {
            Text(
                text = "Are you sure you want to proceed with the payment? This action will process your order and deduct the amount from your payment method.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF49454F),
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Вторичная кнопка (Отмена)
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .height(40.dp)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = "Cancel",
                        color = Color(0xFF2231AA),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Основная кнопка (Подтвердить)
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .height(40.dp),
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2231AA)
                    )
                ) {
                    Text(
                        text = "Confirm",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    )
}