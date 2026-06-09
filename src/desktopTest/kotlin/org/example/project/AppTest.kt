package org.example.project

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import org.example.project.model.Movie
import org.junit.Rule
import org.junit.Test

class AppTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val movies = listOf<Movie>(
        Movie(
            "Avengers - EndGame",
            "https://upload.wikimedia.org/wikipedia/pt/9/9b/Avengers_Endgame.jpg",
            "8.4",
            "2019"
        )
    )

    val invalidMovie = Movie(
        "Avengers - EndGame",
        "https://link-invalido/Avengers_Endgame.jpg",
        "8.4",
        "2019"
    )

    @Test
    fun `should present movie info as expected`() {
        composeTestRule.setContent {
            App(movies)
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
            .onNodeWithContentDescription("Rate trophy")
            .assertIsDisplayed()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodes(hasContentDescription("Logo Avengers - EndGame"))
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithContentDescription("Logo Avengers - EndGame")
            .assertIsDisplayed()
    }

    @Test
    fun `should show error message when image fails`() {
        composeTestRule.setContent {
            App(listOf(invalidMovie))
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