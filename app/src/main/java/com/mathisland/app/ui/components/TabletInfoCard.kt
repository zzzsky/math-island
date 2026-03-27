package com.mathisland.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens
import com.mathisland.app.ui.theme.StatusVariant

@Composable
fun TabletInfoCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    body: String,
    accentColor: Color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.24f),
    containerColor: Color = TextToneTokens.low(MaterialTheme.colorScheme.surface),
    badgeText: String? = null,
    badgeVariant: StatusVariant = StatusVariant.Neutral,
    badgeTag: String? = null
) {
    val animatedAccentColor = animateColorAsState(
        targetValue = accentColor,
        animationSpec = tween(durationMillis = 240),
        label = "tablet-info-card-accent"
    )
    val animatedContainerColor = animateColorAsState(
        targetValue = containerColor,
        animationSpec = tween(durationMillis = 240),
        label = "tablet-info-card-container"
    )
    StoryPanelCard(
        modifier = modifier,
        level = SurfaceLevel.Secondary,
        containerColor = animatedContainerColor.value,
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
                    .background(animatedAccentColor.value)
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(SpacingTokens.Sm))
            TabletChipLabel(text = title)
            badgeText?.let { resolvedBadgeText ->
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(SpacingTokens.Xs))
                StatusChip(
                    text = resolvedBadgeText,
                    variant = badgeVariant,
                    modifier = badgeTag?.let { Modifier.testTag(it) } ?: Modifier
                )
            }
            AnimatedContent(
                targetState = subtitle,
                label = "tablet-info-card-subtitle"
            ) { animatedSubtitle ->
                Text(
                    text = animatedSubtitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = SpacingTokens.Xs)
                )
            }
            AnimatedContent(
                targetState = body,
                label = "tablet-info-card-body"
            ) { animatedBody ->
                Text(
                    text = animatedBody,
                    style = TypographyTokens.BodyPrimary,
                    color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.padding(top = SpacingTokens.Xs)
                )
            }
        }
    }
}
