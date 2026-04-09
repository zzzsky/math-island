package com.mathisland.app.feature.level.renderers

import com.mathisland.app.QuestionRendererType
import com.mathisland.app.domain.model.Question
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
