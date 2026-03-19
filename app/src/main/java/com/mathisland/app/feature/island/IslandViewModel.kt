package com.mathisland.app.feature.island

import com.mathisland.app.feature.map.MapTabletIslandUiState
import com.mathisland.app.feature.map.MapTabletUiState
import com.mathisland.app.feature.map.islandById

data class IslandUiState(
    val island: MapTabletIslandUiState,
    val handoffLabel: String? = null,
    val handoffTitle: String? = null,
    val handoffBody: String? = null,
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
        val handoffVisible = feedback != null && feedback.highlightedIslandId == island.id
        return IslandUiState(
            island = island,
            handoffLabel = feedback?.summaryLabel?.takeIf { handoffVisible },
            handoffTitle = feedback?.summaryTitle?.takeIf { handoffVisible },
            handoffBody = feedback?.summaryBody?.takeIf { handoffVisible }
        )
    }
}
