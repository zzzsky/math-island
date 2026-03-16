package com.mathisland.app.feature.map

import androidx.compose.runtime.Composable
import com.mathisland.app.ui.components.IslandMapCanvas

@Deprecated(
    message = "Use ui.components.IslandMapCanvas as the shared map primitive.",
    replaceWith = ReplaceWith("com.mathisland.app.ui.components.IslandMapCanvas(islands, selectedIslandId, highlightedIslandId, onSelectIsland)")
)
@Composable
fun MapSceneCanvas(
    islands: List<MapTabletIslandUiState>,
    selectedIslandId: String?,
    highlightedIslandId: String? = null,
    onSelectIsland: (String) -> Unit
) {
    IslandMapCanvas(
        islands = islands,
        selectedIslandId = selectedIslandId,
        highlightedIslandId = highlightedIslandId,
        onSelectIsland = onSelectIsland
    )
}
