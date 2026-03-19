package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.domain.model.Question
import com.mathisland.app.ui.theme.TabletSand

@Composable
fun ChantQuestionPane(
    question: Question,
    feedback: AnswerFeedbackUiState? = null,
    actionState: RendererActionState = rendererActionStateFor(feedback = feedback, inputEnabled = true),
    onAnswer: (String) -> Unit
) {
    RendererOptionsColumn(
        question = question,
        rendererTag = "renderer-chant",
        accent = TabletCoral,
        feedback = feedback,
        actionState = actionState,
        header = "口诀回声",
        helper = "先大声念口诀，再点中正确答案。",
        affordance = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color(0x55A65B4B)),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "节拍条",
                        modifier = Modifier.testTag("chant-beat-strip"),
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        repeat(4) { index ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height((20 + index * 6).dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (index % 2 == 0) TabletCoral else TabletSand)
                            )
                        }
                    }
                }
            }
        },
        buttonLabel = "念完选择",
        onAnswer = onAnswer
    )
}
