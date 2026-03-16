package com.mathisland.app.feature.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.components.WoodButton
import com.mathisland.app.ui.theme.TabletDeepWater
import com.mathisland.app.ui.theme.TabletFoam
import com.mathisland.app.ui.theme.TabletInfoSurface
import com.mathisland.app.ui.theme.TabletPanelSurface
import com.mathisland.app.ui.theme.TabletSand
import com.mathisland.app.ui.theme.TabletStatSurface

val TabletDeepWater: Color
    get() = com.mathisland.app.ui.theme.TabletDeepWater

val TabletFoam: Color
    get() = com.mathisland.app.ui.theme.TabletFoam

val TabletSand: Color
    get() = com.mathisland.app.ui.theme.TabletSand

@Composable
fun TabletActionCard(
    title: String,
    subtitle: String,
    buttonText: String,
    buttonTag: String? = null,
    accent: Color,
    onClick: () -> Unit
) {
    StoryPanelCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = TabletPanelSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            )
            WoodButton(
                text = buttonText,
                modifier = buttonTag?.let(Modifier::testTag) ?: Modifier,
                onClick = onClick,
                containerColor = accent
            )
        }
    }
}

@Composable
fun TabletInfoCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    body: String
) {
    StoryPanelCard(
        modifier = modifier,
        containerColor = TabletInfoSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            TabletChipLabel(text = title)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = body,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun TabletStatTile(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    accent: Color
) {
    StoryPanelCard(
        modifier = modifier,
        containerColor = TabletStatSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(99.dp))
                    .background(accent)
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
fun TabletChipLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f),
            style = MaterialTheme.typography.labelLarge
        )
    }
}
