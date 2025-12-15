package com.example.bookstore.data.model

import org.junit.Assert.*
import org.junit.Test

class LoginResponseTest {

    @Test
    fun loginResponse_createsCorrectly() {
        val userDetails = UserDetails(
            email = "test@example.com",
            firstName = "John",
            lastName = "Doe",
            phone = "1234567890"
        )
        
        val response = LoginResponse(
            accessToken = "token123",
            refreshToken = "refresh123",
            userDetails = userDetails
        )

        assertEquals("token123", response.accessToken)
        assertEquals("refresh123", response.refreshToken)
        assertEquals("test@example.com", response.userDetails.email)
        assertEquals("John", response.userDetails.firstName)
        assertEquals("Doe", response.userDetails.lastName)
        assertEquals("1234567890", response.userDetails.phone)
    }

    @Test
    fun userDetails_createsCorrectly() {
        val userDetails = UserDetails(
            email = "user@test.com",
            firstName = "Jane",
            lastName = "Smith",
            phone = "9876543210"
        )

        assertEquals("user@test.com", userDetails.email)
        assertEquals("Jane", userDetails.firstName)
        assertEquals("Smith", userDetails.lastName)
        assertEquals("9876543210", userDetails.phone)
    }
}

