package com.mathisland.app.feature.level

import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState
import com.mathisland.app.feature.level.renderers.RendererActionPhase

data class LessonAttemptCopy(
    val statusSubtitle: String,
    val statusBody: String,
    val nextStepSubtitle: String,
    val nextStepBody: String
)

fun lessonAttemptCopyFor(
    lessonIsReview: Boolean,
    feedback: AnswerFeedbackUiState?,
    phase: RendererActionPhase
): LessonAttemptCopy {
    return when (feedback?.kind) {
        AnswerFeedbackKind.Correct -> LessonAttemptCopy(
            statusSubtitle = "答案已确认",
            statusBody = "马上进入下一题。",
            nextStepSubtitle = "准备切题",
            nextStepBody = "保持节奏，马上进入下一题。"
        )

        AnswerFeedbackKind.Incorrect -> LessonAttemptCopy(
            statusSubtitle = "再次尝试",
            statusBody = feedback.body,
            nextStepSubtitle = "继续重试",
            nextStepBody = "先看刚才的尝试，再换一个答案。"
        )

        AnswerFeedbackKind.TimedWarning -> LessonAttemptCopy(
            statusSubtitle = "限时进行中",
            statusBody = "先交最有把握的答案。",
            nextStepSubtitle = "现在就交",
            nextStepBody = "优先提交这次最有把握的答案。"
        )

        AnswerFeedbackKind.TimeoutExpired -> LessonAttemptCopy(
            statusSubtitle = "本题已超时",
            statusBody = "直接看下一题。",
            nextStepSubtitle = "继续推进",
            nextStepBody = "本题结束，直接看下一题。"
        )

        null -> when (phase) {
            RendererActionPhase.Ready,
            RendererActionPhase.Locked,
            -> if (lessonIsReview) {
                LessonAttemptCopy(
                    statusSubtitle = "先看线索",
                    statusBody = "这节是回放题，先读提示再作答。",
                    nextStepSubtitle = "继续回放",
                    nextStepBody = "先看右侧线索，再提交这次答案。"
                )
            } else {
                LessonAttemptCopy(
                    statusSubtitle = "准备作答",
                    statusBody = "先看题目，再提交答案。",
                    nextStepSubtitle = "现在就答",
                    nextStepBody = "先看右侧题目，再完成这次作答。"
                )
            }

            RendererActionPhase.Retry -> LessonAttemptCopy(
                statusSubtitle = "再次尝试",
                statusBody = "先看提示，再判断答案。",
                nextStepSubtitle = "继续重试",
                nextStepBody = "先看刚才的尝试，再换一个答案。"
            )

            RendererActionPhase.Confirmed -> LessonAttemptCopy(
                statusSubtitle = "答案已确认",
                statusBody = "马上进入下一题。",
                nextStepSubtitle = "准备切题",
                nextStepBody = "保持节奏，马上进入下一题。"
            )

            RendererActionPhase.TimeoutExpired -> LessonAttemptCopy(
                statusSubtitle = "本题已超时",
                statusBody = "直接看下一题。",
                nextStepSubtitle = "继续推进",
                nextStepBody = "本题结束，直接看下一题。"
            )
        }
    }
}
