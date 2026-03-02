package com.example.prolens.view

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prolens.model.BookingModel
import com.example.prolens.viewmodel.BookingViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(viewModel: BookingViewModel) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val userBookings = viewModel.userBookings

    LaunchedEffect(user?.uid) {
        user?.uid?.let { uid ->
            viewModel.fetchUserBookings(uid)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E1E1E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(60.dp),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = user?.email?.split("@")?.get(0)?.replaceFirstChar { it.uppercase() } ?: "User",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = user?.email ?: "No email registered",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Text(
                text = "My Rental History",
                color = Color.Yellow,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (userBookings.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "You haven't rented any gear yet.",
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                }
            }
        } else {

            items(userBookings) { booking ->
                UserBookingCard(booking, viewModel)
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    auth.signOut()
                    val intent = Intent(context, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                    (context as? Activity)?.finish()
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4444)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Logout", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun UserBookingCard(booking: BookingModel, viewModel: BookingViewModel) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = booking.productName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                val detailText = when (booking.status) {
                    "Approved" -> "Ready for pickup! ✅"
                    "Rejected" -> "Request declined ❌"
                    else -> "Awaiting admin review..."
                }
                Text(text = detailText, color = Color.Gray, fontSize = 12.sp)
            }


            if (booking.status == "Pending") {
                IconButton(onClick = {
                    viewModel.deleteBooking(booking.bookingId) { success, message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Cancel Request",
                        tint = Color(0xFFFF5252)
                    )
                }
            }


            Surface(
                color = when (booking.status) {
                    "Approved" -> Color(0xFF1B5E20)
                    "Rejected" -> Color(0xFFB71C1C)
                    else -> Color(0xFF424242)
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = booking.status,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}