package com.example.prolens.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prolens.viewmodel.BookingViewModel

@Composable
fun AdminBookingManager(viewModel: BookingViewModel) {
    val context = LocalContext.current
    val bookings = viewModel.bookings

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Rental Requests",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (bookings.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No requests yet.", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(bookings) { booking ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1E1E)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("User: ${booking.userEmail}", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Gear: ${booking.productName}", color = Color.Yellow)

                            // Status Display
                            Text(
                                text = "Status: ${booking.status}",
                                color = when(booking.status) {
                                    "Approved" -> Color.Green
                                    "Rejected" -> Color.Red
                                    else -> Color.LightGray
                                },
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (booking.status == "Pending") {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            viewModel.updateStatus(booking.bookingId, "Rejected") { _, msg ->
                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                                    ) {
                                        Text("Reject", color = Color.White)
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.updateStatus(booking.bookingId, "Approved") { _, msg ->
                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                                    ) {
                                        Text("Approve", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}