package com.example.prolens.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prolens.R

@Composable
fun HomeScreen() {
    val featuredList = List(5) { it }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)), // Set to Dark to match Dashboard
        contentPadding = PaddingValues(16.dp)
    ) {
        // 1. Welcome Text
        item {
            Text(
                text = "Welcome back, Jelisa 👋",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 2. Search Bar
        item {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search cameras, lenses...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 3. Featured Section
        item {
            Text(
                text = "Featured Equipment",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                itemsIndexed(featuredList) { _, _ ->
                    EquipmentCard()
                }
            }
        }

        // 4. Categories Section
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            CategoryGrid()
        }
    }
}

@Composable
fun EquipmentCard() {
    Card(
        modifier = Modifier.width(220.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(R.drawable.camera_sample),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(130.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Canon EOS R5",
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Rs. 2500/day",
                color = Color(0xFF00BCD4),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CategoryGrid() {
    val categories = listOf("Cameras 📷", "Lenses 🔍", "Tripods 🎥", "Lighting 💡")

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(250.dp), // Fixed height to prevent crash
        userScrollEnabled = false,           // Disable inner scroll
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E1E1E)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePre() {
    HomeScreen()
}