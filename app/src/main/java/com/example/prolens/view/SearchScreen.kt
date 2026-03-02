package com.example.prolens.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prolens.model.ProductModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun SearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val allProducts = remember { mutableStateListOf<ProductModel>() }

    // 1. Fetch products for searching
    LaunchedEffect(Unit) {
        val database = FirebaseDatabase.getInstance().getReference("products")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allProducts.clear()
                for (item in snapshot.children) {
                    item.getValue(ProductModel::class.java)?.let { allProducts.add(it) }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 2. Filter logic
    val filteredProducts = allProducts.filter {
        it.productName.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = "Search Gear",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search cameras, lenses, etc.", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Cyan) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E1E1E),
                    unfocusedContainerColor = Color(0xFF1E1E1E),
                    focusedBorderColor = Color.Cyan,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 3. Results Section
        if (searchQuery.isEmpty()) {
            item {
                Text("Suggestions", color = Color.Gray, style = MaterialTheme.typography.labelLarge)
            }
            val suggestions = listOf("Sony", "Canon", "Lens", "Gimbal")
            items(suggestions) { suggestion ->
                RecentSearchItem(text = suggestion) { searchQuery = suggestion }
            }
        } else {
            items(filteredProducts) { product ->
                SearchResultItem(product)
            }
        }
    }
}

@Composable
fun SearchResultItem(product: ProductModel) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(product.productName, color = Color.White, fontWeight = FontWeight.Bold)
                Text("Rs. ${product.pricePerDay} / day", color = Color.Green, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun RecentSearchItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.History, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, color = Color.LightGray)
    }
}