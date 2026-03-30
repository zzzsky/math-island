package com.mathisland.app.feature.level

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.domain.model.RewardSummary
import org.junit.Rule
import org.junit.Test

class RewardOverlayTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun rewardOverlay_wrapsRewardSurfaceWithoutChangingContract() {
        composeRule.setContent {
            MathIslandTheme {
                RewardOverlay(
                    state = RewardOverlayUiState(
                        reward = RewardSummary(
                            lessonTitle = "海图冲刺赛",
                            starsEarned = 0,
                            correctAnswers = 1,
                            totalQuestions = 3,
                            newIslandId = null,
                            newIslandTitle = null,
                            newStickerName = null,
                            timedOut = true,
                            gradeLabel = "整备评级",
                            gradeDescription = "本轮已命中 1/3 题，先回放再发起下一次冲刺。",
                            secondaryActionLabel = "再试冲刺",
                            secondaryActionLessonId = "challenge-sprint-01"
                        ),
                        totalStars = 12,
                        continueLabel = "回地图看回放",
                        nextStepLabel = "回地图后",
                        nextStepTitle = "下一步先回放再冲刺",
                        nextStepBody = "先回地图查看错题回放站，再决定是否立刻再试一次冲刺。",
                        nextStepDetailTitle = "先回放，再决定是否重试",
                        nextStepDetailBody = "地图会先落到回放路线，右侧面板会优先给出复习或回放课程。",
                        nextActionLabel = "优先动作",
                        nextActionTitle = "先做回放",
                        nextActionBody = "先完成回放或复习，再决定是否重新发起下一次冲刺。"
                    ),
                    onContinue = {},
                    onSecondaryAction = {}
                )
            }
        }

        composeRule.onNodeWithText("本次表现").assertIsDisplayed()
        composeRule.onNodeWithText("整备评级").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("继续航线").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("reward-return-kind-pill").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("reward-next-step-kind-pill").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("reward-next-step-card").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("reward-next-step-detail-card").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("reward-next-action-card").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("reward-next-action-pill").performScrollTo().assertIsDisplayed()
        composeRule.onAllNodesWithTag("reward-return-map").assertCountEquals(1)
    }
}
