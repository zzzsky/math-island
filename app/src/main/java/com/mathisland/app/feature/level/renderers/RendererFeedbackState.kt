package com.mathisland.app.feature.level.renderers

import com.mathisland.app.ui.theme.StatusVariant

enum class OptionFeedbackTone {
    Neutral,
    Retry,
    Confirmed,
    TimeoutExpired
}

data class OptionFeedbackState(
    val tone: OptionFeedbackTone,
    val supportingText: String? = null,
    val badgeText: String? = null,
    val badgeVariant: StatusVariant = StatusVariant.Neutral
)

enum class NumberPadDisplayTone {
    Idle,
    Ready,
    Retry,
    Confirmed,
    TimeoutExpired
}

data class NumberPadDisplayState(
    val tone: NumberPadDisplayTone,
    val displayText: String,
    val supportingText: String,
    val badgeText: String? = null,
    val badgeVariant: StatusVariant = StatusVariant.Neutral
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
            supportingText = "刚才选了这个",
            badgeText = "重试",
            badgeVariant = StatusVariant.Highlight
        )

        AnswerFeedbackKind.Correct -> OptionFeedbackState(
            tone = OptionFeedbackTone.Confirmed,
            supportingText = "这次答对了",
            badgeText = "已确认",
            badgeVariant = StatusVariant.Success
        )

        AnswerFeedbackKind.TimeoutExpired -> OptionFeedbackState(
            tone = OptionFeedbackTone.TimeoutExpired,
            supportingText = "这次尝试超时",
            badgeText = "已超时",
            badgeVariant = StatusVariant.Caution
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
            supportingText = "答案已确认，马上进入下一题",
            badgeText = "已确认",
            badgeVariant = StatusVariant.Success
        )

        AnswerFeedbackKind.Incorrect -> NumberPadDisplayState(
            tone = NumberPadDisplayTone.Retry,
            displayText = submittedAnswer ?: enteredAnswer.ifEmpty { "请输入答案" },
            supportingText = "先检查这次输入，再试一次",
            badgeText = "重试中",
            badgeVariant = StatusVariant.Highlight
        )

        AnswerFeedbackKind.TimeoutExpired -> NumberPadDisplayState(
            tone = NumberPadDisplayTone.TimeoutExpired,
            displayText = submittedAnswer ?: enteredAnswer.ifEmpty { "请输入答案" },
            supportingText = "本题已超时，直接看下一题。",
            badgeText = "已超时",
            badgeVariant = StatusVariant.Caution
        )

        AnswerFeedbackKind.TimedWarning,
        null,
        -> if (enteredAnswer.isNotEmpty()) {
            NumberPadDisplayState(
                tone = NumberPadDisplayTone.Ready,
                displayText = enteredAnswer,
                supportingText = "已输入 ${enteredAnswer.length} 位，准备提交",
                badgeText = "准备提交",
                badgeVariant = StatusVariant.Recommended
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
