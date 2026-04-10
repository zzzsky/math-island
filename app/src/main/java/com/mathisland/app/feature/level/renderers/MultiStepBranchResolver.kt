package com.mathisland.app.feature.level.renderers

import com.mathisland.app.domain.model.Question
import com.mathisland.app.domain.model.StepPresentation

private const val AnyAnswerBranch = "*"

fun multiStepPromptFor(question: Question, state: MultiStepAnswerState): String {
    val stepIndex = state.currentStepIndex(stepCountFor(question))
    val branchKey = state.currentBranchKey(question, stepIndex)
    return question.stepBranchPrompts[branchKey]
        ?: question.stepPrompts.getOrElse(stepIndex) { "" }
}

fun multiStepChoicesFor(question: Question, state: MultiStepAnswerState): List<String> {
    val stepIndex = state.currentStepIndex(stepCountFor(question))
    val branchKey = state.currentBranchKey(question, stepIndex)
    return question.stepBranchChoices[branchKey]
        ?: question.stepChoices.getOrElse(stepIndex) { emptyList() }
}

fun multiStepPresentationFor(question: Question, state: MultiStepAnswerState): StepPresentation {
    val stepIndex = state.currentStepIndex(stepCountFor(question))
    val branchKey = state.currentBranchKey(question, stepIndex)
    return multiStepPresentationFor(question, stepIndex, branchKey)
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
