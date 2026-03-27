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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.feature.level.LessonStatusTone
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TextToneTokens
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
    StoryPanelCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag(cardTag),
        level = SurfaceLevel.Secondary,
        containerColor = containerColor,
        shape = RadiusTokens.CardMd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.Lg),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
        ) {
            StatusChip(
                text = chipText,
                variant = chipVariant,
                modifier = Modifier.testTag(chipTag)
            )
            Text(
                text = title,
                style = titleStyle,
                fontWeight = titleWeight,
                color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface)
            )
            Text(
                text = body,
                modifier = if (bodyTag != null) Modifier.testTag(bodyTag) else Modifier,
                style = TypographyTokens.BodySecondary,
                color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}

internal fun rendererStageContainerColorFor(tone: LessonStatusTone): Color = when (tone) {
    LessonStatusTone.Confirmed -> RendererTokens.FeedbackSuccessSurface
    LessonStatusTone.Retry -> RendererTokens.FeedbackRetrySurface
    LessonStatusTone.Warning -> RendererTokens.FeedbackWarningSurface
    LessonStatusTone.Highlight -> RendererTokens.FeedbackHighlightSurface
    LessonStatusTone.Neutral -> RendererTokens.HelperSurface
}
