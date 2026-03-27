package com.mathisland.app.feature.level.renderers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.feature.level.lessonPromptBadgeTextFor
import com.mathisland.app.feature.level.lessonPromptBadgeVariantFor
import com.mathisland.app.feature.level.lessonPromptBodyFor
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
internal fun RendererPromptCard(
    prompt: String,
    actionState: RendererActionState,
    modifier: Modifier = Modifier,
) {
    RendererStageCard(
        cardTag = "renderer-prompt-card",
        chipTag = "renderer-prompt-chip",
        chipText = lessonPromptBadgeTextFor(
            phase = actionState.phase,
            lessonIsReview = actionState.isReview
        ),
        chipVariant = lessonPromptBadgeVariantFor(
            phase = actionState.phase,
            lessonIsReview = actionState.isReview
        ),
        title = prompt,
        body = lessonPromptBodyFor(
            phase = actionState.phase,
            lessonIsReview = actionState.isReview
        ),
        modifier = modifier,
        containerColor = when (actionState.phase) {
            RendererActionPhase.Retry -> RendererTokens.FeedbackRetrySurface
            RendererActionPhase.Confirmed -> RendererTokens.FeedbackSuccessSurface
            RendererActionPhase.TimeoutExpired -> RendererTokens.FeedbackWarningSurface
            RendererActionPhase.Ready,
            RendererActionPhase.Locked,
            -> RendererTokens.PromptSurface
        },
        titleStyle = TypographyTokens.FeatureTitle,
        titleWeight = FontWeight.Bold
    )
}
