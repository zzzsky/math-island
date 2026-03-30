package com.mathisland.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusVariant

data class ReturnResultStageState(
    val kindLabel: String,
    val summaryTitle: String,
    val summaryBody: String,
    val detailLabel: String? = null,
    val detailTitle: String? = null,
    val detailBody: String? = null,
    val actionLabel: String? = null,
    val actionTitle: String? = null,
    val actionBody: String? = null,
)

@Composable
fun ReturnResultStage(
    state: ReturnResultStageState,
    accentColor: Color,
    badgeVariant: StatusVariant,
    motionProgress: Float,
    chipRevealAt: Float,
    summaryRevealAt: Float,
    supportingRevealAt: Float,
    trailingRevealAt: Float,
    modifier: Modifier = Modifier,
    kindPillTag: String? = null,
    detailCardTag: String? = null,
    actionCardTag: String? = null,
    actionPillTag: String? = null,
    actionEmphasisTag: String? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
    ) {
        AnimatedVisibility(
            visible = motionProgress >= chipRevealAt,
            enter = fadeIn(tween(140)) +
                slideInVertically(tween(160)) { fullHeight -> fullHeight / 8 } +
                scaleIn(tween(140), initialScale = 0.95f)
        ) {
            StatusChip(
                text = state.kindLabel,
                variant = badgeVariant,
                modifier = kindPillTag?.let { Modifier.testTag(it) } ?: Modifier
            )
        }
        AnimatedVisibility(
            visible = motionProgress >= summaryRevealAt,
            enter = fadeIn(tween(160)) +
                slideInVertically(tween(180)) { fullHeight -> fullHeight / 7 } +
                scaleIn(tween(160), initialScale = 0.96f)
        ) {
            SummarySpotlightCard(
                label = state.kindLabel,
                title = state.summaryTitle,
                body = state.summaryBody,
                accent = accentColor
            )
        }
        if (state.detailLabel != null && state.detailTitle != null && state.detailBody != null) {
            AnimatedVisibility(
                visible = motionProgress >= supportingRevealAt,
                enter = fadeIn(tween(180)) +
                    slideInVertically(tween(200)) { fullHeight -> fullHeight / 8 } +
                    scaleIn(tween(180), initialScale = 0.98f)
            ) {
                TabletInfoCard(
                    title = state.detailLabel,
                    subtitle = state.detailTitle,
                    body = state.detailBody,
                    accentColor = accentColor.copy(alpha = 0.8f),
                    badgeText = state.kindLabel,
                    badgeVariant = badgeVariant,
                    modifier = detailCardTag?.let { Modifier.testTag(it) } ?: Modifier
                )
            }
        }
        if (state.actionLabel != null && state.actionTitle != null && state.actionBody != null) {
            AnimatedVisibility(
                visible = motionProgress >= trailingRevealAt,
                enter = fadeIn(tween(180)) +
                    slideInVertically(tween(220)) { fullHeight -> fullHeight / 8 } +
                    scaleIn(tween(180), initialScale = 0.98f)
            ) {
                ReturnActionCard(
                    label = state.actionLabel,
                    title = state.actionTitle,
                    body = state.actionBody,
                    accentColor = accentColor,
                    badgeVariant = badgeVariant,
                    modifier = actionCardTag?.let { Modifier.testTag(it) } ?: Modifier,
                    badgeTag = actionPillTag
                    ,
                    emphasisTag = actionEmphasisTag
                )
            }
        }
    }
}
