package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.ui.components.TabletChipLabel
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
internal fun RendererSectionHeader(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    StoryPanelCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("renderer-action-header"),
        level = SurfaceLevel.Secondary,
        containerColor = RendererTokens.HelperSurface,
        shape = RadiusTokens.CardMd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.Lg),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)
        ) {
            TabletChipLabel(text = title)
            Text(
                text = body,
                modifier = Modifier.testTag("renderer-action-body"),
                style = TypographyTokens.Caption,
                color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
