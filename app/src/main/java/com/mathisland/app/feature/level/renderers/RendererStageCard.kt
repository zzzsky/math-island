package com.mathisland.app.feature.level.renderers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.feature.level.LessonCueCard
import com.mathisland.app.feature.level.LessonStatusTone
import com.mathisland.app.feature.level.lessonStatusAccentColor
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
internal fun RendererStageCard(
    cardTag: String,
    chipTag: String,
    chipText: String,
    chipVariant: StatusVariant,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    containerColor: Color = RendererTokens.HelperSurface,
    titleStyle: TextStyle = TypographyTokens.SupportingLabel,
    titleWeight: FontWeight = FontWeight.SemiBold,
    bodyTag: String? = null,
) {
    LessonCueCard(
        title = title,
        body = body,
        modifier = modifier,
        cardTag = cardTag,
        chipText = chipText,
        chipVariant = chipVariant,
        chipTag = chipTag,
        bodyTag = bodyTag,
        tone = rendererStageToneFor(containerColor),
        accentColor = lessonStatusAccentColor(rendererStageToneFor(containerColor)),
        containerColor = containerColor,
        titleStyle = titleStyle,
        titleWeight = titleWeight,
        bodyStyle = TypographyTokens.BodySecondary
    )
}

internal fun rendererStageContainerColorFor(tone: LessonStatusTone): Color = when (tone) {
    LessonStatusTone.Confirmed -> RendererTokens.FeedbackSuccessSurface
    LessonStatusTone.Retry -> RendererTokens.FeedbackRetrySurface
    LessonStatusTone.Warning -> RendererTokens.FeedbackWarningSurface
    LessonStatusTone.Highlight -> RendererTokens.FeedbackHighlightSurface
    LessonStatusTone.Neutral -> RendererTokens.HelperSurface
}

private fun rendererStageToneFor(containerColor: Color): LessonStatusTone = when (containerColor) {
    RendererTokens.FeedbackSuccessSurface -> LessonStatusTone.Confirmed
    RendererTokens.FeedbackRetrySurface -> LessonStatusTone.Retry
    RendererTokens.FeedbackWarningSurface -> LessonStatusTone.Warning
    RendererTokens.FeedbackHighlightSurface -> LessonStatusTone.Highlight
    else -> LessonStatusTone.Neutral
}
