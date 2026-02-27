package com.example.prolens.model

data class ProductModel(
    var productId: String = "", // Changed from 'val' to 'var' so it can be updated
    val productName: String = "",
    val description: String = "",
    val category: String = "",
    val pricePerDay: Double = 0.0,
    val availability: Boolean = true,
    val productImage: String = "" // Add this if you are using Cloudinary!
) {
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