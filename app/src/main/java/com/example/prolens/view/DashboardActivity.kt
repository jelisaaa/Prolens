package com.example.prolens.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.prolens.R
import com.example.prolens.model.BookingModel
import com.example.prolens.model.ProductModel
import com.example.prolens.ui.theme.ProLensTheme
import com.example.prolens.viewmodel.BookingViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProLensTheme {
                val bookingViewModel: BookingViewModel = viewModel()
                DashboardScreen(bookingViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(bookingViewModel: BookingViewModel) {
    val context = LocalContext.current
    val activity = context as Activity
    var selectedIndex by remember { mutableIntStateOf(0) }
    var isAdmin by remember { mutableStateOf<Boolean?>(null) }

    // Role verification logic
    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)
            userRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val role = snapshot.child("role").getValue(String::class.java)
                    isAdmin = (role == "admin")
                } else {
                    isAdmin = false
                }
            }.addOnFailureListener { isAdmin = false }
        } else {
            isAdmin = false
        }
    }

    if (isAdmin == null) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF121212)), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.Yellow)
        }
    } else {
        Scaffold(
            containerColor = Color(0xFF121212),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("ProLens ${if(isAdmin == true) "Admin" else ""} 📸", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF1E1E1E),
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = { activity.finish() }) {
                            Icon(painter = painterResource(R.drawable.baseline_arrow_back_ios_24), contentDescription = null, tint = Color.White)
                        }
                    }
                )
            },
            floatingActionButton = {
                // Admin gets the 'Add Product' button on Home
                if (selectedIndex == 0 && isAdmin == true) {
                    FloatingActionButton(
                        containerColor = Color.Yellow,
                        contentColor = Color.Black,
                        onClick = { context.startActivity(Intent(context, AddProductActivity::class.java)) }
                    ) { Icon(Icons.Default.Add, contentDescription = "Add Product") }
                }
            },
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF1E1E1E)) {
                    val navItems = listOf("Home", "Search", "Profile", "More")
                    navItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            icon = {
                                val iconRes = when (item) {
                                    "Home" -> R.drawable.baseline_home_24
                                    "Search" -> R.drawable.baseline_search_24
                                    "Profile" -> R.drawable.baseline_person_24
                                    else -> R.drawable.baseline_menu_24
                                }
                                Icon(painterResource(id = iconRes), contentDescription = item)
                            },
                            label = { Text(item) }
                        )
                    }
                }
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                when (selectedIndex) {
                    0 -> if (isAdmin == true) AdminHomeScreen() else UserHomeScreen(bookingViewModel)
                    1 -> SearchScreen()
                    2 -> ProfileScreen(bookingViewModel)
                    // This calls the function from your separate file
                    3 -> if (isAdmin == true) AdminBookingManager(bookingViewModel) else MoreScreen()
                }
            }
        }
    }
}

@Composable
fun AdminHomeScreen() {
    val productList = remember { mutableStateListOf<ProductModel>() }
    val database = FirebaseDatabase.getInstance().getReference("products")

    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (item in snapshot.children) {
                    item.getValue(ProductModel::class.java)?.let { productList.add(it) }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Inventory Management", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Control your gear listings below", color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(12.dp))

        if (productList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No products in inventory.", color = Color.DarkGray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(productList) { product ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = product.productImage,
                                contentDescription = null,
                                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                                Text(product.productName, color = Color.White, fontWeight = FontWeight.Bold)
                                Text("Rs. ${product.pricePerDay}/day", color = Color.Yellow, fontSize = 14.sp)
                            }
                            IconButton(onClick = {
                                database.child(product.productId).removeValue()
                            }) {
                                Icon(painterResource(R.drawable.baseline_delete_24), contentDescription = "Delete", tint = Color(0xFFFF5252))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserHomeScreen(bookingViewModel: BookingViewModel) {
    val context = LocalContext.current
    val productList = remember { mutableStateListOf<ProductModel>() }
    val productDatabase = FirebaseDatabase.getInstance().getReference("products")

    LaunchedEffect(Unit) {
        productDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                for (item in snapshot.children) {
                    item.getValue(ProductModel::class.java)?.let { productList.add(it) }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Available Gear", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(productList) { product ->
                Column(
                    modifier = Modifier
                        .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            val intent = Intent(context, ProductDetailActivity::class.java).apply {
                                putExtra("PRODUCT_DATA", product)
                            }
                            context.startActivity(intent)
                        }
                        .padding(12.dp)
                ) {
                    AsyncImage(
                        model = product.productImage,
                        contentDescription = product.productName,
                        modifier = Modifier.fillMaxWidth().height(120.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = product.productName, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(text = "Rs. ${product.pricePerDay}/day", color = Color.Green, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
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
                                    status = "Pending"
                                )
                                bookingViewModel.createBooking(booking) { success, message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Rent", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}