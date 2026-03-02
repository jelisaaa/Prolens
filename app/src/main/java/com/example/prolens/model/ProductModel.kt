package com.example.prolens.model

import java.io.Serializable // Add this import

data class ProductModel(
    var productId: String = "",
    val productName: String = "",
    val description: String = "", // Note: Use this name in ProductDetailActivity
    val category: String = "",
    val pricePerDay: Double = 0.0,
    val availability: Boolean = true,
    val productImage: String = ""
) : Serializable { // <--- ADD THIS HERE
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "productId" to productId,
            "productName" to productName,
            "description" to description,
            "category" to category,
            "pricePerDay" to pricePerDay,
            "availability" to availability,
            "productImage" to productImage
        )
    }
}