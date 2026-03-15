package com.mathisland.app.feature.reward

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.RewardSummary
import org.junit.Rule
import org.junit.Test

class RewardTabletScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun rewardScreen_showsTimedOutSprintGradeAndRetryAction() {
        composeRule.setContent {
            MathIslandTheme {
                RewardTabletScreen(
                    state = RewardUiState(
                        reward = RewardSummary(
                            lessonTitle = "海图冲刺赛",
                            starsEarned = 0,
                            correctAnswers = 1,
                            totalQuestions = 3,
                            newIslandTitle = null,
                            newStickerName = null,
                            timedOut = true,
                            gradeLabel = "整备评级",
                            gradeDescription = "本轮已命中 1/3 题，先回放再发起下一次冲刺。",
                            secondaryActionLabel = "再试冲刺",
                            secondaryActionLessonId = "challenge-sprint-01"
                        ),
                        totalStars = 12
                    ),
                    onContinue = {},
                    onSecondaryAction = {}
                )
            }
        }

        composeRule.onNodeWithText("时间到").assertIsDisplayed()
        composeRule.onNodeWithText("时间到，本次冲刺记为练习").assertIsDisplayed()
        composeRule.onNodeWithText("整备评级").assertIsDisplayed()
        composeRule.onNodeWithText("12").assertIsDisplayed()
        composeRule.onNodeWithTag("reward-retry-sprint").assertIsDisplayed()
        composeRule.onNodeWithTag("reward-return-map").assertIsDisplayed()
    }
}
