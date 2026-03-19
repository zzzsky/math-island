package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.domain.model.Question
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.TypographyTokens
import com.mathisland.app.ui.theme.TabletSand

@Composable
fun SortQuestionPane(
    question: Question,
    feedback: AnswerFeedbackUiState? = null,
    actionState: RendererActionState = rendererActionStateFor(feedback = feedback, inputEnabled = true),
    onAnswer: (String) -> Unit
) {
    RendererOptionsColumn(
        question = question,
        rendererTag = "renderer-sort",
        accent = TabletSand,
        feedback = feedback,
        actionState = actionState,
        header = "灯塔排序板",
        helper = "比较大小或顺序后，点亮正确信号灯。",
        affordance = {
            StoryPanelCard(
                modifier = Modifier.fillMaxWidth(),
                level = SurfaceLevel.Secondary,
                containerColor = RendererTokens.SortSurface,
                shape = RadiusTokens.CardMd
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacingTokens.Lg),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                ) {
                    Text(
                        text = "排序信号灯",
                        modifier = Modifier.testTag("sort-signal-lights"),
                        style = TypographyTokens.FeatureTitle,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(34.dp),
                        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Sm),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(Color(0xFFEF476F), Color(0xFFFFD166), Color(0xFF06D6A0)).forEach { light ->
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RadiusTokens.Pill)
                                    .background(light)
                            )
                        }
                    }
                }
            }
        },
        buttonLabel = "点亮信号",
        onAnswer = onAnswer
    )
}
