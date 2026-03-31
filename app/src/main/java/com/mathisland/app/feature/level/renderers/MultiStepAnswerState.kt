package com.mathisland.app.feature.level.renderers

data class MultiStepAnswerState(
    val answers: List<String> = emptyList()
) {
    fun currentStepIndex(stepCount: Int): Int = answers.size.coerceAtMost((stepCount - 1).coerceAtLeast(0))

    fun isComplete(stepCount: Int): Boolean = stepCount > 0 && answers.size >= stepCount

    fun advance(answer: String, stepCount: Int): MultiStepAnswerState =
        if (isComplete(stepCount)) {
            this
        } else {
            copy(answers = answers + answer)
        }

    fun reset(): MultiStepAnswerState = copy(answers = emptyList())

    fun encodedAnswer(stepCount: Int): String {
        check(isComplete(stepCount)) { "Missing answers for some steps." }
        return answers.take(stepCount).joinToString(",")
    }
}
