package com.mathisland.app.feature.level

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
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

    @Test
    fun fillBlankQuestion_supportsThreeSlots() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "把长度换算结果补完整。",
            choices = emptyList(),
            correctChoice = "400,90,6",
            hint = "先统一单位，再把数字拖进空格。",
            family = "fill-blank",
            blankParts = listOf("4 米 = ", " 厘米，9 分米 = ", " 厘米，60 厘米 = ", " 分米。"),
            blankOptions = listOf("90", "6", "400", "600")
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

        listOf(2 to 0, 0 to 1, 1 to 2).forEach { (optionIndex, slotIndex) ->
            composeRule.onNodeWithTag("renderer-fill-blank")
                .performScrollToNode(hasTestTag("fill-blank-option-select-$optionIndex"))
            composeRule.onNodeWithTag("fill-blank-option-select-$optionIndex").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("renderer-fill-blank")
                .performScrollToNode(hasTestTag("fill-blank-slot-$slotIndex"))
            composeRule.onNodeWithTag("fill-blank-slot-action-$slotIndex").performClick()
            composeRule.waitForIdle()
        }

        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-submit"))
        composeRule.onNodeWithTag("fill-blank-submit").assertIsEnabled().performClick()

        assertEquals("400,90,6", submittedAnswer)
    }

    @Test
    fun fillBlankQuestion_showsMixedSlotKinds() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "把长度换算补完整。",
            choices = emptyList(),
            correctChoice = "300,米,90",
            hint = "先看每个空格需要填数字还是单位。",
            family = "fill-blank",
            blankParts = listOf("3 ", " = ", "00 厘米，9 分米 = ", " 厘米。"),
            blankOptions = listOf("米", "90", "300", "厘米"),
            blankSlotKinds = listOf("number", "unit", "number")
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
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-slot-kind-0"))
        composeRule.onNodeWithTag("fill-blank-slot-kind-0").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-slot-kind-1"))
        composeRule.onNodeWithTag("fill-blank-slot-kind-1").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-slot-kind-2"))
        composeRule.onNodeWithTag("fill-blank-slot-kind-2").assertIsDisplayed()

        listOf(2 to 0, 0 to 1, 1 to 2).forEach { (optionIndex, slotIndex) ->
            composeRule.onNodeWithTag("renderer-fill-blank")
                .performScrollToNode(hasTestTag("fill-blank-option-select-$optionIndex"))
            composeRule.onNodeWithTag("fill-blank-option-select-$optionIndex").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("renderer-fill-blank")
                .performScrollToNode(hasTestTag("fill-blank-slot-$slotIndex"))
            composeRule.onNodeWithTag("fill-blank-slot-action-$slotIndex").performClick()
            composeRule.waitForIdle()
        }

        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-submit"))
        composeRule.onNodeWithTag("fill-blank-submit").assertIsEnabled().performClick()

        assertEquals("300,米,90", submittedAnswer)
    }

    @Test
    fun fillBlankQuestion_showsPartitionedPoolsAndSupportsSlotFirstFlow() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "按分区选项池把长度换算补完整。",
            choices = emptyList(),
            correctChoice = "米,300,分米,70",
            hint = "先看空格要填数字还是单位，再去对应分区找答案。",
            family = "fill-blank",
            blankParts = listOf("3 ", " = ", " 厘米，7 ", " = ", " 厘米。"),
            blankOptions = listOf("分米", "70", "米", "300", "厘米"),
            blankSlotKinds = listOf("unit", "number", "unit", "number")
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { submittedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-pool-number"))
        composeRule.onNodeWithTag("fill-blank-pool-number").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-pool-unit"))
        composeRule.onNodeWithTag("fill-blank-pool-unit").assertIsDisplayed()

        listOf(0 to 2, 1 to 3, 2 to 0, 3 to 1).forEach { (slotIndex, optionIndex) ->
            composeRule.onNodeWithTag("renderer-fill-blank")
                .performScrollToNode(hasTestTag("fill-blank-slot-action-$slotIndex"))
            composeRule.onNodeWithTag("fill-blank-slot-action-$slotIndex").performClick()
            composeRule.waitForIdle()
            composeRule.onNodeWithTag("renderer-fill-blank")
                .performScrollToNode(hasTestTag("fill-blank-option-select-$optionIndex"))
            composeRule.onNodeWithTag("fill-blank-option-select-$optionIndex").performClick()
            composeRule.waitForIdle()
        }

        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-submit"))
        composeRule.onNodeWithTag("fill-blank-submit").assertIsEnabled().performClick()

        assertEquals("米,300,分米,70", submittedAnswer)
    }
}
