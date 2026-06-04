package org.example.project

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class AppTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `should present movie info as expected`() {
        composeTestRule.setContent {
            App()
        }

        composeTestRule
            .onNodeWithText("Avengers - EndGame")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Rate: 8.4  |  Year: 2019")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Logo Avengers EndGame")
            .assertIsDisplayed()
    }
}