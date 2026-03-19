package com.mathisland.app.feature.island

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.mathisland.app.feature.map.MapFeedbackKind
import com.mathisland.app.ui.components.SummarySpotlightCard

@Composable
fun IslandHandoffCard(
    kind: MapFeedbackKind?,
    label: String,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    val accent = when (kind) {
        MapFeedbackKind.NewIsland -> Color(0xFFF2D48B)
        MapFeedbackKind.Chest -> Color(0xFFE8B86D)
        MapFeedbackKind.Replay -> Color(0xFF7FC2D8)
        MapFeedbackKind.Progress, null -> Color(0xFFF2D48B)
    }
    SummarySpotlightCard(
        label = label,
        title = title,
        body = body,
        accent = accent,
        modifier = modifier.testTag("island-handoff-card")
    )
}
