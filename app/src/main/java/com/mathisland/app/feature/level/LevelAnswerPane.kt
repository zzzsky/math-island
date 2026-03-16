package com.mathisland.app.feature.level

import androidx.compose.runtime.Composable
import com.mathisland.app.domain.model.Question

@Composable
fun LevelAnswerPane(
    question: Question,
    onAnswer: (String) -> Unit
) {
    com.mathisland.app.feature.lesson.LessonAnswerPane(
        question = question,
        onAnswer = onAnswer
    )
}
