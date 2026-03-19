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
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

enum class AnswerFeedbackKind {
    Correct,
    Incorrect,
    TimedWarning,
}

data class AnswerFeedbackUiState(
    val kind: AnswerFeedbackKind,
    val title: String,
    val body: String,
)

@Composable
fun AnswerFeedbackBanner(
    state: AnswerFeedbackUiState,
    modifier: Modifier = Modifier,
) {
    StoryPanelCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("answer-feedback-banner"),
        level = SurfaceLevel.Secondary,
        containerColor = when (state.kind) {
            AnswerFeedbackKind.Correct -> RendererTokens.FeedbackSuccessSurface
            AnswerFeedbackKind.Incorrect -> RendererTokens.FeedbackRetrySurface
            AnswerFeedbackKind.TimedWarning -> RendererTokens.FeedbackWarningSurface
        },
        shape = RadiusTokens.CardMd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.Lg),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)
        ) {
            Text(
                text = state.title,
                style = TypographyTokens.SupportingLabel,
                fontWeight = FontWeight.Bold,
                color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.testTag("answer-feedback-title")
            )
            Text(
                text = state.body,
                style = TypographyTokens.Caption,
                color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.testTag("answer-feedback-body")
            )
        }
    }
}
