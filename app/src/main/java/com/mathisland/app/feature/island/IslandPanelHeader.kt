package com.mathisland.app.feature.island

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.map.MapTabletIslandUiState
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.components.map.MapArtRegistry
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun IslandPanelHeader(
    island: MapTabletIslandUiState,
    modifier: Modifier = Modifier
) {
    val islandArt = MapArtRegistry.resolveIslandArt(island.id)
    val iconPainter = islandArt.iconSlot.drawableResId?.let { painterResource(it) } ?: islandArt.iconArt

    StoryPanelCard(
        modifier = modifier.fillMaxWidth(),
        level = SurfaceLevel.Secondary,
        containerColor = IslandPanelTokens.HeaderSurface,
        borderColor = IslandPanelTokens.HeaderBorder,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(IslandPanelTokens.IconBadge, CircleShape)
                    .testTag("panel-header-icon"),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = iconPainter,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusChip(
                    text = if (island.completed) "本岛完成" else "岛屿面板",
                    variant = if (island.completed) StatusVariant.Success else StatusVariant.Highlight
                )
                Text(
                    text = island.title,
                    style = TypographyTokens.SectionTitle,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.testTag("panel-island-title")
                )
                Text(
                    text = island.subtitle,
                    color = IslandPanelTokens.DescriptionText,
                    style = TypographyTokens.Caption
                )
                Text(
                    text = progressLabel(island),
                    color = IslandPanelTokens.SummaryText,
                    style = TypographyTokens.Caption,
                    modifier = Modifier.testTag("panel-island-progress")
                )
                LinearProgressIndicator(
                    progress = { island.progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = IslandPanelTokens.ProgressFill,
                    trackColor = IslandPanelTokens.ProgressTrack
                )
            }
        }
    }
}

private fun progressLabel(island: MapTabletIslandUiState): String {
    val completedLessons = island.lessons.count { it.completed }
    return "课程进度 $completedLessons/${island.lessons.size} · ${(island.progress * 100).toInt()}%"
}
