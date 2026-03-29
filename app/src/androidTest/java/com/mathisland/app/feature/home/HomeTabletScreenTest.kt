package com.mathisland.app.feature.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.domain.usecase.HomeState
import org.junit.Rule
import org.junit.Test

class HomeTabletScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun homeScreen_showsReviewRecommendationAndPrimaryActions() {
        composeRule.setContent {
            MathIslandTheme {
                HomeTabletScreen(
                    state = HomeState(
                        totalStars = 8,
                        stickerCount = 2,
                        nextLessonId = "review-calculation",
                        nextLessonTitle = "小海鸥求助",
                        nextLessonFocus = "计算复习",
                        nextLessonSummary = "先完成 2 道同类型复习题，再回主线继续推进。",
                        isReview = true
                    ),
                    onContinue = {},
                    onOpenMap = {},
                    onOpenChest = {},
                    onOpenParent = {}
                )
            }
        }

        composeRule.onNodeWithText("数学岛").assertIsDisplayed()
        composeRule.onNodeWithText("小海鸥求助").assertIsDisplayed()
        composeRule.onNodeWithText("8").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithTag("home-continue-adventure").assertIsDisplayed()
        composeRule.onNodeWithTag("home-open-map").assertIsDisplayed()
        composeRule.onNodeWithTag("home-open-chest").assertIsDisplayed()
        composeRule.onNodeWithTag("home-open-parent").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsStandardAdventureCopyAndPrimaryActions() {
        composeRule.setContent {
            MathIslandTheme {
                HomeTabletScreen(
                    state = HomeState(
                        totalStars = 12,
                        stickerCount = 4,
                        nextLessonId = "start-calc-carry-01",
                        nextLessonTitle = "继续冒险",
                        nextLessonFocus = "计算闯关",
                        nextLessonSummary = "从当前解锁的第一节继续出发。",
                        isReview = false
                    ),
                    onContinue = {},
                    onOpenMap = {},
                    onOpenChest = {},
                    onOpenParent = {}
                )
            }
        }

        composeRule.onNodeWithText("数学岛").assertIsDisplayed()
        composeRule.onNodeWithText("开始闯关").assertIsDisplayed()
        composeRule.onNodeWithTag("home-continue-adventure").assertIsDisplayed()
        composeRule.onNodeWithTag("home-open-map").assertIsDisplayed()
        composeRule.onNodeWithTag("home-open-chest").assertIsDisplayed()
        composeRule.onNodeWithTag("home-open-parent").assertIsDisplayed()
    }
}
