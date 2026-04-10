package com.mathisland.app.feature.level

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.domain.model.MatchingGroup
import com.mathisland.app.domain.model.MatchingRound
import com.mathisland.app.domain.model.Question
import com.mathisland.app.domain.model.StepPresentation
import com.mathisland.app.domain.model.StepBranchRule
import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState
import com.mathisland.app.feature.level.renderers.rendererActionStateFor
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LevelAnswerPaneTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun calculationQuestion_usesChoiceRendererAndReturnsTappedChoice() {
        var selectedAnswer: String? = null
        val question = Question(
            prompt = "26 + 18 = ?",
            choices = listOf("44", "45", "34"),
            correctChoice = "44",
            hint = "先算个位，再算十位。",
            family = "calculation"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { selectedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("renderer-choice").assertIsDisplayed()
        composeRule.onNodeWithTag("answer-44").performClick()

        assertEquals("44", selectedAnswer)
    }

    @Test
    fun challengeQuestion_usesNumberPadAndSubmitsTypedValue() {
        var selectedAnswer: String? = null
        val question = Question(
            prompt = "9 x 9 = ?",
            choices = listOf("81", "72", "99"),
            correctChoice = "81",
            hint = "想想 9 个 9。",
            family = "challenge"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = { selectedAnswer = it }
                )
            }
        }

        composeRule.onNodeWithTag("renderer-number-pad").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-number-pad")
            .performScrollToNode(hasTestTag("number-pad-submit"))
        composeRule.onNodeWithTag("number-pad-key-8").performClick()
        composeRule.onNodeWithTag("number-pad-key-1").performClick()
        composeRule.onNodeWithTag("number-pad-submit").performClick()

        assertEquals("81", selectedAnswer)
    }

    @Test
    fun challengeQuestion_usesSharedGuidanceCards() {
        val question = Question(
            prompt = "9 x 9 = ?",
            choices = listOf("81", "72", "99"),
            correctChoice = "81",
            hint = "想想 9 个 9。",
            family = "challenge"
        )
        val feedback = AnswerFeedbackUiState(
            kind = AnswerFeedbackKind.Incorrect,
            title = "再试一次",
            body = "先看提示，再判断一次。",
            submittedAnswer = "72"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    feedback = feedback,
                    actionState = rendererActionStateFor(
                        feedback = feedback,
                        inputEnabled = true
                    ),
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-number-pad").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-prompt-card").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-prompt-chip").assertIsDisplayed()
        composeRule.onAllNodesWithTag("renderer-guidance-card").assertCountEquals(1)
        composeRule.onAllNodesWithTag("renderer-guidance-chip").assertCountEquals(1)
        composeRule.onNodeWithTag("renderer-number-pad")
            .performScrollToNode(hasTestTag("renderer-action-chip"))
        composeRule.onNodeWithTag("renderer-action-header").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-action-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-number-pad")
            .performScrollToNode(hasTestTag("answer-feedback-chip"))
        composeRule.onNodeWithTag("answer-feedback-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("number-pad-status").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-number-pad")
            .performScrollToNode(hasText("重试提示"))
        composeRule.onNodeWithText("重试提示").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-number-pad")
            .performScrollToNode(hasText("先看提示，再换答案。"))
        composeRule.onNodeWithText("先看提示，再换答案。").assertIsDisplayed()
    }

    @Test
    fun challengeQuestion_showsTimeoutExpiredNumberPadState() {
        val question = Question(
            prompt = "9 x 9 = ?",
            choices = listOf("81", "72", "99"),
            correctChoice = "81",
            hint = "想想 9 个 9。",
            family = "challenge"
        )
        val feedback = AnswerFeedbackUiState(
            kind = AnswerFeedbackKind.TimeoutExpired,
            title = "已超时",
            body = "本轮冲刺已经结束，这题按当前结果结算。",
            submittedAnswer = "72"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    feedback = feedback,
                    actionState = rendererActionStateFor(
                        feedback = feedback,
                        inputEnabled = false
                    ),
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-number-pad").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-prompt-card").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-prompt-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-number-pad")
            .performScrollToNode(hasTestTag("renderer-action-chip"))
        composeRule.onNodeWithTag("renderer-action-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-number-pad")
            .performScrollToNode(hasTestTag("answer-feedback-title"))
        composeRule.onNodeWithTag("answer-feedback-title").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-number-pad")
            .performScrollToNode(hasTestTag("answer-feedback-chip"))
        composeRule.onNodeWithTag("answer-feedback-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("number-pad-status").assertIsDisplayed()
        composeRule.onNodeWithTag("number-pad-tone-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-number-pad")
            .performScrollToNode(hasText("本题已超时，直接看下一题。"))
        composeRule.onNodeWithText("本题已超时，直接看下一题。").assertIsDisplayed()
    }

    @Test
    fun calculationQuestion_showsTimeoutExpiredChoiceState() {
        val question = Question(
            prompt = "26 + 18 = ?",
            choices = listOf("44", "45", "34"),
            correctChoice = "44",
            hint = "先算个位，再算十位。",
            family = "calculation"
        )
        val feedback = AnswerFeedbackUiState(
            kind = AnswerFeedbackKind.TimeoutExpired,
            title = "已超时",
            body = "本轮冲刺已经结束，这题按当前结果结算。",
            submittedAnswer = "45"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    feedback = feedback,
                    actionState = rendererActionStateFor(
                        feedback = feedback,
                        inputEnabled = false
                    ),
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-choice").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-prompt-card").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-prompt-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-choice")
            .performScrollToNode(hasTestTag("answer-state-45"))
        composeRule.onNodeWithTag("renderer-action-chip").assertIsDisplayed()
        composeRule.onNodeWithTag("answer-state-45").assertIsDisplayed()
        composeRule.onNodeWithTag("answer-state-chip-45").assertIsDisplayed()
        composeRule.onNodeWithText("这次尝试超时").assertIsDisplayed()
        composeRule.onNodeWithTag("answer-feedback-title").assertIsDisplayed()
    }

    @Test
    fun measurementQuestion_showsRulerAffordance() {
        val question = Question(
            prompt = "小船长约长多少？",
            choices = listOf("8 厘米", "10 厘米", "12 厘米"),
            correctChoice = "10 厘米",
            hint = "观察尺子上的刻度。",
            family = "measurement"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-ruler").assertIsDisplayed()
        composeRule.onNodeWithTag("tablet-ruler-handle").assertIsDisplayed()
        composeRule.onAllNodesWithTag("answer-10 厘米").assertCountEquals(1)
        composeRule.onNodeWithText("先看刻度，再选答案。").assertIsDisplayed()
    }

    @Test
    fun multiplicationQuestion_showsChantAffordance() {
        val question = Question(
            prompt = "7 x 8 = ?",
            choices = listOf("54", "56", "64"),
            correctChoice = "56",
            hint = "先念口诀。",
            family = "multiplication"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-chant").assertIsDisplayed()
        composeRule.onNodeWithTag("chant-beat-strip").assertIsDisplayed()
        composeRule.onNodeWithText("先念口诀，再选答案。").assertIsDisplayed()
    }

    @Test
    fun divisionQuestion_showsGroupingAffordance() {
        val question = Question(
            prompt = "12 个海星平均放进 3 个篮子，每个篮子几个？",
            choices = listOf("3", "4", "5"),
            correctChoice = "4",
            hint = "先分组。",
            family = "division"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-group").assertIsDisplayed()
        composeRule.onNodeWithTag("group-basket-zone").assertIsDisplayed()
        composeRule.onNodeWithText("先分组，再选答案。").assertIsDisplayed()
    }

    @Test
    fun bigNumberQuestion_showsSortingAffordance() {
        val question = Question(
            prompt = "把数字按从小到大排好。",
            choices = listOf("208, 280, 820", "280, 208, 820", "820, 280, 208"),
            correctChoice = "208, 280, 820",
            hint = "先比较百位。",
            family = "big-number"
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-sort").assertIsDisplayed()
        composeRule.onNodeWithTag("sort-signal-lights").assertIsDisplayed()
        composeRule.onNodeWithText("先比较顺序，再选答案。").assertIsDisplayed()
    }

    @Test
    fun matchingQuestion_usesMatchingRenderer() {
        val question = Question(
            prompt = "把数学工具和它最适合表示的意思连起来。",
            choices = emptyList(),
            correctChoice = "尺子=长度,秤=重量,时钟=时间",
            hint = "先看左边工具，再找到右边最贴切的意思。",
            family = "matching",
            leftItems = listOf("尺子", "秤", "时钟"),
            rightItems = listOf("时间", "重量", "长度")
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-matching").assertIsDisplayed()
        composeRule.onNodeWithTag("matching-left-column").assertIsDisplayed()
        composeRule.onNodeWithTag("matching-right-column").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-submit"))
        composeRule.onNodeWithTag("matching-submit").assertIsDisplayed()
    }

    @Test
    fun matchingGroupedQuestion_usesGroupedMatchingRenderer() {
        val question = Question(
            prompt = "按两个小组完成配对。",
            choices = emptyList(),
            correctChoice = "平均分苹果=用除法,合并两堆贝壳=用加法||尺子=测长度,秤=测重量",
            hint = "先完成一组，再完成下一组。",
            family = "matching",
            matchingGroups = listOf(
                MatchingGroup(
                    title = "看场景选算法",
                    leftItems = listOf("平均分苹果", "合并两堆贝壳"),
                    rightItems = listOf("用加法", "用除法")
                ),
                MatchingGroup(
                    title = "看工具选用途",
                    leftItems = listOf("尺子", "秤"),
                    rightItems = listOf("测重量", "测长度")
                )
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

        composeRule.onNodeWithTag("renderer-matching").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-group-0"))
        composeRule.onNodeWithTag("matching-group-0").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-group-1"))
        composeRule.onNodeWithTag("matching-group-1").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-submit"))
        composeRule.onNodeWithTag("matching-submit").assertIsDisplayed()
    }

    @Test
    fun matchingMultiRoundQuestion_usesRoundProgressRenderer() {
        val question = Question(
            prompt = "按两轮完成语义配对。",
            choices = emptyList(),
            correctChoice = "平均分苹果=用除法,合并两堆贝壳=用加法>>>用除法=求每份有多少,用加法=求合起来一共多少",
            hint = "先完成当前轮，再进入下一轮。",
            family = "matching",
            matchingRounds = listOf(
                MatchingRound(
                    title = "第一轮：看场景选算法",
                    prompt = "第一轮：把场景和最合适的算法连起来。",
                    groups = listOf(
                        MatchingGroup(
                            title = "",
                            leftItems = listOf("平均分苹果", "合并两堆贝壳"),
                            rightItems = listOf("用加法", "用除法")
                        )
                    )
                ),
                MatchingRound(
                    title = "第二轮：看算法选作用",
                    prompt = "第二轮：把算法和它最适合解决的问题连起来。",
                    groups = listOf(
                        MatchingGroup(
                            title = "",
                            leftItems = listOf("用除法", "用加法"),
                            rightItems = listOf("求合起来一共多少", "求每份有多少")
                        )
                    )
                )
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

        composeRule.onNodeWithTag("renderer-matching").assertIsDisplayed()
        composeRule.onNodeWithTag("matching-round-progress").assertIsDisplayed()
        composeRule.onNodeWithTag("matching-round-chip-0").assertIsDisplayed()
        composeRule.onNodeWithTag("matching-round-chip-1").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-next-round"))
    }

    @Test
    fun fillBlankQuestion_usesFillBlankRenderer() {
        val question = Question(
            prompt = "把空格补完整。",
            choices = emptyList(),
            correctChoice = "100,200",
            hint = "先看单位，再把数字放进空格。",
            family = "fill-blank",
            blankParts = listOf("1 米 = ", " 厘米，2 米 = ", " 厘米。"),
            blankOptions = listOf("200", "100", "20")
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-fill-blank").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-slot-0"))
        composeRule.onNodeWithTag("fill-blank-slot-0").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-slot-1"))
        composeRule.onNodeWithTag("fill-blank-slot-1").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-submit"))
        composeRule.onNodeWithTag("fill-blank-submit").assertIsDisplayed()
    }

    @Test
    fun fillBlankQuestion_showsMixedSlotCueChips() {
        val question = Question(
            prompt = "把长度换算补完整。",
            choices = emptyList(),
            correctChoice = "300,米,90",
            hint = "先看每个空格需要填数字还是单位。",
            family = "fill-blank",
            blankParts = listOf("3 ", " = ", "00 厘米，9 分米 = ", " 厘米。"),
            blankOptions = listOf("米", "90", "300", "厘米"),
            blankSlotKinds = listOf("number", "unit", "number")
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-fill-blank").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-slot-kind-0"))
        composeRule.onNodeWithTag("fill-blank-slot-kind-0").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-slot-kind-1"))
        composeRule.onNodeWithTag("fill-blank-slot-kind-1").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-slot-kind-2"))
        composeRule.onNodeWithTag("fill-blank-slot-kind-2").assertIsDisplayed()
    }

    @Test
    fun fillBlankQuestion_showsPartitionedPools() {
        val question = Question(
            prompt = "按分区选项池把长度换算补完整。",
            choices = emptyList(),
            correctChoice = "米,300,分米,70",
            hint = "先看空格要填数字还是单位，再去对应分区找答案。",
            family = "fill-blank",
            blankParts = listOf("3 ", " = ", " 厘米，7 ", " = ", " 厘米。"),
            blankOptions = listOf("分米", "70", "米", "300", "厘米"),
            blankSlotKinds = listOf("unit", "number", "unit", "number")
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(
                    question = question,
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-fill-blank").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-pool-number"))
        composeRule.onNodeWithTag("fill-blank-pool-number").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-pool-unit"))
        composeRule.onNodeWithTag("fill-blank-pool-unit").assertIsDisplayed()
    }

    @Test
    fun multiStepQuestion_usesMultiStepRenderer() {
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
                    onAnswer = {}
                )
            }
        }

        composeRule.onNodeWithTag("renderer-multi-step").assertIsDisplayed()
        composeRule.onNodeWithTag("multi-step-progress-card").assertIsDisplayed()
        composeRule.onNodeWithTag("multi-step-prompt").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-submit"))
        composeRule.onNodeWithTag("multi-step-submit").assertIsDisplayed()
    }

    @Test
    fun multiStepConditionalQuestion_switchesBranchPrompt() {
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
                "remainder-step-2" to listOf(StepBranchRule("*", "remainder-step-3"))
            ),
            stepBranchPrompts = mapOf(
                "remainder-step-2" to "第二步：18 ÷ 4 的结果是什么？",
                "remainder-step-3" to "第三步：至少需要几个袋子？"
            ),
            stepBranchChoices = mapOf(
                "remainder-step-2" to listOf("商是4余2", "商是5余1"),
                "remainder-step-3" to listOf("4个袋子", "5个袋子", "6个袋子")
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

        composeRule.onNodeWithTag("renderer-multi-step").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-choice-0"))
        composeRule.onNodeWithTag("multi-step-choice-0").performClick()
        waitForText("第二步：18 ÷ 4 的结果是什么？")
        composeRule.onNodeWithText("第二步：18 ÷ 4 的结果是什么？").assertIsDisplayed()
    }

    @Test
    fun multiStepConditionalQuestion_canRenderSharedFinalBranchPrompt() {
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
                    onAnswer = {}
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
    }

    @Test
    fun multiStepQuestion_rendersPresentationMetadata() {
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
                "exact-step-2" to StepPresentation("整除路线", "直接说出整除后的商。", "整除结果")
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

        composeRule.onNodeWithTag("renderer-multi-step").assertIsDisplayed()
        composeRule.onNodeWithTag("multi-step-stage-title").assertIsDisplayed()
        composeRule.onNodeWithTag("multi-step-stage-support").assertIsDisplayed()
        composeRule.onNodeWithText("先定路线").assertIsDisplayed()
        composeRule.onNodeWithText("先判断这次平均分会不会有剩余。").assertIsDisplayed()
    }

    @Test
    fun multiStepQuestion_disablesActionsDuringConfirmationTransition() {
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

        composeRule.onNodeWithTag("multi-step-reset").assertIsNotEnabled()
        composeRule.onNodeWithTag("multi-step-submit").assertIsNotEnabled()
        composeRule.onNodeWithTag("multi-step-choice-1").assertIsNotEnabled()
    }

    @Test
    fun multiStepQuestion_keepsActiveStageVisibleWhenRecapExpands() {
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
                "exact-step-2" to StepPresentation("整除路线", "直接说出整除后的商。", "整除结果")
            )
        )

        composeRule.setContent {
            MathIslandTheme {
                LevelAnswerPane(question = question, onAnswer = {})
            }
        }

        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-choice-1"))
        composeRule.onNodeWithTag("multi-step-choice-1").performClick()
        waitForText("第二步：12 ÷ 3 的结果是什么？")

        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-recap-toggle-0"))
        composeRule.onNodeWithTag("multi-step-recap-toggle-0").performClick()

        waitForTag("multi-step-recap-prompt-0")
        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasText("第二步：12 ÷ 3 的结果是什么？"))
        composeRule.onNodeWithText("第二步：12 ÷ 3 的结果是什么？").assertIsDisplayed()
        composeRule.onNodeWithTag("multi-step-choice-0").assertIsDisplayed()
    }

    private fun waitForText(text: String) {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun waitForTag(tag: String) {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
