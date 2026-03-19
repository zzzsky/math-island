package com.mathisland.app.feature.island

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.mathisland.app.ui.components.SummarySpotlightCard

@Composable
fun IslandHandoffCard(
    label: String,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    SummarySpotlightCard(
        label = label,
        title = title,
        body = body,
        accent = Color(0xFFF2D48B),
        modifier = modifier.testTag("island-handoff-card")
    )
}
