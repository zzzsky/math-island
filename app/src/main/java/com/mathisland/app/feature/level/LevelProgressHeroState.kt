package com.mathisland.app.feature.level

import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState
import com.mathisland.app.feature.level.renderers.RendererActionPhase
import com.mathisland.app.feature.level.renderers.rendererActionStateFor
import com.mathisland.app.ui.theme.StatusVariant

data class LevelProgressHeroState(
    val heroBadgeText: String,
    val heroBadgeVariant: StatusVariant,
    val heroTone: LessonStatusTone,
    val routeBadgeText: String,
    val routeBadgeVariant: StatusVariant,
    val routeSummary: String,
    val trailingSummary: String,
    val timerChipText: String?,
    val timerNote: String?,
)

fun levelProgressHeroStateFor(
    state: LevelUiState,
    remainingSeconds: Int,
    feedback: AnswerFeedbackUiState?,
): LevelProgressHeroState {
    val lesson = state.lesson
    val actionState = rendererActionStateFor(
        feedback = feedback,
        inputEnabled = feedback?.kind != AnswerFeedbackKind.Correct && feedback?.kind != AnswerFeedbackKind.TimeoutExpired,
        isReview = lesson.isReview
    )
    return LevelProgressHeroState(
        heroBadgeText = when (actionState.phase) {
            RendererActionPhase.Retry -> "再次尝试"
            RendererActionPhase.Confirmed -> "答案已确认"
            RendererActionPhase.TimeoutExpired -> "本题已超时"
            RendererActionPhase.Ready,
            RendererActionPhase.Locked,
            -> if (lesson.isReview) "小海鸥求助" else lesson.focus
        },
        heroBadgeVariant = when (actionState.phase) {
            RendererActionPhase.Retry -> StatusVariant.Highlight
            RendererActionPhase.Confirmed -> StatusVariant.Success
            RendererActionPhase.TimeoutExpired -> StatusVariant.Caution
            RendererActionPhase.Ready,
            RendererActionPhase.Locked,
            -> if (lesson.isReview) StatusVariant.Caution else StatusVariant.Highlight
        },
        heroTone = when (actionState.phase) {
            RendererActionPhase.Retry -> LessonStatusTone.Retry
            RendererActionPhase.Confirmed -> LessonStatusTone.Confirmed
            RendererActionPhase.TimeoutExpired -> LessonStatusTone.Warning
            RendererActionPhase.Ready,
            RendererActionPhase.Locked,
            -> if (lesson.timeLimitSeconds != null) LessonStatusTone.Highlight else LessonStatusTone.Neutral
        },
        routeBadgeText = when (actionState.phase) {
            RendererActionPhase.Retry -> "重试提示"
            RendererActionPhase.Confirmed -> "继续推进"
            RendererActionPhase.TimeoutExpired -> "本题结束"
            RendererActionPhase.Ready,
            RendererActionPhase.Locked,
            -> if (lesson.timeLimitSeconds != null) "冲刺提示" else "当前路线"
        },
        routeBadgeVariant = when (actionState.phase) {
            RendererActionPhase.Retry -> StatusVariant.Highlight
            RendererActionPhase.Confirmed -> StatusVariant.Success
            RendererActionPhase.TimeoutExpired -> StatusVariant.Caution
            RendererActionPhase.Ready,
            RendererActionPhase.Locked,
            -> if (lesson.timeLimitSeconds != null) StatusVariant.Caution else StatusVariant.Neutral
        },
        routeSummary = state.flowHint,
        trailingSummary = "总星星 ${state.totalStars}",
        timerChipText = lesson.timeLimitSeconds?.let { "限时 ${formatSupportCountdown(remainingSeconds)}" },
        timerNote = lesson.timeLimitSeconds?.let { "超时会直接结算，本题不计通关。" },
    )
}
