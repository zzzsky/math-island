package com.mathisland.app.feature.level

import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState

data class LevelSupportCardModel(
    val tag: String,
    val title: String,
    val subtitle: String,
    val body: String,
    val tone: LessonStatusTone? = null
)

data class LevelSupportRailState(
    val routeSummary: String,
    val trailingSummary: String,
    val timerChipText: String?,
    val timerNote: String?,
    val cards: List<LevelSupportCardModel>
)

fun levelSupportRailStateFor(
    state: LevelUiState,
    remainingSeconds: Int,
    feedback: AnswerFeedbackUiState?
): LevelSupportRailState {
    val lesson = state.lesson
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
        add(
            LevelSupportCardModel(
                tag = "lesson-question-card",
                title = if (lesson.isReview) "回放线索" else "题目提示",
                subtitle = state.question.prompt,
                body = state.question.hint.ifBlank {
                    if (lesson.isReview) {
                        "先看线索，再重新判断一次。"
                    } else {
                        "先看题目，再提交答案。"
                    }
                }
            )
        )
    }

    return LevelSupportRailState(
        routeSummary = state.flowHint,
        trailingSummary = "总星星 ${state.totalStars}",
        timerChipText = lesson.timeLimitSeconds?.let { "限时 ${formatSupportCountdown(remainingSeconds)}" },
        timerNote = lesson.timeLimitSeconds?.let { "倒计时结束会直接结算，本轮不计通关。" },
        cards = cards
    )
}

private fun formatSupportCountdown(totalSeconds: Int): String {
    val safeSeconds = totalSeconds.coerceAtLeast(0)
    val minutes = safeSeconds / 60
    val seconds = safeSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
