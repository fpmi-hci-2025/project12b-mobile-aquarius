package com.example.bookstore.data.model

data class BookResponse(
    val id: String,
    val title: String,
    val description: String,
    val publicationYear: Int,
    val pageCount: Int,
    val price: Double,
    val weight: Double,
    val base64CoverImage: String?,
    val publisher: String,
    val authors: List<String>,
    val genres: List<String>,
    val averageRating: Double,
    val reviewCount: Int,
    val quantity: Int
)

