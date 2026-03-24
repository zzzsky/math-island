package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.domain.model.Question
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.components.TabletChipLabel
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TabletDeepWater
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

internal val TabletCoral = Color(0xFFEE964B)
internal val TabletSky = Color(0xFF8ECae6)
internal val TabletMint = Color(0xFF9ADBC7)

@Composable
internal fun RendererOptionsColumn(
    question: Question,
    rendererTag: String,
    accent: Color,
    feedback: AnswerFeedbackUiState? = null,
    actionState: RendererActionState = rendererActionStateFor(feedback = feedback, inputEnabled = true),
    header: String? = null,
    helper: String? = null,
    affordance: @Composable (() -> Unit)? = null,
    buttonLabel: String,
    onAnswer: (String) -> Unit
) {
    RendererPanelStack(
        rendererTag = rendererTag,
        context = if (header != null && helper != null) {
            {
                StoryPanelCard(
                    level = SurfaceLevel.Secondary,
                    containerColor = RendererTokens.HelperSurface,
                    shape = RadiusTokens.CardMd
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(SpacingTokens.Lg),
                        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)
                    ) {
                        TabletChipLabel(text = header)
                        Text(
                            text = helper,
                            style = TypographyTokens.Caption,
                            color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }
            }
        } else {
            null
        },
        feedback = feedback,
        affordance = affordance,
    ) {
        val resolvedButtonLabel = actionState.resolveLabel(buttonLabel)
        val resolvedRole = actionState.resolveRole(ActionRole.Primary)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(RendererTokens.ActionGroupGap)
        ) {
            RendererSectionHeader(
                title = actionState.sectionTitle(),
                body = actionState.sectionBody()
            )
            question.choices.forEach { choice ->
                val optionFeedback = optionFeedbackStateFor(choice, feedback)
                StoryPanelCard(
                    level = SurfaceLevel.Secondary,
                    containerColor = when (optionFeedback.tone) {
                        OptionFeedbackTone.Neutral -> RendererTokens.OptionSurface
                        OptionFeedbackTone.Retry -> RendererTokens.OptionRetrySurface
                        OptionFeedbackTone.Confirmed -> RendererTokens.OptionCorrectSurface
                    },
                    borderColor = when (optionFeedback.tone) {
                        OptionFeedbackTone.Neutral -> null
                        OptionFeedbackTone.Retry -> RendererTokens.OptionRetryBorder
                        OptionFeedbackTone.Confirmed -> RendererTokens.OptionCorrectBorder
                    },
                    shape = RadiusTokens.CardMd
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(SpacingTokens.Lg),
                        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                    ) {
                        Text(
                            text = choice,
                            style = TypographyTokens.FeatureTitle,
                            fontWeight = FontWeight.SemiBold
                        )
                        optionFeedback.supportingText?.let { supportingText ->
                            Text(
                                text = supportingText,
                                style = TypographyTokens.Caption,
                                color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier.testTag("answer-state-$choice")
                            )
                        }
                        ActionButton(
                            modifier = Modifier.testTag("answer-$choice"),
                            text = resolvedButtonLabel,
                            onClick = { onAnswer(choice) },
                            enabled = actionState.enabled,
                            role = resolvedRole,
                            containerColor = if (resolvedRole == ActionRole.Primary) accent else null,
                            contentColor = if (resolvedRole == ActionRole.Primary) TabletDeepWater else null
                        )
                    }
                }
            }
        }
    }
}
