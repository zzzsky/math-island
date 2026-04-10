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
fun FillBlankQuestionPane(
    question: Question,
    feedback: AnswerFeedbackUiState? = null,
    actionState: RendererActionState = rendererActionStateFor(feedback = feedback, inputEnabled = true),
    onAnswer: (String) -> Unit
) {
    var fillBlankState by remember(question.prompt, question.blankParts, question.blankOptions, question.blankSlotKinds) {
        mutableStateOf(FillBlankAnswerState())
    }
    val parts = question.blankParts
    val options = question.blankOptions
    val slotCount = (parts.size - 1).coerceAtLeast(0)
    val slotKinds = question.blankSlotKinds.takeIf { it.size == slotCount }
        ?: List(slotCount) { "number" }
    val optionKinds = options.map(::optionKindFor)
    val canSubmit = actionState.enabled && slotCount > 0 && fillBlankState.isComplete(slotCount)

    RendererPanelStack(
        rendererTag = "renderer-fill-blank",
        prompt = {
            RendererPromptCard(
                prompt = question.prompt,
                actionState = actionState
            )
        },
        context = {
            RendererGuidanceCard(
                title = "先看空格类型",
                body = "先选空格或选项，再到对应分区完成填空。",
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

            StoryPanelCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fill-blank-sentence"),
                level = SurfaceLevel.Secondary,
                containerColor = RendererTokens.FillBlankSurface,
                borderColor = RendererTokens.FillBlankSlotBorder,
                shape = RadiusTokens.CardMd
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacingTokens.Lg),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
                ) {
                    repeat(slotCount) { slotIndex ->
                        val assignedOptionIndex = fillBlankState.assignments[slotIndex]
                        val slotKind = slotKinds[slotIndex]
                        val slotSelected = fillBlankState.selectedSlotIndex == slotIndex
                        val selectedOptionKind = fillBlankState.selectedOptionIndex?.let { optionKinds[it] }
                        val assignedOption = assignedOptionIndex?.let(options::get)
                        val mismatch = assignedOption != null && optionKinds[assignedOptionIndex] != slotKind
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                        ) {
                            Text(
                                text = parts[slotIndex],
                                style = TypographyTokens.BodyPrimary,
                                color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            StoryPanelCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("fill-blank-slot-$slotIndex"),
                                level = SurfaceLevel.Secondary,
                                containerColor = if (assignedOptionIndex != null) {
                                    if (mismatch) RendererTokens.OptionSurface else RendererTokens.FillBlankFilledSurface
                                } else if (slotSelected || selectedOptionKind == slotKind) {
                                    RendererTokens.FillBlankSurface
                                } else {
                                    RendererTokens.FillBlankSlotSurface
                                },
                                borderColor = if (assignedOptionIndex != null) {
                                    if (mismatch) MaterialTheme.colorScheme.error else RendererTokens.FillBlankFilledBorder
                                } else if (slotSelected || selectedOptionKind == slotKind) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    RendererTokens.FillBlankSlotBorder
                                },
                                shape = RadiusTokens.CardMd
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(SpacingTokens.Md),
                                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)
                                ) {
                                    if (assignedOptionIndex != null) {
                                        StatusChip(
                                            text = if (mismatch) "类型不符" else "已填入",
                                            variant = if (mismatch) StatusVariant.Caution else StatusVariant.Success,
                                            modifier = Modifier.testTag("fill-blank-slot-chip-$slotIndex")
                                        )
                                    }
                                    StatusChip(
                                        text = slotKindLabelFor(slotKind),
                                        variant = StatusVariant.Neutral,
                                        modifier = Modifier.testTag("fill-blank-slot-kind-$slotIndex")
                                    )
                                    Text(
                                        text = assignedOption ?: "点这里填空",
                                        style = TypographyTokens.FeatureTitle,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    ActionButton(
                                        text = when {
                                            fillBlankState.selectedOptionIndex != null -> "填到这里"
                                            slotSelected -> "已选空格"
                                            else -> "先选这个空格"
                                        },
                                        onClick = {
                                            fillBlankState = if (fillBlankState.selectedOptionIndex != null) {
                                                fillBlankState.assignTo(slotIndex)
                                            } else {
                                                fillBlankState.selectSlot(slotIndex)
                                            }
                                        },
                                        enabled = actionState.enabled,
                                        modifier = Modifier.testTag("fill-blank-slot-action-$slotIndex"),
                                        role = if (slotSelected) ActionRole.Secondary else ActionRole.OutlinedSecondary
                                    )
                                }
                            }
                            Text(
                                text = parts[slotIndex + 1],
                                style = TypographyTokens.BodyPrimary,
                                color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier.weight(1f, fill = false)
                            )
                        }
                    }
                }
            }

            partitionedFillBlankOptions(options, optionKinds).forEach { (kind, optionIndexes) ->
                StoryPanelCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("fill-blank-pool-$kind"),
                    level = SurfaceLevel.Secondary,
                    containerColor = if (fillBlankState.selectedSlotIndex?.let(slotKinds::get) == kind ||
                        fillBlankState.selectedOptionIndex?.let { optionKinds[it] } == kind
                    ) {
                        RendererTokens.FillBlankSurface
                    } else {
                        RendererTokens.OptionSurface
                    },
                    borderColor = if (fillBlankState.selectedSlotIndex?.let(slotKinds::get) == kind ||
                        fillBlankState.selectedOptionIndex?.let { optionKinds[it] } == kind
                    ) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        null
                    },
                    shape = RadiusTokens.CardMd
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(SpacingTokens.Lg),
                        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                    ) {
                        StatusChip(
                            text = slotKindLabelFor(kind),
                            variant = if (fillBlankState.selectedSlotIndex?.let(slotKinds::get) == kind ||
                                fillBlankState.selectedOptionIndex?.let { optionKinds[it] } == kind
                            ) {
                                StatusVariant.Highlight
                            } else {
                                StatusVariant.Neutral
                            },
                            modifier = Modifier.testTag("fill-blank-pool-chip-$kind")
                        )
                        optionIndexes.forEach { index ->
                            val option = options[index]
                            val selected = fillBlankState.selectedOptionIndex == index
                            val filled = fillBlankState.assignments.values.contains(index)
                            StoryPanelCard(
                                modifier = Modifier.testTag("fill-blank-option-$index"),
                                level = SurfaceLevel.Secondary,
                                containerColor = when {
                                    filled -> RendererTokens.FillBlankFilledSurface
                                    selected -> RendererTokens.FillBlankSurface
                                    else -> RendererTokens.OptionSurface
                                },
                                borderColor = when {
                                    filled -> RendererTokens.FillBlankFilledBorder
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
                                    if (filled) {
                                        StatusChip(
                                            text = "已使用",
                                            variant = StatusVariant.Success,
                                            modifier = Modifier.testTag("fill-blank-option-chip-$index")
                                        )
                                    }
                                    StatusChip(
                                        text = slotKindLabelFor(kind),
                                        variant = StatusVariant.Neutral,
                                        modifier = Modifier.testTag("fill-blank-option-kind-$index")
                                    )
                                    Text(
                                        text = option,
                                        style = TypographyTokens.FeatureTitle,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    ActionButton(
                                        text = when {
                                            selected -> "已选中"
                                            fillBlankState.selectedSlotIndex != null -> "填入已选空格"
                                            else -> "选中后填空"
                                        },
                                        onClick = { fillBlankState = fillBlankState.assignSelected(index) },
                                        modifier = Modifier.testTag("fill-blank-option-select-$index"),
                                        role = if (selected) ActionRole.Secondary else ActionRole.OutlinedSecondary,
                                        enabled = actionState.enabled
                                    )
                                }
                            }
                        }
                    }
                }
            }

            StoryPanelCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("fill-blank-summary-card"),
                level = SurfaceLevel.Secondary,
                containerColor = RendererTokens.FillBlankSurface,
                borderColor = RendererTokens.FillBlankSlotBorder,
                shape = RadiusTokens.CardMd
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacingTokens.Lg),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                ) {
                    Text(
                        text = if (fillBlankState.isComplete(slotCount)) {
                            "填空完成"
                        } else {
                            "填空进度 ${fillBlankState.assignments.size}/$slotCount"
                        },
                        style = TypographyTokens.SupportingLabel,
                        color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
                    )
                    ActionButton(
                        text = actionState.resolveLabel("提交填空"),
                        onClick = { onAnswer(fillBlankState.encodedAnswer(options, slotCount)) },
                        enabled = canSubmit,
                        modifier = Modifier.testTag("fill-blank-submit"),
                        role = actionState.resolveRole(ActionRole.Primary)
                    )
                }
            }
        }
    }
}

private fun optionKindFor(option: String): String =
    if (option.all { it.isDigit() }) "number" else "unit"

private fun partitionedFillBlankOptions(
    options: List<String>,
    optionKinds: List<String>
): List<Pair<String, List<Int>>> =
    listOf("number", "unit").mapNotNull { kind ->
        val indexes = optionKinds.indices.filter { optionKinds[it] == kind }
        indexes.takeIf { it.isNotEmpty() }?.let { kind to it }
    }

private fun slotKindLabelFor(kind: String): String = when (kind) {
    "unit" -> "填单位"
    else -> "填数字"
}
