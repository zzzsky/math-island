package com.mathisland.app.feature.level.renderers

import com.mathisland.app.domain.model.Question

data class MultiStepAnswerState(
    val answers: List<String> = emptyList(),
    val branchByStep: Map<Int, String> = emptyMap()
) {
    fun currentStepIndex(stepCount: Int): Int = answers.size.coerceAtMost((stepCount - 1).coerceAtLeast(0))

    fun isComplete(stepCount: Int): Boolean = stepCount > 0 && answers.size >= stepCount

    fun currentBranchKey(question: Question, stepIndex: Int): String =
        branchByStep[stepIndex]
            ?: question.stepBranchKeys.getOrNull(stepIndex)
            ?: "step-$stepIndex"

    fun advance(answer: String, stepCount: Int, nextBranchKey: String? = null): MultiStepAnswerState =
        if (isComplete(stepCount)) {
            this
        } else {
            copy(
                answers = answers + answer,
                branchByStep = if (nextBranchKey == null) {
                    branchByStep
                } else {
                    branchByStep + ((answers.size + 1) to nextBranchKey)
                }
            )
        }

    fun reset(): MultiStepAnswerState = copy(
        answers = emptyList(),
        branchByStep = emptyMap()
    )

    fun encodedAnswer(stepCount: Int): String {
        check(isComplete(stepCount)) { "Missing answers for some steps." }
        return answers.take(stepCount).joinToString(",")
    }
}
