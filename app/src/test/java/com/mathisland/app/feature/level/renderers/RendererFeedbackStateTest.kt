package com.mathisland.app.feature.level.renderers

import com.mathisland.app.feature.level.LessonStatusTone
import com.mathisland.app.feature.level.lessonStatusToneFor
import org.junit.Assert.assertEquals
import org.junit.Test

class RendererFeedbackStateTest {
    private val timeoutExpiredKind = AnswerFeedbackKind.valueOf("TimeoutExpired")

    @Test
    fun feedbackTone_usesRetryToneForIncorrectAnswers() {
        val tone = lessonStatusToneFor(
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Incorrect,
                title = "再试一次",
                body = "提示",
                submittedAnswer = "45"
            )
        )

        assertEquals(LessonStatusTone.Retry, tone)
    }

    @Test
    fun feedbackTone_usesConfirmedToneForCorrectAnswers() {
        val tone = lessonStatusToneFor(
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Correct,
                title = "答对了",
                body = "继续推进",
                submittedAnswer = "44"
            )
        )

        assertEquals(LessonStatusTone.Confirmed, tone)
    }

    @Test
    fun optionFeedback_marksIncorrectSubmittedChoice() {
        val state = optionFeedbackStateFor(
            choice = "45",
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Incorrect,
                title = "再试一次",
                body = "提示",
                submittedAnswer = "45"
            )
        )

        assertEquals(OptionFeedbackTone.Retry, state.tone)
        assertEquals("刚才选了这个", state.supportingText)
        assertEquals("重试", state.badgeText)
    }

    @Test
    fun optionFeedback_marksCorrectSubmittedChoice() {
        val state = optionFeedbackStateFor(
            choice = "44",
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Correct,
                title = "答对了",
                body = "继续推进",
                submittedAnswer = "44"
            )
        )

        assertEquals(OptionFeedbackTone.Confirmed, state.tone)
        assertEquals("这次答对了", state.supportingText)
        assertEquals("已确认", state.badgeText)
    }

    @Test
    fun optionFeedback_marksTimeoutExpiredSubmittedChoice() {
        val state = optionFeedbackStateFor(
            choice = "45",
            feedback = AnswerFeedbackUiState(
                kind = timeoutExpiredKind,
                title = "已超时",
                body = "本题时间已到",
                submittedAnswer = "45"
            )
        )

        assertEquals(OptionFeedbackTone.TimeoutExpired, state.tone)
        assertEquals("这次尝试超时", state.supportingText)
        assertEquals("已超时", state.badgeText)
    }

    @Test
    fun numberPadDisplay_prefersSubmittedAnswerDuringRetry() {
        val state = numberPadDisplayStateFor(
            enteredAnswer = "4",
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Incorrect,
                title = "再试一次",
                body = "提示",
                submittedAnswer = "45"
            )
        )

        assertEquals(NumberPadDisplayTone.Retry, state.tone)
        assertEquals("45", state.displayText)
        assertEquals("先检查这次输入，再试一次", state.supportingText)
        assertEquals("重试中", state.badgeText)
    }

    @Test
    fun numberPadDisplay_showsReadinessWhenTyping() {
        val state = numberPadDisplayStateFor(
            enteredAnswer = "81",
            feedback = null
        )

        assertEquals(NumberPadDisplayTone.Ready, state.tone)
        assertEquals("81", state.displayText)
        assertEquals("已输入 2 位，准备提交", state.supportingText)
        assertEquals("准备提交", state.badgeText)
    }

    @Test
    fun numberPadDisplay_showsTimeoutExpiredSubmittedValueAndCopy() {
        val state = numberPadDisplayStateFor(
            enteredAnswer = "",
            feedback = AnswerFeedbackUiState(
                kind = timeoutExpiredKind,
                title = "已超时",
                body = "本题时间已到",
                submittedAnswer = "45"
            )
        )

        assertEquals(NumberPadDisplayTone.TimeoutExpired, state.tone)
        assertEquals("45", state.displayText)
        assertEquals("本题已超时，直接看下一题。", state.supportingText)
        assertEquals("已超时", state.badgeText)
    }
}
