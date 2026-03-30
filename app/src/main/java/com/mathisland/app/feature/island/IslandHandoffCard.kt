package com.mathisland.app.feature.island

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import com.mathisland.app.feature.map.MapFeedbackKind
import com.mathisland.app.feature.map.motionSpec
import com.mathisland.app.ui.components.ReturnResultStage
import com.mathisland.app.ui.components.ReturnResultStageState

@Composable
fun IslandHandoffCard(
    kind: MapFeedbackKind?,
    stageState: ReturnResultStageState? = null,
    fallbackLabel: String?,
    fallbackTitle: String?,
    fallbackBody: String?,
    fallbackDetailLabel: String?,
    fallbackDetailTitle: String?,
    fallbackDetailBody: String?,
    fallbackActionLabel: String?,
    fallbackActionTitle: String?,
    fallbackActionBody: String?,
    modifier: Modifier = Modifier,
) {
    val motionSpec = (kind ?: MapFeedbackKind.Progress).motionSpec()
    val revealProgress = remember { Animatable(0f) }
    val resolvedStageState = stageState ?: ReturnResultStageState(
        kindLabel = fallbackLabel ?: "",
        summaryTitle = fallbackTitle ?: "",
        summaryBody = fallbackBody ?: "",
        detailLabel = fallbackDetailLabel,
        detailTitle = fallbackDetailTitle,
        detailBody = fallbackDetailBody,
        actionLabel = fallbackActionLabel,
        actionTitle = fallbackActionTitle,
        actionBody = fallbackActionBody
    )

    LaunchedEffect(kind, resolvedStageState) {
        revealProgress.stop()
        revealProgress.snapTo(0f)
        revealProgress.animateTo(1f, tween(durationMillis = 240, easing = FastOutSlowInEasing))
    }

    val motionValue = revealProgress.value
    ReturnResultStage(
        state = resolvedStageState,
        accentColor = motionSpec.accent,
        badgeVariant = motionSpec.badgeVariant,
        motionProgress = motionValue,
        chipRevealAt = motionSpec.chipRevealAt,
        summaryRevealAt = motionSpec.summaryRevealAt,
        supportingRevealAt = motionSpec.supportingRevealAt,
        trailingRevealAt = motionSpec.trailingRevealAt,
        modifier = modifier
            .graphicsLayer {
                val lift = 1f + (motionValue * motionSpec.cardScaleBoost)
                scaleX = lift
                scaleY = lift
                alpha = 0.90f + (motionValue * 0.10f)
                translationY = (1f - motionValue) * 8f
            }
            .testTag("island-handoff-card"),
        kindPillTag = "island-handoff-kind-pill",
        detailCardTag = "island-handoff-detail-card",
        actionCardTag = "island-handoff-action-card",
        actionPillTag = "island-handoff-action-pill",
        actionEmphasisTag = "island-handoff-action-emphasis"
    )
}
