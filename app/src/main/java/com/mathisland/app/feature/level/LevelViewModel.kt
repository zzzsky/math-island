package com.mathisland.app.feature.level

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.domain.model.Question
import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState

data class LevelUiState(
    val lesson: Lesson,
    val question: Question,
    val questionIndex: Int,
    val totalQuestions: Int,
    val totalStars: Int,
    val flowHint: String,
    val initialFeedback: AnswerFeedbackUiState? = null,
)

object LevelViewModel {
    fun uiState(
        controller: MathIslandGameController,
        progress: GameProgress
    ): LevelUiState? {
        val lesson = controller.currentLesson(progress) ?: return null
        val question = controller.currentQuestion(progress) ?: return null
        return LevelUiState(
            lesson = lesson,
            question = question,
            questionIndex = progress.activeQuestionIndex,
            totalQuestions = lesson.questions.size,
            totalStars = progress.totalStars,
            flowHint = when {
                lesson.timeLimitSeconds != null -> "冲刺结束后会显示评级，并决定是否优先进入错题回放。"
                lesson.isReview -> "完成这轮复习后，会回到主线继续推荐下一节课程。"
                else -> "完成本节后会先结算星星，再回地图继续探索。"
            },
            initialFeedback = if (lesson.timeLimitSeconds != null) {
                AnswerFeedbackUiState(
                    kind = AnswerFeedbackKind.TimedWarning,
                    title = "限时进行中",
                    body = "注意倒计时，先交当前最有把握的答案。"
                )
            } else {
                null
            }
        )
    }
}
