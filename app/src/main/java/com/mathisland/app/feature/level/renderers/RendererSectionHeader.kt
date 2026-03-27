package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.mathisland.app.ui.theme.StatusVariant

@Composable
internal fun RendererSectionHeader(
    badgeText: String,
    badgeVariant: StatusVariant,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.testTag("renderer-action-header")
    ) {
        RendererGuidanceCard(
            badgeText = badgeText,
            badgeVariant = badgeVariant,
            cardTag = "renderer-action-card",
            chipTag = "renderer-action-chip",
            title = title,
            body = body
        )
    }
}
