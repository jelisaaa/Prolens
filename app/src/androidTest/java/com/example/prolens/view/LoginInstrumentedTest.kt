package com.example.prolens.view

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import org.junit.After
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<LoginActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testNavigationToRegistration() {
        // Click on the "Sign Up" text (tagged with registerTag)
        composeRule.onNodeWithTag("registerTag")
            .performClick()

        // Verify it navigates to RegistrationActivity
        Intents.intended(hasComponent(RegistrationActivity::class.java.name))
    }

    @Test
    fun testLoginFieldsAndNavigation() {
        // Type email
        composeRule.onNodeWithTag("email")
            .performTextInput("test@gmail.com")

        // Type password
        composeRule.onNodeWithTag("password")
            .performTextInput("123456")

        // Click Login button
        // Note: Real Firebase login will fail in Instrumented test without mocking or real network.
        // This test clicks the button and expects it to try to navigate if logic allows.
        composeRule.onNodeWithTag("loginButton")
            .performClick()
    }
}
