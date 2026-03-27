package com.mathisland.app.feature.level.renderers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.ui.theme.StatusVariant
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
        chipText = when (actionState.phase) {
            RendererActionPhase.Retry -> "回看题目"
            RendererActionPhase.Confirmed -> "本题完成"
            RendererActionPhase.TimeoutExpired -> "本题结束"
            RendererActionPhase.Ready,
            RendererActionPhase.Locked,
            -> "先看题目"
        },
        chipVariant = when (actionState.phase) {
            RendererActionPhase.Retry -> StatusVariant.Highlight
            RendererActionPhase.Confirmed -> StatusVariant.Success
            RendererActionPhase.TimeoutExpired -> StatusVariant.Caution
            RendererActionPhase.Ready,
            RendererActionPhase.Locked,
            -> StatusVariant.Recommended
        },
        title = prompt,
        body = when (actionState.phase) {
            RendererActionPhase.Retry -> "先看题目，再换一个答案。"
            RendererActionPhase.Confirmed -> "这题已经确认，准备切到下一题。"
            RendererActionPhase.TimeoutExpired -> "本题结束，直接看下一题。"
            RendererActionPhase.Ready,
            RendererActionPhase.Locked,
            -> "先读清题目，再开始这次作答。"
        },
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
