package com.mathisland.app.feature.level.renderers

import com.mathisland.app.domain.model.MatchingGroup

data class MatchingSelection(
    val groupIndex: Int,
    val leftIndex: Int
)

data class MatchingAnswerState(
    val selectedLeft: MatchingSelection? = null,
    val assignmentsByGroup: Map<Int, Map<Int, Int>> = emptyMap()
) {
    val selectedLeftIndex: Int?
        get() = selectedLeft?.takeIf { it.groupIndex == 0 }?.leftIndex

    val assignments: Map<Int, Int>
        get() = assignmentsForGroup(0)

    fun selectLeft(index: Int): MatchingAnswerState =
        selectLeft(groupIndex = 0, leftIndex = index)

    fun selectLeft(groupIndex: Int, leftIndex: Int): MatchingAnswerState =
        copy(
            selectedLeft = if (selectedLeft?.groupIndex == groupIndex && selectedLeft.leftIndex == leftIndex) {
                null
            } else {
                MatchingSelection(groupIndex, leftIndex)
            }
        )

    fun assignTo(rightIndex: Int): MatchingAnswerState {
        return assignTo(groupIndex = 0, rightIndex = rightIndex)
    }

    fun assignTo(groupIndex: Int, rightIndex: Int): MatchingAnswerState {
        val currentSelection = selectedLeft ?: return this
        if (currentSelection.groupIndex != groupIndex) return this
        val groupAssignments = assignmentsForGroup(groupIndex)
            .filterKeys { it != currentSelection.leftIndex }
            .filterValues { it != rightIndex }
        val nextAssignments = assignmentsByGroup + (groupIndex to (groupAssignments + (currentSelection.leftIndex to rightIndex)))
        return copy(
            selectedLeft = null,
            assignmentsByGroup = nextAssignments
        )
    }

    fun isComplete(leftCount: Int): Boolean = assignments.size == leftCount

    fun isComplete(groups: List<MatchingGroup>): Boolean =
        groups.indices.all { groupIndex ->
            assignmentsForGroup(groupIndex).size == groups[groupIndex].leftItems.size
        }

    fun encodedAnswer(leftItems: List<String>, rightItems: List<String>): String =
        encodedAnswer(
            listOf(
                MatchingGroup(
                    title = "",
                    leftItems = leftItems,
                    rightItems = rightItems
                )
            )
        )

    fun encodedAnswer(groups: List<MatchingGroup>): String =
        groups.mapIndexed { groupIndex, group ->
            group.leftItems.indices.joinToString(",") { leftIndex ->
                val rightIndex = assignmentsForGroup(groupIndex)[leftIndex]
                    ?: error("Missing assignment for group $groupIndex left index $leftIndex")
                "${group.leftItems[leftIndex]}=${group.rightItems[rightIndex]}"
            }
        }.joinToString("||")

    fun assignmentsForGroup(groupIndex: Int): Map<Int, Int> =
        assignmentsByGroup[groupIndex].orEmpty()

    fun completedPairs(groups: List<MatchingGroup>): Int =
        groups.indices.sumOf { assignmentsForGroup(it).size }

    fun totalPairs(groups: List<MatchingGroup>): Int =
        groups.sumOf { it.leftItems.size }
}
