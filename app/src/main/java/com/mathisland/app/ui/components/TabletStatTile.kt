package com.mathisland.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.SurfaceLevel

@Composable
fun TabletStatTile(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    accent: Color
) {
    StoryPanelCard(
        modifier = modifier,
        level = SurfaceLevel.Secondary,
        containerColor = accent.copy(alpha = 0.12f).compositeOver(MaterialTheme.colorScheme.surface),
        borderColor = accent.copy(alpha = 0.28f),
        shape = RadiusTokens.CardMd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.Lg)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RadiusTokens.Pill)
                        .background(accent)
                )
                Spacer(modifier = Modifier.width(SpacingTokens.Xs))
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = accent,
                modifier = Modifier.padding(top = SpacingTokens.Sm)
            )
            Box(
                modifier = Modifier
                    .padding(top = SpacingTokens.Sm)
                    .fillMaxWidth()
                    .clip(RadiusTokens.Pill)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                accent.copy(alpha = 0.5f),
                                accent.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .padding(vertical = 2.dp)
            )
        }
    }
}
