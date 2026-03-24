package com.mathisland.app.feature.level.renderers

import org.junit.Assert.assertEquals
import org.junit.Test

class RendererFeedbackStateTest {
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
        assertEquals("这是刚才的尝试", state.supportingText)
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
        assertEquals("这就是本次提交", state.supportingText)
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
        assertEquals("先检查刚才的输入，再试一次", state.supportingText)
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
    }
}
