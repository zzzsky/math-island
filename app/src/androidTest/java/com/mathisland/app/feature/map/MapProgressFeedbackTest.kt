package com.mathisland.app.feature.map

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.mathisland.app.MathIslandTheme
import org.junit.Rule
import org.junit.Test

class MapProgressFeedbackTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun feedback_showsUnlockAndStarsMessage() {
        composeRule.setContent {
            MathIslandTheme {
                MapProgressFeedback(
                    feedback = MapFeedbackUiState(
                        title = "新岛已解锁",
                        body = "测量与图形岛已开放，累计获得 3 颗星星。",
                        highlightedIslandId = "measurement-island",
                        starsEarned = 3,
                        chestReady = true
                    )
                )
            }
        }

        composeRule.onNodeWithTag("map-progress-feedback").assertIsDisplayed()
        composeRule.onNodeWithTag("map-feedback-title").assertIsDisplayed()
        composeRule.onNodeWithText("新岛已解锁").assertIsDisplayed()
        composeRule.onNodeWithTag("map-feedback-body").assertIsDisplayed()
        composeRule.onNodeWithText("测量与图形岛已开放，累计获得 3 颗星星。").assertIsDisplayed()
        composeRule.onNodeWithTag("map-feedback-summary").assertIsDisplayed()
        composeRule.onNodeWithTag("map-feedback-stars-pill").assertIsDisplayed()
        composeRule.onNodeWithTag("map-feedback-chest-pill").assertIsDisplayed()
    }
}
