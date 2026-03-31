package com.mathisland.app.feature.level.renderers

data class MatchingAnswerState(
    val selectedLeftIndex: Int? = null,
    val assignments: Map<Int, Int> = emptyMap()
) {
    fun selectLeft(index: Int): MatchingAnswerState =
        copy(selectedLeftIndex = if (selectedLeftIndex == index) null else index)

    fun assignTo(rightIndex: Int): MatchingAnswerState {
        val leftIndex = selectedLeftIndex ?: return this
        val clearedAssignments = assignments
            .filterKeys { it != leftIndex }
            .filterValues { it != rightIndex }
        return copy(
            selectedLeftIndex = null,
            assignments = clearedAssignments + (leftIndex to rightIndex)
        )
    }

    fun isComplete(leftCount: Int): Boolean = assignments.size == leftCount

    fun encodedAnswer(leftItems: List<String>, rightItems: List<String>): String =
        leftItems.indices.joinToString(",") { leftIndex ->
            val rightIndex = assignments[leftIndex]
                ?: error("Missing assignment for left index $leftIndex")
            "${leftItems[leftIndex]}=${rightItems[rightIndex]}"
        }
}
