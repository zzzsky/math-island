package com.mathisland.app.feature.level.renderers

data class FillBlankAnswerState(
    val selectedOptionIndex: Int? = null,
    val assignments: Map<Int, Int> = emptyMap()
) {
    fun selectOption(index: Int): FillBlankAnswerState =
        copy(selectedOptionIndex = if (selectedOptionIndex == index) null else index)

    fun assignTo(slotIndex: Int): FillBlankAnswerState {
        val optionIndex = selectedOptionIndex ?: return this
        val clearedAssignments = assignments
            .filterKeys { it != slotIndex }
            .filterValues { it != optionIndex }
        return copy(
            selectedOptionIndex = null,
            assignments = clearedAssignments + (slotIndex to optionIndex)
        )
    }

    fun isComplete(slotCount: Int): Boolean = assignments.size == slotCount

    fun encodedAnswer(options: List<String>, slotCount: Int): String =
        (0 until slotCount).joinToString(",") { slotIndex ->
            val optionIndex = assignments[slotIndex]
                ?: error("Missing assignment for slot index $slotIndex")
            options[optionIndex]
        }
}
