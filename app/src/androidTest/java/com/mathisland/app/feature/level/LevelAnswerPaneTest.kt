package com.mathisland.app.feature.level

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.domain.model.Question
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LevelAnswerPaneTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun calculationQuestion_usesChoiceRendererAndReturnsTappedChoice() {
        var selectedAnswer: String? = null
        val question = Question(
            prompt = "26 + 18 = ?",
            choices = listOf("44", "45", "34"),
            correctChoice = "44",
            hint = "先算个位，再算十位。",
            family = "calculation"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { selectedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("renderer-choice").assertIsDisplayed()
        composeRule.onNodeWithTag("answer-44").performClick()

        assertEquals("44", selectedAnswer)
    }

    @Test
    fun challengeQuestion_usesNumberPadAndSubmitsTypedValue() {
        var selectedAnswer: String? = null
        val question = Question(
            prompt = "9 x 9 = ?",
            choices = listOf("81", "72", "99"),
            correctChoice = "81",
            hint = "想想 9 个 9。",
            family = "challenge"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { selectedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("renderer-number-pad").assertIsDisplayed()
        composeRule.onNodeWithTag("number-pad-key-8").performClick()
        composeRule.onNodeWithTag("number-pad-key-1").performClick()
        composeRule.onNodeWithTag("number-pad-submit").performClick()

        assertEquals("81", selectedAnswer)
    }

    @Test
    fun measurementQuestion_showsRulerAffordance() {
        val question = Question(
            prompt = "小船长约长多少？",
            choices = listOf("8 厘米", "10 厘米", "12 厘米"),
            correctChoice = "10 厘米",
            hint = "观察尺子上的刻度。",
            family = "measurement"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-ruler").assertIsDisplayed()
        composeRule.onNodeWithTag("tablet-ruler-handle").assertIsDisplayed()
        composeRule.onAllNodesWithTag("answer-10 厘米").assertCountEquals(1)
        composeRule.onNodeWithText("拖动尺子观察刻度，再选择最合适的答案。").assertIsDisplayed()
    }
}
