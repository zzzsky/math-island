package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun RulerQuestionPane(
    question: Question,
    feedback: AnswerFeedbackUiState? = null,
    inputEnabled: Boolean = true,
    onAnswer: (String) -> Unit
) {
    RendererOptionsColumn(
        question = question,
        rendererTag = "renderer-ruler",
        accent = TabletSky,
        feedback = feedback,
        inputEnabled = inputEnabled,
        header = "尺子工坊",
        helper = "拖动尺子观察刻度，再选择最合适的答案。",
        affordance = {
            StoryPanelCard(
                modifier = Modifier.fillMaxWidth(),
                level = SurfaceLevel.Secondary,
                containerColor = RendererTokens.RulerSurface,
                shape = RadiusTokens.CardMd
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(SpacingTokens.Lg),
                    verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                ) {
                    Text(
                        text = "虚拟尺子",
                        style = TypographyTokens.FeatureTitle,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RadiusTokens.Pill)
                            .background(RendererTokens.HandleTrack)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(start = 72.dp)
                                .size(width = 36.dp, height = 12.dp)
                                .clip(RadiusTokens.Pill)
                                .background(TabletSky)
                                .testTag("tablet-ruler-handle")
                        )
                    }
                }
            }
        },
        buttonLabel = "对准刻度",
        onAnswer = onAnswer
    )
}
