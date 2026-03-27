package com.mathisland.app.feature.level

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun LessonCueCard(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    cardTag: String? = null,
    eyebrowText: String? = null,
    eyebrowTag: String? = null,
    chipText: String? = null,
    chipVariant: StatusVariant = StatusVariant.Neutral,
    chipTag: String? = null,
    titleTag: String? = null,
    bodyTag: String? = null,
    tone: LessonStatusTone = LessonStatusTone.Neutral,
    accentColor: Color? = null,
    containerColor: Color? = null,
    titleStyle: TextStyle = TypographyTokens.FeatureTitle,
    titleWeight: FontWeight = FontWeight.Bold,
    bodyStyle: TextStyle = TypographyTokens.BodyPrimary,
) {
    val animatedAccentColor = animateColorAsState(
        targetValue = accentColor ?: lessonStatusAccentColor(tone),
        animationSpec = tween(durationMillis = 220),
        label = "lesson-cue-accent"
    )
    val animatedContainerColor = animateColorAsState(
        targetValue = containerColor ?: lessonStatusSurfaceColor(tone),
        animationSpec = tween(durationMillis = 220),
        label = "lesson-cue-container"
    )
    StoryPanelCard(
        modifier = cardTag?.let { modifier.testTag(it) } ?: modifier,
        level = SurfaceLevel.Secondary,
        containerColor = animatedContainerColor.value,
        shape = RadiusTokens.CardLg
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.Md, vertical = SpacingTokens.Md),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .clip(RadiusTokens.Pill)
                    .background(animatedAccentColor.value)
            )
            eyebrowText?.let { eyebrow ->
                Spacer(modifier = Modifier.height(SpacingTokens.Xs))
                Text(
                    text = eyebrow,
                    style = TypographyTokens.MicroLabel,
                    color = TextToneTokens.supporting(MaterialTheme.colorScheme.onSurface),
                    modifier = eyebrowTag?.let { Modifier.testTag(it) } ?: Modifier
                )
            }
            chipText?.let { badge ->
                Spacer(modifier = Modifier.height(SpacingTokens.Xs))
                StatusChip(
                    text = badge,
                    variant = chipVariant,
                    modifier = chipTag?.let { Modifier.testTag(it) } ?: Modifier
                )
            }
            AnimatedContent(
                targetState = title,
                label = "lesson-cue-title"
            ) { animatedTitle ->
                Text(
                    text = animatedTitle,
                    style = titleStyle,
                    fontWeight = titleWeight,
                    color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface),
                    modifier = titleTag?.let { Modifier.testTag(it) } ?: Modifier
                )
            }
            AnimatedContent(
                targetState = body,
                label = "lesson-cue-body"
            ) { animatedBody ->
                Text(
                    text = animatedBody,
                    style = bodyStyle,
                    color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface),
                    modifier = bodyTag?.let { Modifier.testTag(it) } ?: Modifier
                )
            }
        }
    }
}

@Composable
internal fun lessonStatusAccentColor(tone: LessonStatusTone): Color {
    return when (tone) {
        LessonStatusTone.Neutral -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.24f)
        LessonStatusTone.Highlight -> Color(0x809ADBC7)
        LessonStatusTone.Retry -> Color(0x80F2B880)
        LessonStatusTone.Confirmed -> Color(0x809ADBC7)
        LessonStatusTone.Warning -> Color(0x80D9D48A)
    }
}

internal fun lessonStatusSurfaceColor(tone: LessonStatusTone): Color {
    return when (tone) {
        LessonStatusTone.Neutral -> Color(0x0F000000)
        LessonStatusTone.Highlight -> Color(0x1A9ADBC7)
        LessonStatusTone.Retry -> Color(0x1AF2B880)
        LessonStatusTone.Confirmed -> Color(0x1A9ADBC7)
        LessonStatusTone.Warning -> Color(0x1AD9D48A)
    }
}
