package com.example.prolens.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.prolens.model.BookingModel
import com.example.prolens.model.ProductModel
import com.example.prolens.ui.theme.ProLensTheme
import com.example.prolens.viewmodel.BookingViewModel
import com.google.firebase.auth.FirebaseAuth

class ProductDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        @Suppress("DEPRECATION")
        val product = intent.getSerializableExtra("PRODUCT_DATA") as? ProductModel

        setContent {
            ProLensTheme {
                if (product != null) {
                    val bookingViewModel: BookingViewModel = viewModel()
                    ProductDetailScreen(product, bookingViewModel) { finish() }
                } else {
                    // Safety check if data is missing
                    Toast.makeText(this, "Product data missing", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(product: ProductModel, viewModel: BookingViewModel, onBack: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        containerColor = Color(0xFF121212),
        topBar = {
            TopAppBar(
                title = { Text("Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E1E1E))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Large Image Header
            AsyncImage(
                model = product.productImage,
                contentDescription = product.productName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = product.productName,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Rs. ${product.pricePerDay} / day",
                    color = Color.Yellow,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                HorizontalDivider(color = Color.DarkGray, thickness = 1.dp)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Description",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = product.description,
                    color = Color.LightGray,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Action Button for Rental Requests
                Button(
                    onClick = {
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null) {
                            val booking = BookingModel(
                                userId = user.uid,
                                userEmail = user.email ?: "",
                                productId = product.productId,
                                productName = product.productName,
                                price = product.pricePerDay,
                                status = "Pending",
                                timestamp = System.currentTimeMillis()
                            )


                            Toast.makeText(context, "Processing request...", Toast.LENGTH_SHORT).show()

                            viewModel.createBooking(booking) { success, message ->
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                if (success) {
                                    onBack() // Navigate back on success
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please Login to Rent", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Request Rental",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}