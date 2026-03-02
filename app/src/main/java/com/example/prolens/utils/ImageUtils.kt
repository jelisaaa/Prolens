package com.example.prolens.utils

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

class ImageUtils(private val activity: ComponentActivity) {
    // This must be initialized using the activity's result registry
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    fun registerLaunchers(onImageSelected: (Uri?) -> Unit) {
        // Register the Photo Picker contract
        pickMedia = activity.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                onImageSelected(uri)
            }
        }
    }

    fun launchImagePicker() {
        // This opens the clean Google Photo Picker interface
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}