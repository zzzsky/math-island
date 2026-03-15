package com.mathisland.app

import com.mathisland.app.data.content.CurriculumRepository
import com.mathisland.app.data.content.curriculumToGameIslands
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MathIslandGameControllerTest {
    private val controller = MathIslandGameController(sampleIslands())
    private val contentDir = sequenceOf(
        File("src/main/assets/content"),
        File("app/src/main/assets/content")
    ).first { candidate -> candidate.exists() }
    private val curriculumController = MathIslandGameController(
        curriculumToGameIslands(CurriculumRepository.loadFromFiles(contentDir))
    )

    @Test
    fun continueAdventure_movesToFirstUnlockedLesson() {
        val state = controller.continueAdventure(controller.initialState())

        assertEquals(AppDestination.LESSON, state.destination)
        assertEquals("calc-bridge", state.activeLessonId)
    }

    @Test
    fun clearingAnIsland_unlocksNextIsland_andAddsSticker() {
        var state = controller.initialState()

        controller.islands.first().lessons.forEach { lesson ->
            state = controller.startLesson(state, lesson.id)
            lesson.questions.forEach { question ->
                state = controller.answer(state, question.correctChoice)
            }
            state = controller.claimReward(state)
        }

        assertTrue(state.unlockedIslandIds.contains("multiplication"))
        assertTrue(state.stickerNames.contains("Bridge Builder"))
        assertEquals(AppDestination.MAP, state.destination)
    }

    @Test
    fun oneMistake_awardsTwoStars() {
        val lesson = controller.islands.first().lessons.first()
        var state = controller.startLesson(controller.initialState(), lesson.id)

        state = controller.answer(state, lesson.questions[0].correctChoice)
        state = controller.answer(state, "wrong")
        state = controller.answer(state, lesson.questions[2].correctChoice)

        assertEquals(AppDestination.REWARD, state.destination)
        assertEquals(2, state.pendingReward?.starsEarned)
        assertEquals(2, state.totalStars)
    }

    @Test
    fun twoConsecutiveWrongAnswers_scheduleSeagullReview() {
        val lesson = controller.islands.first().lessons.first()
        var state = controller.startLesson(controller.initialState(), lesson.id)

        state = controller.answer(state, "34")
        state = controller.answer(state, "45")
        state = controller.answer(state, lesson.questions[2].correctChoice)

        assertEquals("calculation", state.pendingReview?.questionFamily)

        state = controller.claimReward(state)
        state = controller.continueAdventure(state)

        assertEquals("review-calculation", state.activeLessonId)
        assertEquals("小海鸥求助", controller.currentLesson(state)?.title)
    }

    @Test
    fun perfectSeagullReview_clearsPendingReview() {
        var state = controller.initialState().copy(
            pendingReview = ReviewTask(questionFamily = "calculation")
        )

        state = controller.continueAdventure(state)

        val reviewLesson = controller.currentLesson(state) ?: error("Expected review lesson.")
        reviewLesson.questions.forEach { question ->
            state = controller.answer(state, question.correctChoice)
        }

        assertNull(state.pendingReview)
        assertEquals(1, state.pendingReward?.starsEarned)
    }

    @Test
    fun challengeReplayLesson_usesPendingReviewFamily_andClearsItOnPerfectRun() {
        var state = curriculumController.initialState().copy(
            unlockedIslandIds = curriculumController.islands.map { island -> island.id }.toSet(),
            pendingReview = ReviewTask(questionFamily = "calculation")
        )

        state = curriculumController.startLesson(state, "challenge-review-01")

        val lesson = curriculumController.currentLesson(state) ?: error("Expected challenge replay lesson.")
        assertEquals("calculation", lesson.questions.first().family)
        assertEquals("26 + 18 = ?", lesson.questions.first().prompt)

        lesson.questions.forEach { question ->
            state = curriculumController.answer(state, question.correctChoice)
        }

        assertNull(state.pendingReview)
    }

    @Test
    fun expiringChallengeSprint_returnsTimedOutReward_withoutMarkingLessonComplete() {
        var state = curriculumController.initialState().copy(
            unlockedIslandIds = curriculumController.islands.map { island -> island.id }.toSet()
        )

        state = curriculumController.startLesson(state, "challenge-sprint-01")
        state = curriculumController.answer(state, "81")
        state = curriculumController.expireLesson(state)

        assertEquals(AppDestination.REWARD, state.destination)
        assertEquals(0, state.pendingReward?.starsEarned)
        assertEquals(true, state.pendingReward?.timedOut)
        assertTrue(!state.completedLessonIds.contains("challenge-sprint-01"))
        assertEquals("challenge", state.pendingReview?.questionFamily)
    }

    @Test
    fun timedOutChallengeSprint_prioritizesChallengeReplayLesson_onContinueAdventure() {
        var state = curriculumController.initialState().copy(
            unlockedIslandIds = curriculumController.islands.map { island -> island.id }.toSet()
        )

        state = curriculumController.startLesson(state, "challenge-sprint-01")
        state = curriculumController.expireLesson(state)
        state = curriculumController.claimReward(state)
        state = curriculumController.continueAdventure(state)

        assertEquals("challenge-review-01", state.activeLessonId)
        assertEquals("错题回放站", curriculumController.currentLesson(state)?.title)
    }

    @Test
    fun perfectChallengeReplay_offersRetrySprintAction() {
        var state = curriculumController.initialState().copy(
            unlockedIslandIds = curriculumController.islands.map { island -> island.id }.toSet(),
            pendingReview = ReviewTask(questionFamily = "calculation")
        )

        state = curriculumController.startLesson(state, "challenge-review-01")
        val lesson = curriculumController.currentLesson(state) ?: error("Expected challenge replay lesson.")
        lesson.questions.forEach { question ->
            state = curriculumController.answer(state, question.correctChoice)
        }

        assertEquals("再试冲刺", state.pendingReward?.secondaryActionLabel)
        assertEquals("challenge-sprint-01", state.pendingReward?.secondaryActionLessonId)
    }

    @Test
    fun perfectChallengeSprint_assignsGoldGrade() {
        var state = curriculumController.initialState().copy(
            unlockedIslandIds = curriculumController.islands.map { island -> island.id }.toSet()
        )

        state = curriculumController.startLesson(state, "challenge-sprint-01")
        val lesson = curriculumController.currentLesson(state) ?: error("Expected challenge sprint lesson.")
        lesson.questions.forEach { question ->
            state = curriculumController.answer(state, question.correctChoice)
        }

        assertEquals("金帆评级", state.pendingReward?.gradeLabel)
        assertNotNull(state.pendingReward?.gradeDescription)
    }
}
