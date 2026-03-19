package com.mathisland.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.map.MapTabletIslandUiState
import com.mathisland.app.ui.components.map.MapArtRegistry
import com.mathisland.app.ui.components.map.MapArtSource
import com.mathisland.app.ui.components.map.RoutePainter
import com.mathisland.app.ui.components.map.IslandNodePainter
import com.mathisland.app.ui.components.map.SeaBackdropPainter

@Composable
fun IslandMapCanvas(
    islands: List<MapTabletIslandUiState>,
    selectedIslandId: String?,
    highlightedIslandId: String? = null,
    motionProgress: Float = 0f,
    onSelectIsland: (String) -> Unit,
    artSource: MapArtSource = MapArtRegistry
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(28.dp))
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("map-scene-canvas")
    ) {
        SeaBackdropPainter(
            modifier = Modifier
                .fillMaxSize()
                .testTag("map-sea-backdrop-layer"),
            artSource = artSource
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            islands.forEach { island ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (island.id != islands.first().id) {
                        Box(modifier = Modifier.testTag("map-route-highlight-${island.id}")) {
                            RoutePainter(
                                islandId = island.id,
                                highlighted = highlightedIslandId == island.id,
                                motionProgress = motionProgress,
                                modifier = Modifier.fillMaxWidth(),
                                artSource = artSource
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(if (selectedIslandId == island.id) 78.dp else 68.dp)
                            .clickable { onSelectIsland(island.id) }
                            .testTag("map-node-${island.id}")
                    ) {
                        IslandNodePainter(
                            island = island,
                            selected = selectedIslandId == island.id,
                            highlighted = highlightedIslandId == island.id,
                            motionProgress = motionProgress,
                            artSource = artSource,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    if (highlightedIslandId == island.id) {
                        Text(
                            text = "新路线",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.testTag("map-node-highlight-${island.id}")
                        )
                    }
                    Text(
                        text = when {
                            !island.unlocked -> "待解锁"
                            island.completed -> "已完成"
                            selectedIslandId == island.id -> "当前岛屿"
                            else -> "可探索"
                        },
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.92f),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.testTag("map-node-status-${island.id}")
                    )
                }
            }
        }
    }
}
