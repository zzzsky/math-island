package com.mathisland.app.ui.components.map

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.map.MapTabletIslandUiState

private val LockedIsland = Color(0x665C7080)
private val CompletedIsland = Color(0xFF4B6F44)
private val FocusRing = Color(0xFFF4D58D)
private val BaseIsland = Color(0xFF18475A)

@Composable
fun IslandNodePainter(
    island: MapTabletIslandUiState,
    selected: Boolean,
    highlighted: Boolean,
    artSource: MapArtSource = MapArtRegistry,
    modifier: Modifier = Modifier
) {
    val islandArt = MapArtRegistry.resolveIslandArt(island.id, artSource)
    val visualScale by animateFloatAsState(
        targetValue = if (selected || highlighted) 1.05f else 1f,
        animationSpec = tween(durationMillis = 350),
        label = "map-node-visual-scale-${island.id}"
    )

    val nodeBackground = when {
        !island.unlocked -> LockedIsland
        island.completed -> CompletedIsland
        highlighted -> FocusRing
        else -> BaseIsland
    }
    val chipBackground =
        if (selected || highlighted) FocusRing else Color.White.copy(alpha = 0.18f)
    val titleColor =
        if (selected || highlighted) Color(0xFF114B5F) else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(999.dp)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(visualScale),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = islandArt.baseArt,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(nodeBackground)
            )

            if (!island.unlocked) {
                Image(
                    painter = islandArt.lockedOverlay,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (island.unlocked && !island.completed) {
                Image(
                    painter = islandArt.unlockedTint,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(chipBackground),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = islandArt.iconArt,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )

                Text(
                    text = island.title.take(2),
                    color = titleColor,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.clearAndSetSemantics {}
                )
            }

            if (selected || highlighted) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(2.dp, FocusRing, CircleShape)
                )
            }

            if (island.completed) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(14.dp)
                        .background(FocusRing, CircleShape)
                )
            }
        }
    }
}
