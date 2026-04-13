package com.mathisland.app.feature.level.renderers

import com.mathisland.app.QuestionRendererType
import com.mathisland.app.domain.model.Question
import com.mathisland.app.domain.model.StepFeedbackHint
import com.mathisland.app.domain.model.StepPresentation
import com.mathisland.app.domain.model.StepBranchRule
import com.mathisland.app.rendererTypeFor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MultiStepAnswerStateTest {
    @Test
    fun conditionalBranch_resolvesNextPromptPath() {
        val question = Question(
            prompt = "按步骤判断装袋方案。",
            choices = emptyList(),
            correctChoice = "有余数,商是4余2,5个袋子",
            hint = "先看有没有余数。",
            family = "multi-step",
            stepPrompts = listOf("第一步", "第二步", "第三步"),
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
                "remainder-step-2" to listOf(StepBranchRule("*", "remainder-step-3")),
                "exact-step-2" to listOf(StepBranchRule("*", "exact-step-3"))
            ),
            stepBranchPrompts = mapOf(
                "remainder-step-2" to "第二步：18 ÷ 4 的结果是什么？",
                "exact-step-2" to "第二步：如果正好分完，每袋装几个？"
            ),
            stepBranchChoices = mapOf(
                "remainder-step-2" to listOf("商是4余2", "商是5余1"),
                "exact-step-2" to listOf("每袋4个", "每袋5个")
            )
        )

        val state = MultiStepAnswerState()
            .advance("有余数", 3, nextBranchKeyFor(question, MultiStepAnswerState(), "有余数"))

        assertEquals("remainder-step-2", state.currentBranchKey(question, 1))
        assertEquals("第二步：18 ÷ 4 的结果是什么？", multiStepPromptFor(question, state))
        assertEquals(listOf("商是4余2", "商是5余1"), multiStepChoicesFor(question, state))
    }

    @Test
    fun conditionalBranch_encodedAnswer_followsChosenPath() {
        val state = MultiStepAnswerState()
            .advance("有余数", 3, "remainder-step-2")
            .advance("商是4余2", 3, "remainder-step-3")
            .advance("5个袋子", 3)

        assertEquals("有余数,商是4余2,5个袋子", state.encodedAnswer(3))
    }

    @Test
    fun conditionalBranch_canConvergeToSharedFinalStep() {
        val question = Question(
            prompt = "按条件步骤完成装盒判断。",
            choices = emptyList(),
            correctChoice = "正好分完,商是4,4个盒子",
            hint = "先判断有没有余数，再走对应步骤，最后收束到同一个判断。",
            family = "multi-step",
            stepPrompts = listOf("第一步", "第二步", "第三步"),
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

        val remainderState = MultiStepAnswerState()
            .advance("有余数", 3, nextBranchKeyFor(question, MultiStepAnswerState(), "有余数"))
            .advance(
                "商是4余2",
                3,
                nextBranchKeyFor(
                    question,
                    MultiStepAnswerState()
                        .advance("有余数", 3, "remainder-step-2"),
                    "商是4余2"
                )
            )
        val exactState = MultiStepAnswerState()
            .advance("正好分完", 3, nextBranchKeyFor(question, MultiStepAnswerState(), "正好分完"))
            .advance(
                "商是4",
                3,
                nextBranchKeyFor(
                    question,
                    MultiStepAnswerState()
                        .advance("正好分完", 3, "exact-step-2"),
                    "商是4"
                )
            )

        assertEquals("shared-final-step", remainderState.currentBranchKey(question, 2))
        assertEquals("shared-final-step", exactState.currentBranchKey(question, 2))
        assertEquals("第三步：现在至少要准备几个盒子？", multiStepPromptFor(question, remainderState))
        assertEquals("第三步：现在至少要准备几个盒子？", multiStepPromptFor(question, exactState))
    }

    @Test
    fun branchPresentation_resolvesStageSupportAndAnswerLabel() {
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

        val exactStepOne = MultiStepAnswerState()
            .advance("正好分完", 4, nextBranchKeyFor(question, MultiStepAnswerState(), "正好分完"))
        val exactStepTwo = exactStepOne.advance(
            "商是4",
            4,
            nextBranchKeyFor(question, exactStepOne, "商是4")
        )
        val completed = exactStepTwo
            .advance(
                "4个盒子",
                4,
                nextBranchKeyFor(question, exactStepTwo, "4个盒子")
            )
            .advance("正好装完，不用多准备", 4)

        assertEquals("整除路线", multiStepPresentationFor(question, exactStepOne).stageTitle)
        assertEquals("直接说出整除后的商。", multiStepPresentationFor(question, exactStepOne).supportText)
        assertEquals("统一装盒", multiStepPresentationFor(question, exactStepTwo).stageTitle)
        assertEquals("分支判断", multiStepAnswerLabelFor(question, completed, 0))
        assertEquals("整除结果", multiStepAnswerLabelFor(question, completed, 1))
        assertEquals("装盒数量", multiStepAnswerLabelFor(question, completed, 2))
        assertEquals("最终结论", multiStepAnswerLabelFor(question, completed, 3))
    }

    @Test
    fun submittedAnswer_reconstructsBranchAwareStateForFeedbackReview() {
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
            )
        )

        val reconstructed = multiStepStateForSubmittedAnswer(
            question = question,
            submittedAnswer = "正好分完,商是4,4个盒子,正好装完，不用多准备"
        )

        assertEquals(
            listOf("正好分完", "商是4", "4个盒子", "正好装完，不用多准备"),
            reconstructed.answers
        )
        assertEquals("exact-step-2", reconstructed.currentBranchKey(question, 1))
        assertEquals("shared-final-step", reconstructed.currentBranchKey(question, 2))
        assertEquals("shared-wrap-up-step", reconstructed.currentBranchKey(question, 3))
    }

    @Test
    fun feedbackHint_resolvesRetryFocusAndFallbackCopy() {
        val hint = StepFeedbackHint(
            incorrectLabel = "优先重看",
            incorrectBody = "先回到这一步重新核对。",
            expandOnIncorrect = true
        )

        val retryState = multiStepRecapFeedbackStateFor(
            feedbackKind = AnswerFeedbackKind.Incorrect,
            hint = hint
        )
        val timeoutFallback = multiStepRecapFeedbackStateFor(
            feedbackKind = AnswerFeedbackKind.TimeoutExpired,
            hint = null
        )

        assertEquals("优先重看", retryState.chipText)
        assertEquals("先回到这一步重新核对。", retryState.body)
        assertTrue(retryState.autoExpand)
        assertEquals("本题结束", timeoutFallback.chipText)
        assertTrue(timeoutFallback.body.isNotBlank())
    }

    @Test
    fun encodedAnswer_followsStepOrder() {
        val state = MultiStepAnswerState()
            .advance("平均分给 3 只小猴", 2)
            .advance("每只 4 个", 2)

        assertEquals("平均分给 3 只小猴,每只 4 个", state.encodedAnswer(2))
    }

    @Test
    fun reset_clearsRecordedSteps() {
        val state = MultiStepAnswerState()
            .advance("平均分给 3 只小猴", 2)
            .reset()

        assertEquals(emptyList<String>(), state.answers)
    }

    @Test
    fun multiStepRendererType_isMapped() {
        assertEquals(QuestionRendererType.MULTI_STEP, rendererTypeFor("multi-step"))
        assertTrue(MultiStepAnswerState(listOf("a", "b")).isComplete(2))
    }
}
