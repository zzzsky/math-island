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
    val totalStars: Int,
    val flowHint: String
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
            totalStars = progress.totalStars,
            flowHint = when {
                lesson.timeLimitSeconds != null -> "冲刺结束后会显示评级，并决定是否优先进入错题回放。"
                lesson.isReview -> "完成这轮复习后，会回到主线继续推荐下一节课程。"
                else -> "完成本节后会先结算星星，再回地图继续探索。"
            }
        )
    }
}
