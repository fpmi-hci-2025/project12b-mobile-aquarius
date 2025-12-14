package com.example.bookstore.data.mapper

import com.example.bookstore.data.model.BookResponse
import com.example.bookstore.ui.Book

object BookMapper {
    fun toBook(bookResponse: BookResponse): Book {
        // Обрабатываем base64 изображение - если оно есть, используем как есть
        // API возвращает base64 строку, которую можно использовать напрямую
        val imageUrl = bookResponse.base64CoverImage?.let { 
            // Если строка уже содержит data:image, используем как есть
            if (it.startsWith("data:image")) {
                it
            } else {
                // Иначе добавляем префикс для data URI
                "data:image/jpeg;base64,$it"
            }
        } ?: ""
        
        return Book(
            id = bookResponse.id,
            title = bookResponse.title,
            author = bookResponse.authors.joinToString(", ").ifEmpty { "Unknown Author" },
            price = bookResponse.price,
            imageUrl = imageUrl,
            description = bookResponse.description
        )
    }

    fun toBookList(bookResponses: List<BookResponse>): List<Book> {
        return bookResponses.map { toBook(it) }
    }
}

