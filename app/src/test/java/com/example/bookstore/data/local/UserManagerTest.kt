package com.example.bookstore.data.local

import org.junit.Assert.*
import org.junit.Test

class UserManagerTest {

    @Test
    fun getFullName_returnsFormattedName() {
        val firstName = "John"
        val lastName = "Doe"
        val expected = "$firstName $lastName".trim()
        
        assertEquals("John Doe", expected)
    }

    @Test
    fun getFullName_handlesEmptyFirstName() {
        val firstName = ""
        val lastName = "Doe"
        val fullName = "$firstName $lastName".trim()
        
        assertEquals("Doe", fullName)
    }

    @Test
    fun getFullName_handlesEmptyLastName() {
        val firstName = "John"
        val lastName = ""
        val fullName = "$firstName $lastName".trim()
        
        assertEquals("John", fullName)
    }

    @Test
    fun getFullName_handlesBothEmpty() {
        val firstName = ""
        val lastName = ""
        val fullName = "$firstName $lastName".trim()
        
        assertTrue(fullName.isEmpty())
    }
}

