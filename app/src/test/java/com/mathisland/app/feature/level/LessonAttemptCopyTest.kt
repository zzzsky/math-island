package com.mathisland.app.feature.level

import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState
import com.mathisland.app.feature.level.renderers.RendererActionPhase
import org.junit.Assert.assertEquals
import org.junit.Test

class LessonAttemptCopyTest {
    @Test
    fun attemptCopy_forRetryUsesSharedLessonLanguage() {
        val copy = lessonAttemptCopyFor(
            lessonIsReview = false,
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Incorrect,
                title = "再试一次",
                body = "先看提示，再判断答案。",
                submittedAnswer = "72"
            ),
            phase = RendererActionPhase.Retry
        )

        assertEquals("再次尝试", copy.statusSubtitle)
        assertEquals("先看提示，再判断答案。", copy.statusBody)
        assertEquals("继续重试", copy.nextStepSubtitle)
        assertEquals("先看刚才的尝试，再换一个答案。", copy.nextStepBody)
    }

    @Test
    fun attemptCopy_forConfirmedUsesSharedLessonLanguage() {
        val copy = lessonAttemptCopyFor(
            lessonIsReview = false,
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Correct,
                title = "答对了",
                body = "马上进入下一题。",
                submittedAnswer = "81"
            ),
            phase = RendererActionPhase.Confirmed
        )

        assertEquals("答案已确认", copy.statusSubtitle)
        assertEquals("马上进入下一题。", copy.statusBody)
        assertEquals("准备切题", copy.nextStepSubtitle)
        assertEquals("保持节奏，马上进入下一题。", copy.nextStepBody)
    }

    @Test
    fun attemptCopy_forReviewReadyKeepsReplayGuidance() {
        val copy = lessonAttemptCopyFor(
            lessonIsReview = true,
            feedback = null,
            phase = RendererActionPhase.Ready
        )

        assertEquals("先看线索", copy.statusSubtitle)
        assertEquals("这节是回放题，先读提示再作答。", copy.statusBody)
        assertEquals("继续回放", copy.nextStepSubtitle)
        assertEquals("先看右侧线索，再提交这次答案。", copy.nextStepBody)
    }
}
