package com.mathisland.app.feature.level

import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState
import com.mathisland.app.feature.level.renderers.RendererActionPhase
import com.mathisland.app.ui.theme.StatusVariant

data class LessonAttemptCopy(
    val statusSubtitle: String,
    val statusBody: String,
    val nextStepSubtitle: String,
    val nextStepBody: String
)

fun lessonActionBadgeTextFor(phase: RendererActionPhase): String = when (phase) {
    RendererActionPhase.Ready -> "开始作答"
    RendererActionPhase.Retry -> "继续作答"
    RendererActionPhase.Confirmed -> "准备切题"
    RendererActionPhase.Locked -> "稍等片刻"
    RendererActionPhase.TimeoutExpired -> "本题结束"
}

fun lessonActionBadgeVariantFor(phase: RendererActionPhase): StatusVariant = when (phase) {
    RendererActionPhase.Ready -> StatusVariant.Recommended
    RendererActionPhase.Retry -> StatusVariant.Highlight
    RendererActionPhase.Confirmed -> StatusVariant.Success
    RendererActionPhase.Locked -> StatusVariant.Neutral
    RendererActionPhase.TimeoutExpired -> StatusVariant.Caution
}

fun lessonRailHeaderBadgeTextFor(
    phase: RendererActionPhase,
    lessonIsReview: Boolean,
): String = when (phase) {
    RendererActionPhase.Retry -> "重试状态"
    RendererActionPhase.Confirmed -> "已确认"
    RendererActionPhase.TimeoutExpired -> "本题结束"
    RendererActionPhase.Ready,
    RendererActionPhase.Locked,
    -> "学习状态"
}

fun lessonRailHeaderBadgeVariantFor(phase: RendererActionPhase): StatusVariant = when (phase) {
    RendererActionPhase.Retry -> StatusVariant.Highlight
    RendererActionPhase.Confirmed -> StatusVariant.Success
    RendererActionPhase.TimeoutExpired -> StatusVariant.Caution
    RendererActionPhase.Ready,
    RendererActionPhase.Locked,
    -> StatusVariant.Neutral
}

fun lessonFeedbackTitleFor(kind: AnswerFeedbackKind): String = when (kind) {
    AnswerFeedbackKind.Correct -> "答案已确认"
    AnswerFeedbackKind.Incorrect -> "再次尝试"
    AnswerFeedbackKind.TimedWarning -> "限时进行中"
    AnswerFeedbackKind.TimeoutExpired -> "本题已超时"
}

fun lessonFeedbackBadgeTextFor(kind: AnswerFeedbackKind): String = when (kind) {
    AnswerFeedbackKind.Correct -> "已确认"
    AnswerFeedbackKind.Incorrect -> "重试中"
    AnswerFeedbackKind.TimedWarning -> "限时进行中"
    AnswerFeedbackKind.TimeoutExpired -> "本题结束"
}

fun lessonFeedbackBadgeVariantFor(kind: AnswerFeedbackKind): StatusVariant = when (kind) {
    AnswerFeedbackKind.Correct -> StatusVariant.Success
    AnswerFeedbackKind.Incorrect -> StatusVariant.Highlight
    AnswerFeedbackKind.TimedWarning,
    AnswerFeedbackKind.TimeoutExpired,
    -> StatusVariant.Caution
}

fun lessonPromptBadgeTextFor(
    phase: RendererActionPhase,
    lessonIsReview: Boolean,
): String = when (phase) {
    RendererActionPhase.Retry -> if (lessonIsReview) "重看线索" else "重看题目"
    RendererActionPhase.Confirmed -> "本题完成"
    RendererActionPhase.TimeoutExpired -> "本题结束"
    RendererActionPhase.Ready,
    RendererActionPhase.Locked,
    -> if (lessonIsReview) "先看线索" else "先看题目"
}

fun lessonPromptBadgeVariantFor(
    phase: RendererActionPhase,
    lessonIsReview: Boolean,
): StatusVariant = when (phase) {
    RendererActionPhase.Retry -> StatusVariant.Highlight
    RendererActionPhase.Confirmed -> StatusVariant.Success
    RendererActionPhase.TimeoutExpired -> StatusVariant.Caution
    RendererActionPhase.Ready,
    RendererActionPhase.Locked,
    -> if (lessonIsReview) StatusVariant.Highlight else StatusVariant.Recommended
}

fun lessonPromptBodyFor(
    phase: RendererActionPhase,
    lessonIsReview: Boolean,
): String = when (phase) {
    RendererActionPhase.Retry -> if (lessonIsReview) {
        "先重看线索，再换答案。"
    } else {
        "先重看题目，再换答案。"
    }
    RendererActionPhase.Confirmed -> "本题已确认，准备下一题。"
    RendererActionPhase.TimeoutExpired -> "本题已结束，直接看下一题。"
    RendererActionPhase.Ready,
    RendererActionPhase.Locked,
    -> if (lessonIsReview) {
        "先读线索，再开始作答。"
    } else {
        "先读题目，再开始作答。"
    }
}

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
            nextStepBody = "保持节奏，准备下一题。"
        )

        AnswerFeedbackKind.Incorrect -> LessonAttemptCopy(
            statusSubtitle = "再次尝试",
            statusBody = feedback.body,
            nextStepSubtitle = "继续重试",
            nextStepBody = "先看这次尝试，再换答案。"
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
                    nextStepBody = "先看右侧线索，再提交答案。"
                )
            } else {
                LessonAttemptCopy(
                    statusSubtitle = "准备作答",
                    statusBody = "先看题目，再提交答案。",
                    nextStepSubtitle = "现在就答",
                    nextStepBody = "先看右侧题目，再提交答案。"
                )
            }

            RendererActionPhase.Retry -> LessonAttemptCopy(
                statusSubtitle = "再次尝试",
                statusBody = "先看提示，再判断答案。",
                nextStepSubtitle = "继续重试",
                nextStepBody = "先看这次尝试，再换答案。"
            )

            RendererActionPhase.Confirmed -> LessonAttemptCopy(
                statusSubtitle = "答案已确认",
                statusBody = "马上进入下一题。",
                nextStepSubtitle = "准备切题",
                nextStepBody = "保持节奏，准备下一题。"
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
