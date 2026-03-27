package com.mathisland.app.feature.level

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.domain.model.Question
import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState
import org.junit.Rule
import org.junit.Test

class LevelTabletScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun levelScreen_wrapsLessonSurfaceWithoutChangingContract() {
        val lesson = Lesson(
            id = "challenge-sprint-01",
            islandId = "challenge-island",
            title = "海图冲刺赛",
            focus = "综合挑战",
            summary = "3 题限时冲刺",
            questions = listOf(
                Question(
                    prompt = "9 x 9 = ?",
                    choices = listOf("81", "72", "99"),
                    correctChoice = "81",
                    hint = "想想 9 个 9。",
                    family = "challenge"
                )
            ),
            timeLimitSeconds = 8
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelTabletScreen(
                    state = LevelUiState(
                        lesson = lesson,
                        question = lesson.questions.first(),
                        questionIndex = 0,
                        totalQuestions = 3,
                        totalStars = 12,
                        flowHint = "冲刺结束后会显示评级，并决定是否优先进入错题回放。"
                    ),
                    onQuit = {},
                    answerPane = {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .testTag("level-answer-pane")
                        )
                    }
                )
            }
        }

        composeRule.onNodeWithText("海图冲刺赛").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-timer").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-timer-note").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-flow-hint").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-attempt-status").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-timer-status").assertIsDisplayed()
        composeRule.onNodeWithTag("level-answer-pane").assertIsDisplayed()
    }

    @Test
    fun levelScreen_showsTimeoutExpiredStateInStatusCard() {
        val lesson = Lesson(
            id = "challenge-sprint-01",
            islandId = "challenge-island",
            title = "海图冲刺赛",
            focus = "综合挑战",
            summary = "3 题限时冲刺",
            questions = listOf(
                Question(
                    prompt = "9 x 9 = ?",
                    choices = listOf("81", "72", "99"),
                    correctChoice = "81",
                    hint = "想想 9 个 9。",
                    family = "challenge"
                )
            ),
            timeLimitSeconds = 8
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelTabletScreen(
                    state = LevelUiState(
                        lesson = lesson,
                        question = lesson.questions.first(),
                        questionIndex = 0,
                        totalQuestions = 3,
                        totalStars = 12,
                        flowHint = "冲刺结束后会显示评级，并决定是否优先进入错题回放。",
                        initialFeedback = AnswerFeedbackUiState(
                            kind = AnswerFeedbackKind.TimeoutExpired,
                            title = "本题已超时",
                            body = "直接看下一题。",
                            submittedAnswer = "72"
                        )
                    ),
                    onQuit = {},
                    answerPane = {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .testTag("level-answer-pane")
                        )
                    }
                )
            }
        }

        composeRule.onNodeWithTag("lesson-attempt-status").assertIsDisplayed()
        composeRule.onNodeWithText("本题已超时").assertIsDisplayed()
        composeRule.onNodeWithText("直接看下一题。").assertIsDisplayed()
    }
}
