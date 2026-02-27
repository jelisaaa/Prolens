package com.example.prolens.repository // Ensure this matches your ProLens package structure

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.prolens.model.ProductModel
import com.google.firebase.database.*
import java.io.InputStream
import java.util.concurrent.Executors

class ProductRepoImpl : ProductRepo {

    // Cloudinary configuration for uploading equipment images [cite: 14, 37]
    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dx5d730ps",
            "api_key" to "492212291256596",
            "api_secret" to "IwzqliMoKAwuWBl2DT0RIdhHWDc"
        )
    )

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    // "products" node supports real-time browsing of cameras, lenses, etc. [cite: 33, 34]
    private val ref: DatabaseReference = database.getReference("products")

    override fun addProduct(model: ProductModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        model.productId = id

        ref.child(id).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product added successfully") // Supports Easy Inventory Management [cite: 45]
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun updateProduct(model: ProductModel, callback: (Boolean, String) -> Unit) {
        ref.child(model.productId).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product updated successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit) {
        ref.child(productId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Product deleted successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getAllProduct(callback: (Boolean, String, List<ProductModel>?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ProductModel>()
                for (data in snapshot.children) {
                    val product = data.getValue(ProductModel::class.java)
                    product?.let { list.add(it) }
                }
                callback(true, "Products fetched", list) // Facilitates Easy Equipment Browsing [cite: 33]
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun getProductById(productId: String, callback: (Boolean, String, ProductModel?) -> Unit) {
        ref.child(productId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(ProductModel::class.java)
                callback(true, "Product found", product)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun getProductByCategory(categoryId: String, callback: (Boolean, String, List<ProductModel>?) -> Unit) {
        // Queries products by category to support organized browsing [cite: 33]
        ref.orderByChild("category").equalTo(categoryId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ProductModel>()
                for (data in snapshot.children) {
                    val product = data.getValue(ProductModel::class.java)
                    product?.let { list.add(it) }
                }
                callback(true, "Category products fetched", list)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executor.execute {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
                val uploadResult = cloudinary.uploader().upload(inputStream?.readBytes(), ObjectUtils.emptyMap())
                val url = uploadResult["url"] as String

                handler.post {
                    callback(url) // Returns the URL for profile or product images [cite: 14, 37]
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { callback(null) }
            }
        }
    }

    override fun getFileNameFromUri(context: Context, imageUri: Uri): String? {
        var result: String? = null
        if (imageUri.scheme == "content") {
            val cursor = context.contentResolver.query(imageUri, null, null, null, null)
            cursor.use {
                if (it != null && it.moveToFirst()) {
                    result = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = imageUri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }
}