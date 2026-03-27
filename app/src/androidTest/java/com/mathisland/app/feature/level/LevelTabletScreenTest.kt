package com.mathisland.app.feature.level

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
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
        composeRule.onNodeWithTag("lesson-route-summary").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-attempt-status").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-next-step-card").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-support-rail")
            .performScrollToNode(hasTestTag("lesson-timer-status"))
        composeRule.onNodeWithTag("lesson-timer-status").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-support-rail")
            .performScrollToNode(hasTestTag("lesson-question-card"))
        composeRule.onNodeWithTag("lesson-question-card").assertIsDisplayed()
        composeRule.onNodeWithTag("level-answer-pane").assertIsDisplayed()
    }

    @Test
    fun levelScreen_hidesTimerArtifactsForUntimedLesson() {
        val lesson = Lesson(
            id = "calculation-01",
            islandId = "calculation-island",
            title = "海湾加法课",
            focus = "加法",
            summary = "2 题热身",
            questions = listOf(
                Question(
                    prompt = "26 + 18 = ?",
                    choices = listOf("44", "45", "34"),
                    correctChoice = "44",
                    hint = "先算个位，再算十位。",
                    family = "calculation"
                )
            )
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelTabletScreen(
                    state = LevelUiState(
                        lesson = lesson,
                        question = lesson.questions.first(),
                        questionIndex = 0,
                        totalQuestions = 2,
                        totalStars = 8,
                        flowHint = "完成本节后会先结算星星，再回地图继续探索。"
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

        composeRule.onNodeWithTag("lesson-route-summary").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-attempt-status").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-next-step-card").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-support-rail")
            .performScrollToNode(hasTestTag("lesson-question-card"))
        composeRule.onNodeWithTag("lesson-question-card").assertIsDisplayed()
        composeRule.onAllNodesWithTag("lesson-timer").assertCountEquals(0)
        composeRule.onAllNodesWithTag("lesson-timer-note").assertCountEquals(0)
        composeRule.onAllNodesWithTag("lesson-timer-status").assertCountEquals(0)
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
