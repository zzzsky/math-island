package com.mathisland.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.map.MapTabletIslandUiState

private val LockedIsland = Color(0x665C7080)
private val CompletedIsland = Color(0xFF4B6F44)
private val FocusRing = Color(0xFFF4D58D)
private val RouteGlow = Color(0x66F4D58D)

@Composable
fun IslandMapCanvas(
    islands: List<MapTabletIslandUiState>,
    selectedIslandId: String?,
    highlightedIslandId: String? = null,
    onSelectIsland: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF0F4C5C), Color(0xFF1B6B83), Color(0xFF77B8D8))
                )
            )
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("map-scene-canvas")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            islands.forEach { island ->
                val nodeScale by animateFloatAsState(
                    targetValue = if (selectedIslandId == island.id || highlightedIslandId == island.id) 1.05f else 1f,
                    animationSpec = tween(durationMillis = 350),
                    label = "map-node-scale-${island.id}"
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (island.id != islands.first().id) {
                        val routeAlpha by animateFloatAsState(
                            targetValue = if (highlightedIslandId == island.id) 1f else 0.5f,
                            animationSpec = tween(durationMillis = 600),
                            label = "map-route-alpha-${island.id}"
                        )
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    FocusRing.copy(alpha = routeAlpha)
                                        .compositeOver(RouteGlow)
                                )
                                .testTag("map-route-highlight-${island.id}")
                        )
                    }
                    Box(
                        modifier = Modifier
                            .scale(nodeScale)
                            .size(if (selectedIslandId == island.id) 78.dp else 68.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    !island.unlocked -> LockedIsland
                                    island.completed -> CompletedIsland
                                    highlightedIslandId == island.id -> FocusRing
                                    else -> Color(0xFF18475A)
                                }
                            )
                            .clickable { onSelectIsland(island.id) }
                            .testTag("map-node-${island.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selectedIslandId == island.id || highlightedIslandId == island.id) {
                                        FocusRing
                                    } else {
                                        Color.White.copy(alpha = 0.18f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = island.title.take(2),
                                color = if (selectedIslandId == island.id || highlightedIslandId == island.id) {
                                    Color(0xFF114B5F)
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                    if (highlightedIslandId == island.id) {
                        Text(
                            text = "新路线",
                            color = FocusRing,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
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
