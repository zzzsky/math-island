package com.mathisland.app.feature.level

import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState

enum class LessonStatusTone {
    Neutral,
    Highlight,
    Retry,
    Confirmed,
    Warning
}

fun lessonStatusToneFor(feedback: AnswerFeedbackUiState?): LessonStatusTone {
    return when (feedback?.kind) {
        AnswerFeedbackKind.Correct -> LessonStatusTone.Confirmed
        AnswerFeedbackKind.Incorrect -> LessonStatusTone.Retry
        AnswerFeedbackKind.TimedWarning -> LessonStatusTone.Warning
        AnswerFeedbackKind.TimeoutExpired -> LessonStatusTone.Warning
        null -> LessonStatusTone.Neutral
    }
}
