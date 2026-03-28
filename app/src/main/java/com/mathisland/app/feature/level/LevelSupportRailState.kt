package com.mathisland.app.feature.level

import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState
import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.RendererActionPhase
import com.mathisland.app.feature.level.renderers.rendererActionStateFor
import com.mathisland.app.ui.theme.StatusVariant

data class LevelSupportCardModel(
    val tag: String,
    val title: String,
    val subtitle: String,
    val body: String,
    val tone: LessonStatusTone? = null,
    val badgeText: String? = null,
    val badgeVariant: StatusVariant = StatusVariant.Neutral
)

data class LevelSupportRailState(
    val headerBadgeText: String,
    val headerBadgeVariant: StatusVariant,
    val cards: List<LevelSupportCardModel>
)

fun levelSupportRailStateFor(
    state: LevelUiState,
    remainingSeconds: Int,
    feedback: AnswerFeedbackUiState?
): LevelSupportRailState {
    val lesson = state.lesson
    val actionState = rendererActionStateFor(
        feedback = feedback,
        inputEnabled = feedback?.kind != AnswerFeedbackKind.Correct && feedback?.kind != AnswerFeedbackKind.TimeoutExpired,
        isReview = lesson.isReview
    )
    val copy = lessonAttemptCopyFor(
        lessonIsReview = lesson.isReview,
        feedback = feedback,
        phase = actionState.phase
    )
    val cards = buildList {
        val attempt = attemptStatusCardStateFor(
            lesson = lesson,
            feedback = feedback
        )
        add(
            LevelSupportCardModel(
                tag = "lesson-attempt-status",
                title = attempt.title,
                subtitle = attempt.subtitle,
                body = attempt.body,
                tone = attempt.tone
            )
        )
        add(
            LevelSupportCardModel(
                tag = "lesson-next-step-card",
                title = "下一步",
                subtitle = copy.nextStepSubtitle,
                body = copy.nextStepBody,
                tone = when (actionState.phase) {
                    RendererActionPhase.Retry -> LessonStatusTone.Retry
                    RendererActionPhase.Confirmed -> LessonStatusTone.Confirmed
                    RendererActionPhase.TimeoutExpired -> LessonStatusTone.Warning
                    RendererActionPhase.Ready,
                    RendererActionPhase.Locked,
                    -> if (lesson.timeLimitSeconds != null) LessonStatusTone.Highlight else LessonStatusTone.Neutral
                },
                badgeText = when (actionState.phase) {
                    else -> lessonActionBadgeTextFor(actionState.phase)
                },
                badgeVariant = lessonActionBadgeVariantFor(actionState.phase)
            )
        )
        if (lesson.timeLimitSeconds != null) {
            val timer = timerPressureCardStateFor(
                lesson = lesson,
                remainingSeconds = remainingSeconds
            )
            add(
                LevelSupportCardModel(
                    tag = "lesson-timer-status",
                    title = timer.title,
                    subtitle = timer.subtitle,
                    body = timer.body,
                    tone = timer.tone
                )
            )
        }
    }

    return LevelSupportRailState(
        headerBadgeText = lessonRailHeaderBadgeTextFor(actionState.phase, lesson.isReview),
        headerBadgeVariant = lessonRailHeaderBadgeVariantFor(actionState.phase),
        cards = cards
    )
}

internal fun formatSupportCountdown(totalSeconds: Int): String {
    val safeSeconds = totalSeconds.coerceAtLeast(0)
    val minutes = safeSeconds / 60
    val seconds = safeSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
