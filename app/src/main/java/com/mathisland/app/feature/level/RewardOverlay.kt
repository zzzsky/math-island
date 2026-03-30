package com.mathisland.app.feature.level

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.domain.model.RewardSummary
import com.mathisland.app.feature.map.motionSpec
import com.mathisland.app.feature.map.rewardFeedbackKind
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.components.ReturnActionCard
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.components.SummarySpotlightCard
import com.mathisland.app.ui.components.SurfaceCard
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.components.TabletInfoCard
import com.mathisland.app.ui.components.TabletStatTile
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun RewardOverlay(
    state: RewardOverlayUiState,
    onContinue: () -> Unit,
    onSecondaryAction: (() -> Unit)? = null
) {
    val reward = state.reward
    val motionSpec = rewardFeedbackKind(reward).motionSpec()
    val revealProgress = remember { Animatable(0f) }

    LaunchedEffect(
        reward.lessonTitle,
        reward.starsEarned,
        reward.correctAnswers,
        reward.totalQuestions,
        reward.newIslandTitle,
        reward.newStickerName,
        reward.timedOut
    ) {
        revealProgress.stop()
        revealProgress.snapTo(0f)
        revealProgress.animateTo(1f, tween(durationMillis = 300, easing = FastOutSlowInEasing))
    }

    val motionValue = revealProgress.value
    val panelColor = Color(0xF3183A49)
    val accentMint = Color(0xFF9ADBC7)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SurfaceCard(
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .graphicsLayer {
                    val lift = 1f + (motionValue * motionSpec.cardScaleBoost)
                    scaleX = lift
                    scaleY = lift
                    alpha = 0.92f + (motionValue * 0.08f)
                    translationY = (1f - motionValue) * 10f
                },
            level = SurfaceLevel.Page,
            containerColor = panelColor,
            shape = RadiusTokens.Sheet
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpacingTokens.Sheet)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.Lg)
            ) {
                RewardRevealBlock(
                    visible = motionValue >= motionSpec.chipRevealAt
                ) {
                    RewardSummaryHeader(
                        reward = reward,
                        totalStars = state.totalStars,
                        primaryAccent = motionSpec.accent,
                        accentMint = accentMint,
                        returnLabel = state.continueLabel,
                        returnVariant = rewardStatusVariant(reward)
                    )
                }
                RewardRevealBlock(
                    visible = motionValue >= motionSpec.summaryRevealAt
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)) {
                        RewardSectionHeader(
                            title = "本次表现",
                            body = "先看清结果，再决定是继续推进、回放复习，还是立刻再试一次。"
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                        ) {
                            TabletStatTile(
                                modifier = Modifier.weight(1f),
                                title = "本关星星",
                                value = reward.starsEarned.toString(),
                                accent = motionSpec.accent
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
                    }
                }
                RewardRevealBlock(
                    visible = motionValue >= motionSpec.spotlightRevealAt
                ) {
                    SummarySpotlightCard(
                        label = "本轮结论",
                        title = rewardSpotlightTitle(reward),
                        body = rewardSpotlightBody(reward),
                        accent = motionSpec.accent
                    )
                }
                RewardRevealBlock(
                    visible = motionValue >= motionSpec.supportingRevealAt
                ) {
                    RewardHighlights(reward = reward)
                }
                RewardRevealBlock(
                    visible = motionValue >= motionSpec.detailRevealAt
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)) {
                        RewardSectionHeader(
                            title = "继续航线",
                            body = "下一步已经整理好，保留当前流程和按钮契约，只把决策信息摆得更清楚。"
                        )
                        StoryPanelCard(
                            level = SurfaceLevel.Secondary,
                            shape = RadiusTokens.CardLg,
                            containerColor = Color.White.copy(alpha = 0.06f),
                            borderColor = motionSpec.accent.copy(alpha = 0.22f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(SpacingTokens.Xl),
                                verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
                            ) {
                                AnimatedVisibility(
                                    visible = motionValue >= motionSpec.detailRevealAt,
                                    enter = fadeIn(tween(150)) +
                                        slideInVertically(tween(170)) { fullHeight -> fullHeight / 7 } +
                                        scaleIn(tween(150), initialScale = 0.96f)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)) {
                                        StatusChip(
                                            text = state.continueLabel,
                                            variant = rewardStatusVariant(reward),
                                            modifier = Modifier.testTag("reward-next-step-kind-pill")
                                        )
                                        TabletInfoCard(
                                            title = state.nextStepLabel,
                                            subtitle = state.nextStepTitle,
                                            body = state.nextStepBody,
                                            badgeText = state.continueLabel,
                                            badgeVariant = rewardStatusVariant(reward),
                                            modifier = Modifier.testTag("reward-next-step-card")
                                        )
                                    }
                                }
                                AnimatedVisibility(
                                    visible = motionValue >= motionSpec.supportingRevealAt,
                                    enter = fadeIn(tween(180)) +
                                        slideInVertically(tween(200)) { fullHeight -> fullHeight / 8 } +
                                        scaleIn(tween(180), initialScale = 0.98f)
                                ) {
                                    TabletInfoCard(
                                        title = "回地图后",
                                        subtitle = state.nextStepDetailTitle,
                                        body = state.nextStepDetailBody,
                                        accentColor = motionSpec.accent.copy(alpha = 0.8f),
                                        badgeText = state.continueLabel,
                                        badgeVariant = rewardStatusVariant(reward),
                                        modifier = Modifier.testTag("reward-next-step-detail-card")
                                    )
                                }
                                AnimatedVisibility(
                                    visible = motionValue >= motionSpec.trailingRevealAt,
                                    enter = fadeIn(tween(180)) +
                                        slideInVertically(tween(220)) { fullHeight -> fullHeight / 8 } +
                                        scaleIn(tween(180), initialScale = 0.98f)
                                ) {
                                    ReturnActionCard(
                                        label = state.nextActionLabel,
                                        title = state.nextActionTitle,
                                        body = state.nextActionBody,
                                        accentColor = motionSpec.accent.copy(alpha = 0.8f),
                                        badgeVariant = rewardStatusVariant(reward),
                                        modifier = Modifier.testTag("reward-next-action-card"),
                                        badgeTag = "reward-next-action-pill"
                                    )
                                }
                                AnimatedVisibility(
                                    visible = motionValue >= motionSpec.trailingRevealAt,
                                    enter = fadeIn(tween(180)) +
                                        slideInVertically(tween(220)) { fullHeight -> fullHeight / 8 }
                                ) {
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
        }
    }
}

@Composable
private fun RewardRevealBlock(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(180)) +
            slideInVertically(tween(200)) { fullHeight -> fullHeight / 8 } +
            scaleIn(tween(180), initialScale = 0.98f)
    ) {
        content()
    }
}

@Composable
private fun RewardSummaryHeader(
    reward: RewardSummary,
    totalStars: Int,
    primaryAccent: Color,
    accentMint: Color,
    returnLabel: String,
    returnVariant: StatusVariant
) {
    StoryPanelCard(
        level = SurfaceLevel.Primary,
        shape = RadiusTokens.Sheet,
        containerColor = Color.White.copy(alpha = 0.03f),
        borderColor = Color.White.copy(alpha = 0.14f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RadiusTokens.Sheet)
                .background(
                    Brush.linearGradient(
                        listOf(
                            primaryAccent.copy(alpha = 0.14f),
                            Color.White.copy(alpha = 0.04f),
                            accentMint.copy(alpha = 0.12f)
                        )
                    )
                )
                .border(1.dp, Color.White.copy(alpha = 0.08f), RadiusTokens.Sheet)
                .padding(SpacingTokens.Xxl)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Lg),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)) {
                        StatusChip(
                            text = if (reward.timedOut) "时间到" else "关卡完成",
                            variant = if (reward.timedOut) StatusVariant.Caution else StatusVariant.Success
                        )
                        StatusChip(
                            text = returnLabel,
                            variant = returnVariant,
                            modifier = Modifier.testTag("reward-return-kind-pill")
                        )
                    }
                    Text(
                        text = reward.lessonTitle,
                        style = TypographyTokens.SectionTitle,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = rewardHeaderSummary(reward),
                        style = TypographyTokens.BodyLead,
                        color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
                    )
                }
                Column(
                    modifier = Modifier
                        .width(150.dp)
                        .clip(RadiusTokens.CardMd)
                        .background(Color.White.copy(alpha = 0.08f))
                        .padding(horizontal = SpacingTokens.Md, vertical = SpacingTokens.Lg),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "总星星",
                        style = TypographyTokens.SupportingLabel,
                        color = TextToneTokens.supporting(MaterialTheme.colorScheme.onSurface)
                    )
                    Text(
                        text = totalStars.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        color = primaryAccent
                    )
                    Text(
                        text = if (reward.timedOut) "复习优先级已更新" else "进度已写入航线",
                        style = TypographyTokens.BodySecondary,
                        color = TextToneTokens.low(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        }
    }
}

private fun rewardStatusVariant(reward: RewardSummary): StatusVariant =
    when {
        reward.timedOut -> StatusVariant.Highlight
        reward.newIslandTitle != null -> StatusVariant.Recommended
        reward.newStickerName != null -> StatusVariant.Highlight
        else -> StatusVariant.Success
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
    Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)) {
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
            style = TypographyTokens.BodySecondary,
            color = TextToneTokens.supporting(MaterialTheme.colorScheme.onSurface)
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

private fun rewardSpotlightTitle(reward: RewardSummary): String =
    when {
        reward.timedOut -> "这轮先记为练习"
        reward.newIslandTitle != null -> "新岛已经解锁"
        reward.newStickerName != null -> "贴纸已经收入宝箱"
        reward.gradeLabel != null -> reward.gradeLabel
        else -> "本轮已顺利完成"
    }

private fun rewardSpotlightBody(reward: RewardSummary): String =
    when {
        reward.timedOut -> "先回地图查看回放与推荐路线，再决定是否立刻重新发起冲刺。"
        reward.newIslandTitle != null -> "地图会把焦点切到新的主线岛屿，方便你继续往前推进。"
        reward.newStickerName != null -> "奖励已经进入收藏，下次回地图时可以顺手打开宝箱查看。"
        reward.gradeDescription != null -> reward.gradeDescription
        else -> "继续按钮会按照当前推荐路线，带你回到地图继续探索。"
    }
