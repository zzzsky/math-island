package com.mathisland.app.feature.parent

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import com.mathisland.app.MathIslandTheme
import org.junit.Rule
import org.junit.Test

class ParentSummaryTabletScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun parentSummaryScreen_showsAllSummaryCards() {
        composeRule.setContent {
            MathIslandTheme {
                ParentSummaryTabletScreen(
                    state = ParentSummaryUiState(
                        todayLearnedText = "修桥加减法",
                        weakTopicsText = "测量与图形",
                        streakText = "3 天",
                        recommendedIslandText = "测量与图形岛"
                    ),
                    onBackHome = {}
                )
            }
        }

        composeRule.onNodeWithTag("parent-summary-screen").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-hero-section").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-primary-summary-section").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-supporting-details-section").assertIsDisplayed()
        composeRule.onNodeWithText("家长学习摘要").assertIsDisplayed()
        composeRule.onNodeWithText("修桥加减法").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-today-stat").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-streak-stat").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-weak-stat").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-primary-card").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-scroll")
            .performScrollToNode(hasTestTag("parent-summary-weak-card"))
        composeRule.onNodeWithTag("parent-summary-weak-card").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-streak-card").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-scroll")
            .performScrollToNode(hasTestTag("parent-summary-next-action-section"))
        composeRule.onNodeWithTag("parent-summary-next-action-section").assertIsDisplayed()
        composeRule.onNodeWithText("测量与图形岛").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-next-action-card").assertIsDisplayed()
    }

    @Test
    fun parentGateScreen_showsQuestionAndAnswerChoices() {
        composeRule.setContent {
            MathIslandTheme {
                ParentGateScreen(
                    state = ParentGateViewModel.uiState(),
                    onAnswer = {},
                    onBackHome = {}
                )
            }
        }

        composeRule.onNodeWithTag("parent-gate-panel").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-answer-row").assertIsDisplayed()
        composeRule.onNodeWithText("请先完成一道口算").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-answer-15").assertIsDisplayed()
    }
}
