package com.mathisland.app.feature.level.renderers

import com.mathisland.app.feature.level.lessonAttemptCopyFor
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.StatusVariant

enum class RendererActionPhase {
    Ready,
    Retry,
    Confirmed,
    Locked,
    TimeoutExpired,
}

data class RendererActionState(
    val phase: RendererActionPhase,
    val enabled: Boolean,
    val isReview: Boolean = false,
) {
    fun resolveLabel(defaultLabel: String): String = when (phase) {
        RendererActionPhase.Retry -> "再试一次"
        RendererActionPhase.Confirmed -> "已确认"
        RendererActionPhase.TimeoutExpired -> "已超时"
        RendererActionPhase.Ready,
        RendererActionPhase.Locked,
        -> defaultLabel
    }

    fun resolveRole(defaultRole: ActionRole): ActionRole = when (phase) {
        RendererActionPhase.Ready -> defaultRole
        RendererActionPhase.Retry -> ActionRole.Secondary
        RendererActionPhase.Confirmed -> ActionRole.Completed
        RendererActionPhase.Locked -> ActionRole.Secondary
        RendererActionPhase.TimeoutExpired -> ActionRole.Secondary
    }

    fun sectionTitle(): String = when (phase) {
        RendererActionPhase.Ready -> lessonAttemptCopyFor(
            lessonIsReview = isReview,
            feedback = null,
            phase = phase
        ).nextStepSubtitle
        RendererActionPhase.Retry -> lessonAttemptCopyFor(
            lessonIsReview = isReview,
            feedback = null,
            phase = phase
        ).nextStepSubtitle
        RendererActionPhase.Confirmed -> lessonAttemptCopyFor(
            lessonIsReview = isReview,
            feedback = null,
            phase = phase
        ).nextStepSubtitle
        RendererActionPhase.Locked -> "正在检查"
        RendererActionPhase.TimeoutExpired -> lessonAttemptCopyFor(
            lessonIsReview = isReview,
            feedback = null,
            phase = phase
        ).nextStepSubtitle
    }

    fun sectionBody(): String = when (phase) {
        RendererActionPhase.Ready -> lessonAttemptCopyFor(
            lessonIsReview = isReview,
            feedback = null,
            phase = phase
        ).nextStepBody
        RendererActionPhase.Retry -> lessonAttemptCopyFor(
            lessonIsReview = isReview,
            feedback = null,
            phase = phase
        ).nextStepBody
        RendererActionPhase.Confirmed -> lessonAttemptCopyFor(
            lessonIsReview = isReview,
            feedback = null,
            phase = phase
        ).nextStepBody
        RendererActionPhase.Locked -> "稍等片刻。"
        RendererActionPhase.TimeoutExpired -> lessonAttemptCopyFor(
            lessonIsReview = isReview,
            feedback = null,
            phase = phase
        ).nextStepBody
    }

    fun sectionBadgeText(): String = when (phase) {
        RendererActionPhase.Ready -> "开始作答"
        RendererActionPhase.Retry -> "继续作答"
        RendererActionPhase.Confirmed -> "准备切题"
        RendererActionPhase.Locked -> "稍等片刻"
        RendererActionPhase.TimeoutExpired -> "本题结束"
    }

    fun sectionBadgeVariant(): StatusVariant = when (phase) {
        RendererActionPhase.Ready -> StatusVariant.Recommended
        RendererActionPhase.Retry -> StatusVariant.Highlight
        RendererActionPhase.Confirmed -> StatusVariant.Success
        RendererActionPhase.Locked -> StatusVariant.Neutral
        RendererActionPhase.TimeoutExpired -> StatusVariant.Caution
    }
}

fun rendererActionStateFor(
    feedback: AnswerFeedbackUiState?,
    inputEnabled: Boolean,
    isReview: Boolean = false,
): RendererActionState = when {
    feedback?.kind == AnswerFeedbackKind.TimeoutExpired -> RendererActionState(
        phase = RendererActionPhase.TimeoutExpired,
        enabled = false,
        isReview = isReview
    )

    !inputEnabled && feedback?.kind == AnswerFeedbackKind.Correct -> RendererActionState(
        phase = RendererActionPhase.Confirmed,
        enabled = false,
        isReview = isReview
    )

    !inputEnabled -> RendererActionState(
        phase = RendererActionPhase.Locked,
        enabled = false,
        isReview = isReview
    )

    feedback?.kind == AnswerFeedbackKind.Incorrect -> RendererActionState(
        phase = RendererActionPhase.Retry,
        enabled = true,
        isReview = isReview
    )

    else -> RendererActionState(
        phase = RendererActionPhase.Ready,
        enabled = inputEnabled,
        isReview = isReview
    )
}
