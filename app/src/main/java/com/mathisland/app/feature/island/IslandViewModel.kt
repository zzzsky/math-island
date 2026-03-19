package com.mathisland.app.feature.island

import com.mathisland.app.feature.map.MapFeedbackKind
import com.mathisland.app.feature.map.MapTabletIslandUiState
import com.mathisland.app.feature.map.MapTabletLessonUiState
import com.mathisland.app.feature.map.MapTabletUiState
import com.mathisland.app.feature.map.mapReturnCopy

enum class IslandPrimaryActionMode {
    StartLesson,
    OpenChest
}

data class IslandUiState(
    val island: MapTabletIslandUiState,
    val handoffKind: MapFeedbackKind? = null,
    val handoffLabel: String? = null,
    val handoffTitle: String? = null,
    val handoffBody: String? = null,
    val primaryLessonId: String? = null,
    val primaryActionLabel: String? = null,
    val primaryActionMode: IslandPrimaryActionMode = IslandPrimaryActionMode.StartLesson,
)

object IslandViewModel {
    fun uiState(
        mapState: MapTabletUiState,
        selectedIslandId: String?
    ): IslandUiState {
        val island = mapState.islands.firstOrNull { it.id == selectedIslandId }
            ?: mapState.islands.firstOrNull { it.id == mapState.recommendedIslandId }
            ?: mapState.islands.first()
        val feedback = mapState.feedback
        val handoffVisible = feedback != null &&
            (feedback.highlightedIslandId == null || feedback.highlightedIslandId == island.id)
        val primaryLesson = preferredPrimaryLesson(island, feedback?.kind)
        return IslandUiState(
            island = island,
            handoffKind = feedback?.kind?.takeIf { handoffVisible },
            handoffLabel = feedback?.summaryLabel?.takeIf { handoffVisible },
            handoffTitle = feedback?.summaryTitle?.takeIf { handoffVisible },
            handoffBody = feedback?.summaryBody?.takeIf { handoffVisible },
            primaryLessonId = primaryLesson?.id,
            primaryActionLabel = primaryActionLabel(primaryLesson, feedback?.kind),
            primaryActionMode = if (feedback?.kind == MapFeedbackKind.Chest) {
                IslandPrimaryActionMode.OpenChest
            } else {
                IslandPrimaryActionMode.StartLesson
            }
        )
    }

    private fun preferredPrimaryLesson(
        island: MapTabletIslandUiState,
        kind: MapFeedbackKind?
    ): MapTabletLessonUiState? {
        val defaultLesson = island.lessons.firstOrNull { lesson -> lesson.enabled && !lesson.completed }
            ?: island.lessons.firstOrNull()
        if (kind != MapFeedbackKind.Replay) {
            return defaultLesson
        }
        return island.lessons.firstOrNull { lesson ->
            lesson.enabled &&
                !lesson.completed &&
                (
                    lesson.id.contains("review", ignoreCase = true) ||
                        lesson.title.contains("回放") ||
                        lesson.title.contains("复习") ||
                        lesson.summary.contains("回放") ||
                        lesson.summary.contains("复习")
                    )
        } ?: defaultLesson
    }

    private fun primaryActionLabel(
        lesson: MapTabletLessonUiState?,
        kind: MapFeedbackKind?
    ): String? {
        if (lesson == null && kind != MapFeedbackKind.Chest) {
            return null
        }
        return when (kind) {
            MapFeedbackKind.NewIsland -> lesson?.let { "开始主线 ${it.title}" }
            MapFeedbackKind.Chest -> mapReturnCopy(MapFeedbackKind.Chest).summaryLabel
            MapFeedbackKind.Replay -> lesson?.let { "先做回放 ${it.title}" }
            MapFeedbackKind.Progress, null -> lesson?.let {
                if (it.completed) "再次练习 ${it.title}" else "继续 ${it.title}"
            }
        }
    }
}
