package com.mathisland.app.feature.level

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
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

class MultiStepQuestionPaneTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun multiStepQuestion_advancesAndSubmitsInOrder() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "按步骤完成平均分。",
            choices = emptyList(),
            correctChoice = "平均分给 3 只小猴,每只 4 个",
            hint = "先想平均分，再判断每份有几个。",
            family = "multi-step",
            stepPrompts = listOf(
                "第一步：先判断这题要怎么分？",
                "第二步：每只小猴分到几个？"
            ),
            stepChoices = listOf(
                listOf("平均分给 3 只小猴", "先把 12 和 3 相加", "先比较水果颜色"),
                listOf("每只 3 个", "每只 4 个", "每只 5 个")
            )
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { submittedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("renderer-multi-step").assertIsDisplayed()
        composeRule.onNodeWithTag("multi-step-submit").assertIsNotEnabled()
        composeRule.onNodeWithTag("multi-step-prompt").assertIsDisplayed()

        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-choice-0"))
        composeRule.onNodeWithTag("multi-step-choice-0").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasText("第二步：每只小猴分到几个？"))
        composeRule.onNodeWithText("第二步：每只小猴分到几个？").assertIsDisplayed()

        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-choice-1"))
        composeRule.onNodeWithTag("multi-step-choice-1").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-submit"))
        composeRule.onNodeWithTag("multi-step-submit").assertIsEnabled().performClick()

        assertEquals("平均分给 3 只小猴,每只 4 个", submittedAnswer)
    }

    @Test
    fun multiStepQuestion_supportsThreeStepSequences() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "按步骤完成装袋判断。",
            choices = emptyList(),
            correctChoice = "先算 17 ÷ 3,商是 5 余 2,6 个袋子",
            hint = "先算除法，再判断余下的要不要多准备一个袋子。",
            family = "multi-step",
            stepPrompts = listOf(
                "第一步：先要算什么？",
                "第二步：17 ÷ 3 的结果是什么？",
                "第三步：至少需要几个袋子？"
            ),
            stepChoices = listOf(
                listOf("先算 17 ÷ 3", "先算 17 + 3", "先比较袋子颜色"),
                listOf("商是 4 余 1", "商是 5 余 2", "商是 6 余 1"),
                listOf("5 个袋子", "6 个袋子", "7 个袋子")
            )
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { submittedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("renderer-multi-step").assertIsDisplayed()
        composeRule.onNodeWithTag("multi-step-submit").assertIsNotEnabled()

        listOf(0, 1, 1).forEachIndexed { index, choiceIndex ->
            composeRule.onNodeWithTag("renderer-multi-step")
                .performScrollToNode(hasTestTag("multi-step-choice-$choiceIndex"))
            composeRule.onNodeWithTag("multi-step-choice-$choiceIndex").performClick()
            composeRule.waitForIdle()
            if (index == 0) {
                composeRule.onNodeWithText("第二步：17 ÷ 3 的结果是什么？").assertIsDisplayed()
            }
            if (index == 1) {
                composeRule.onNodeWithText("第三步：至少需要几个袋子？").assertIsDisplayed()
            }
        }

        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-submit"))
        composeRule.onNodeWithTag("multi-step-submit").assertIsEnabled().performClick()

        assertEquals("先算 17 ÷ 3,商是 5 余 2,6 个袋子", submittedAnswer)
    }
}
