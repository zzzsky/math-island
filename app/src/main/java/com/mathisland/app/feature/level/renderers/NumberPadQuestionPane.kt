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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.domain.model.Question
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.components.TabletChipLabel
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
    onAnswer: (String) -> Unit
) {
    var enteredAnswer by remember(question.prompt) { mutableStateOf("") }
    val keypadRows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("清除", "0", "提交")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("renderer-number-pad"),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
    ) {
        StoryPanelCard(
            level = SurfaceLevel.Secondary,
            containerColor = RendererTokens.NumberPadSurface,
            shape = RadiusTokens.CardMd
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpacingTokens.Lg),
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
            ) {
                TabletChipLabel(text = "数字键盘")
                Text(
                    text = "可输入答案：${question.choices.joinToString(" / ")}",
                    style = TypographyTokens.Caption,
                    color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
                )
                StoryPanelCard(
                    level = SurfaceLevel.Primary,
                    containerColor = RendererTokens.NumberPadDisplaySurface,
                    shape = RadiusTokens.CardMd
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = SpacingTokens.Lg, vertical = SpacingTokens.Xl)
                            .testTag("number-pad-display"),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = enteredAnswer.ifEmpty { "请输入答案" },
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

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
                        text = key,
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
                        enabled = key != "提交" || enteredAnswer.isNotEmpty(),
                        role = if (key == "提交") ActionRole.Recommended else ActionRole.Secondary,
                        containerColor = if (key == "提交") TabletSand else RendererTokens.OptionSurface,
                        contentColor = if (key == "提交") TabletDeepWater else TabletFoam,
                        textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        shape = RadiusTokens.CardMd
                    )
                }
            }
        }
    }
}
