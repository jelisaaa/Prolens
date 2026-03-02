package com.example.prolens.viewmodel

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.prolens.model.ProductModel
import com.example.prolens.repository.ProductRepo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ProductViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun uploadImage_success_test() {
        val repo = mock<ProductRepo>()
        val viewModel = ProductViewModel(repo)
        val context = mock<Context>()
        val uri = mock<Uri>()
        val expectedUrl = "https://cloudinary.com/image.jpg"

        doAnswer { invocation ->
            val callback = invocation.getArgument<(String?) -> Unit>(2)
            callback(expectedUrl)
            null
        }.`when`(repo).uploadImage(eq(context), eq(uri), any())

        var resultUrl: String? = null
        viewModel.uploadImage(context, uri) { url ->
            resultUrl = url
        }

        assertEquals(expectedUrl, resultUrl)
        verify(repo).uploadImage(eq(context), eq(uri), any())
    }

    @Test
    fun addProduct_success_test() {
        val repo = mock<ProductRepo>()
        val viewModel = ProductViewModel(repo)
        val product = ProductModel(
            productId = "123",
            productName = "Camera",
            description = "DSLR",
            pricePerDay = 50.0,
            productImage = "url"
        )

        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(1)
            callback(true, "Product added")
            null
        }.`when`(repo).addProduct(eq(product), any())

        var successResult = false
        var messageResult = ""

        viewModel.addProduct(product) { success, msg ->
            successResult = success
            messageResult = msg
        }

        assertTrue(successResult)
        assertEquals("Product added", messageResult)
        verify(repo).addProduct(eq(product), any())
    }
}
