package com.example.bookstore.ui
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookstore.data.mapper.CartMapper
import com.example.bookstore.viewmodel.CartViewModel
import com.example.bookstore.data.model.LoginResponse
import com.example.bookstore.data.local.UserManager
import com.example.bookstore.data.repository.OrderRepository
import com.example.bookstore.data.model.CreateOrderRequest
import com.example.bookstore.data.model.OrderItemRequest
import com.example.bookstore.data.model.PaymentRequest
import com.example.bookstore.viewmodel.OrderViewModel
import com.example.bookstore.viewmodel.CreateOrderUiState
import com.example.bookstore.viewmodel.PayOrderUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSavedClick: () -> Unit,
    onHomeClick: () -> Unit,
    onCheckoutClick: () -> Unit,
    viewModel: CartViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val createOrderState by orderViewModel.createOrderState.collectAsState()
    val payOrderState by orderViewModel.payOrderState.collectAsState()
    var selectedTab by remember { mutableStateOf(1) }
    var deliveryAddress by remember { mutableStateOf("") }
    var orderComment by remember { mutableStateOf("") }

    // Загружаем корзину при первом запуске
    LaunchedEffect(Unit) {
        viewModel.loadCart()
    }

    // Обрабатываем успешное создание заказа - сразу оплачиваем
    LaunchedEffect(createOrderState) {
        when (val state = createOrderState) {
            is com.example.bookstore.viewmodel.CreateOrderUiState.Success -> {
                val cartState = uiState
                if (cartState is com.example.bookstore.viewmodel.CartUiState.Success) {
                    val totalPrice = cartState.cart.totalPrice
                    orderViewModel.payOrder(state.orderId, "Card", totalPrice)
                }
            }
            else -> {}
        }
    }

    // Обрабатываем успешную оплату - переходим на главную
    LaunchedEffect(payOrderState) {
        if (payOrderState is com.example.bookstore.viewmodel.PayOrderUiState.Success) {
            onHomeClick()
        }
    }

    // Create a local variable for smart casting
    val currentState = uiState
    val cartItems = when (currentState) {
        is com.example.bookstore.viewmodel.CartUiState.Success -> {
            CartMapper.toUiCartItems(currentState.cart)
                .filter { it.book.id.isNotBlank() && 
                         it.book.id != "00000000-0000-0000-0000-000000000000" }
        }
        else -> emptyList()
    }

    Scaffold(
        topBar = {
            CommonAppBar(
                title = "Cart",
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
                onCartClick = { /* Уже на экране корзины */ },
                onSavedClick = onSavedClick
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            EmptyCartContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            when (currentState) {
                is com.example.bookstore.viewmodel.CartUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is com.example.bookstore.viewmodel.CartUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentState.message,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    CartContent(
                        cartItems = cartItems,
                        onRemoveItem = { book ->
                            viewModel.removeBookFromCart(book.id)
                        },
                        onUpdateQuantity = { book, newQuantity ->
                            viewModel.removeBookFromCart(book.id)
                            viewModel.addBookToCart(book.id, newQuantity)
                        },
                        onCheckoutClick = {
                            val cartState = uiState
                            if (cartState is com.example.bookstore.viewmodel.CartUiState.Success) {
                                val validOrderItems = cartState.cart.cartItems
                                    .filter { cartItem ->
                                        cartItem.bookId.isNotBlank() &&
                                        cartItem.bookId != "00000000-0000-0000-0000-000000000000" &&
                                        cartItem.bookId.matches(Regex("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"))
                                    }
                                    .map { cartItem ->
                                        OrderItemRequest(
                                            bookId = cartItem.bookId,
                                            count = cartItem.quantity
                                        )
                                    }
                                
                                if (validOrderItems.isNotEmpty() && deliveryAddress.isNotBlank()) {
                                    val request = CreateOrderRequest(
                                        customerNotes = orderComment.ifBlank { null },
                                        deliveryAddress = deliveryAddress,
                                        orderItems = validOrderItems
                                    )
                                    orderViewModel.createOrder(request)
                                }
                            }
                        },
                        deliveryAddress = deliveryAddress,
                        onDeliveryAddressChange = { deliveryAddress = it },
                        orderComment = orderComment,
                        onOrderCommentChange = { orderComment = it },
                        isLoading = createOrderState is com.example.bookstore.viewmodel.CreateOrderUiState.Loading ||
                                   payOrderState is com.example.bookstore.viewmodel.PayOrderUiState.Loading,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun EmptyCartContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Your cart is empty",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add some books to get started",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun CartContent(
    cartItems: List<CartItem>,
    onRemoveItem: (Book) -> Unit,
    onUpdateQuantity: (Book, Int) -> Unit,
    onCheckoutClick: () -> Unit,
    deliveryAddress: String,
    onDeliveryAddressChange: (String) -> Unit,
    orderComment: String,
    onOrderCommentChange: (String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val totalPrice = cartItems.sumOf { it.book.price * it.quantity }

    Column(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ) {
            items(cartItems) { cartItem ->
                CartItemCard(
                    cartItem = cartItem,
                    onRemove = { onRemoveItem(cartItem.book) },
                    onQuantityChange = { newQuantity ->
                        onUpdateQuantity(cartItem.book, newQuantity)
                    }
                )
            }

            // Delivery Address Input
            item {
                DeliveryAddressInput(
                    address = deliveryAddress,
                    onAddressChange = onDeliveryAddressChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            // Order Comment
            item {
                OrderCommentSection(
                    comment = orderComment,
                    onCommentChange = onOrderCommentChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        }

        // Checkout Button
        Button(
            onClick = onCheckoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2231AA)
            ),
            enabled = deliveryAddress.isNotBlank() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "Pay - $${String.format("%.2f", totalPrice)}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryAddressInput(
    address: String,
    onAddressChange: (String) -> Unit,
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
                text = "Delivery Address",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1D1B20),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TextField(
                value = address,
                onValueChange = onAddressChange,
                placeholder = {
                    Text(
                        text = "Enter delivery address",
                        color = Color.Gray
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderCommentSection(
    comment: String,
    onCommentChange: (String) -> Unit,
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
                text = "Order Comment (Optional)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1D1B20),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            TextField(
                value = comment,
                onValueChange = onCommentChange,
                placeholder = {
                    Text(
                        text = "Add any special instructions or comments for your order...",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                singleLine = false,
                maxLines = 4
            )

            if (comment.isNotEmpty()) {
                Text(
                    text = "${comment.length}/200 characters",
                    fontSize = 12.sp,
                    color = Color(0xFF49454F),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onRemove: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3F3F3)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Book Cover Placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEADDFF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cover",
                    color = Color(0xFF4F378A),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Book Details
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = cartItem.book.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF1D1B20),
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = cartItem.book.author,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF313037),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${cartItem.book.price}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1D1B20)
                    )

                    // Quantity Controls
                    QuantitySelector(
                        quantity = cartItem.quantity,
                        onQuantityChange = onQuantityChange
                    )
                }
            }

            // Delete Button
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove from cart",
                    tint = Color(0xFF49454F)
                )
            }
        }
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Decrease Button
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = if (quantity > 1) Color(0xFF2231AA) else Color.LightGray,
                    shape = CircleShape
                )
                .clickable(
                    enabled = quantity > 1,
                    onClick = { if (quantity > 1) onQuantityChange(quantity - 1) }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "−",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Quantity Display
        Text(
            text = quantity.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1D1B20),
            modifier = Modifier.width(24.dp),
            textAlign = TextAlign.Center
        )

        // Increase Button
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFF2231AA), CircleShape)
                .clickable { onQuantityChange(quantity + 1) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun OrderSummary(
    totalPrice: Double,
    itemCount: Int,
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
                color = Color(0xFF1D1B20)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Items ($itemCount)",
                    fontSize = 14.sp,
                    color = Color(0xFF49454F)
                )
                Text(
                    text = "$${String.format("%.2f", totalPrice)}",
                    fontSize = 14.sp,
                    color = Color(0xFF49454F)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Shipping",
                    fontSize = 14.sp,
                    color = Color(0xFF49454F)
                )
                Text(
                    text = "Free",
                    fontSize = 14.sp,
                    color = Color(0xFF49454F)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                color = Color(0xFF49454F).copy(alpha = 0.2f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1D1B20)
                )
                Text(
                    text = "$${String.format("%.2f", totalPrice)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1D1B20)
                )
            }
        }
    }
}

// Data classes
data class CartItem(
    val book: Book,
    val quantity: Int = 1
)

// Sample data
fun getSampleCartItems(): List<CartItem> {
    return listOf(
        CartItem(
            book = Book(
                id = "1",
                title = "GUI Programming 2nd edition",
                author = "John Doe",
                price = 29.99
            ),
            quantity = 1
        ),
        CartItem(
            book = Book(
                id = "2",
                title = "Advanced Android Development",
                author = "Jane Smith",
                price = 34.99
            ),
            quantity = 2
        ),
        CartItem(
            book = Book(
                id = "3",
                title = "Kotlin Programming",
                author = "Mike Johnson",
                price = 24.99
            ),
            quantity = 1
        ),
        CartItem(
            book = Book(
                id = "4",
                title = "Android Architecture",
                author = "Sarah Wilson",
                price = 39.99
            ),
            quantity = 1
        )
    )
}