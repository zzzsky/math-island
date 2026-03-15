package com.mathisland.app.domain.usecase

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.data.progress.ProgressRepository

data class MapLessonState(
    val id: String,
    val title: String,
    val completed: Boolean,
    val enabled: Boolean
)

data class MapIslandState(
    val id: String,
    val title: String,
    val unlocked: Boolean,
    val progress: Float,
    val lessons: List<MapLessonState>
)

data class MapState(
    val totalStars: Int,
    val islands: List<MapIslandState>
)

class GetMapStateUseCase(
    private val repository: ProgressRepository,
    private val controller: MathIslandGameController
) {
    operator fun invoke(): MapState {
        val progress = repository.load()
        return MapState(
            totalStars = progress.totalStars,
            islands = controller.islands.map { island ->
                val unlocked = progress.unlockedIslandIds.contains(island.id)
                MapIslandState(
                    id = island.id,
                    title = island.title,
                    unlocked = unlocked,
                    progress = controller.islandProgress(progress, island),
                    lessons = island.lessons.map { lesson ->
                        MapLessonState(
                            id = lesson.id,
                            title = lesson.title,
                            completed = controller.lessonCompleted(progress, lesson),
                            enabled = unlocked
                        )
                    }
                )
            }
        )
    }
}
