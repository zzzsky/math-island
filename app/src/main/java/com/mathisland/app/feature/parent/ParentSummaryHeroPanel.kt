package com.mathisland.app.feature.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mathisland.app.ui.components.SummarySpotlightCard
import com.mathisland.app.ui.components.TabletStatTile
import com.mathisland.app.ui.theme.SpacingTokens

@Composable
fun ParentSummaryHeroPanel(
    state: ParentSummaryUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
    ) {
        SummarySpotlightCard(
            label = "今日总览",
            title = state.heroTitle,
            body = state.heroBody,
            accent = Color(0xFF8ECae6)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
        ) {
            TabletStatTile(
                modifier = Modifier.weight(1f),
                title = "今日学习",
                value = state.todayCountText,
                accent = Color(0xFF8ECae6)
            )
            TabletStatTile(
                modifier = Modifier.weight(1f),
                title = "连续学习",
                value = state.streakText,
                accent = Color(0xFF9ADBC7)
            )
            TabletStatTile(
                modifier = Modifier.weight(1f),
                title = "薄弱项",
                value = state.weakTopicCountText,
                accent = Color(0xFFF2D48B)
            )
        }
    }
}
