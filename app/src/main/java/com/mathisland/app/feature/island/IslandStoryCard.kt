package com.mathisland.app.feature.island

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.map.MapTabletIslandUiState
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.components.TabletChipLabel
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun IslandStoryCard(
    island: MapTabletIslandUiState,
    modifier: Modifier = Modifier
) {
    StoryPanelCard(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, IslandPanelTokens.StoryBorder, RoundedCornerShape(24.dp))
            .testTag("panel-story-card"),
        level = SurfaceLevel.Secondary,
        containerColor = IslandPanelTokens.StorySurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TabletChipLabel(text = "探索提示")
            Text(
                text = island.description,
                style = TypographyTokens.BodyLead,
                color = TextToneTokens.high(IslandPanelTokens.DescriptionText)
            )
            Text(
                text = "当前主题：${island.subtitle}",
                style = TypographyTokens.SupportingLabel,
                color = TextToneTokens.supporting(IslandPanelTokens.SummaryText)
            )
        }
    }
}
