package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.mathisland.app.domain.model.MatchingGroup
import com.mathisland.app.domain.model.MatchingRound
import com.mathisland.app.domain.model.Question
import com.mathisland.app.feature.level.lessonGuidanceBadgeTextFor
import com.mathisland.app.feature.level.lessonGuidanceBadgeVariantFor
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun MatchingQuestionPane(
    question: Question,
    feedback: AnswerFeedbackUiState? = null,
    actionState: RendererActionState = rendererActionStateFor(feedback = feedback, inputEnabled = true),
    onAnswer: (String) -> Unit
) {
    val rounds = matchingRoundsForQuestion(question)
    var matchingState by remember(question.prompt, question.leftItems, question.rightItems, question.matchingGroups) {
        mutableStateOf(MatchingAnswerState())
    }
    val currentRound = rounds[matchingState.currentRoundIndex]
    val groups = currentRound.groups
    val hasGroupedSections = groups.size > 1
    val isFinalRound = matchingState.currentRoundIndex == rounds.lastIndex
    val canAdvance = actionState.enabled && matchingState.canAdvanceRound(rounds)
    val canSubmit = actionState.enabled && isFinalRound && matchingState.isRoundComplete(currentRound)

    RendererPanelStack(
        rendererTag = "renderer-matching",
        prompt = {
            RendererPromptCard(
                prompt = currentRound.prompt,
                actionState = actionState
            )
        },
        context = {
            RendererGuidanceCard(
                title = if (rounds.size > 1) "先完成这一轮配对" else "先完成配对",
                body = if (rounds.size > 1) {
                    "先完成当前轮，再进入下一轮。"
                } else {
                    "先点左侧项目，再点右侧目标完成连接。"
                },
                badgeText = lessonGuidanceBadgeTextFor(actionState.phase),
                badgeVariant = lessonGuidanceBadgeVariantFor(actionState.phase),
                containerColor = rendererStageContainerColorFor(actionState.stageTone())
            )
        },
        feedback = feedback
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(RendererTokens.ActionGroupGap)
        ) {
            RendererSectionHeader(
                badgeText = actionState.sectionBadgeText(),
                badgeVariant = actionState.sectionBadgeVariant(),
                title = if (rounds.size > 1) {
                    "第 ${matchingState.currentRoundIndex + 1} / ${rounds.size} 轮"
                } else {
                    actionState.sectionTitle()
                },
                body = if (rounds.size > 1) {
                    currentRound.title
                } else {
                    actionState.sectionBody()
                }
            )
            if (rounds.size > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("matching-round-progress"),
                    horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)
                ) {
                    rounds.forEachIndexed { roundIndex, _ ->
                        StatusChip(
                                text = when {
                                    roundIndex < matchingState.currentRoundIndex -> "已完成"
                                    roundIndex == matchingState.currentRoundIndex -> "当前轮"
                                    else -> "下一轮"
                                },
                                variant = when {
                                    roundIndex < matchingState.currentRoundIndex -> StatusVariant.Success
                                    roundIndex == matchingState.currentRoundIndex -> StatusVariant.Highlight
                                    else -> StatusVariant.Neutral
                                },
                            modifier = Modifier.testTag("matching-round-chip-$roundIndex")
                        )
                    }
                }
            }
            groups.forEachIndexed { groupIndex, group ->
                val groupAssignments = matchingState.assignmentsForGroup(
                    roundIndex = matchingState.currentRoundIndex,
                    groupIndex = groupIndex
                )
                val selectedLeft = matchingState.selectedLeft
                StoryPanelCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("matching-group-$groupIndex"),
                    level = SurfaceLevel.Secondary,
                    containerColor = RendererTokens.MatchingSurface,
                    borderColor = RendererTokens.MatchingSlotBorder,
                    shape = RadiusTokens.CardMd
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(SpacingTokens.Lg),
                        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
                    ) {
                        if (hasGroupedSections) {
                            Text(
                                text = group.title,
                                style = TypographyTokens.SectionTitle,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag(matchingLeftColumnTag(groupIndex, hasGroupedSections)),
                                verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                            ) {
                                group.leftItems.forEachIndexed { leftIndex, item ->
                                    val assignedRightIndex = groupAssignments[leftIndex]
                                    val selected = selectedLeft?.groupIndex == groupIndex &&
                                        selectedLeft.leftIndex == leftIndex
                                    StoryPanelCard(
                                        modifier = Modifier.testTag(matchingLeftTag(groupIndex, leftIndex, hasGroupedSections)),
                                        level = SurfaceLevel.Secondary,
                                        containerColor = when {
                                            assignedRightIndex != null -> RendererTokens.MatchingConnectedSurface
                                            selected -> RendererTokens.MatchingSurface
                                            else -> RendererTokens.OptionSurface
                                        },
                                        borderColor = when {
                                            assignedRightIndex != null -> RendererTokens.MatchingConnectedBorder
                                            selected -> MaterialTheme.colorScheme.primary
                                            else -> null
                                        },
                                        shape = RadiusTokens.CardMd
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(SpacingTokens.Lg),
                                            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)
                                        ) {
                                            assignedRightIndex?.let {
                                                StatusChip(
                                                    text = "已连接",
                                                    variant = StatusVariant.Success,
                                                    modifier = Modifier.testTag(matchingLeftChipTag(groupIndex, leftIndex, hasGroupedSections))
                                                )
                                            }
                                            Text(
                                                text = item,
                                                style = TypographyTokens.FeatureTitle,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            ActionButton(
                                                text = if (selected) "已选中" else "选中后连接",
                                                onClick = { matchingState = matchingState.selectLeft(groupIndex, leftIndex) },
                                                modifier = Modifier.testTag(matchingLeftSelectTag(groupIndex, leftIndex, hasGroupedSections)),
                                                role = if (selected) ActionRole.Secondary else ActionRole.OutlinedSecondary
                                            )
                                        }
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag(matchingRightColumnTag(groupIndex, hasGroupedSections)),
                                verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                            ) {
                                group.rightItems.forEachIndexed { rightIndex, item ->
                                    val linkedLeftIndex = groupAssignments.entries
                                        .firstOrNull { it.value == rightIndex }
                                        ?.key
                                    StoryPanelCard(
                                        modifier = Modifier.testTag(matchingRightTag(groupIndex, rightIndex, hasGroupedSections)),
                                        level = SurfaceLevel.Secondary,
                                        containerColor = if (linkedLeftIndex != null) {
                                            RendererTokens.MatchingConnectedSurface
                                        } else {
                                            RendererTokens.MatchingSlotSurface
                                        },
                                        borderColor = if (linkedLeftIndex != null) {
                                            RendererTokens.MatchingConnectedBorder
                                        } else {
                                            RendererTokens.MatchingSlotBorder
                                        },
                                        shape = RadiusTokens.CardMd
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(SpacingTokens.Lg),
                                            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)
                                        ) {
                                            linkedLeftIndex?.let {
                                                StatusChip(
                                                    text = "已匹配",
                                                    variant = StatusVariant.Success,
                                                    modifier = Modifier.testTag(matchingRightChipTag(groupIndex, rightIndex, hasGroupedSections))
                                                )
                                            }
                                            Text(
                                                text = item,
                                                style = TypographyTokens.FeatureTitle,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            ActionButton(
                                                text = if (selectedLeft?.groupIndex == groupIndex) "连接到这里" else "等待选择",
                                                onClick = { matchingState = matchingState.assignTo(groupIndex, rightIndex) },
                                                enabled = selectedLeft?.groupIndex == groupIndex && actionState.enabled,
                                                modifier = Modifier.testTag(matchingRightAssignTag(groupIndex, rightIndex, hasGroupedSections)),
                                                role = ActionRole.Secondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            StoryPanelCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("matching-summary-card"),
                level = SurfaceLevel.Secondary,
                containerColor = RendererTokens.MatchingSurface,
                borderColor = RendererTokens.MatchingSlotBorder,
                shape = RadiusTokens.CardMd
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacingTokens.Lg),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                ) {
                    Text(
                        text = if (matchingState.isGroupSetComplete(groups)) {
                            "配对完成"
                        } else if (rounds.size > 1) {
                            "本轮进度 ${matchingState.completedPairs(groups)}/${matchingState.totalPairs(groups)}"
                        } else {
                            "配对进度 ${matchingState.completedPairs(groups)}/${matchingState.totalPairs(groups)}"
                        },
                        style = TypographyTokens.SupportingLabel,
                        color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
                    )
                    if (rounds.size > 1 && !isFinalRound) {
                        ActionButton(
                            text = "进入下一轮",
                            onClick = {
                                matchingState = matchingState.advanceRound(rounds)
                            },
                            enabled = canAdvance,
                            modifier = Modifier.testTag("matching-next-round"),
                            role = ActionRole.Secondary
                        )
                    } else {
                        ActionButton(
                            text = actionState.resolveLabel("提交配对"),
                            onClick = {
                                onAnswer(
                                    if (rounds.size > 1) {
                                        matchingState.encodeRounds(rounds)
                                    } else {
                                        matchingState.encodeGroupsForSingleRound(groups)
                                    }
                                )
                            },
                            enabled = canSubmit,
                            modifier = Modifier.testTag("matching-submit"),
                            role = actionState.resolveRole(ActionRole.Primary)
                        )
                    }
                }
            }
        }
    }
}

private fun matchingRoundsForQuestion(question: Question): List<MatchingRound> =
    question.matchingRounds.takeIf { it.isNotEmpty() }
        ?: listOf(
            MatchingRound(
                title = "",
                prompt = question.prompt,
                groups = question.matchingGroups.takeIf { it.isNotEmpty() }
                    ?: listOf(
                        MatchingGroup(
                            title = "",
                            leftItems = question.leftItems,
                            rightItems = question.rightItems
                        )
                    )
            )
        )

private fun matchingLeftColumnTag(groupIndex: Int, grouped: Boolean): String =
    if (grouped) "matching-left-column-$groupIndex" else "matching-left-column"

private fun matchingRightColumnTag(groupIndex: Int, grouped: Boolean): String =
    if (grouped) "matching-right-column-$groupIndex" else "matching-right-column"

private fun matchingLeftTag(groupIndex: Int, leftIndex: Int, grouped: Boolean): String =
    if (grouped) "matching-left-$groupIndex-$leftIndex" else "matching-left-$leftIndex"

private fun matchingRightTag(groupIndex: Int, rightIndex: Int, grouped: Boolean): String =
    if (grouped) "matching-right-$groupIndex-$rightIndex" else "matching-right-$rightIndex"

private fun matchingLeftChipTag(groupIndex: Int, leftIndex: Int, grouped: Boolean): String =
    if (grouped) "matching-left-chip-$groupIndex-$leftIndex" else "matching-left-chip-$leftIndex"

private fun matchingRightChipTag(groupIndex: Int, rightIndex: Int, grouped: Boolean): String =
    if (grouped) "matching-right-chip-$groupIndex-$rightIndex" else "matching-right-chip-$rightIndex"

private fun matchingLeftSelectTag(groupIndex: Int, leftIndex: Int, grouped: Boolean): String =
    if (grouped) "matching-left-select-$groupIndex-$leftIndex" else "matching-left-select-$leftIndex"

private fun matchingRightAssignTag(groupIndex: Int, rightIndex: Int, grouped: Boolean): String =
    if (grouped) "matching-right-assign-$groupIndex-$rightIndex" else "matching-right-assign-$rightIndex"
