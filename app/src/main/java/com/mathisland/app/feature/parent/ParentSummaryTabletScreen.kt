package com.mathisland.app.feature.parent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.components.SummarySpotlightCard
import com.mathisland.app.ui.components.SurfaceCard
import com.mathisland.app.ui.components.TabletInfoCard
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ParentSummaryTabletScreen(
    state: ParentSummaryUiState,
    onBackHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("parent-summary-screen"),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Lg)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "家长学习摘要",
                style = TypographyTokens.ScreenTitle,
                fontWeight = FontWeight.Black
            )
        }
        SurfaceCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            level = SurfaceLevel.Page
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("parent-summary-scroll")
                    .verticalScroll(rememberScrollState())
                    .padding(SpacingTokens.Xxl),
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.Lg)
            ) {
                ParentSummaryEndingHero(state = state)
                ParentSummaryPrimarySummary(state = state)
                ParentSummarySupportingDetails(state = state)
                ParentSummaryNextAction(
                    state = state,
                    onBackHome = onBackHome
                )
            }
        }
    }
}

@Composable
private fun ParentSummaryEndingHero(state: ParentSummaryUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("parent-summary-hero-section"),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
    ) {
        ParentSummaryHeroPanel(state = state)
    }
}

@Composable
private fun ParentSummaryPrimarySummary(state: ParentSummaryUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("parent-summary-primary-summary-section"),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
    ) {
        ParentSummarySectionHeader(
            title = "学习报告",
            body = "先看今天最重要的学习结论，再决定接下来优先跟进什么。"
        )
        SummarySpotlightCard(
            label = "今日重点",
            title = state.todayLearnedText,
            body = "今天完成的内容已经整理成一条主结论，方便快速确认主线进度。",
            accent = Color(0xFF8ECae6),
            modifier = Modifier.testTag("parent-summary-primary-card")
        )
    }
}

@Composable
private fun ParentSummarySupportingDetails(state: ParentSummaryUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("parent-summary-supporting-details-section"),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
    ) {
        ParentSummarySectionHeader(
            title = "补充信息",
            body = "把今天需要关注的薄弱项和连续学习情况放在次级层，方便扫读。"
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
        ) {
            TabletInfoCard(
                modifier = Modifier
                    .weight(1f)
                    .testTag("parent-summary-weak-card"),
                title = "薄弱知识点",
                subtitle = state.weakTopicsText,
                body = "这些知识点建议优先回看，下一轮复习会优先照顾这里。"
            )
            TabletInfoCard(
                modifier = Modifier
                    .weight(1f)
                    .testTag("parent-summary-streak-card"),
                title = "连续学习",
                subtitle = state.streakText,
                body = "保持稳定节奏比一次性拉长时长更重要。"
            )
        }
    }
}

@Composable
private fun ParentSummaryNextAction(
    state: ParentSummaryUiState,
    onBackHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("parent-summary-next-action-section"),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
    ) {
        ParentSummarySectionHeader(
            title = "建议行动",
            body = "把下一步优先事项单独抬高，方便结束前快速决定后续安排。"
        )
        StoryPanelCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("parent-summary-next-action-card"),
            level = SurfaceLevel.Secondary,
            containerColor = Color(0xFF8ECae6).copy(alpha = 0.12f),
            borderColor = Color(0xFF8ECae6).copy(alpha = 0.24f),
            shape = RadiusTokens.CardLg
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpacingTokens.Xl),
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
            ) {
                Text(
                    text = "下次优先关注",
                    style = TypographyTokens.SupportingLabel,
                    color = TextToneTokens.supporting(MaterialTheme.colorScheme.onSurface)
                )
                Text(
                    text = state.recommendedIslandText,
                    style = TypographyTokens.FeatureTitle,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "建议下一次先从这里继续，先收窄薄弱项，再回到主线推进。",
                    style = TypographyTokens.BodyPrimary,
                    color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    ActionButton(
                        text = "返回首页",
                        onClick = onBackHome,
                        role = ActionRole.Completed
                    )
                }
            }
        }
    }
}

@Composable
private fun ParentSummarySectionHeader(
    title: String,
    body: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = TypographyTokens.FeatureTitle,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = body,
            style = TypographyTokens.BodySecondary,
            color = TextToneTokens.supporting(MaterialTheme.colorScheme.onSurface)
        )
    }
}
