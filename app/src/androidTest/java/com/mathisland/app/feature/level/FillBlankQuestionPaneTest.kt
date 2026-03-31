package com.mathisland.app.feature.level

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.domain.model.Question
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class FillBlankQuestionPaneTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun fillBlankQuestion_requiresAllSlotsBeforeSubmit() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "把空格补完整。",
            choices = emptyList(),
            correctChoice = "100,200",
            hint = "先看单位，再把数字放进空格。",
            family = "fill-blank",
            blankParts = listOf("1 米 = ", " 厘米，2 米 = ", " 厘米。"),
            blankOptions = listOf("200", "100", "20")
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { submittedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("renderer-fill-blank").assertIsDisplayed()
        composeRule.onNodeWithTag("fill-blank-submit").assertIsNotEnabled()

        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-option-select-1"))
        composeRule.onNodeWithTag("fill-blank-option-select-1").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-slot-0"))
        composeRule.onNodeWithTag("fill-blank-slot-action-0").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-option-select-0"))
        composeRule.onNodeWithTag("fill-blank-option-select-0").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-slot-1"))
        composeRule.onNodeWithTag("fill-blank-slot-action-1").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-submit"))
        composeRule.onNodeWithTag("fill-blank-submit").assertIsEnabled().performClick()

        assertEquals("100,200", submittedAnswer)
    }
}
