package com.mathisland.app.feature.level

import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState
import com.mathisland.app.feature.level.renderers.RendererActionPhase

data class LevelStatusCardState(
    val tone: LessonStatusTone,
    val title: String,
    val subtitle: String,
    val body: String
)

fun attemptStatusCardStateFor(
    lesson: Lesson,
    feedback: AnswerFeedbackUiState?
): LevelStatusCardState {
    val copy = lessonAttemptCopyFor(
        lessonIsReview = lesson.isReview,
        feedback = feedback,
        phase = when (feedback?.kind) {
            AnswerFeedbackKind.Correct -> RendererActionPhase.Confirmed
            AnswerFeedbackKind.Incorrect -> RendererActionPhase.Retry
            AnswerFeedbackKind.TimedWarning -> RendererActionPhase.Ready
            AnswerFeedbackKind.TimeoutExpired -> RendererActionPhase.TimeoutExpired
            null -> RendererActionPhase.Ready
        }
    )
    return when (feedback?.kind) {
        AnswerFeedbackKind.Correct -> LevelStatusCardState(
            tone = LessonStatusTone.Confirmed,
            title = "当前状态",
            subtitle = copy.statusSubtitle,
            body = copy.statusBody
        )

        AnswerFeedbackKind.Incorrect -> LevelStatusCardState(
            tone = LessonStatusTone.Retry,
            title = "当前状态",
            subtitle = copy.statusSubtitle,
            body = copy.statusBody
        )

        AnswerFeedbackKind.TimedWarning -> LevelStatusCardState(
            tone = LessonStatusTone.Warning,
            title = "当前状态",
            subtitle = copy.statusSubtitle,
            body = copy.statusBody
        )

        AnswerFeedbackKind.TimeoutExpired -> LevelStatusCardState(
            tone = LessonStatusTone.Warning,
            title = "当前状态",
            subtitle = copy.statusSubtitle,
            body = copy.statusBody
        )

        null -> LevelStatusCardState(
            tone = LessonStatusTone.Neutral,
            title = "当前状态",
            subtitle = copy.statusSubtitle,
            body = copy.statusBody
        )
    }
}

fun timerPressureCardStateFor(
    lesson: Lesson,
    remainingSeconds: Int
): LevelStatusCardState {
    val totalSeconds = lesson.timeLimitSeconds ?: remainingSeconds
    val halfThreshold = kotlin.math.floor(totalSeconds * 0.5f).toInt()
    val subtitle = when {
        remainingSeconds <= LevelMotionTokens.TimeoutWarningFinalSeconds -> "最后冲刺"
        halfThreshold > LevelMotionTokens.TimeoutWarningFinalSeconds && remainingSeconds <= halfThreshold -> "时间过半"
        else -> "保持节奏"
    }
    val body = when (subtitle) {
        "最后冲刺" -> "只剩 ${formatCountdownText(remainingSeconds)}，先交最有把握的答案。"
        "时间过半" -> "还剩 ${formatCountdownText(remainingSeconds)}，优先提交当前答案。"
        else -> "还剩 ${formatCountdownText(remainingSeconds)}，继续推进。"
    }
    return LevelStatusCardState(
        tone = when (subtitle) {
            "最后冲刺" -> LessonStatusTone.Warning
            "时间过半" -> LessonStatusTone.Highlight
            else -> LessonStatusTone.Neutral
        },
        title = "倒计时节奏",
        subtitle = subtitle,
        body = body
    )
}

private fun formatCountdownText(totalSeconds: Int): String {
    val safeSeconds = totalSeconds.coerceAtLeast(0)
    val minutes = safeSeconds / 60
    val seconds = safeSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
