package com.mathisland.app.feature.level.renderers

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
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
import kotlinx.coroutines.delay

private const val MultiStepConfirmationDelayMillis = 320
private const val MultiStepRecapHighlightMillis = 720

private data class PendingMultiStepTransition(
    val stepIndex: Int,
    val choiceIndex: Int,
    val answer: String,
    val nextBranchKey: String?
)

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
        question.stepBranchChoices,
        question.stepPresentations,
        question.stepBranchPresentations
    ) {
        mutableStateOf(MultiStepAnswerState())
    }
    var pendingTransition by remember(
        question.prompt,
        question.stepPrompts,
        question.stepChoices,
        question.stepBranchKeys,
        question.stepBranchRules,
        question.stepBranchPrompts,
        question.stepBranchChoices,
        question.stepPresentations,
        question.stepBranchPresentations
    ) {
        mutableStateOf<PendingMultiStepTransition?>(null)
    }
    var expandedRecapStepIndexes by remember(
        question.prompt,
        question.stepPrompts,
        question.stepChoices,
        question.stepBranchKeys,
        question.stepBranchRules,
        question.stepBranchPrompts,
        question.stepBranchChoices,
        question.stepPresentations,
        question.stepBranchPresentations
    ) {
        mutableStateOf(setOf<Int>())
    }
    var recentlyCompletedStepIndex by remember(
        question.prompt,
        question.stepPrompts,
        question.stepChoices,
        question.stepBranchKeys,
        question.stepBranchRules,
        question.stepBranchPrompts,
        question.stepBranchChoices,
        question.stepPresentations,
        question.stepBranchPresentations
    ) {
        mutableStateOf<Int?>(null)
    }
    val stepCount = stepCountFor(question)
    val completed = multiStepState.isComplete(stepCount)
    val currentStepIndex = multiStepState.currentStepIndex(stepCount)
    val currentPrompt = multiStepPromptFor(question, multiStepState)
    val currentChoices = multiStepChoicesFor(question, multiStepState)
    val currentPresentation = multiStepPresentationFor(question, multiStepState)
    val transitionInFlight = pendingTransition != null
    val stageScale by animateFloatAsState(
        targetValue = if (transitionInFlight) 0.985f else 1f,
        animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        label = "multiStepStageScale"
    )
    val stageAlpha by animateFloatAsState(
        targetValue = if (transitionInFlight) 0.94f else 1f,
        animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        label = "multiStepStageAlpha"
    )
    val canSubmit = actionState.enabled && completed && !transitionInFlight

    LaunchedEffect(pendingTransition, stepCount) {
        val transition = pendingTransition ?: return@LaunchedEffect
        delay(MultiStepConfirmationDelayMillis.toLong())
        multiStepState = multiStepState.advance(
            answer = transition.answer,
            stepCount = stepCount,
            nextBranchKey = transition.nextBranchKey
        )
        recentlyCompletedStepIndex = transition.stepIndex
        pendingTransition = null
    }

    LaunchedEffect(recentlyCompletedStepIndex) {
        if (recentlyCompletedStepIndex == null) return@LaunchedEffect
        delay(MultiStepRecapHighlightMillis.toLong())
        recentlyCompletedStepIndex = null
    }

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
                            val isDone = index < multiStepState.answers.size ||
                                pendingTransition?.stepIndex == index
                            StatusChip(
                                text = if (isDone) "步骤 ${index + 1}" else "待完成",
                                variant = if (isDone) StatusVariant.Success else StatusVariant.Neutral,
                                modifier = Modifier.testTag("multi-step-progress-chip-$index")
                            )
                        }
                    }
                }
            }

            if (multiStepState.answers.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("multi-step-recap-column"),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                ) {
                    multiStepState.answers.forEachIndexed { index, answer ->
                        val recapPresentation = multiStepPresentationFor(question, multiStepState, index)
                        val recapPrompt = multiStepPromptFor(question, multiStepState, index)
                        val expanded = expandedRecapStepIndexes.contains(index)
                        val isRecent = recentlyCompletedStepIndex == index
                        StoryPanelCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("multi-step-recap-card-$index"),
                            level = SurfaceLevel.Secondary,
                            containerColor = if (isRecent) {
                                RendererTokens.MultiStepCompletedSurface
                            } else {
                                RendererTokens.MultiStepSurface
                            },
                            borderColor = if (isRecent) {
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
                                verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                                ) {
                                    StatusChip(
                                        text = if (isRecent) "刚完成" else "已完成",
                                        variant = StatusVariant.Success
                                    )
                                    Text(
                                        text = multiStepAnswerLabelFor(question, multiStepState, index),
                                        style = TypographyTokens.SupportingLabel,
                                        color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
                                    )
                                    ActionButton(
                                        text = if (expanded) "收起" else "展开",
                                        onClick = {
                                            expandedRecapStepIndexes = if (expanded) {
                                                expandedRecapStepIndexes - index
                                            } else {
                                                expandedRecapStepIndexes + index
                                            }
                                        },
                                        modifier = Modifier.testTag("multi-step-recap-toggle-$index"),
                                        role = ActionRole.OutlinedSecondary
                                    )
                                }
                                Text(
                                    text = recapPresentation.stageTitle,
                                    style = TypographyTokens.FeatureTitle,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = answer,
                                    style = TypographyTokens.BodyPrimary,
                                    color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface)
                                )
                                if (expanded) {
                                    Text(
                                        text = recapPrompt,
                                        style = TypographyTokens.BodyPrimary,
                                        color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface),
                                        modifier = Modifier.testTag("multi-step-recap-prompt-$index")
                                    )
                                    if (recapPresentation.supportText.isNotBlank()) {
                                        Text(
                                            text = recapPresentation.supportText,
                                            style = TypographyTokens.Caption,
                                            color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface),
                                            modifier = Modifier.testTag("multi-step-recap-support-$index")
                                        )
                                    }
                                    Text(
                                        text = "${multiStepAnswerLabelFor(question, multiStepState, index)}: $answer",
                                        style = TypographyTokens.BodyPrimary,
                                        color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface),
                                        modifier = Modifier.testTag("multi-step-recap-answer-$index")
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
                    .graphicsLayer {
                        scaleX = stageScale
                        scaleY = stageScale
                        alpha = stageAlpha
                    }
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
                        text = currentPresentation.stageTitle,
                        style = TypographyTokens.SupportingLabel,
                        color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.testTag("multi-step-stage-title")
                    )

                    if (transitionInFlight) {
                        StatusChip(
                            text = "已确认",
                            variant = StatusVariant.Success,
                            modifier = Modifier.testTag("multi-step-stage-confirming")
                        )
                    }

                    if (!completed && currentPresentation.supportText.isNotBlank()) {
                        Text(
                            text = currentPresentation.supportText,
                            style = TypographyTokens.BodyPrimary,
                            color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier.testTag("multi-step-stage-support")
                        )
                    }

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
                                text = "${multiStepAnswerLabelFor(question, multiStepState, index)}: $answer",
                                style = TypographyTokens.BodyPrimary,
                                color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier.testTag("multi-step-answer-$index")
                            )
                        }
                    } else {
                        currentChoices.forEachIndexed { index, choice ->
                            val isPendingSelected = pendingTransition?.choiceIndex == index
                            StoryPanelCard(
                                modifier = Modifier.testTag("multi-step-choice-card-$index"),
                                level = SurfaceLevel.Secondary,
                                containerColor = if (isPendingSelected) {
                                    RendererTokens.OptionCorrectSurface
                                } else {
                                    RendererTokens.OptionSurface
                                },
                                borderColor = if (isPendingSelected) {
                                    RendererTokens.OptionCorrectBorder
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
                                    Text(
                                        text = choice,
                                        style = TypographyTokens.FeatureTitle,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    if (isPendingSelected) {
                                        StatusChip(
                                            text = "已确认",
                                            variant = StatusVariant.Success,
                                            modifier = Modifier.testTag("multi-step-confirmed-chip-$index")
                                        )
                                    }
                                    ActionButton(
                                        text = if (isPendingSelected) {
                                            "已确认"
                                        } else if (currentStepIndex == stepCount - 1) {
                                            "完成这一步"
                                        } else {
                                            "进入下一步"
                                        },
                                        onClick = {
                                            if (!transitionInFlight) {
                                                pendingTransition = PendingMultiStepTransition(
                                                    stepIndex = currentStepIndex,
                                                    choiceIndex = index,
                                                    answer = choice,
                                                    nextBranchKey = nextBranchKeyFor(question, multiStepState, choice)
                                                )
                                            }
                                        },
                                        modifier = Modifier.testTag("multi-step-choice-$index"),
                                        enabled = actionState.enabled && !transitionInFlight,
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
                            onClick = {
                                multiStepState = multiStepState.reset()
                                pendingTransition = null
                                expandedRecapStepIndexes = emptySet()
                                recentlyCompletedStepIndex = null
                            },
                            enabled = multiStepState.answers.isNotEmpty() && actionState.enabled && !transitionInFlight,
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
