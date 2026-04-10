package com.mathisland.app

import com.mathisland.app.data.content.CurriculumRepository
import com.mathisland.app.data.content.curriculumToGameIslands
import com.mathisland.app.domain.model.AppDestination
import com.mathisland.app.domain.model.ReviewTask
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

    @Test
    fun classificationMatchingLesson_resolvesMatchingQuestions() {
        val lesson = curriculumController.islands
            .first { it.id == "classification-island" }
            .lessons
            .first { it.id == "classification-match-01" }

        assertEquals("matching", lesson.questions.first().family)
        assertEquals(listOf("尺子", "秤", "时钟"), lesson.questions.first().leftItems)
        assertEquals(listOf("时间", "重量", "长度"), lesson.questions.first().rightItems)
    }

    @Test
    fun groupedMatchingLesson_resolvesMatchingGroups() {
        val lesson = curriculumController.islands
            .first { it.id == "classification-island" }
            .lessons
            .first { it.id == "classification-match-05" }

        assertEquals("matching", lesson.questions.first().family)
        assertEquals(2, lesson.questions.first().matchingGroups.size)
        assertEquals("看场景选算法", lesson.questions.first().matchingGroups.first().title)
    }

    @Test
    fun multiRoundMatchingLesson_resolvesMatchingRounds() {
        val lesson = curriculumController.islands
            .first { it.id == "classification-island" }
            .lessons
            .first { it.id == "classification-match-06" }

        assertEquals("matching", lesson.questions.first().family)
        assertEquals(2, lesson.questions.first().matchingRounds.size)
        assertEquals("第一轮：看场景选算法", lesson.questions.first().matchingRounds.first().title)
        assertEquals(
            "第二轮：把算法和它最适合解决的问题连起来。",
            lesson.questions.first().matchingRounds.last().prompt
        )
    }

    @Test
    fun measurementFillBlankLesson_resolvesFillBlankQuestions() {
        val lesson = curriculumController.islands
            .first { it.id == "measurement-geometry-island" }
            .lessons
            .first { it.id == "measure-fill-01" }

        assertEquals("fill-blank", lesson.questions.first().family)
        assertEquals(
            listOf("1 米 = ", " 厘米，2 米 = ", " 厘米。"),
            lesson.questions.first().blankParts
        )
        assertEquals(
            listOf("200", "100", "20"),
            lesson.questions.first().blankOptions
        )
    }

    @Test
    fun divisionMultiStepLesson_resolvesMultiStepQuestions() {
        val lesson = curriculumController.islands
            .first { it.id == "division-island" }
            .lessons
            .first { it.id == "division-steps-01" }

        assertEquals("multi-step", lesson.questions.first().family)
        assertEquals(
            listOf("第一步：先判断这题要怎么分？", "第二步：每只小猴分到几个？"),
            lesson.questions.first().stepPrompts
        )
        assertEquals(
            listOf(
                listOf("平均分给 3 只小猴", "先把 12 和 3 相加", "先比较水果颜色"),
                listOf("每只 3 个", "每只 4 个", "每只 5 个")
            ),
            lesson.questions.first().stepChoices
        )
    }

    @Test
    fun conditionalMultiStepLesson_resolvesBranchMetadata() {
        val lesson = curriculumController.islands
            .first { it.id == "division-island" }
            .lessons
            .first { it.id == "division-steps-05" }

        assertEquals("multi-step", lesson.questions.first().family)
        assertEquals(
            listOf("branch-start", "step-2", "step-3"),
            lesson.questions.first().stepBranchKeys
        )
        assertTrue(lesson.questions.first().stepBranchRules.containsKey("branch-start"))
        assertEquals(
            "第二步：18 ÷ 4 的结果是什么？",
            lesson.questions.first().stepBranchPrompts["remainder-step-2"]
        )
    }

    @Test
    fun matchingFillBlankAndMultiStepExpansionLessons_areAvailable() {
        val classificationLessons = curriculumController.islands
            .first { it.id == "classification-island" }
            .lessons
            .map { it.id }
        val measurementLessons = curriculumController.islands
            .first { it.id == "measurement-geometry-island" }
            .lessons
            .map { it.id }
        val divisionLessons = curriculumController.islands
            .first { it.id == "division-island" }
            .lessons
            .map { it.id }

        assertTrue(classificationLessons.containsAll(listOf(
            "classification-match-01",
            "classification-match-02",
            "classification-match-03",
            "classification-match-04",
            "classification-match-05",
            "classification-match-06"
        )))
        assertTrue(measurementLessons.containsAll(listOf(
            "measure-fill-01",
            "measure-fill-02",
            "measure-fill-03",
            "measure-fill-04"
        )))
        assertTrue(divisionLessons.containsAll(listOf(
            "division-steps-01",
            "division-steps-02",
            "division-steps-03",
            "division-steps-04",
            "division-steps-05"
        )))
    }

    @Test
    fun advancedRendererLessons_resolveExpandedQuestionShapes() {
        val matchingLesson = curriculumController.islands
            .first { it.id == "classification-island" }
            .lessons
            .first { it.id == "classification-match-04" }
        val fillBlankLesson = curriculumController.islands
            .first { it.id == "measurement-geometry-island" }
            .lessons
            .first { it.id == "measure-fill-04" }
        val multiStepLesson = curriculumController.islands
            .first { it.id == "division-island" }
            .lessons
            .first { it.id == "division-steps-04" }

        assertEquals(4, matchingLesson.questions.first().leftItems.size)
        assertEquals(4, matchingLesson.questions.first().rightItems.size)
        assertEquals(4, fillBlankLesson.questions.first().blankOptions.size)
        assertEquals(4, fillBlankLesson.questions.first().blankParts.size)
        assertEquals(3, multiStepLesson.questions.first().stepPrompts.size)
        assertEquals(3, multiStepLesson.questions.first().stepChoices.size)
    }

    @Test
    fun mixedFillBlankLesson_resolvesSlotKinds() {
        val lesson = curriculumController.islands
            .first { it.id == "measurement-geometry-island" }
            .lessons
            .first { it.id == "measure-fill-05" }

        assertEquals("fill-blank", lesson.questions.first().family)
        assertEquals(
            listOf("number", "unit", "number"),
            lesson.questions.first().blankSlotKinds
        )
    }
}
