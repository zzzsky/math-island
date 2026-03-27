package com.mathisland.app.feature.level

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.material3.MaterialTheme
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.components.TabletInfoCard
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.TextToneTokens

@Composable
fun LevelSupportRail(
    state: LevelSupportRailState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.testTag("lesson-support-rail"),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
    ) {
        StatusChip(
            text = state.headerBadgeText,
            variant = state.headerBadgeVariant
        )
        state.cards.forEach { card ->
            TabletInfoCard(
                title = card.title,
                subtitle = card.subtitle,
                body = card.body,
                accentColor = card.tone?.let { lessonStatusAccentColor(it) }
                    ?: MaterialTheme.colorScheme.secondary.copy(alpha = 0.24f),
                containerColor = card.tone?.let { lessonStatusSurfaceColor(it) }
                    ?: TextToneTokens.low(MaterialTheme.colorScheme.surface),
                badgeText = card.badgeText,
                badgeVariant = card.badgeVariant,
                badgeTag = "${card.tag}-chip",
                modifier = Modifier.testTag(card.tag)
            )
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
