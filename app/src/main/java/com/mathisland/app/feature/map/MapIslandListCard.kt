package com.mathisland.app.feature.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.island.IslandPanelTokens
import com.mathisland.app.ui.components.TabletChipLabel
import com.mathisland.app.ui.components.map.MapArtRegistry

@Composable
fun MapIslandListCard(
    island: MapTabletIslandUiState,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit
) {
    val islandArt = MapArtRegistry.resolveIslandArt(island.id)
    val iconPainter = islandArt.iconSlot.drawableResId?.let { painterResource(it) } ?: islandArt.iconArt
    val containerColor = when {
        selected -> IslandPanelTokens.HeaderSurface
        island.completed -> IslandPanelTokens.LessonCompletedSurface
        island.unlocked -> IslandPanelTokens.LessonSurface
        else -> IslandPanelTokens.StorySurface
    }
    val borderColor = when {
        selected -> IslandPanelTokens.RecommendedBorder
        island.completed -> IslandPanelTokens.CompletedBorder
        else -> IslandPanelTokens.LessonBorder
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable(onClick = onSelect)
            .testTag("select-island-${island.id}"),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(CircleShape)
                            .border(1.dp, borderColor.copy(alpha = 0.7f), CircleShape)
                    ) {
                    }
                    Image(
                        painter = iconPainter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("map-list-icon-${island.id}")
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = island.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = island.subtitle,
                        color = IslandPanelTokens.DescriptionText
                    )
                }
                TabletChipLabel(text = islandStatusLabel(island, selected))
            }

            LinearProgressIndicator(
                progress = { island.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(999.dp)),
                color = IslandPanelTokens.ProgressFill,
                trackColor = IslandPanelTokens.ProgressTrack
            )

            Text(
                text = "${island.lessons.count { it.completed }}/${island.lessons.size} 课程完成",
                color = IslandPanelTokens.SummaryText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.testTag("map-list-card-surface-${island.id}")
            )
        }
    }
}

private fun islandStatusLabel(
    island: MapTabletIslandUiState,
    selected: Boolean
): String = when {
    selected -> "当前焦点"
    island.completed -> "已完成"
    island.unlocked -> "已解锁"
    else -> "等待前岛完成"
}
