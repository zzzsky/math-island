package com.mathisland.app.feature.level.renderers

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.mathisland.app.feature.level.LessonStatusTone
import com.mathisland.app.feature.level.LessonCueCard
import com.mathisland.app.feature.level.lessonFeedbackBadgeTextFor
import com.mathisland.app.feature.level.lessonFeedbackBadgeVariantFor
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusVariant
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
    LessonCueCard(
        title = state.title,
        body = state.body,
        modifier = modifier
            .fillMaxWidth()
            .testTag("answer-feedback-banner"),
        chipText = answerFeedbackBadgeTextFor(state),
        chipVariant = answerFeedbackBadgeVariantFor(state),
        chipTag = "answer-feedback-chip",
        titleTag = "answer-feedback-title",
        bodyTag = "answer-feedback-body",
        tone = tone,
        titleStyle = TypographyTokens.SupportingLabel,
        bodyStyle = TypographyTokens.Caption
    )
}

private fun answerFeedbackBadgeTextFor(state: AnswerFeedbackUiState): String =
    lessonFeedbackBadgeTextFor(state.kind)

private fun answerFeedbackBadgeVariantFor(state: AnswerFeedbackUiState): StatusVariant =
    lessonFeedbackBadgeVariantFor(state.kind)

private fun answerFeedbackBannerToneFor(state: AnswerFeedbackUiState): LessonStatusTone {
    return when (state.kind) {
        AnswerFeedbackKind.Correct -> LessonStatusTone.Confirmed
        AnswerFeedbackKind.Incorrect -> LessonStatusTone.Retry
        AnswerFeedbackKind.TimedWarning,
        AnswerFeedbackKind.TimeoutExpired,
        -> LessonStatusTone.Warning
    }
}
