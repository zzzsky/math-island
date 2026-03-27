package com.mathisland.app.feature.level.renderers

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mathisland.app.ui.theme.StatusVariant

@Composable
internal fun RendererGuidanceCard(
    title: String,
    body: String,
    badgeText: String = "操作提示",
    badgeVariant: StatusVariant = StatusVariant.Neutral,
    cardTag: String = "renderer-guidance-card",
    chipTag: String = "renderer-guidance-chip",
    modifier: Modifier = Modifier,
) {
    RendererStageCard(
        cardTag = cardTag,
        chipTag = chipTag,
        chipText = badgeText,
        chipVariant = badgeVariant,
        title = title,
        body = body,
        modifier = modifier,
        containerColor = RendererTokens.HelperSurface,
        bodyTag = "renderer-guidance-body"
    )
}
