package com.mathisland.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.SurfaceLevel

@Composable
fun TabletInfoCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    body: String
) {
    StoryPanelCard(
        modifier = modifier,
        level = SurfaceLevel.Secondary,
        shape = RadiusTokens.CardLg
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.Md, vertical = SpacingTokens.Md)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .clip(RadiusTokens.Pill)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.24f))
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(SpacingTokens.Sm))
            TabletChipLabel(text = title)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = SpacingTokens.Xs)
            )
            Text(
                text = body,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
                modifier = Modifier.padding(top = SpacingTokens.Xs)
            )
        }
    }
}
