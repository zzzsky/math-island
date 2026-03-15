package com.mathisland.app.feature.lesson

import com.mathisland.app.GameProgress
import com.mathisland.app.Lesson
import com.mathisland.app.MathIslandGameController
import com.mathisland.app.Question

data class LessonUiState(
    val lesson: Lesson,
    val question: Question,
    val questionIndex: Int,
    val totalQuestions: Int,
    val totalStars: Int
)

object LessonViewModel {
    fun uiState(
        controller: MathIslandGameController,
        progress: GameProgress
    ): LessonUiState? {
        val lesson = controller.currentLesson(progress) ?: return null
        val question = controller.currentQuestion(progress) ?: return null
        return LessonUiState(
            lesson = lesson,
            question = question,
            questionIndex = progress.activeQuestionIndex,
            totalQuestions = lesson.questions.size,
            totalStars = progress.totalStars
        )
    }
}
