package com.mathisland.app.feature.level

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.domain.model.Question
import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState
import com.mathisland.app.feature.level.renderers.rendererActionStateFor
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
        composeRule.onNodeWithTag("renderer-number-pad")
            .performScrollToNode(hasTestTag("number-pad-submit"))
        composeRule.onNodeWithTag("number-pad-key-8").performClick()
        composeRule.onNodeWithTag("number-pad-key-1").performClick()
        composeRule.onNodeWithTag("number-pad-submit").performClick()

        assertEquals("81", selectedAnswer)
    }

    @Test
    fun challengeQuestion_usesSharedGuidanceCards() {
        val question = Question(
            prompt = "9 x 9 = ?",
            choices = listOf("81", "72", "99"),
            correctChoice = "81",
            hint = "想想 9 个 9。",
            family = "challenge"
        )
        val feedback = AnswerFeedbackUiState(
            kind = AnswerFeedbackKind.Incorrect,
            title = "再试一次",
            body = "先看提示，再判断一次。",
            submittedAnswer = "72"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    feedback = feedback,
                    actionState = rendererActionStateFor(
                        feedback = feedback,
                        inputEnabled = true
                    ),
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-number-pad").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-prompt-card").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-prompt-chip").assertIsDisplayed()
        composeRule.onAllNodesWithTag("renderer-guidance-card").assertCountEquals(1)
        composeRule.onAllNodesWithTag("renderer-guidance-chip").assertCountEquals(1)
        composeRule.onNodeWithTag("renderer-action-header").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-action-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("answer-feedback-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("number-pad-status").assertIsDisplayed()
    }

    @Test
    fun challengeQuestion_showsTimeoutExpiredNumberPadState() {
        val question = Question(
            prompt = "9 x 9 = ?",
            choices = listOf("81", "72", "99"),
            correctChoice = "81",
            hint = "想想 9 个 9。",
            family = "challenge"
        )
        val feedback = AnswerFeedbackUiState(
            kind = AnswerFeedbackKind.TimeoutExpired,
            title = "已超时",
            body = "本轮冲刺已经结束，这题按当前结果结算。",
            submittedAnswer = "72"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    feedback = feedback,
                    actionState = rendererActionStateFor(
                        feedback = feedback,
                        inputEnabled = false
                    ),
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-number-pad").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-prompt-card").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-prompt-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-action-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("answer-feedback-title").assertIsDisplayed()
        composeRule.onNodeWithTag("answer-feedback-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("number-pad-status").assertIsDisplayed()
        composeRule.onNodeWithTag("number-pad-tone-chip").assertIsDisplayed()
        composeRule.onNodeWithText("本题已超时，直接看下一题。").assertIsDisplayed()
    }

    @Test
    fun calculationQuestion_showsTimeoutExpiredChoiceState() {
        val question = Question(
            prompt = "26 + 18 = ?",
            choices = listOf("44", "45", "34"),
            correctChoice = "44",
            hint = "先算个位，再算十位。",
            family = "calculation"
        )
        val feedback = AnswerFeedbackUiState(
            kind = AnswerFeedbackKind.TimeoutExpired,
            title = "已超时",
            body = "本轮冲刺已经结束，这题按当前结果结算。",
            submittedAnswer = "45"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    feedback = feedback,
                    actionState = rendererActionStateFor(
                        feedback = feedback,
                        inputEnabled = false
                    ),
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-choice").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-prompt-card").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-prompt-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-choice")
            .performScrollToNode(hasTestTag("answer-state-45"))
        composeRule.onNodeWithTag("renderer-action-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("answer-state-45").assertIsDisplayed()
        composeRule.onNodeWithTag("answer-state-chip-45").assertIsDisplayed()
        composeRule.onNodeWithText("这次尝试超时").assertIsDisplayed()
        composeRule.onNodeWithTag("answer-feedback-title").assertIsDisplayed()
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
        composeRule.onNodeWithText("先看刻度，再选答案。").assertIsDisplayed()
    }

    @Test
    fun multiplicationQuestion_showsChantAffordance() {
        val question = Question(
            prompt = "7 x 8 = ?",
            choices = listOf("54", "56", "64"),
            correctChoice = "56",
            hint = "先念口诀。",
            family = "multiplication"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-chant").assertIsDisplayed()
        composeRule.onNodeWithTag("chant-beat-strip").assertIsDisplayed()
        composeRule.onNodeWithText("先念口诀，再选答案。").assertIsDisplayed()
    }

    @Test
    fun divisionQuestion_showsGroupingAffordance() {
        val question = Question(
            prompt = "12 个海星平均放进 3 个篮子，每个篮子几个？",
            choices = listOf("3", "4", "5"),
            correctChoice = "4",
            hint = "先分组。",
            family = "division"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-group").assertIsDisplayed()
        composeRule.onNodeWithTag("group-basket-zone").assertIsDisplayed()
        composeRule.onNodeWithText("先分组，再选答案。").assertIsDisplayed()
    }

    @Test
    fun bigNumberQuestion_showsSortingAffordance() {
        val question = Question(
            prompt = "把数字按从小到大排好。",
            choices = listOf("208, 280, 820", "280, 208, 820", "820, 280, 208"),
            correctChoice = "208, 280, 820",
            hint = "先比较百位。",
            family = "big-number"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-sort").assertIsDisplayed()
        composeRule.onNodeWithTag("sort-signal-lights").assertIsDisplayed()
        composeRule.onNodeWithText("先比较顺序，再选答案。").assertIsDisplayed()
    }
}
