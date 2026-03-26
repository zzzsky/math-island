package com.mathisland.app.feature.level.renderers

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import com.mathisland.app.feature.level.LessonStatusTone
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
    TimeoutExpired,
}

data class AnswerFeedbackUiState(
    val kind: AnswerFeedbackKind,
    val title: String,
    val body: String,
    val submittedAnswer: String? = null,
)

@Composable
fun AnswerFeedbackBanner(
    state: AnswerFeedbackUiState,
    modifier: Modifier = Modifier,
) {
    val tone = answerFeedbackBannerToneFor(state)
    val animatedContainerColor = animateColorAsState(
        targetValue = when (tone) {
            LessonStatusTone.Confirmed -> RendererTokens.FeedbackSuccessSurface
            LessonStatusTone.Retry -> RendererTokens.FeedbackRetrySurface
            LessonStatusTone.Warning -> RendererTokens.FeedbackWarningSurface
            LessonStatusTone.Highlight -> RendererTokens.FeedbackHighlightSurface
            LessonStatusTone.Neutral -> RendererTokens.HelperSurface
        },
        animationSpec = tween(durationMillis = 220),
        label = "answer-feedback-container"
    )
    StoryPanelCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("answer-feedback-banner"),
        level = SurfaceLevel.Secondary,
        containerColor = animatedContainerColor.value,
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
            AnimatedContent(
                targetState = state.body,
                label = "answer-feedback-body"
            ) { animatedBody ->
                Text(
                    text = animatedBody,
                    style = TypographyTokens.Caption,
                    color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.testTag("answer-feedback-body")
                )
            }
        }
    }
}

private fun answerFeedbackBannerToneFor(state: AnswerFeedbackUiState): LessonStatusTone {
    return when (state.kind) {
        AnswerFeedbackKind.Correct -> LessonStatusTone.Confirmed
        AnswerFeedbackKind.Incorrect -> LessonStatusTone.Retry
        AnswerFeedbackKind.TimedWarning,
        AnswerFeedbackKind.TimeoutExpired,
        -> LessonStatusTone.Warning
    }
}
