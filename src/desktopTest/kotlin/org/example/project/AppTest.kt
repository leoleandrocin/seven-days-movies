package org.example.project

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
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
            .onNodeWithText("2019")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("8.4")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Troféu de Nota")
            .assertIsDisplayed()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodes(hasContentDescription("Logo Avengers EndGame"))
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithContentDescription("Logo Avengers EndGame")
            .assertIsDisplayed()
    }

    @Test
    fun `should show error message when image fails`() {
        composeTestRule.setContent {
            App(imageUrl = "https://link-invalido-que-vai-falhar.com/poster.jpg")
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodes(hasText("Error trying to load the image!"))
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithText("Error trying to load the image!")
            .assertIsDisplayed()
    }
}