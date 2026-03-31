package com.mathisland.app.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.domain.usecase.HomeState
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.components.TabletInfoCard
import com.mathisland.app.ui.components.TabletStatTile
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun HomeHeroPanel(
    state: HomeState,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.testTag("home-hero-panel"),
        colors = CardDefaults.cardColors(containerColor = Color(0xCC113B4A)),
        shape = RadiusTokens.CardLg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpacingTokens.Section),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)) {
                StatusChip(text = "TABLET MVP", variant = StatusVariant.Neutral)
                Text(
                    text = "数学岛",
                    style = TypographyTokens.ScreenHero,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "短回合数学冒险，把今天的 3 到 5 分钟变成一段可见的地图推进。",
                    style = TypographyTokens.BodyLead,
                    color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface)
                )
                state.nextLessonTitle?.let { lessonTitle ->
                    TabletInfoCard(
                        modifier = Modifier.testTag("home-recommendation-card"),
                        title = if (state.isReview) "小海鸥求助" else "继续冒险",
                        subtitle = listOfNotNull(lessonTitle, state.nextLessonFocus)
                            .joinToString(" · "),
                        body = state.nextLessonSummary.orEmpty()
                    )
                }
            }

            Row(
                modifier = Modifier.testTag("home-stats-row"),
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
            ) {
                TabletStatTile(
                    modifier = Modifier.weight(1f),
                    title = "星星",
                    value = state.totalStars.toString(),
                    accent = MaterialTheme.colorScheme.primary
                )
                TabletStatTile(
                    modifier = Modifier.weight(1f),
                    title = "贴纸",
                    value = state.stickerCount.toString(),
                    accent = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
