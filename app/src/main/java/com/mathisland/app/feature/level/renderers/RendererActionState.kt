package com.mathisland.app.feature.level.renderers

import com.mathisland.app.ui.theme.ActionRole

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
        RendererActionPhase.Ready -> "准备作答"
        RendererActionPhase.Retry -> "正在重试"
        RendererActionPhase.Confirmed -> "已确认"
        RendererActionPhase.Locked -> "正在检查"
        RendererActionPhase.TimeoutExpired -> "已超时"
    }

    fun sectionBody(): String = when (phase) {
        RendererActionPhase.Ready -> "先看题目，再提交这次答案。"
        RendererActionPhase.Retry -> "先看提示，再判断一次。"
        RendererActionPhase.Confirmed -> "答案已确认，马上进入下一题。"
        RendererActionPhase.Locked -> "这次提交正在检查，请稍等片刻。"
        RendererActionPhase.TimeoutExpired -> "本题时间已到，请直接进入下一题。"
    }
}

fun rendererActionStateFor(
    feedback: AnswerFeedbackUiState?,
    inputEnabled: Boolean,
): RendererActionState = when {
    feedback?.kind == AnswerFeedbackKind.TimeoutExpired -> RendererActionState(
        phase = RendererActionPhase.TimeoutExpired,
        enabled = false
    )

    !inputEnabled && feedback?.kind == AnswerFeedbackKind.Correct -> RendererActionState(
        phase = RendererActionPhase.Confirmed,
        enabled = false
    )

    !inputEnabled -> RendererActionState(
        phase = RendererActionPhase.Locked,
        enabled = false
    )

    feedback?.kind == AnswerFeedbackKind.Incorrect -> RendererActionState(
        phase = RendererActionPhase.Retry,
        enabled = true
    )

    else -> RendererActionState(
        phase = RendererActionPhase.Ready,
        enabled = inputEnabled
    )
}
