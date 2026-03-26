package com.mathisland.app.feature.level

import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState

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
    return when (feedback?.kind) {
        AnswerFeedbackKind.Correct -> LevelStatusCardState(
            tone = LessonStatusTone.Confirmed,
            title = "当前状态",
            subtitle = "已确认",
            body = feedback.body
        )

        AnswerFeedbackKind.Incorrect -> LevelStatusCardState(
            tone = LessonStatusTone.Retry,
            title = "当前状态",
            subtitle = "正在重试",
            body = feedback.body
        )

        AnswerFeedbackKind.TimedWarning -> LevelStatusCardState(
            tone = LessonStatusTone.Warning,
            title = "当前状态",
            subtitle = "限时进行中",
            body = feedback.body
        )

        AnswerFeedbackKind.TimeoutExpired -> LevelStatusCardState(
            tone = LessonStatusTone.Warning,
            title = "当前状态",
            subtitle = "已超时",
            body = feedback.body
        )

        null -> LevelStatusCardState(
            tone = LessonStatusTone.Neutral,
            title = "当前状态",
            subtitle = if (lesson.isReview) "先看线索" else "准备作答",
            body = if (lesson.isReview) {
                "这节是回放题，先读提示再作答。"
            } else {
                "先看题目，再提交这次答案。"
            }
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
        "最后冲刺" -> "只剩 ${formatCountdownText(remainingSeconds)}，优先快速提交，不要停在这一题。"
        "时间过半" -> "还剩 ${formatCountdownText(remainingSeconds)}，先交当前最有把握的答案。"
        else -> "当前还剩 ${formatCountdownText(remainingSeconds)}，保持节奏继续推进。"
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
