package com.mathisland.app.feature.map

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.domain.model.GameProgress

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
    val lessons: List<MapTabletLessonUiState>,
    val handoffKind: MapFeedbackKind? = null,
    val handoffBadge: String? = null,
    val handoffBody: String? = null
)

data class MapTabletUiState(
    val totalStars: Int,
    val recommendedIslandId: String,
    val feedback: MapFeedbackUiState? = null,
    val islands: List<MapTabletIslandUiState>
)

fun MapTabletUiState.islandById(id: String?): MapTabletIslandUiState? =
    islands.firstOrNull { it.id == id }

object MapViewModel {
    fun uiState(
        controller: MathIslandGameController,
        progress: GameProgress,
        feedback: MapFeedbackUiState? = null
    ): MapTabletUiState {
        val islands = controller.islands.map { island ->
            val unlocked = progress.unlockedIslandIds.contains(island.id)
            val completed = island.lessons.all { lesson -> controller.lessonCompleted(progress, lesson) }
            val handoffKind = islandHandoffKind(
                islandId = island.id,
                recommendedIslandId = controller.recommendedLesson(progress)?.islandId,
                feedback = feedback
            )
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
                },
                handoffKind = handoffKind,
                handoffBadge = handoffKind?.let { mapReturnCopy(it).listBadge },
                handoffBody = handoffKind?.let { mapReturnCopy(it).listBody }
            )
        }
        val recommendedIslandId = controller.recommendedLesson(progress)?.islandId
            ?: islands.firstOrNull { island -> island.unlocked && !island.completed }?.id
            ?: islands.firstOrNull()?.id
            ?: ""
        return MapTabletUiState(
            totalStars = progress.totalStars,
            recommendedIslandId = recommendedIslandId,
            feedback = feedback,
            islands = islands
        )
    }

    private fun islandHandoffKind(
        islandId: String,
        recommendedIslandId: String?,
        feedback: MapFeedbackUiState?
    ): MapFeedbackKind? {
        val kind = feedback?.kind ?: return null
        val appliesToIsland = when (kind) {
            MapFeedbackKind.NewIsland -> feedback.highlightedIslandId == islandId
            MapFeedbackKind.Chest,
            MapFeedbackKind.Replay,
            MapFeedbackKind.Progress -> recommendedIslandId == islandId
        }
        if (!appliesToIsland) {
            return null
        }
        return kind
    }
}
