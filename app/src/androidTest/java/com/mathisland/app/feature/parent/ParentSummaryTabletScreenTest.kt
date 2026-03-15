package com.mathisland.app.feature.parent

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.ParentSummary
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
                    summary = ParentSummary(
                        todayLearned = listOf("修桥加减法"),
                        weakTopics = listOf("测量与图形"),
                        streakDays = 3,
                        recommendedIsland = "测量与图形岛"
                    ),
                    onBackHome = {}
                )
            }
        }

        composeRule.onNodeWithText("家长学习摘要").assertIsDisplayed()
        composeRule.onNodeWithText("修桥加减法").assertIsDisplayed()
        composeRule.onNodeWithText("测量与图形").assertIsDisplayed()
        composeRule.onNodeWithText("3 天").assertIsDisplayed()
        composeRule.onNodeWithText("测量与图形岛").assertIsDisplayed()
    }

    @Test
    fun parentGateScreen_showsQuestionAndAnswerChoices() {
        composeRule.setContent {
            MathIslandTheme {
                ParentGateScreen(
                    onAnswer = {},
                    onBackHome = {}
                )
            }
        }

        composeRule.onNodeWithText("请先完成一道口算").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-answer-15").assertIsDisplayed()
    }
}
