package com.mathisland.app.domain.usecase

import com.mathisland.app.GameProgress
import com.mathisland.app.Island
import com.mathisland.app.Lesson
import com.mathisland.app.ReviewTask
import com.mathisland.app.RewardSummary

data class LessonResultOutcome(
    val starsEarned: Int,
    val pendingReview: ReviewTask?,
    val reward: RewardSummary
)

class SubmitLessonResultUseCase(
    private val islands: List<Island>
) {
    fun onTimeout(state: GameProgress, lesson: Lesson): LessonResultOutcome {
        val totalQuestions = lesson.questions.size
        val fallbackReview = lesson.questions.firstOrNull()?.family ?: "challenge"
        return LessonResultOutcome(
            starsEarned = 0,
            pendingReview = state.scheduledReviewFamily?.let(::ReviewTask) ?: ReviewTask(fallbackReview),
            reward = RewardSummary(
                lessonTitle = lesson.title,
                starsEarned = 0,
                correctAnswers = state.correctAnswersInLesson,
                totalQuestions = totalQuestions,
                newIslandId = null,
                newIslandTitle = null,
                newStickerName = null,
                timedOut = true,
                gradeLabel = challengeGradeLabel(
                    lessonId = lesson.id,
                    correctAnswers = state.correctAnswersInLesson,
                    totalQuestions = totalQuestions,
                    timedOut = true
                ),
                gradeDescription = challengeGradeDescription(
                    lessonId = lesson.id,
                    correctAnswers = state.correctAnswersInLesson,
                    totalQuestions = totalQuestions,
                    timedOut = true
                )
            )
        )
    }

    fun onLessonCompleted(
        state: GameProgress,
        lesson: Lesson,
        correctAnswers: Int,
        newIslandId: String? = null,
        newIslandTitle: String? = null,
        newStickerName: String? = null
    ): LessonResultOutcome {
        val totalQuestions = lesson.questions.size
        return if (lesson.isReview) {
            val clearedReview = correctAnswers == totalQuestions
            val pendingReview = if (clearedReview) null else state.pendingReview
            val starsEarned = if (clearedReview) 1 else 0
            LessonResultOutcome(
                starsEarned = starsEarned,
                pendingReview = pendingReview,
                reward = RewardSummary(
                    lessonTitle = lesson.title,
                    starsEarned = starsEarned,
                    correctAnswers = correctAnswers,
                    totalQuestions = totalQuestions,
                    newIslandId = null,
                    newIslandTitle = null,
                    newStickerName = null,
                    secondaryActionLabel = replaySecondaryActionLabel(
                        lessonId = lesson.id,
                        pendingReview = pendingReview
                    ),
                    secondaryActionLessonId = replaySecondaryActionLessonId(
                        lessonId = lesson.id,
                        pendingReview = pendingReview
                    )
                )
            )
        } else {
            val starsEarned = when {
                correctAnswers == totalQuestions -> 3
                correctAnswers >= totalQuestions - 1 -> 2
                else -> 1
            }
            val pendingReview = when {
                state.scheduledReviewFamily != null -> ReviewTask(state.scheduledReviewFamily)
                lesson.id == CHALLENGE_REPLAY_LESSON_ID &&
                    state.pendingReview != null &&
                    correctAnswers == totalQuestions -> null
                else -> state.pendingReview
            }
            LessonResultOutcome(
                starsEarned = starsEarned,
                pendingReview = pendingReview,
                reward = RewardSummary(
                    lessonTitle = lesson.title,
                    starsEarned = starsEarned,
                    correctAnswers = correctAnswers,
                    totalQuestions = totalQuestions,
                    newIslandId = newIslandId,
                    newIslandTitle = newIslandTitle,
                    newStickerName = newStickerName,
                    gradeLabel = challengeGradeLabel(
                        lessonId = lesson.id,
                        correctAnswers = correctAnswers,
                        totalQuestions = totalQuestions,
                        timedOut = false
                    ),
                    gradeDescription = challengeGradeDescription(
                        lessonId = lesson.id,
                        correctAnswers = correctAnswers,
                        totalQuestions = totalQuestions,
                        timedOut = false
                    ),
                    secondaryActionLabel = replaySecondaryActionLabel(
                        lessonId = lesson.id,
                        pendingReview = pendingReview
                    ),
                    secondaryActionLessonId = replaySecondaryActionLessonId(
                        lessonId = lesson.id,
                        pendingReview = pendingReview
                    )
                )
            )
        }
    }

    private fun challengeGradeLabel(
        lessonId: String,
        correctAnswers: Int,
        totalQuestions: Int,
        timedOut: Boolean
    ): String? {
        if (lessonId != CHALLENGE_SPRINT_LESSON_ID) {
            return null
        }
        return when {
            timedOut -> "整备评级"
            correctAnswers == totalQuestions -> "金帆评级"
            correctAnswers == totalQuestions - 1 -> "银帆评级"
            else -> "铜帆评级"
        }
    }

    private fun challengeGradeDescription(
        lessonId: String,
        correctAnswers: Int,
        totalQuestions: Int,
        timedOut: Boolean
    ): String? {
        if (lessonId != CHALLENGE_SPRINT_LESSON_ID) {
            return null
        }
        return when {
            timedOut && correctAnswers > 0 -> "本轮已命中 $correctAnswers/$totalQuestions 题，先回放再发起下一次冲刺。"
            timedOut -> "这轮还没形成有效冲刺结果，先补给再出发。"
            correctAnswers == totalQuestions -> "全速通关，已经拿到本轮最高评级。"
            correctAnswers == totalQuestions - 1 -> "只差一步就能满帆通关，再试一次就能冲金。"
            else -> "已经完成本轮冲刺，先复盘再继续提速。"
        }
    }

    private fun replaySecondaryActionLabel(
        lessonId: String,
        pendingReview: ReviewTask?
    ): String? = when {
        lessonId == CHALLENGE_REPLAY_LESSON_ID && pendingReview == null -> "再试冲刺"
        else -> null
    }

    private fun replaySecondaryActionLessonId(
        lessonId: String,
        pendingReview: ReviewTask?
    ): String? = when {
        lessonId == CHALLENGE_REPLAY_LESSON_ID && pendingReview == null -> CHALLENGE_SPRINT_LESSON_ID
        else -> null
    }

    companion object {
        private const val CHALLENGE_SPRINT_LESSON_ID = "challenge-sprint-01"
        private const val CHALLENGE_REPLAY_LESSON_ID = "challenge-review-01"
    }
}
