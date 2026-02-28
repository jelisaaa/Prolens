package com.example.prolens.model

data class BookingModel(
    val bookingId: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val productId: String = "",
    val productName: String = "",
    val price: Double = 0.0,
    val status: String = "Pending", // Pending, Approved, Rejected
    val timestamp: Long = System.currentTimeMillis()
)