package com.mathisland.app.feature.level

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.domain.model.Question
import com.mathisland.app.domain.model.StepPresentation
import com.mathisland.app.domain.model.StepBranchRule
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
        waitForText("第二步：每只小猴分到几个？")
        composeRule.onNodeWithText("第二步：每只小猴分到几个？").assertIsDisplayed()

        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-choice-1"))
        composeRule.onNodeWithTag("multi-step-choice-1").performClick()
        waitForTagToDisappear("multi-step-stage-confirming")
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
            if (index == 0) {
                waitForText("第二步：17 ÷ 3 的结果是什么？")
                composeRule.onNodeWithText("第二步：17 ÷ 3 的结果是什么？").assertIsDisplayed()
            }
            if (index == 1) {
                waitForText("第三步：至少需要几个袋子？")
                composeRule.onNodeWithText("第三步：至少需要几个袋子？").assertIsDisplayed()
            }
        }

        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-submit"))
        composeRule.onNodeWithTag("multi-step-submit").assertIsEnabled().performClick()

        assertEquals("先算 17 ÷ 3,商是 5 余 2,6 个袋子", submittedAnswer)
    }

    @Test
    fun multiStepQuestion_switchesPromptByConditionalBranch() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "按步骤判断装袋方案。",
            choices = emptyList(),
            correctChoice = "有余数,商是4余2,5个袋子",
            hint = "先看有没有余数。",
            family = "multi-step",
            stepPrompts = listOf("第一步：先判断会不会有剩余？", "第二步", "第三步"),
            stepChoices = listOf(
                listOf("有余数", "正好分完", "还要先做加法"),
                listOf("占位"),
                listOf("占位")
            ),
            stepBranchKeys = listOf("branch-start", "step-2", "step-3"),
            stepBranchRules = mapOf(
                "branch-start" to listOf(
                    StepBranchRule("有余数", "remainder-step-2"),
                    StepBranchRule("正好分完", "exact-step-2"),
                    StepBranchRule("还要先做加法", "add-step-2")
                ),
                "remainder-step-2" to listOf(StepBranchRule("*", "remainder-step-3")),
                "exact-step-2" to listOf(StepBranchRule("*", "exact-step-3")),
                "add-step-2" to listOf(StepBranchRule("*", "add-step-3"))
            ),
            stepBranchPrompts = mapOf(
                "remainder-step-2" to "第二步：18 ÷ 4 的结果是什么？",
                "remainder-step-3" to "第三步：至少需要几个袋子？",
                "exact-step-2" to "第二步：如果正好分完，每袋装几个？",
                "exact-step-3" to "第三步：这种情况至少需要几个袋子？",
                "add-step-2" to "第二步：这题先算加法对吗？",
                "add-step-3" to "第三步：现在该回到除法判断。"
            ),
            stepBranchChoices = mapOf(
                "remainder-step-2" to listOf("商是4余2", "商是5余1"),
                "remainder-step-3" to listOf("4个袋子", "5个袋子", "6个袋子"),
                "exact-step-2" to listOf("每袋4个", "每袋5个"),
                "exact-step-3" to listOf("4个袋子", "5个袋子"),
                "add-step-2" to listOf("不对，要先做除法", "对，先做加法"),
                "add-step-3" to listOf("回到除法判断", "直接提交")
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
        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-choice-0"))
        composeRule.onNodeWithTag("multi-step-choice-0").performClick()
        waitForText("第二步：18 ÷ 4 的结果是什么？")
        composeRule.onNodeWithText("第二步：18 ÷ 4 的结果是什么？").assertIsDisplayed()

        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-choice-0"))
        composeRule.onNodeWithTag("multi-step-choice-0").performClick()
        waitForText("第三步：至少需要几个袋子？")
        composeRule.onNodeWithText("第三步：至少需要几个袋子？").assertIsDisplayed()

        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-choice-1"))
        composeRule.onNodeWithTag("multi-step-choice-1").performClick()
        waitForTagToDisappear("multi-step-stage-confirming")
        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-submit"))
        composeRule.onNodeWithTag("multi-step-submit").assertIsEnabled().performClick()

        assertEquals("有余数,商是4余2,5个袋子", submittedAnswer)
    }

    @Test
    fun multiStepQuestion_convergesDifferentBranchesIntoSharedFinalPrompt() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "按条件步骤完成装盒判断。",
            choices = emptyList(),
            correctChoice = "正好分完,商是4,4个盒子",
            hint = "先判断有没有余数，再走对应步骤，最后收束到同一个判断。",
            family = "multi-step",
            stepPrompts = listOf("第一步：先判断会不会有剩余？", "第二步", "第三步"),
            stepChoices = listOf(
                listOf("有余数", "正好分完"),
                listOf("占位"),
                listOf("占位")
            ),
            stepBranchKeys = listOf("branch-start", "step-2", "step-3"),
            stepBranchRules = mapOf(
                "branch-start" to listOf(
                    StepBranchRule("有余数", "remainder-step-2"),
                    StepBranchRule("正好分完", "exact-step-2")
                ),
                "remainder-step-2" to listOf(StepBranchRule("*", "shared-final-step")),
                "exact-step-2" to listOf(StepBranchRule("*", "shared-final-step"))
            ),
            stepBranchPrompts = mapOf(
                "remainder-step-2" to "第二步：14 ÷ 3 的结果是什么？",
                "exact-step-2" to "第二步：12 ÷ 3 的结果是什么？",
                "shared-final-step" to "第三步：现在至少要准备几个盒子？"
            ),
            stepBranchChoices = mapOf(
                "remainder-step-2" to listOf("商是4余2", "商是5余1"),
                "exact-step-2" to listOf("商是4", "商是3"),
                "shared-final-step" to listOf("4个盒子", "5个盒子", "6个盒子")
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
        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-choice-1"))
        composeRule.onNodeWithTag("multi-step-choice-1").performClick()
        waitForText("第二步：12 ÷ 3 的结果是什么？")
        composeRule.onNodeWithText("第二步：12 ÷ 3 的结果是什么？").assertIsDisplayed()

        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-choice-0"))
        composeRule.onNodeWithTag("multi-step-choice-0").performClick()
        waitForText("第三步：现在至少要准备几个盒子？")
        composeRule.onNodeWithText("第三步：现在至少要准备几个盒子？").assertIsDisplayed()

        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-choice-0"))
        composeRule.onNodeWithTag("multi-step-choice-0").performClick()
        waitForTagToDisappear("multi-step-stage-confirming")
        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-submit"))
        composeRule.onNodeWithTag("multi-step-submit").assertIsEnabled().performClick()

        assertEquals("正好分完,商是4,4个盒子", submittedAnswer)
    }

    @Test
    fun multiStepQuestion_usesPresentationMetadataForCurrentStageAndSummary() {
        var submittedAnswer: String? = null
        val question = Question(
            prompt = "按条件步骤完成装盒复核。",
            choices = emptyList(),
            correctChoice = "正好分完,商是4,4个盒子,正好装完，不用多准备",
            hint = "按步骤判断，再用统一结论收尾。",
            family = "multi-step",
            stepPrompts = listOf("第一步", "第二步", "第三步", "第四步"),
            stepChoices = listOf(
                listOf("有余数", "正好分完"),
                listOf("占位"),
                listOf("4个盒子", "5个盒子"),
                listOf("正好装完，不用多准备", "有余数，要多准备1个盒子")
            ),
            stepBranchKeys = listOf("branch-start", "step-2", "shared-final-step", "shared-wrap-up-step"),
            stepBranchRules = mapOf(
                "branch-start" to listOf(
                    StepBranchRule("有余数", "remainder-step-2"),
                    StepBranchRule("正好分完", "exact-step-2")
                ),
                "remainder-step-2" to listOf(StepBranchRule("*", "shared-final-step")),
                "exact-step-2" to listOf(StepBranchRule("*", "shared-final-step")),
                "shared-final-step" to listOf(StepBranchRule("*", "shared-wrap-up-step"))
            ),
            stepBranchPrompts = mapOf(
                "remainder-step-2" to "第二步：12 ÷ 5 的结果是什么？",
                "exact-step-2" to "第二步：12 ÷ 3 的结果是什么？",
                "shared-final-step" to "第三步：现在至少要准备几个盒子？",
                "shared-wrap-up-step" to "第四步：最后该怎么复述？"
            ),
            stepBranchChoices = mapOf(
                "remainder-step-2" to listOf("商是2余2", "商是4余1"),
                "exact-step-2" to listOf("商是4", "商是3"),
                "shared-final-step" to listOf("4个盒子", "5个盒子"),
                "shared-wrap-up-step" to listOf("正好装完，不用多准备", "有余数，要多准备1个盒子")
            ),
            stepPresentations = listOf(
                StepPresentation("先定路线", "先判断这次平均分会不会有剩余。", "分支判断"),
                StepPresentation("计算结果", "把除法结果说清楚。", "除法结果"),
                StepPresentation("统一装盒", "不管哪条路，最后都要回到装盒数量。", "装盒数量"),
                StepPresentation("完整结论", "把你的判断完整说出来。", "最终结论")
            ),
            stepBranchPresentations = mapOf(
                "remainder-step-2" to StepPresentation("余数路线", "先说出商和余数。", "余数结果"),
                "exact-step-2" to StepPresentation("整除路线", "直接说出整除后的商。", "整除结果"),
                "shared-final-step" to StepPresentation("统一装盒", "现在两条路线都回到同一个装盒判断。", "装盒数量")
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

        composeRule.onNodeWithTag("multi-step-stage-title").assertIsDisplayed()
        composeRule.onNodeWithText("先定路线").assertIsDisplayed()
        composeRule.onNodeWithText("先判断这次平均分会不会有剩余。").assertIsDisplayed()

        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-choice-1"))
        composeRule.onNodeWithTag("multi-step-choice-1").performClick()
        waitForText("整除路线")
        composeRule.onNodeWithText("整除路线").assertIsDisplayed()
        composeRule.onNodeWithText("直接说出整除后的商。").assertIsDisplayed()

        listOf(0, 0, 0).forEach { choiceIndex ->
            composeRule.onNodeWithTag("renderer-multi-step")
                .performScrollToNode(hasTestTag("multi-step-choice-$choiceIndex"))
            composeRule.onNodeWithTag("multi-step-choice-$choiceIndex").performClick()
            waitForTagToDisappear("multi-step-stage-confirming")
        }

        composeRule.onNodeWithText("分支判断: 正好分完").assertIsDisplayed()
        composeRule.onNodeWithText("整除结果: 商是4").assertIsDisplayed()
        composeRule.onNodeWithText("装盒数量: 4个盒子").assertIsDisplayed()
        composeRule.onNodeWithText("最终结论: 正好装完，不用多准备").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-submit"))
        composeRule.onNodeWithTag("multi-step-submit").assertIsEnabled().performClick()

        assertEquals("正好分完,商是4,4个盒子,正好装完，不用多准备", submittedAnswer)
    }

    @Test
    fun multiStepQuestion_showsConfirmationStateBeforeAdvancing() {
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
                listOf("平均分给 3 只小猴", "先把 12 和 3 相加"),
                listOf("每只 3 个", "每只 4 个")
            )
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-choice-0"))
        composeRule.onNodeWithTag("multi-step-choice-0").performClick()

        composeRule.onNodeWithTag("multi-step-confirmed-chip-0").assertIsDisplayed()
        composeRule.onNodeWithTag("multi-step-stage-confirming").assertIsDisplayed()
        composeRule.onNodeWithTag("multi-step-progress-chip-0").assertIsDisplayed()

        waitForText("第二步：每只小猴分到几个？")
        composeRule.onNodeWithText("第二步：每只小猴分到几个？").assertIsDisplayed()
    }

    private fun waitForText(text: String) {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun waitForTagToDisappear(tag: String) {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag(tag).fetchSemanticsNodes().isEmpty()
        }
    }
}
