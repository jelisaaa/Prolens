package com.example.prolens.repository

import com.example.prolens.model.BookingModel
import com.google.firebase.database.*

class BookingRepoImpl : BookingRepo {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    // "Bookings" node stores all rental transactions for ProLens
    private val ref: DatabaseReference = database.getReference("Bookings")

    override fun placeBooking(model: BookingModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        // Ensure the model has the generated ID
        val bookingWithId = model.copy(bookingId = id)

        ref.child(id).setValue(bookingWithId).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Rental request sent successfully") // Facilitates Easy Rental Process
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun updateBookingStatus(bookingId: String, status: String, callback: (Boolean, String) -> Unit) {
        // Updates status (e.g., to 'Approved') to notify the user
        ref.child(bookingId).child("status").setValue(status).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Booking status updated to $status")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun deleteBooking(bookingId: String, callback: (Boolean, String) -> Unit) {
        ref.child(bookingId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Booking deleted successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getAllBookings(callback: (Boolean, String, List<BookingModel>?) -> Unit) {
        // Real-time listener for Admin to monitor all gear requests
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<BookingModel>()
                for (data in snapshot.children) {
                    val booking = data.getValue(BookingModel::class.java)
                    booking?.let { list.add(it) }
                }
                callback(true, "Bookings fetched", list)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun getBookingsByUserId(userId: String, callback: (Boolean, String, List<BookingModel>?) -> Unit) {
        // Filters bookings so users can see their own rental history
        ref.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<BookingModel>()
                for (data in snapshot.children) {
                    val booking = data.getValue(BookingModel::class.java)
                    booking?.let { list.add(it) }
                }
                callback(true, "User bookings fetched", list)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun getBookingById(bookingId: String, callback: (Boolean, String, BookingModel?) -> Unit) {
        ref.child(bookingId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val booking = snapshot.getValue(BookingModel::class.java)
                callback(true, "Booking found", booking)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }
}