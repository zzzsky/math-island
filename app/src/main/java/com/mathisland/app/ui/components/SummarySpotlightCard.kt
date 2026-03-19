package com.mathisland.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.compositeOver
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun SummarySpotlightCard(
    label: String,
    title: String,
    body: String,
    accent: Color,
    modifier: Modifier = Modifier,
) {
    StoryPanelCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("summary-spotlight-card"),
        level = SurfaceLevel.Secondary,
        shape = RadiusTokens.CardLg,
        containerColor = accent.copy(alpha = 0.12f).compositeOver(MaterialTheme.colorScheme.surface),
        borderColor = accent.copy(alpha = 0.28f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.Xl),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
        ) {
            TabletChipLabel(text = label)
            Text(
                text = title,
                style = TypographyTokens.FeatureTitle,
                fontWeight = FontWeight.Black
            )
            Text(
                text = body,
                style = TypographyTokens.BodyPrimary,
                color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}
