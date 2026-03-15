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
    val progress: Float,
    val lessons: List<MapTabletLessonUiState>
)

data class MapTabletUiState(
    val totalStars: Int,
    val islands: List<MapTabletIslandUiState>
)

object MapViewModel {
    fun uiState(
        controller: MathIslandGameController,
        progress: GameProgress
    ): MapTabletUiState = MapTabletUiState(
        totalStars = progress.totalStars,
        islands = controller.islands.map { island ->
            val unlocked = progress.unlockedIslandIds.contains(island.id)
            MapTabletIslandUiState(
                id = island.id,
                title = island.title,
                subtitle = island.subtitle,
                description = island.description,
                unlocked = unlocked,
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
    )
}
