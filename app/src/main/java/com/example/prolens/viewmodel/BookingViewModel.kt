package com.example.prolens.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.prolens.model.BookingModel
import com.example.prolens.repository.BookingRepo
import com.example.prolens.repository.BookingRepoImpl

class BookingViewModel(
    private val repo: BookingRepo = BookingRepoImpl()
) : ViewModel() {


    private val _bookings = mutableStateListOf<BookingModel>()
    val bookings: List<BookingModel> = _bookings

    private val _userBookings = mutableStateListOf<BookingModel>()
    val userBookings: List<BookingModel> = _userBookings

    init {
        fetchBookings()
    }

    private fun fetchBookings() {
        repo.getAllBookings { success, message, data ->
            if (success && data != null) {
                _bookings.clear()
                _bookings.addAll(data)
            }
        }
    }

    fun fetchUserBookings(userId: String) {
        repo.getBookingsByUserId(userId) { success, message, data ->
            if (success && data != null) {
                _userBookings.clear()
                _userBookings.addAll(data)
            }
        }
    }

    fun updateStatus(bookingId: String, status: String, onResult: (Boolean, String) -> Unit) {
        repo.updateBookingStatus(bookingId, status) { success, message ->
            onResult(success, message)
        }
    }

    fun createBooking(booking: BookingModel, onResult: (Boolean, String) -> Unit) {
        repo.placeBooking(booking) { success, message ->
            onResult(success, message)
        }
    }

    fun deleteBooking(bookingId: String, onResult: (Boolean, String) -> Unit) {
        repo.deleteBooking(bookingId) { success, message ->
            // Note: Because getBookingsByUserId uses a ValueEventListener,
            // the _userBookings list will update automatically when the data is deleted from Firebase.
            onResult(success, message)
        }
    }
}