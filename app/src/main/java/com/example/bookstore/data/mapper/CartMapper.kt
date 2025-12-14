package com.example.bookstore.data.mapper

import com.example.bookstore.data.model.CartItem as ApiCartItem
import com.example.bookstore.data.model.CartResponse
import com.example.bookstore.ui.Book
import com.example.bookstore.ui.CartItem as UiCartItem

object CartMapper {
    fun toUiCartItem(apiCartItem: ApiCartItem): UiCartItem {
        return UiCartItem(
            book = Book(
                id = apiCartItem.bookId,
                title = apiCartItem.bookTitle,
                author = "", // API не возвращает автора в CartItem
                price = apiCartItem.bookPrice,
                imageUrl = "", // API не возвращает изображение в CartItem
                description = ""
            ),
            quantity = apiCartItem.quantity
        )
    }

    fun toUiCartItems(cartResponse: CartResponse): List<UiCartItem> {
        return cartResponse.cartItems.map { toUiCartItem(it) }
    }
}
