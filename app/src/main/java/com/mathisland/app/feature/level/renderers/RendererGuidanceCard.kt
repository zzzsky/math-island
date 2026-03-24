package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.components.TabletChipLabel
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

@Composable
internal fun RendererGuidanceCard(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    StoryPanelCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("renderer-guidance-card"),
        level = SurfaceLevel.Secondary,
        containerColor = RendererTokens.HelperSurface,
        shape = RadiusTokens.CardMd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.Lg),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
        ) {
            TabletChipLabel(text = title)
            Text(
                text = body,
                modifier = Modifier.testTag("renderer-guidance-body"),
                style = TypographyTokens.BodySecondary,
                color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}
