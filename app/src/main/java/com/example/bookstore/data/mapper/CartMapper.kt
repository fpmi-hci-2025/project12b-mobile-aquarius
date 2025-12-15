// app/src/main/java/com/example/bookstore/data/mapper/CartMapper.kt

package com.example.bookstore.data.mapper

import android.util.Log
import com.example.bookstore.data.model.CartItem as ApiCartItem
import com.example.bookstore.data.model.CartResponse
import com.example.bookstore.ui.Book
import com.example.bookstore.ui.CartItem as UiCartItem

object CartMapper {
    fun toUiCartItem(apiCartItem: ApiCartItem): UiCartItem {
        Log.d("CartMapper", "apiCartItem = $apiCartItem")
        // Преобразуем base64 в data:image/... как в BookMapper
        val imageUrl = apiCartItem.base64CoverImage?.let {
            if (it.startsWith("data:image")) {
                it
            } else {
                "data:image/jpeg;base64,$it"
            }
        } ?: ""

        return UiCartItem(
            book = Book(
                id = apiCartItem.bookId,
                title = apiCartItem.bookTitle,
                author = "", // API не возвращает автора в CartItem
                price = apiCartItem.bookPrice,
                imageUrl = imageUrl,
                description = ""
            ),
            quantity = apiCartItem.quantity
        )
    }

    fun toUiCartItems(cartResponse: CartResponse): List<UiCartItem> {
        return cartResponse.cartItems.map { toUiCartItem(it) }
    }
}