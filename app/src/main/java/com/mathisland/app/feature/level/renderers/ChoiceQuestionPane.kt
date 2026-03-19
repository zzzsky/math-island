package com.mathisland.app.feature.level.renderers

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.mathisland.app.domain.model.Question

@Composable
fun ChoiceQuestionPane(
    question: Question,
    feedback: AnswerFeedbackUiState? = null,
    inputEnabled: Boolean = true,
    onAnswer: (String) -> Unit
) {
    RendererOptionsColumn(
        question = question,
        rendererTag = "renderer-choice",
        accent = MaterialTheme.colorScheme.primary,
        feedback = feedback,
        inputEnabled = inputEnabled,
        buttonLabel = "选择这个答案",
        onAnswer = onAnswer
    )
}
