package com.example.bookstore.data.api

import com.example.bookstore.data.model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {
    @GET("api/books/search")
    suspend fun searchBooks(
        @Query("Title") title: String? = null,
        @Query("AuthorName") authorName: String? = null,
        @Query("GenreName") genreName: String? = null,
        @Query("PublisherName") publisherName: String? = null,
        @Query("MinPrice") minPrice: Double? = null,
        @Query("MaxPrice") maxPrice: Double? = null,
        @Query("PublicationYearFrom") publicationYearFrom: Int? = null,
        @Query("PublicationYearTo") publicationYearTo: Int? = null,
        @Query("MinPageCount") minPageCount: Int? = null,
        @Query("MaxPageCount") maxPageCount: Int? = null,
        @Query("InStock") inStock: Boolean? = null,
        @Query("MinRating") minRating: Double? = null,
        @Query("PageNumber") pageNumber: Int? = null,
        @Query("PageSize") pageSize: Int? = null
    ): List<BookResponse>
}

