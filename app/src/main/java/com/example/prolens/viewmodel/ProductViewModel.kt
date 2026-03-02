package com.example.prolens.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.prolens.model.ProductModel
import com.example.prolens.repository.ProductRepo

class ProductViewModel(private val repo: ProductRepo) : ViewModel() {

    private val _products = MutableLiveData<ProductModel?>()
    val products: MutableLiveData<ProductModel?> get() = _products

    private val _allProducts = MutableLiveData<List<ProductModel>?>()
    val allProducts: MutableLiveData<List<ProductModel>?> get() = _allProducts

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> get() = _loading

    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit) {
        _loading.postValue(true)
        repo.uploadImage(context, imageUri) { imageUrl ->

            callback(imageUrl)
        }
    }

    fun addProduct(model: ProductModel, callback: (Boolean, String) -> Unit) {
        repo.addProduct(model) { success, message ->
            _loading.postValue(false)
            callback(success, message)
        }
    }

    fun getAllProduct() {
        _loading.postValue(true)
        repo.getAllProduct { success, message, data ->
            _loading.postValue(false)
            if (success) {
                _allProducts.postValue(data)
            }
        }
    }

    fun getProductById(productId: String) {
        repo.getProductById(productId) { success, message, data ->
            if (success) {
                _products.postValue(data)
            }
        }
    }

    fun deleteProduct(id: String, callback: (Boolean, String) -> Unit) {
        repo.deleteProduct(id) { success, message ->
            if (success) {
                // Update the list immediately in the UI after deletion
                _allProducts.value = _allProducts.value?.filterNot { it.productId == id }
            }
            callback(success, message)
        }
    }
}