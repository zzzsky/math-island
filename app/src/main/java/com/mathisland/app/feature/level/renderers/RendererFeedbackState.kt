package com.mathisland.app.feature.level.renderers

enum class OptionFeedbackTone {
    Neutral,
    Retry,
    Confirmed
}

data class OptionFeedbackState(
    val tone: OptionFeedbackTone,
    val supportingText: String? = null
)

enum class NumberPadDisplayTone {
    Idle,
    Ready,
    Retry,
    Confirmed
}

data class NumberPadDisplayState(
    val tone: NumberPadDisplayTone,
    val displayText: String,
    val supportingText: String
)

fun optionFeedbackStateFor(
    choice: String,
    feedback: AnswerFeedbackUiState?
): OptionFeedbackState {
    if (feedback?.submittedAnswer != choice) {
        return OptionFeedbackState(OptionFeedbackTone.Neutral)
    }
    return when (feedback.kind) {
        AnswerFeedbackKind.Incorrect -> OptionFeedbackState(
            tone = OptionFeedbackTone.Retry,
            supportingText = "这是刚才的尝试"
        )

        AnswerFeedbackKind.Correct -> OptionFeedbackState(
            tone = OptionFeedbackTone.Confirmed,
            supportingText = "这就是本次提交"
        )

        AnswerFeedbackKind.TimedWarning -> OptionFeedbackState(OptionFeedbackTone.Neutral)
    }
}

fun numberPadDisplayStateFor(
    enteredAnswer: String,
    feedback: AnswerFeedbackUiState?
): NumberPadDisplayState {
    val submittedAnswer = feedback?.submittedAnswer
    return when (feedback?.kind) {
        AnswerFeedbackKind.Correct -> NumberPadDisplayState(
            tone = NumberPadDisplayTone.Confirmed,
            displayText = submittedAnswer ?: enteredAnswer.ifEmpty { "请输入答案" },
            supportingText = "答案已确认，马上进入下一题"
        )

        AnswerFeedbackKind.Incorrect -> NumberPadDisplayState(
            tone = NumberPadDisplayTone.Retry,
            displayText = submittedAnswer ?: enteredAnswer.ifEmpty { "请输入答案" },
            supportingText = "先检查刚才的输入，再试一次"
        )

        AnswerFeedbackKind.TimedWarning,
        null,
        -> if (enteredAnswer.isNotEmpty()) {
            NumberPadDisplayState(
                tone = NumberPadDisplayTone.Ready,
                displayText = enteredAnswer,
                supportingText = "已输入 ${enteredAnswer.length} 位，准备提交"
            )
        } else {
            NumberPadDisplayState(
                tone = NumberPadDisplayTone.Idle,
                displayText = "请输入答案",
                supportingText = "输入完成后再提交"
            )
        }
    }
}
