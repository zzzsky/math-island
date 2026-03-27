package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
internal fun RendererPromptCard(
    prompt: String,
    actionState: RendererActionState,
    modifier: Modifier = Modifier,
) {
    StoryPanelCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("renderer-prompt-card"),
        level = SurfaceLevel.Secondary,
        containerColor = when (actionState.phase) {
            RendererActionPhase.Retry -> RendererTokens.FeedbackRetrySurface
            RendererActionPhase.Confirmed -> RendererTokens.FeedbackSuccessSurface
            RendererActionPhase.TimeoutExpired -> RendererTokens.FeedbackWarningSurface
            RendererActionPhase.Ready,
            RendererActionPhase.Locked,
            -> RendererTokens.PromptSurface
        },
        shape = RadiusTokens.CardMd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.Lg),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
        ) {
            StatusChip(
                text = when (actionState.phase) {
                    RendererActionPhase.Retry -> "回看题目"
                    RendererActionPhase.Confirmed -> "本题完成"
                    RendererActionPhase.TimeoutExpired -> "本题结束"
                    RendererActionPhase.Ready,
                    RendererActionPhase.Locked,
                    -> "先看题目"
                },
                variant = when (actionState.phase) {
                    RendererActionPhase.Retry -> StatusVariant.Highlight
                    RendererActionPhase.Confirmed -> StatusVariant.Success
                    RendererActionPhase.TimeoutExpired -> StatusVariant.Caution
                    RendererActionPhase.Ready,
                    RendererActionPhase.Locked,
                    -> StatusVariant.Recommended
                },
                modifier = Modifier.testTag("renderer-prompt-chip")
            )
            Text(
                text = prompt,
                style = TypographyTokens.FeatureTitle,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = when (actionState.phase) {
                    RendererActionPhase.Retry -> "先看题目，再换一个答案。"
                    RendererActionPhase.Confirmed -> "这题已经确认，准备切到下一题。"
                    RendererActionPhase.TimeoutExpired -> "本题结束，直接看下一题。"
                    RendererActionPhase.Ready,
                    RendererActionPhase.Locked,
                    -> "先读清题目，再开始这次作答。"
                },
                style = TypographyTokens.BodySecondary,
                color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}
