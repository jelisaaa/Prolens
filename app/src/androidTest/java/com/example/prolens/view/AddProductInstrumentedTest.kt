package com.example.prolens.view

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddProductInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<AddProductActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testAddProductFlow() {
        // 1. Setup a fake image result for the picker
        // We use a mock Uri and wrap it in a result intent
        val resultUri = Uri.parse("android.resource://com.example.prolens/drawable/camera_sample")
        val resultIntent = Intent()
        resultIntent.data = resultUri
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent)

        // 2. Tell Espresso Intents to automatically "return" this image 
        // when the system image picker (ACTION_GET_CONTENT or PICK_IMAGES) is opened.
        // This bypasses the need to access the private 'selectedImageUri' variable.
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result)
        intending(hasAction("android.provider.action.PICK_IMAGES")).respondWith(result)

        // 3. Fill Name
        composeRule.onNodeWithTag("productNameField")
            .performTextInput("Test Camera")

        // 4. Fill Description
        composeRule.onNodeWithTag("productDescriptionField")
            .performTextInput("Test Description")

        // 5. Fill Price
        composeRule.onNodeWithTag("productPriceField")
            .performTextInput("100")

        // 6. Click the Upload Image Area
        // This triggers the picker, which immediately "returns" our fake URI
        composeRule.onNodeWithTag("uploadImageCard")
            .performClick()

        // 7. Click Save Button
        // Now the internal 'selectedImageUri' is not null, so it passes the validation
        composeRule.onNodeWithTag("saveProductButton")
            .performClick()
    }
}
