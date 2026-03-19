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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.domain.model.Question
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens

@Composable
fun GroupQuestionPane(
    question: Question,
    feedback: AnswerFeedbackUiState? = null,
    inputEnabled: Boolean = true,
    onAnswer: (String) -> Unit
) {
    RendererOptionsColumn(
        question = question,
        rendererTag = "renderer-group",
        accent = TabletMint,
        feedback = feedback,
        inputEnabled = inputEnabled,
        header = "分组操作台",
        helper = "先想想该怎么分组或分类，再确认答案。",
        affordance = {
            StoryPanelCard(
                modifier = Modifier.fillMaxWidth(),
                level = SurfaceLevel.Secondary,
                containerColor = RendererTokens.GroupSurface,
                shape = RadiusTokens.CardMd
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacingTokens.Lg),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                ) {
                    Text(
                        text = "分类篮子",
                        modifier = Modifier.testTag("group-basket-zone"),
                        style = TypographyTokens.FeatureTitle,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(88.dp),
                        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                    ) {
                        listOf("篮子 A", "篮子 B").forEach { label ->
                            StoryPanelCard(
                                modifier = Modifier.weight(1f),
                                level = SurfaceLevel.Primary,
                                containerColor = RendererTokens.GroupBasketSurface,
                                shape = RadiusTokens.CardMd
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(88.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        style = TypographyTokens.Caption,
                                        color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        buttonLabel = "确认分组",
        onAnswer = onAnswer
    )
}
