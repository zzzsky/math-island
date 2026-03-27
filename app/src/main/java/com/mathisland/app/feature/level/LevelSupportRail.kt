package com.mathisland.app.feature.level

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.theme.SpacingTokens

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
            LessonCueCard(
                title = card.subtitle,
                body = card.body,
                eyebrowText = card.title,
                tone = card.tone ?: LessonStatusTone.Neutral,
                chipText = card.badgeText,
                chipVariant = card.badgeVariant,
                chipTag = "${card.tag}-chip",
                modifier = Modifier,
                cardTag = card.tag
            )
        }
    }
}
