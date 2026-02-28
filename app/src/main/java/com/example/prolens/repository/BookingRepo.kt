package com.example.prolens.repository

import com.example.prolens.model.BookingModel

interface BookingRepo {
    fun placeBooking(model: BookingModel, callback: (Boolean, String) -> Unit)
    fun updateBookingStatus(bookingId: String, status: String, callback: (Boolean, String) -> Unit)
    fun deleteBooking(bookingId: String, callback: (Boolean, String) -> Unit)
    fun getAllBookings(callback: (Boolean, String, List<BookingModel>?) -> Unit)
    fun getBookingsByUserId(userId: String, callback: (Boolean, String, List<BookingModel>?) -> Unit)
    fun getBookingById(bookingId: String, callback: (Boolean, String, BookingModel?) -> Unit)
}