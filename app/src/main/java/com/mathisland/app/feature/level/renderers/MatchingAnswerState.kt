package com.mathisland.app.feature.level.renderers

import com.mathisland.app.domain.model.MatchingGroup
import com.mathisland.app.domain.model.MatchingRound

data class MatchingSelection(
    val roundIndex: Int,
    val groupIndex: Int,
    val leftIndex: Int
)

data class MatchingAnswerState(
    val currentRoundIndex: Int = 0,
    val selectedLeft: MatchingSelection? = null,
    val assignmentsByRound: Map<Int, Map<Int, Map<Int, Int>>> = emptyMap()
) {
    val selectedLeftIndex: Int?
        get() = selectedLeft?.takeIf { it.roundIndex == 0 && it.groupIndex == 0 }?.leftIndex

    val assignments: Map<Int, Int>
        get() = assignmentsForGroup(roundIndex = 0, groupIndex = 0)

    fun selectLeft(index: Int): MatchingAnswerState =
        selectLeft(groupIndex = 0, leftIndex = index)

    fun selectLeft(groupIndex: Int, leftIndex: Int): MatchingAnswerState =
        copy(
            selectedLeft = if (selectedLeft?.groupIndex == groupIndex && selectedLeft.leftIndex == leftIndex) {
                null
            } else {
                MatchingSelection(currentRoundIndex, groupIndex, leftIndex)
            }
        )

    fun assignTo(rightIndex: Int): MatchingAnswerState {
        return assignTo(groupIndex = 0, rightIndex = rightIndex)
    }

    fun assignTo(groupIndex: Int, rightIndex: Int): MatchingAnswerState {
        val currentSelection = selectedLeft ?: return this
        if (currentSelection.roundIndex != currentRoundIndex || currentSelection.groupIndex != groupIndex) return this
        val groupAssignments = assignmentsForGroup(currentRoundIndex, groupIndex)
            .filterKeys { it != currentSelection.leftIndex }
            .filterValues { it != rightIndex }
        val roundAssignments = assignmentsForRound(currentRoundIndex) + (groupIndex to (groupAssignments + (currentSelection.leftIndex to rightIndex)))
        val nextAssignments = assignmentsByRound + (currentRoundIndex to roundAssignments)
        return copy(
            selectedLeft = null,
            assignmentsByRound = nextAssignments
        )
    }

    fun isComplete(leftCount: Int): Boolean = assignments.size == leftCount

    fun isGroupSetComplete(groups: List<MatchingGroup>): Boolean =
        isRoundComplete(
            MatchingRound(
                title = "",
                prompt = "",
                groups = groups
            )
        )

    fun isRoundComplete(round: MatchingRound): Boolean =
        round.groups.indices.all { groupIndex ->
            assignmentsForGroup(currentRoundIndex, groupIndex).size == round.groups[groupIndex].leftItems.size
        }

    fun isRoundSetComplete(rounds: List<MatchingRound>): Boolean =
        rounds.indices.all { roundIndex ->
            val round = rounds[roundIndex]
            round.groups.indices.all { groupIndex ->
                assignmentsForGroup(roundIndex, groupIndex).size == round.groups[groupIndex].leftItems.size
            }
        }

    fun canAdvanceRound(rounds: List<MatchingRound>): Boolean =
        currentRoundIndex < rounds.lastIndex && isRoundComplete(rounds[currentRoundIndex])

    fun advanceRound(rounds: List<MatchingRound>): MatchingAnswerState =
        if (!canAdvanceRound(rounds)) {
            this
        } else {
            copy(
                currentRoundIndex = currentRoundIndex + 1,
                selectedLeft = null
            )
        }

    fun encodedAnswer(leftItems: List<String>, rightItems: List<String>): String =
        encodeGroupsForSingleRound(
            listOf(
                MatchingGroup(
                    title = "",
                    leftItems = leftItems,
                    rightItems = rightItems
                )
            )
        )

    fun encodeGroupsForSingleRound(groups: List<MatchingGroup>): String =
        encodeGroups(roundIndex = 0, groups = groups)

    fun encodeRounds(rounds: List<MatchingRound>): String =
        rounds.indices.joinToString(">>>") { roundIndex ->
            encodeGroups(roundIndex = roundIndex, groups = rounds[roundIndex].groups)
        }

    private fun encodeGroups(roundIndex: Int, groups: List<MatchingGroup>): String =
        groups.mapIndexed { groupIndex, group ->
            group.leftItems.indices.joinToString(",") { leftIndex ->
                val rightIndex = assignmentsForGroup(roundIndex, groupIndex)[leftIndex]
                    ?: error("Missing assignment for round $roundIndex group $groupIndex left index $leftIndex")
                "${group.leftItems[leftIndex]}=${group.rightItems[rightIndex]}"
            }
        }.joinToString("||")

    fun assignmentsForRound(roundIndex: Int): Map<Int, Map<Int, Int>> =
        assignmentsByRound[roundIndex].orEmpty()

    fun assignmentsForGroup(roundIndex: Int, groupIndex: Int): Map<Int, Int> =
        assignmentsForRound(roundIndex)[groupIndex].orEmpty()

    fun completedPairs(groups: List<MatchingGroup>): Int =
        groups.indices.sumOf { assignmentsForGroup(currentRoundIndex, it).size }

    fun totalPairs(groups: List<MatchingGroup>): Int =
        groups.sumOf { it.leftItems.size }
}
