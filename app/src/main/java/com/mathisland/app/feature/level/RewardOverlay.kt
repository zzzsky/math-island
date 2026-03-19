package com.mathisland.app.feature.level

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.domain.model.RewardSummary
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.components.SurfaceCard
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.components.TabletInfoCard
import com.mathisland.app.ui.components.TabletStatTile
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun RewardOverlay(
    state: RewardOverlayUiState,
    onContinue: () -> Unit,
    onSecondaryAction: (() -> Unit)? = null
) {
    val reward = state.reward
    val panelColor = Color(0xF3183A49)
    val accentGold = Color(0xFFF2D48B)
    val accentMint = Color(0xFF9ADBC7)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SurfaceCard(
            modifier = Modifier.fillMaxWidth(0.72f),
            level = SurfaceLevel.Page,
            containerColor = panelColor,
            shape = RoundedCornerShape(32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                RewardSummaryHeader(
                    reward = reward,
                    totalStars = state.totalStars,
                    accentGold = accentGold,
                    accentMint = accentMint
                )
                RewardSectionHeader(
                    title = "本次表现",
                    body = "先看清结果，再决定是继续推进、回放复习，还是立刻再试一次。"
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TabletStatTile(
                        modifier = Modifier.weight(1f),
                        title = "本关星星",
                        value = reward.starsEarned.toString(),
                        accent = accentGold
                    )
                    TabletStatTile(
                        modifier = Modifier.weight(1f),
                        title = "答对题目",
                        value = "${reward.correctAnswers}/${reward.totalQuestions}",
                        accent = MaterialTheme.colorScheme.secondary
                    )
                    TabletStatTile(
                        modifier = Modifier.weight(1f),
                        title = "累计星星",
                        value = state.totalStars.toString(),
                        accent = accentMint
                    )
                }
                RewardHighlights(
                    reward = reward
                )
                RewardSectionHeader(
                    title = "继续航线",
                    body = "下一步已经整理好，保留当前流程和按钮契约，只把决策信息摆得更清楚。"
                )
                StoryPanelCard(
                    level = SurfaceLevel.Secondary,
                    shape = RoundedCornerShape(28.dp),
                    containerColor = Color.White.copy(alpha = 0.06f),
                    borderColor = accentGold.copy(alpha = 0.22f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TabletInfoCard(
                            title = "下一步",
                            subtitle = state.nextStepTitle,
                            body = state.nextStepBody,
                            modifier = Modifier.testTag("reward-next-step-card")
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            reward.secondaryActionLabel?.let { label ->
                                onSecondaryAction?.let { action ->
                                    ActionButton(
                                        text = label,
                                        modifier = Modifier
                                            .testTag("reward-retry-sprint")
                                            .padding(end = 12.dp),
                                        onClick = action,
                                        role = ActionRole.OutlinedSecondary,
                                        contentColor = MaterialTheme.colorScheme.secondary,
                                        borderColor = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                            ActionButton(
                                text = state.continueLabel,
                                modifier = Modifier.testTag("reward-return-map"),
                                onClick = onContinue,
                                role = ActionRole.Primary,
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RewardSummaryHeader(
    reward: RewardSummary,
    totalStars: Int,
    accentGold: Color,
    accentMint: Color
) {
    StoryPanelCard(
        level = SurfaceLevel.Primary,
        shape = RoundedCornerShape(30.dp),
        containerColor = Color.White.copy(alpha = 0.03f),
        borderColor = Color.White.copy(alpha = 0.14f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            accentGold.copy(alpha = 0.14f),
                            Color.White.copy(alpha = 0.04f),
                            accentMint.copy(alpha = 0.12f)
                        )
                    )
                )
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(30.dp))
                .padding(22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatusChip(
                        text = if (reward.timedOut) "时间到" else "关卡完成",
                        variant = if (reward.timedOut) StatusVariant.Caution else StatusVariant.Success
                    )
                    Text(
                        text = reward.lessonTitle,
                        style = TypographyTokens.SectionTitle,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = rewardHeaderSummary(reward),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(
                    modifier = Modifier
                        .width(150.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "总星星",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                    )
                    Text(
                        text = totalStars.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        color = accentGold
                    )
                    Text(
                        text = if (reward.timedOut) "复习优先级已更新" else "进度已写入航线",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RewardHighlights(
    reward: RewardSummary
) {
    val hasHighlights =
        reward.timedOut ||
            (reward.gradeLabel != null && reward.gradeDescription != null) ||
            reward.newIslandTitle != null ||
            reward.newStickerName != null
    if (!hasHighlights) {
        return
    }

    RewardSectionHeader(
        title = "奖励与进展",
        body = "把这轮最重要的变化单独收拢成卡片，回地图前先看清楚收获和提醒。"
    )
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (reward.timedOut) {
            TabletInfoCard(
                title = "冲刺结果",
                subtitle = "时间到，本次冲刺记为练习",
                body = "本关不会计入通关，但会把综合挑战加入下一轮优先复习。"
            )
        }
        if (reward.gradeLabel != null && reward.gradeDescription != null) {
            TabletInfoCard(
                title = "成绩分级",
                subtitle = reward.gradeLabel,
                body = reward.gradeDescription
            )
        }
        reward.newIslandTitle?.let { title ->
            TabletInfoCard(
                title = "新岛屿解锁",
                subtitle = title,
                body = "前一座主岛已经完成，可以继续拓展地图。"
            )
        }
        reward.newStickerName?.let { sticker ->
            TabletInfoCard(
                title = "贴纸奖励",
                subtitle = sticker,
                body = "岛屿全清后会进入宝箱收藏。"
            )
        }
    }
}

@Composable
private fun RewardSectionHeader(
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
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
        )
    }
}

private fun rewardHeaderSummary(reward: RewardSummary): String =
    when {
        reward.timedOut -> "这次冲刺先记为练习，错题与综合挑战会优先排进下一轮复习。"
        reward.newIslandTitle != null -> "主线推进完成，地图已经为你打开下一段岛屿航线。"
        reward.newStickerName != null -> "本次奖励已经收入宝箱，回地图后可以顺手查看收藏。"
        reward.gradeLabel != null -> "成绩与推荐路线已经整理好，继续前先快速看一眼报告。"
        else -> "本次结果已经结算完毕，继续按钮会按当前推荐路线推进。"
    }
