package com.mathisland.app.domain.usecase

import com.mathisland.app.GameProgress
import com.mathisland.app.Lesson
import com.mathisland.app.MathIslandGameController
import com.mathisland.app.ReviewTask
import com.mathisland.app.data.content.CurriculumRepository
import com.mathisland.app.data.content.curriculumToGameIslands
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SubmitLessonResultUseCaseTest {
    private val controller = MathIslandGameController(
        curriculumToGameIslands(
            CurriculumRepository.loadFromFiles(
                sequenceOf(
                    File("src/main/assets/content"),
                    File("app/src/main/assets/content")
                ).first { candidate -> candidate.exists() }
            )
        )
    )
    private val useCase = SubmitLessonResultUseCase(controller.islands)

    @Test
    fun timedOutChallengeSprint_returnsZeroStarChallengeReviewResult() {
        val lesson = controller.islands
            .first { island -> island.id == "challenge-island" }
            .lessons
            .first { current -> current.id == "challenge-sprint-01" }
        val initial = controller.initialState().copy(
            unlockedIslandIds = controller.islands.map { island -> island.id }.toSet()
        )

        val result = useCase.onTimeout(
            state = initial.copy(correctAnswersInLesson = 1),
            lesson = lesson
        )

        assertEquals(0, result.starsEarned)
        assertEquals("challenge", result.pendingReview?.questionFamily)
        assertEquals("整备评级", result.reward.gradeLabel)
        assertTrue(result.reward.timedOut)
    }

    @Test
    fun clearedChallengeReplay_returnsRetrySprintAction_andClearsReview() {
        val lesson = controller.islands
            .first { island -> island.id == "challenge-island" }
            .lessons
            .first { current -> current.id == "challenge-review-01" }
        val initial = controller.initialState().copy(
            unlockedIslandIds = controller.islands.map { island -> island.id }.toSet(),
            pendingReview = ReviewTask("calculation")
        )

        val result = useCase.onLessonCompleted(
            state = initial,
            lesson = lesson,
            correctAnswers = lesson.questions.size
        )

        assertEquals("再试冲刺", result.reward.secondaryActionLabel)
        assertEquals("challenge-sprint-01", result.reward.secondaryActionLessonId)
        assertNull(result.pendingReview)
    }
}
