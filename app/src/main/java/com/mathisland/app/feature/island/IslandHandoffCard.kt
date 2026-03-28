package com.mathisland.app.feature.island

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.mathisland.app.feature.map.MapFeedbackKind
import com.mathisland.app.ui.components.SummarySpotlightCard
import com.mathisland.app.ui.components.TabletInfoCard
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusVariant

@Composable
fun IslandHandoffCard(
    kind: MapFeedbackKind?,
    label: String,
    title: String,
    body: String,
    detailLabel: String?,
    detailTitle: String?,
    detailBody: String?,
    modifier: Modifier = Modifier,
) {
    val accent = when (kind) {
        MapFeedbackKind.NewIsland -> Color(0xFFF2D48B)
        MapFeedbackKind.Chest -> Color(0xFFE8B86D)
        MapFeedbackKind.Replay -> Color(0xFF7FC2D8)
        MapFeedbackKind.Progress, null -> Color(0xFFF2D48B)
    }
    Column(
        modifier = modifier.testTag("island-handoff-card"),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
    ) {
        SummarySpotlightCard(
            label = label,
            title = title,
            body = body,
            accent = accent
        )
        if (detailLabel != null && detailTitle != null && detailBody != null) {
            TabletInfoCard(
                title = detailLabel,
                subtitle = detailTitle,
                body = detailBody,
                accentColor = accent.copy(alpha = 0.8f),
                badgeText = label,
                badgeVariant = when (kind) {
                    MapFeedbackKind.NewIsland -> StatusVariant.Recommended
                    MapFeedbackKind.Chest -> StatusVariant.Highlight
                    MapFeedbackKind.Replay -> StatusVariant.Highlight
                    MapFeedbackKind.Progress, null -> StatusVariant.Success
                },
                modifier = Modifier.testTag("island-handoff-detail-card")
            )
        }
    }
}
