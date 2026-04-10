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
fun MultiStepQuestionPane(
    question: Question,
    feedback: AnswerFeedbackUiState? = null,
    actionState: RendererActionState = rendererActionStateFor(feedback = feedback, inputEnabled = true),
    onAnswer: (String) -> Unit
) {
    var multiStepState by remember(
        question.prompt,
        question.stepPrompts,
        question.stepChoices,
        question.stepBranchKeys,
        question.stepBranchRules,
        question.stepBranchPrompts,
        question.stepBranchChoices
    ) {
        mutableStateOf(MultiStepAnswerState())
    }
    val stepCount = stepCountFor(question)
    val completed = multiStepState.isComplete(stepCount)
    val currentStepIndex = multiStepState.currentStepIndex(stepCount)
    val currentPrompt = multiStepPromptFor(question, multiStepState)
    val currentChoices = multiStepChoicesFor(question, multiStepState)
    val canSubmit = actionState.enabled && completed

    RendererPanelStack(
        rendererTag = "renderer-multi-step",
        prompt = {
            RendererPromptCard(
                prompt = question.prompt,
                actionState = actionState
            )
        },
        context = {
            RendererGuidanceCard(
                title = "先按顺序完成步骤",
                body = "每次先完成当前步骤，再进入下一步。",
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
                    .testTag("multi-step-progress-card"),
                level = SurfaceLevel.Secondary,
                containerColor = RendererTokens.MultiStepSurface,
                borderColor = RendererTokens.MultiStepProgressBorder,
                shape = RadiusTokens.CardMd
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacingTokens.Lg),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                ) {
                    Text(
                        text = if (completed) "步骤完成" else "当前步骤 ${currentStepIndex + 1}/$stepCount",
                        style = TypographyTokens.SupportingLabel,
                        color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)
                    ) {
                        repeat(stepCount) { index ->
                            val isDone = index < multiStepState.answers.size
                            StatusChip(
                                text = if (isDone) "步骤 ${index + 1}" else "待完成",
                                variant = if (isDone) StatusVariant.Success else StatusVariant.Neutral,
                                modifier = Modifier.testTag("multi-step-progress-chip-$index")
                            )
                        }
                    }
                }
            }

            StoryPanelCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("multi-step-stage-card"),
                level = SurfaceLevel.Secondary,
                containerColor = if (completed) {
                    RendererTokens.MultiStepCompletedSurface
                } else {
                    RendererTokens.MultiStepProgressSurface
                },
                borderColor = if (completed) {
                    RendererTokens.MultiStepCompletedBorder
                } else {
                    RendererTokens.MultiStepProgressBorder
                },
                shape = RadiusTokens.CardMd
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacingTokens.Lg),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
                ) {
                    Text(
                        text = if (completed) {
                            "本题步骤都已完成"
                        } else {
                            currentPrompt
                        },
                        style = TypographyTokens.FeatureTitle,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.testTag("multi-step-prompt")
                    )

                    if (completed) {
                        multiStepState.answers.forEachIndexed { index, answer ->
                            Text(
                                text = "步骤 ${index + 1}: $answer",
                                style = TypographyTokens.BodyPrimary,
                                color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier.testTag("multi-step-answer-$index")
                            )
                        }
                    } else {
                        currentChoices.forEachIndexed { index, choice ->
                            StoryPanelCard(
                                modifier = Modifier.testTag("multi-step-choice-card-$index"),
                                level = SurfaceLevel.Secondary,
                                containerColor = RendererTokens.OptionSurface,
                                shape = RadiusTokens.CardMd
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(SpacingTokens.Lg),
                                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                                ) {
                                    Text(
                                        text = choice,
                                        style = TypographyTokens.FeatureTitle,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    ActionButton(
                                        text = if (currentStepIndex == stepCount - 1) "完成这一步" else "进入下一步",
                                        onClick = {
                                            multiStepState = multiStepState.advance(
                                                answer = choice,
                                                stepCount = stepCount,
                                                nextBranchKey = nextBranchKeyFor(question, multiStepState, choice)
                                            )
                                        },
                                        modifier = Modifier.testTag("multi-step-choice-$index"),
                                        enabled = actionState.enabled,
                                        role = ActionRole.Primary
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
                    .testTag("multi-step-summary-card"),
                level = SurfaceLevel.Secondary,
                containerColor = RendererTokens.MultiStepSurface,
                borderColor = RendererTokens.MultiStepProgressBorder,
                shape = RadiusTokens.CardMd
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacingTokens.Lg),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                ) {
                    Text(
                        text = if (completed) "步骤答案已记录" else "还差 ${stepCount - multiStepState.answers.size} 步",
                        style = TypographyTokens.SupportingLabel,
                        color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                    ) {
                        ActionButton(
                            text = "重新开始",
                            onClick = { multiStepState = multiStepState.reset() },
                            enabled = multiStepState.answers.isNotEmpty() && actionState.enabled,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("multi-step-reset"),
                            role = ActionRole.OutlinedSecondary
                        )
                        ActionButton(
                            text = actionState.resolveLabel("提交步骤"),
                            onClick = { onAnswer(multiStepState.encodedAnswer(stepCount)) },
                            enabled = canSubmit,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("multi-step-submit"),
                            role = actionState.resolveRole(ActionRole.Primary)
                        )
                    }
                }
            }
        }
    }
}
