package com.example.bookstore.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NavigationDrawer(
    onClose: () -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    onSavedClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(200.dp),
        drawerShape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp),
        drawerContainerColor = Color.White
    ) {
        // Menu Header (optional)
        Text(
            text = "Menu",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF1D1B20),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Divider(color = Color(0xFFE5E5E5), thickness = 1.dp)

        // Menu Items
        NavigationDrawerItem(
            label = {
                Text(
                    text = "Home",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1D1B20)
                )
            },
            selected = false,
            onClick = {
                onHomeClick()
                onClose()
            },
            modifier = Modifier.fillMaxWidth()
        )

        NavigationDrawerItem(
            label = {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1D1B20)
                )
            },
            selected = false,
            onClick = {
                onProfileClick()
                onClose()
            },
            modifier = Modifier.fillMaxWidth()
        )

        NavigationDrawerItem(
            label = {
                Text(
                    text = "Cart",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1D1B20)
                )
            },
            selected = false,
            onClick = {
                onCartClick()
                onClose()
            },
            modifier = Modifier.fillMaxWidth()
        )

        NavigationDrawerItem(
            label = {
                Text(
                    text = "Saved",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1D1B20)
                )
            },
            selected = false,
            onClick = {
                onSavedClick()
                onClose()
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Divider(color = Color(0xFFE5E5E5), thickness = 1.dp)

        // Logout Item
        NavigationDrawerItem(
            label = {
                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1D1B20)
                )
            },
            selected = false,
            onClick = {
                onLogoutClick()
                onClose()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}