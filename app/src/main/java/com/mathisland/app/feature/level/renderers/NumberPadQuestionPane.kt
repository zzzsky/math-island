package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.domain.model.Question
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TabletDeepWater
import com.mathisland.app.ui.theme.TabletFoam
import com.mathisland.app.ui.theme.TabletSand
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun NumberPadQuestionPane(
    question: Question,
    feedback: AnswerFeedbackUiState? = null,
    actionState: RendererActionState = rendererActionStateFor(feedback = feedback, inputEnabled = true),
    onAnswer: (String) -> Unit
) {
    var enteredAnswer by remember(question.prompt) { mutableStateOf("") }
    val displayState = numberPadDisplayStateFor(
        enteredAnswer = enteredAnswer,
        feedback = feedback
    )
    val keypadRows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("清除", "0", "提交")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(RendererTokens.SectionGap)
    ) {
        val submitActionRole = actionState.resolveRole(ActionRole.Recommended)
        val submitLabel = actionState.resolveLabel("提交")

        RendererPanelStack(
            rendererTag = "renderer-number-pad",
            context = {
                RendererGuidanceCard(
                    title = "数字键盘",
                    body = "可输入答案：${question.choices.joinToString(" / ")}"
                )
            },
            feedback = feedback,
            affordance = {
                StoryPanelCard(
                    level = SurfaceLevel.Secondary,
                    containerColor = when (displayState.tone) {
                        NumberPadDisplayTone.Idle,
                        NumberPadDisplayTone.Ready,
                        -> RendererTokens.NumberPadSurface
                        NumberPadDisplayTone.Retry -> RendererTokens.OptionRetrySurface
                        NumberPadDisplayTone.Confirmed -> RendererTokens.OptionCorrectSurface
                    },
                    shape = RadiusTokens.CardMd
                ) {
                    StoryPanelCard(
                        level = SurfaceLevel.Primary,
                        containerColor = when (displayState.tone) {
                            NumberPadDisplayTone.Idle,
                            NumberPadDisplayTone.Ready,
                            -> RendererTokens.NumberPadDisplaySurface
                            NumberPadDisplayTone.Retry -> RendererTokens.OptionRetryBorder
                            NumberPadDisplayTone.Confirmed -> RendererTokens.OptionCorrectBorder
                        },
                        shape = RadiusTokens.CardMd
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = SpacingTokens.Lg, vertical = SpacingTokens.Xl),
                            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Xs)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("number-pad-display"),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = displayState.displayText,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = displayState.supportingText,
                                style = TypographyTokens.BodySecondary,
                                color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier.testTag("number-pad-status")
                            )
                        }
                    }
                }
            }
        ) {
            StoryPanelCard(
                level = SurfaceLevel.Secondary,
                containerColor = RendererTokens.OptionSurface,
                shape = RadiusTokens.CardMd
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacingTokens.Lg),
                    verticalArrangement = Arrangement.spacedBy(RendererTokens.ActionRowGap)
                ) {
                    RendererSectionHeader(
                        title = actionState.sectionTitle(),
                        body = actionState.sectionBody()
                    )
                    keypadRows.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                        ) {
                            row.forEach { key ->
                                val tag = when (key) {
                                    "清除" -> "number-pad-clear"
                                    "提交" -> "number-pad-submit"
                                    else -> "number-pad-key-$key"
                                }
                                ActionButton(
                                    text = if (key == "提交") submitLabel else key,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(68.dp)
                                        .testTag(tag),
                                    onClick = {
                                        when (key) {
                                            "清除" -> enteredAnswer = ""
                                            "提交" -> onAnswer(enteredAnswer)
                                            else -> enteredAnswer += key
                                        }
                                    },
                                    enabled = if (key == "提交") {
                                        actionState.enabled && enteredAnswer.isNotEmpty()
                                    } else if (key == "清除") {
                                        actionState.enabled && enteredAnswer.isNotEmpty()
                                    } else {
                                        actionState.enabled
                                    },
                                    role = if (key == "提交") submitActionRole else ActionRole.Secondary,
                                    containerColor = if (key == "提交" && submitActionRole == ActionRole.Recommended) {
                                        TabletSand
                                    } else if (key == "提交" && submitActionRole == ActionRole.Secondary) {
                                        null
                                    } else {
                                        RendererTokens.OptionSurface
                                    },
                                    contentColor = if (key == "提交" && submitActionRole == ActionRole.Recommended) {
                                        TabletDeepWater
                                    } else if (key == "提交" && submitActionRole == ActionRole.Secondary) {
                                        null
                                    } else {
                                        TabletFoam
                                    },
                                    textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    shape = RadiusTokens.CardMd
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
