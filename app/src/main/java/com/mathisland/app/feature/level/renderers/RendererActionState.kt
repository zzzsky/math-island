package com.mathisland.app.feature.level.renderers

import com.mathisland.app.ui.theme.ActionRole

enum class RendererActionPhase {
    Ready,
    Retry,
    Confirmed,
    Locked,
}

data class RendererActionState(
    val phase: RendererActionPhase,
    val enabled: Boolean,
) {
    fun resolveLabel(defaultLabel: String): String = when (phase) {
        RendererActionPhase.Retry -> "再试一次"
        RendererActionPhase.Confirmed -> "已确认"
        RendererActionPhase.Ready,
        RendererActionPhase.Locked,
        -> defaultLabel
    }

    fun resolveRole(defaultRole: ActionRole): ActionRole = when (phase) {
        RendererActionPhase.Ready -> defaultRole
        RendererActionPhase.Retry -> ActionRole.Secondary
        RendererActionPhase.Confirmed -> ActionRole.Completed
        RendererActionPhase.Locked -> ActionRole.Secondary
    }

    fun sectionTitle(): String = when (phase) {
        RendererActionPhase.Ready -> "开始作答"
        RendererActionPhase.Retry -> "重新尝试"
        RendererActionPhase.Confirmed -> "答案已确认"
        RendererActionPhase.Locked -> "正在检查"
    }

    fun sectionBody(): String = when (phase) {
        RendererActionPhase.Ready -> "选一个你认为正确的答案。"
        RendererActionPhase.Retry -> "结合提示再判断一次，不用着急。"
        RendererActionPhase.Confirmed -> "系统正在推进到下一步。"
        RendererActionPhase.Locked -> "请稍等片刻，马上可以继续操作。"
    }
}

fun rendererActionStateFor(
    feedback: AnswerFeedbackUiState?,
    inputEnabled: Boolean,
): RendererActionState = when {
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
