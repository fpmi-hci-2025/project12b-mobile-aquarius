package com.example.bookstore.utils

import com.example.bookstore.utils.PriceFormatter.formatPrice
import org.junit.Assert.*
import org.junit.Test

class PriceFormatterTest {

    @Test
    fun formatPrice_formatsCorrectly() {
        assertEquals("$10,00", formatPrice(10.0))
        assertEquals("$25,50", formatPrice(25.5))
        assertEquals("$0,99", formatPrice(0.99))
        assertEquals("$1000,00", formatPrice(1000.0))
    }

    @Test
    fun formatPrice_handlesZero() {
        assertEquals("$0,00", formatPrice(0.0))
    }
}

// Простая утилита для форматирования цены
object PriceFormatter {
    fun formatPrice(price: Double): String {
        return "$${String.format("%.2f", price)}"
    }
}

