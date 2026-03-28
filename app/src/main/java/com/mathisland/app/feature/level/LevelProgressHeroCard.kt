package com.mathisland.app.feature.level

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens
import androidx.compose.ui.unit.dp

@Composable
fun LevelProgressHeroCard(
    lesson: Lesson,
    questionIndex: Int,
    totalQuestions: Int,
    heroState: LevelProgressHeroState,
    modifier: Modifier = Modifier
) {
    StoryPanelCard(
        modifier = modifier.fillMaxWidth(),
        level = SurfaceLevel.Secondary,
        containerColor = lessonStatusSurfaceColor(heroState.heroTone),
        shape = RadiusTokens.CardLg
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.Lg, vertical = SpacingTokens.Md),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusChip(
                    text = heroState.heroBadgeText,
                    variant = heroState.heroBadgeVariant,
                    modifier = Modifier.testTag("lesson-hero-chip")
                )
                if (heroState.timerChipText != null) {
                    StatusChip(
                        text = heroState.timerChipText,
                        modifier = Modifier.testTag("lesson-timer"),
                        variant = StatusVariant.Caution
                    )
                } else {
                    Text(
                        text = heroState.trailingSummary,
                        style = TypographyTokens.SupportingLabel,
                        color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
            Text(
                text = lesson.title,
                style = TypographyTokens.SectionTitle,
                fontWeight = FontWeight.Bold
            )
            LinearProgressIndicator(
                progress = { (questionIndex + 1).toFloat() / totalQuestions.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RadiusTokens.Pill),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = Color.White.copy(alpha = 0.12f)
            )
            Text(
                text = "第 ${questionIndex + 1} / $totalQuestions 题",
                style = TypographyTokens.BodySecondary,
                color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
            )
            Text(
                text = heroState.routeSummary,
                style = TypographyTokens.BodyPrimary,
                color = TextToneTokens.supporting(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.testTag("lesson-route-summary")
            )
            StatusChip(
                text = heroState.routeBadgeText,
                variant = heroState.routeBadgeVariant,
                modifier = Modifier.testTag("lesson-route-chip")
            )
            heroState.timerNote?.let { timerNote ->
                Text(
                    text = timerNote,
                    style = TypographyTokens.BodyPrimary,
                    color = TextToneTokens.supporting(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.testTag("lesson-timer-note")
                )
            }
        }
    }
}
