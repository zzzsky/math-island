package com.mathisland.app.feature.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.components.ReturnResultStage
import com.mathisland.app.ui.components.ReturnResultStageState
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun MapProgressFeedback(
    feedback: MapFeedbackUiState,
    motionProgress: Float = 0f
) {
    val motionSpec = feedback.kind.motionSpec()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                val pulseScale = 1f + (motionProgress * motionSpec.cardScaleBoost)
                scaleX = pulseScale
                scaleY = pulseScale
                alpha = 0.92f + (motionProgress * 0.08f)
                translationY = (1f - motionProgress) * 8f
            }
            .testTag("map-progress-feedback"),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xCC215A6D).copy(alpha = 0.86f + (motionProgress * 0.12f))
        ),
        shape = RoundedCornerShape(22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = motionSpec.accent.copy(alpha = 0.90f + (motionProgress * 0.10f))
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                AnimatedVisibility(
                    visible = motionProgress >= motionSpec.chipRevealAt,
                    enter = fadeIn(tween(120)) +
                        slideInVertically(tween(120)) { fullHeight -> fullHeight / 8 } +
                        scaleIn(tween(120), initialScale = 0.94f)
                ) {
                    StatusChip(
                        text = feedback.summaryLabel,
                        modifier = Modifier.testTag("map-feedback-kind-pill"),
                        variant = motionSpec.badgeVariant
                    )
                }
                Text(
                    text = feedback.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("map-feedback-title")
                )
                Text(
                    text = feedback.body,
                    style = TypographyTokens.BodyPrimary,
                    color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.testTag("map-feedback-body")
                )
                if (feedback.starsEarned > 0 || feedback.chestReady) {
                    AnimatedVisibility(
                        visible = motionProgress >= motionSpec.spotlightRevealAt,
                        enter = fadeIn(tween(140)) +
                            slideInVertically(tween(140)) { fullHeight -> fullHeight / 6 } +
                            scaleIn(tween(140), initialScale = 0.96f)
                    ) {
                        Text(
                            text = "本次推进",
                            style = TypographyTokens.SupportingLabel,
                            fontWeight = FontWeight.SemiBold,
                            color = motionSpec.accent,
                            modifier = Modifier.testTag("map-feedback-summary")
                        )
                    }
                }
                AnimatedVisibility(
                    visible = motionProgress >= motionSpec.supportingRevealAt,
                    enter = fadeIn(tween(160)) +
                        slideInVertically(tween(180)) { fullHeight -> fullHeight / 7 } +
                        scaleIn(tween(160), initialScale = 0.98f)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (feedback.starsEarned > 0) {
                            StatusChip(
                                text = "+${feedback.starsEarned} 星星",
                                modifier = Modifier.testTag("map-feedback-stars-pill"),
                                variant = motionSpec.badgeVariant,
                                leadingIcon = Icons.Default.Star,
                            )
                        }
                        if (feedback.chestReady) {
                            StatusChip(
                                text = "宝箱有新收藏",
                                modifier = Modifier.testTag("map-feedback-chest-pill"),
                                variant = motionSpec.badgeVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MapReturnSummaryCard(
    feedback: MapFeedbackUiState,
    modifier: Modifier = Modifier,
    motionProgress: Float = 0f
) {
    val motionSpec = feedback.kind.motionSpec()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = 1f + (motionProgress * motionSpec.cardScaleBoost)
                scaleY = 1f + (motionProgress * motionSpec.cardScaleBoost)
                alpha = 0.88f + (motionProgress * 0.12f)
                translationY = (1f - motionProgress) * 10f
            }
            .testTag("map-return-summary"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ReturnResultStage(
            state = feedback.stageState ?: mapReturnCopy(feedback.kind).toReturnResultStageState(
                summaryTitle = feedback.summaryTitle,
                summaryBody = feedback.summaryBody,
                detailTitle = feedback.detailTitle,
                detailBody = feedback.detailBody,
                actionTitle = feedback.actionTitle,
                actionBody = feedback.actionBody
            ),
            accentColor = motionSpec.accent,
            badgeVariant = motionSpec.badgeVariant,
            motionProgress = motionProgress,
            chipRevealAt = motionSpec.chipRevealAt,
            summaryRevealAt = motionSpec.summaryRevealAt,
            supportingRevealAt = motionSpec.supportingRevealAt,
            trailingRevealAt = motionSpec.trailingRevealAt,
            kindPillTag = "map-return-kind-pill",
            detailCardTag = "map-return-detail-card",
            actionCardTag = "map-return-action-card",
            actionPillTag = "map-return-action-pill",
            actionEmphasisTag = "map-return-action-emphasis"
        )
    }
}
