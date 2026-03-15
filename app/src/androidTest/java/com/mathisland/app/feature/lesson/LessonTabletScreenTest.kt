package com.mathisland.app.feature.lesson

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
import com.mathisland.app.Lesson
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.Question
import org.junit.Rule
import org.junit.Test

class LessonTabletScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun lessonScreen_showsQuestionProgressAndTimerNote() {
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
                LessonTabletScreen(
                    state = LessonUiState(
                        lesson = lesson,
                        question = lesson.questions.first(),
                        questionIndex = 0,
                        totalQuestions = 3,
                        totalStars = 12
                    ),
                    onQuit = {},
                    answerPane = {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .testTag("lesson-answer-pane")
                        )
                    }
                )
            }
        }

        composeRule.onNodeWithText("海图冲刺赛").assertIsDisplayed()
        composeRule.onNodeWithText("第 1 / 3 题").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-timer").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-timer-note").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-answer-pane").assertIsDisplayed()
    }
}
