package com.example.bookstore.data.repository

import com.example.bookstore.data.api.BookApiService
import com.example.bookstore.data.model.BookResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookApiService: BookApiService
) {
    suspend fun searchBooks(
        title: String? = null,
        authorName: String? = null,
        genreName: String? = null,
        publisherName: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        publicationYearFrom: Int? = null,
        publicationYearTo: Int? = null,
        minPageCount: Int? = null,
        maxPageCount: Int? = null,
        inStock: Boolean? = null,
        minRating: Double? = null,
        pageNumber: Int? = null,
        pageSize: Int? = null
    ): List<BookResponse> {
        return bookApiService.searchBooks(
            title = title,
            authorName = authorName,
            genreName = genreName,
            publisherName = publisherName,
            minPrice = minPrice,
            maxPrice = maxPrice,
            publicationYearFrom = publicationYearFrom,
            publicationYearTo = publicationYearTo,
            minPageCount = minPageCount,
            maxPageCount = maxPageCount,
            inStock = inStock,
            minRating = minRating,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
    }

//    suspend fun getBookById(bookId: String): BookResponse {
//        return bookApiService.getBookById(bookId)
//    }
}

