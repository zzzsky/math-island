package com.mathisland.app.feature.level.renderers

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.mathisland.app.domain.model.Question

@Composable
fun ChoiceQuestionPane(
    question: Question,
    onAnswer: (String) -> Unit
) {
    RendererOptionsColumn(
        question = question,
        rendererTag = "renderer-choice",
        accent = MaterialTheme.colorScheme.primary,
        buttonLabel = "选择这个答案",
        onAnswer = onAnswer
    )
}
