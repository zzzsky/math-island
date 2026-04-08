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

class MatchingQuestionPaneTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun matchingQuestion_requiresAllPairsBeforeSubmit() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "把数学工具和它最适合表示的意思连起来。",
            choices = emptyList(),
            correctChoice = "尺子=长度,秤=重量,时钟=时间",
            hint = "先看左边工具，再找到右边最贴切的意思。",
            family = "matching",
            leftItems = listOf("尺子", "秤", "时钟"),
            rightItems = listOf("时间", "重量", "长度")
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { submittedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("renderer-matching").assertIsDisplayed()
        composeRule.onNodeWithTag("matching-submit").assertIsNotEnabled()

        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-left-select-0"))
        composeRule.onNodeWithTag("matching-left-select-0").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-right-assign-2"))
        composeRule.onNodeWithTag("matching-right-assign-2").assertIsEnabled()
        composeRule.onNodeWithTag("matching-right-assign-2").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-left-select-1"))
        composeRule.onNodeWithTag("matching-left-select-1").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-right-assign-1"))
        composeRule.onNodeWithTag("matching-right-assign-1").assertIsEnabled()
        composeRule.onNodeWithTag("matching-right-assign-1").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-left-select-2"))
        composeRule.onNodeWithTag("matching-left-select-2").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-right-assign-0"))
        composeRule.onNodeWithTag("matching-right-assign-0").assertIsEnabled()
        composeRule.onNodeWithTag("matching-right-assign-0").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-submit"))

        composeRule.onNodeWithTag("matching-submit").assertIsEnabled().performClick()

        assertEquals("尺子=长度,秤=重量,时钟=时间", submittedAnswer)
    }

    @Test
    fun matchingQuestion_supportsFourPairAssignments() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "把工具和它最贴切的用途连起来。",
            choices = emptyList(),
            correctChoice = "尺子=长度,秤=重量,日历=日期,温度计=温度",
            hint = "先看左边工具，再找右边最贴切的用途。",
            family = "matching",
            leftItems = listOf("尺子", "秤", "日历", "温度计"),
            rightItems = listOf("温度", "日期", "重量", "长度")
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { submittedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("renderer-matching").assertIsDisplayed()
        composeRule.onNodeWithTag("matching-submit").assertIsNotEnabled()

        listOf(3, 2, 1, 0).forEachIndexed { leftIndex, rightIndex ->
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-left-select-$leftIndex"))
            composeRule.onNodeWithTag("matching-left-select-$leftIndex").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-right-assign-$rightIndex"))
            composeRule.onNodeWithTag("matching-right-assign-$rightIndex").performClick()
            composeRule.waitForIdle()
        }

        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-submit"))
        composeRule.onNodeWithTag("matching-submit").assertIsEnabled().performClick()

        assertEquals("尺子=长度,秤=重量,日历=日期,温度计=温度", submittedAnswer)
    }
}
