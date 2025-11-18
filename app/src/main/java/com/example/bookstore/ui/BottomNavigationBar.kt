package com.example.bookstore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
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
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    onSavedClick: () -> Unit // Добавляем отдельный обработчик
) {
    val navItems = listOf("Home", "Cart", "Saved", "Profile")
    val navIcons = listOf(
        Icons.Default.Home,
        Icons.Default.ShoppingCart,
        Icons.Default.Favorite,
        Icons.Default.Person
    )

    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2231AA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp + navigationBarHeight)
                .padding(bottom = navigationBarHeight),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEachIndexed { index, item ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            when (index) {
                                1 -> onCartClick()     // Cart
                                2 -> onSavedClick()    // Saved - используем отдельный обработчик
                                3 -> onProfileClick()  // Profile
                                else -> onTabSelected(index) // Home
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (selectedTab == index) Color(0xFF5360CD) else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = navIcons[index],
                            contentDescription = item,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}