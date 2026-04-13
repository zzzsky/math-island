package com.mathisland.app.feature.level.renderers

import com.mathisland.app.domain.model.Question
import com.mathisland.app.domain.model.StepFeedbackHint
import com.mathisland.app.domain.model.StepPresentation
import com.mathisland.app.ui.theme.StatusVariant

private const val AnyAnswerBranch = "*"

data class MultiStepRecapFeedbackState(
    val chipText: String,
    val body: String,
    val chipVariant: StatusVariant,
    val autoExpand: Boolean = false
)

fun multiStepPromptFor(question: Question, state: MultiStepAnswerState): String {
    val stepIndex = state.currentStepIndex(stepCountFor(question))
    return multiStepPromptFor(question, state, stepIndex)
}

fun multiStepChoicesFor(question: Question, state: MultiStepAnswerState): List<String> {
    val stepIndex = state.currentStepIndex(stepCountFor(question))
    val branchKey = state.currentBranchKey(question, stepIndex)
    return question.stepBranchChoices[branchKey]
        ?: question.stepChoices.getOrElse(stepIndex) { emptyList() }
}

fun multiStepPresentationFor(question: Question, state: MultiStepAnswerState): StepPresentation {
    val stepIndex = state.currentStepIndex(stepCountFor(question))
    return multiStepPresentationFor(question, state, stepIndex)
}

fun multiStepAnswerLabelFor(
    question: Question,
    state: MultiStepAnswerState,
    answeredStepIndex: Int
): String = multiStepPresentationFor(
    question = question,
    stepIndex = answeredStepIndex,
    branchKey = state.currentBranchKey(question, answeredStepIndex)
).answerLabel.ifBlank { "步骤 ${answeredStepIndex + 1}" }

fun nextBranchKeyFor(
    question: Question,
    state: MultiStepAnswerState,
    answer: String
): String? {
    val stepCount = stepCountFor(question)
    val currentStepIndex = state.currentStepIndex(stepCount)
    val nextStepIndex = currentStepIndex + 1
    if (nextStepIndex >= stepCount) return null

    val currentBranchKey = state.currentBranchKey(question, currentStepIndex)
    val nextRule = question.stepBranchRules[currentBranchKey]
        .orEmpty()
        .firstOrNull { it.whenAnswer == answer }
        ?: question.stepBranchRules[currentBranchKey]
            .orEmpty()
            .firstOrNull { it.whenAnswer == AnyAnswerBranch }

    return nextRule?.nextBranchKey
        ?: question.stepBranchKeys.getOrNull(nextStepIndex)
        ?: currentBranchKey
}

fun stepCountFor(question: Question): Int =
    minOf(question.stepPrompts.size, question.stepChoices.size)

fun multiStepStateForSubmittedAnswer(
    question: Question,
    submittedAnswer: String?
): MultiStepAnswerState {
    val stepCount = stepCountFor(question)
    if (stepCount == 0 || submittedAnswer.isNullOrBlank()) return MultiStepAnswerState()

    return submittedAnswer
        .split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .take(stepCount)
        .fold(MultiStepAnswerState()) { state, answer ->
            state.advance(
                answer = answer,
                stepCount = stepCount,
                nextBranchKey = nextBranchKeyFor(question, state, answer)
            )
        }
}

fun multiStepRecapFeedbackStateFor(
    feedbackKind: AnswerFeedbackKind,
    hint: StepFeedbackHint?
): MultiStepRecapFeedbackState = when (feedbackKind) {
    AnswerFeedbackKind.Correct -> MultiStepRecapFeedbackState(
        chipText = hint?.correctLabel ?: "步骤通过",
        body = hint?.correctBody ?: "这一步的判断已经成立，可以继续保留这次思路。",
        chipVariant = StatusVariant.Success
    )

    AnswerFeedbackKind.Incorrect -> MultiStepRecapFeedbackState(
        chipText = hint?.incorrectLabel ?: "建议重看",
        body = hint?.incorrectBody ?: "先回到这一步重新核对，再从头完成整道题。",
        chipVariant = StatusVariant.Caution,
        autoExpand = hint?.expandOnIncorrect == true
    )

    AnswerFeedbackKind.TimedWarning,
    AnswerFeedbackKind.TimeoutExpired,
    -> MultiStepRecapFeedbackState(
        chipText = hint?.timeoutLabel ?: "本题结束",
        body = hint?.timeoutBody ?: "这一题已经按当前步骤结算，先看过程，再进入下一题。",
        chipVariant = StatusVariant.Caution,
        autoExpand = hint?.expandOnTimeout == true
    )
}

fun multiStepPromptFor(
    question: Question,
    state: MultiStepAnswerState,
    stepIndex: Int
): String {
    val branchKey = state.currentBranchKey(question, stepIndex)
    return question.stepBranchPrompts[branchKey]
        ?: question.stepPrompts.getOrElse(stepIndex) { "" }
}

fun multiStepPresentationFor(
    question: Question,
    state: MultiStepAnswerState,
    stepIndex: Int
): StepPresentation {
    val branchKey = state.currentBranchKey(question, stepIndex)
    return multiStepPresentationFor(question, stepIndex, branchKey)
}

private fun multiStepPresentationFor(
    question: Question,
    stepIndex: Int,
    branchKey: String
): StepPresentation = question.stepBranchPresentations[branchKey]
    ?: question.stepPresentations.getOrNull(stepIndex)
    ?: StepPresentation(
        stageTitle = "步骤 ${stepIndex + 1}",
        supportText = "",
        answerLabel = "步骤 ${stepIndex + 1}"
    )
