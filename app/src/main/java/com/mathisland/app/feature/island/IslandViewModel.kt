package com.mathisland.app.feature.island

import com.mathisland.app.feature.map.MapTabletIslandUiState
import com.mathisland.app.feature.map.MapTabletUiState

data class IslandUiState(
    val island: MapTabletIslandUiState
)

object IslandViewModel {
    fun uiState(
        mapState: MapTabletUiState,
        selectedIslandId: String?
    ): IslandUiState {
        val island = mapState.islands.firstOrNull { it.id == selectedIslandId }
            ?: mapState.islands.firstOrNull { it.id == mapState.recommendedIslandId }
            ?: mapState.islands.first()
        return IslandUiState(island = island)
    }
}
