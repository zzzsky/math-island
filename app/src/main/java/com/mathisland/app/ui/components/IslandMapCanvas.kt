package com.mathisland.app.ui.components

import androidx.compose.runtime.Composable
import com.mathisland.app.feature.map.MapSceneCanvas
import com.mathisland.app.feature.map.MapTabletIslandUiState

@Composable
fun IslandMapCanvas(
    islands: List<MapTabletIslandUiState>,
    selectedIslandId: String?,
    highlightedIslandId: String? = null,
    onSelectIsland: (String) -> Unit
) {
    MapSceneCanvas(
        islands = islands,
        selectedIslandId = selectedIslandId,
        highlightedIslandId = highlightedIslandId,
        onSelectIsland = onSelectIsland
    )
}
