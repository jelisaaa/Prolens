package com.example.prolens.view

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.prolens.R
import com.example.prolens.model.ProductModel
import com.example.prolens.repository.ProductRepoImpl
import com.example.prolens.utils.ImageUtils
import com.example.prolens.viewmodel.ProductViewModel
import java.util.UUID

class AddProductActivity : ComponentActivity() {
    private lateinit var imageUtils: ImageUtils
    private var selectedImageUri by mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Initialize ImageUtils
        imageUtils = ImageUtils(this)

        // 2. Register launchers before setContent
        imageUtils.registerLaunchers { uri ->
            Log.d("ProLens", "Image Uri Received: $uri")
            selectedImageUri = uri
        }

        setContent {
            AddProductBody(
                selectedImageUri = selectedImageUri,
                onPickImage = {
                    Log.d("ProLens", "onPickImage triggered from UI")
                    imageUtils.launchImagePicker()
                }
            )
        }
    }
}

@Composable
fun AddProductBody(
    selectedImageUri: Uri?,
    onPickImage: () -> Unit,
    viewModel: ProductViewModel = ProductViewModel(ProductRepoImpl())
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }

    // Theme Colors
    val backgroundGray = Color(0xFFF2F2F2)
    val cardGray = Color(0xFFFFFFFF)
    val primaryGray = Color(0xFF444444)
    val lightGray = Color(0xFF888888)
    val buttonGray = Color(0xFF5E5E5E)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGray)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardGray),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "New Equipment",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryGray
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // --- UPLOAD AREA ---
                // Clickable is now on the card surface to ensure maximum touch area
                OutlinedCard(
                    onClick = { onPickImage() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = backgroundGray),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(lightGray))
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                                    contentDescription = null,
                                    tint = primaryGray,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Tap to Upload Image", color = primaryGray, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- FORM FIELDS ---
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGray,
                        unfocusedBorderColor = lightGray
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Product Description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGray,
                        unfocusedBorderColor = lightGray
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    placeholder = { Text("Price Per Day") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryGray,
                        unfocusedBorderColor = lightGray
                    )
                )

                Spacer(modifier = Modifier.height(30.dp))

                if (isUploading) {
                    CircularProgressIndicator(color = buttonGray)
                } else {
                    Button(
                        onClick = {
                            if (name.isBlank() || price.isBlank() || selectedImageUri == null) {
                                Toast.makeText(context, "Fill all details & image", Toast.LENGTH_SHORT).show()
                            } else {
                                isUploading = true
                                viewModel.uploadImage(context, selectedImageUri!!) { imageUrl ->
                                    if (imageUrl != null) {
                                        val productId = UUID.randomUUID().toString()
                                        val product = ProductModel(
                                            productId = productId,
                                            productName = name,
                                            description = description,
                                            pricePerDay = price.toDoubleOrNull() ?: 0.0,
                                            category = category,
                                            productImage = imageUrl
                                        )
                                        viewModel.addProduct(product) { success, message ->
                                            isUploading = false
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                            if (success) activity?.finish()
                                        }
                                    } else {
                                        isUploading = false
                                        Toast.makeText(context, "Cloudinary upload failed", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonGray)
                    ) {
                        Text("Save Equipment", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}