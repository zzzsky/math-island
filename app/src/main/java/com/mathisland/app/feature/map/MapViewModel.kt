package com.mathisland.app.feature.map

import com.mathisland.app.GameProgress
import com.mathisland.app.MathIslandGameController

data class MapTabletLessonUiState(
    val id: String,
    val title: String,
    val summary: String,
    val completed: Boolean,
    val enabled: Boolean
)

data class MapTabletIslandUiState(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val unlocked: Boolean,
    val completed: Boolean,
    val progress: Float,
    val lessons: List<MapTabletLessonUiState>
)

data class MapTabletUiState(
    val totalStars: Int,
    val recommendedIslandId: String,
    val islands: List<MapTabletIslandUiState>
)

object MapViewModel {
    fun uiState(
        controller: MathIslandGameController,
        progress: GameProgress
    ): MapTabletUiState {
        val islands = controller.islands.map { island ->
            val unlocked = progress.unlockedIslandIds.contains(island.id)
            val completed = island.lessons.all { lesson -> controller.lessonCompleted(progress, lesson) }
            MapTabletIslandUiState(
                id = island.id,
                title = island.title,
                subtitle = island.subtitle,
                description = island.description,
                unlocked = unlocked,
                completed = completed,
                progress = controller.islandProgress(progress, island),
                lessons = island.lessons.map { lesson ->
                    MapTabletLessonUiState(
                        id = lesson.id,
                        title = lesson.title,
                        summary = lesson.summary,
                        completed = controller.lessonCompleted(progress, lesson),
                        enabled = unlocked
                    )
                }
            )
        }
        val recommendedIslandId = controller.recommendedLesson(progress)?.islandId
            ?: islands.firstOrNull { island -> island.unlocked && !island.completed }?.id
            ?: islands.firstOrNull()?.id
            ?: ""
        return MapTabletUiState(
            totalStars = progress.totalStars,
            recommendedIslandId = recommendedIslandId,
            islands = islands
        )
    }
}
