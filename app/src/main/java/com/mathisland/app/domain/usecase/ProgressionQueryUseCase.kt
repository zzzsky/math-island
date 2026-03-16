package com.mathisland.app.domain.usecase

import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.Island
import com.mathisland.app.domain.model.Lesson

class ProgressionQueryUseCase(
    private val islands: List<Island>
) {
    fun nextPlayableLesson(state: GameProgress): Lesson? {
        islands
            .filter { island -> state.unlockedIslandIds.contains(island.id) }
            .forEach { island ->
                island.lessons.firstOrNull { lesson ->
                    !state.completedLessonIds.contains(lesson.id)
                }?.let { lesson ->
                    return lesson
                }
            }

        return null
    }

    fun islandProgress(state: GameProgress, island: Island): Float {
        val completed = island.lessons.count { lesson -> state.completedLessonIds.contains(lesson.id) }
        return completed.toFloat() / island.lessons.size.toFloat()
    }

    fun lessonCompleted(state: GameProgress, lesson: Lesson): Boolean =
        state.completedLessonIds.contains(lesson.id)

    fun computeUnlockedIslands(completedLessonIds: Set<String>): Set<String> {
        val unlocked = mutableSetOf<String>()
        islands.forEachIndexed { index, island ->
            if (index == 0) {
                unlocked += island.id
            } else {
                val previousIsland = islands[index - 1]
                val previousCleared = previousIsland.lessons.all { lesson ->
                    completedLessonIds.contains(lesson.id)
                }
                if (previousCleared) {
                    unlocked += island.id
                }
            }
        }
        return unlocked
    }
}
