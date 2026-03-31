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
    var matchingState by remember(question.prompt, question.leftItems, question.rightItems) {
        mutableStateOf(MatchingAnswerState())
    }
    val leftItems = question.leftItems
    val rightItems = question.rightItems
    val canSubmit = actionState.enabled && matchingState.isComplete(leftItems.size)

    RendererPanelStack(
        rendererTag = "renderer-matching",
        prompt = {
            RendererPromptCard(
                prompt = question.prompt,
                actionState = actionState
            )
        },
        context = {
            RendererGuidanceCard(
                title = "先完成配对",
                body = "先点左侧项目，再点右侧目标完成连接。",
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
                title = actionState.sectionTitle(),
                body = actionState.sectionBody()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("matching-left-column"),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                ) {
                    leftItems.forEachIndexed { index, item ->
                        val assignedRightIndex = matchingState.assignments[index]
                        val selected = matchingState.selectedLeftIndex == index
                        StoryPanelCard(
                            modifier = Modifier.testTag("matching-left-$index"),
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
                                        modifier = Modifier.testTag("matching-left-chip-$index")
                                    )
                                }
                                Text(
                                    text = item,
                                    style = TypographyTokens.FeatureTitle,
                                    fontWeight = FontWeight.SemiBold
                                )
                                ActionButton(
                                    text = if (selected) "已选中" else "选中后连接",
                                    onClick = { matchingState = matchingState.selectLeft(index) },
                                    modifier = Modifier.testTag("matching-left-select-$index"),
                                    role = if (selected) ActionRole.Secondary else ActionRole.OutlinedSecondary
                                )
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("matching-right-column"),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                ) {
                    rightItems.forEachIndexed { index, item ->
                        val linkedLeftIndex = matchingState.assignments.entries
                            .firstOrNull { it.value == index }
                            ?.key
                        StoryPanelCard(
                            modifier = Modifier.testTag("matching-right-$index"),
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
                                        modifier = Modifier.testTag("matching-right-chip-$index")
                                    )
                                }
                                Text(
                                    text = item,
                                    style = TypographyTokens.FeatureTitle,
                                    fontWeight = FontWeight.SemiBold
                                )
                                ActionButton(
                                    text = if (matchingState.selectedLeftIndex != null) "连接到这里" else "等待选择",
                                    onClick = { matchingState = matchingState.assignTo(index) },
                                    enabled = matchingState.selectedLeftIndex != null && actionState.enabled,
                                    modifier = Modifier.testTag("matching-right-assign-$index"),
                                    role = ActionRole.Secondary
                                )
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
                        text = if (matchingState.isComplete(leftItems.size)) "配对完成" else "配对进度 ${matchingState.assignments.size}/${leftItems.size}",
                        style = TypographyTokens.SupportingLabel,
                        color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
                    )
                    ActionButton(
                        text = actionState.resolveLabel("提交配对"),
                        onClick = {
                            onAnswer(matchingState.encodedAnswer(leftItems, rightItems))
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
