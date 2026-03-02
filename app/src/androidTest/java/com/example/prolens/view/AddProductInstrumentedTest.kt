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
        val resultUri = Uri.parse("android.resource://com.example.prolens/drawable/camera_sample")
        val resultIntent = Intent()
        resultIntent.data = resultUri
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent)

        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result)
        intending(hasAction("android.provider.action.PICK_IMAGES")).respondWith(result)

        composeRule.onNodeWithTag("productNameField")
            .performTextInput("Test Camera")

        composeRule.onNodeWithTag("productDescriptionField")
            .performTextInput("Test Description")

        composeRule.onNodeWithTag("productPriceField")
            .performTextInput("100")

        composeRule.onNodeWithTag("uploadImageCard")
            .performClick()

        composeRule.onNodeWithTag("saveProductButton")
            .performClick()
    }
}
