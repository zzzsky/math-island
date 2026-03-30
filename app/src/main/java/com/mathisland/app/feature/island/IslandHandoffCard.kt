package com.mathisland.app.feature.island

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import com.mathisland.app.feature.map.MapFeedbackKind
import com.mathisland.app.feature.map.motionSpec
import com.mathisland.app.ui.components.ReturnActionCard
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.components.SummarySpotlightCard
import com.mathisland.app.ui.components.TabletInfoCard
import com.mathisland.app.ui.theme.SpacingTokens

@Composable
fun IslandHandoffCard(
    kind: MapFeedbackKind?,
    label: String,
    title: String,
    body: String,
    detailLabel: String?,
    detailTitle: String?,
    detailBody: String?,
    actionLabel: String?,
    actionTitle: String?,
    actionBody: String?,
    modifier: Modifier = Modifier,
) {
    val motionSpec = (kind ?: MapFeedbackKind.Progress).motionSpec()
    val revealProgress = remember { Animatable(0f) }

    LaunchedEffect(kind, label, title, body, detailLabel, detailTitle, detailBody, actionLabel, actionTitle, actionBody) {
        revealProgress.stop()
        revealProgress.snapTo(0f)
        revealProgress.animateTo(1f, tween(durationMillis = 240, easing = FastOutSlowInEasing))
    }

    val motionValue = revealProgress.value
    val accent = motionSpec.accent
    Column(
        modifier = modifier
            .graphicsLayer {
                val lift = 1f + (motionValue * motionSpec.cardScaleBoost)
                scaleX = lift
                scaleY = lift
                alpha = 0.90f + (motionValue * 0.10f)
                translationY = (1f - motionValue) * 8f
            }
            .testTag("island-handoff-card"),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
    ) {
        AnimatedVisibility(
            visible = motionValue >= motionSpec.chipRevealAt,
            enter = fadeIn(tween(120)) +
                slideInVertically(tween(140)) { fullHeight -> fullHeight / 8 } +
                scaleIn(tween(120), initialScale = 0.95f)
        ) {
            StatusChip(
                text = label,
                variant = motionSpec.badgeVariant,
                modifier = Modifier.testTag("island-handoff-kind-pill")
            )
        }
        AnimatedVisibility(
            visible = motionValue >= motionSpec.summaryRevealAt,
            enter = fadeIn(tween(140)) +
                slideInVertically(tween(160)) { fullHeight -> fullHeight / 7 } +
                scaleIn(tween(140), initialScale = 0.96f)
        ) {
            SummarySpotlightCard(
                label = label,
                title = title,
                body = body,
                accent = accent
            )
        }
        if (detailLabel != null && detailTitle != null && detailBody != null) {
            AnimatedVisibility(
                visible = motionValue >= motionSpec.supportingRevealAt,
                enter = fadeIn(tween(160)) +
                    slideInVertically(tween(180)) { fullHeight -> fullHeight / 8 } +
                    scaleIn(tween(160), initialScale = 0.98f)
            ) {
                TabletInfoCard(
                    title = detailLabel,
                    subtitle = detailTitle,
                    body = detailBody,
                    accentColor = accent.copy(alpha = 0.8f),
                    badgeText = label,
                    badgeVariant = motionSpec.badgeVariant,
                    modifier = Modifier.testTag("island-handoff-detail-card")
                )
            }
        }
        if (actionLabel != null && actionTitle != null && actionBody != null) {
            AnimatedVisibility(
                visible = motionValue >= motionSpec.trailingRevealAt,
                enter = fadeIn(tween(180)) +
                    slideInVertically(tween(200)) { fullHeight -> fullHeight / 8 } +
                    scaleIn(tween(180), initialScale = 0.98f)
            ) {
                ReturnActionCard(
                    label = actionLabel,
                    title = actionTitle,
                    body = actionBody,
                    accentColor = accent.copy(alpha = 0.8f),
                    badgeVariant = motionSpec.badgeVariant,
                    modifier = Modifier.testTag("island-handoff-action-card"),
                    badgeTag = "island-handoff-action-pill"
                )
            }
        }
    }
}
