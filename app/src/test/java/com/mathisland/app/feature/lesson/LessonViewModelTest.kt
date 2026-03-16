package com.mathisland.app.feature.lesson

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.Island
import com.mathisland.app.Lesson
import com.mathisland.app.Question
import com.mathisland.app.sampleIslands
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class LessonViewModelTest {
    private val controller = MathIslandGameController(sampleIslands())

    @Test
    fun uiState_mapsActiveLessonIntoScreenModel() {
        val initialState = controller.initialState()
        val firstLesson = controller.islands.first().lessons.first()
        val lessonState = controller.startLesson(initialState, firstLesson.id)

        val uiState = LessonViewModel.uiState(controller, lessonState)

        assertNotNull(uiState)
        assertEquals(firstLesson.id, uiState?.lesson?.id)
        assertEquals(firstLesson.questions.first().prompt, uiState?.question?.prompt)
        assertEquals(0, uiState?.questionIndex)
        assertEquals(firstLesson.questions.size, uiState?.totalQuestions)
        assertEquals(0, uiState?.totalStars)
    }

    @Test
    fun uiState_buildsFlowHintForTimedSprintLesson() {
        val sprintLesson = Lesson(
            id = "challenge-sprint-01",
            islandId = "challenge",
            title = "海图冲刺赛",
            focus = "综合挑战",
            summary = "3 题限时冲刺",
            questions = listOf(
                Question(
                    prompt = "9 x 9 = ?",
                    choices = listOf("81", "72", "99"),
                    correctChoice = "81",
                    hint = "九九八十一。",
                    family = "challenge"
                )
            ),
            timeLimitSeconds = 8
        )
        val sprintController = MathIslandGameController(
            sampleIslands() + Island(
                id = "challenge",
                title = "综合挑战岛",
                subtitle = "冲刺与回放",
                description = "最后的冒险站",
                rewardSticker = "Challenge Badge",
                lessons = listOf(sprintLesson)
            )
        )
        val initialState = sprintController.initialState().copy(
            unlockedIslandIds = sprintController.initialState().unlockedIslandIds + "challenge"
        )
        val lessonState = sprintController.startLesson(initialState, sprintLesson.id)

        val uiState = LessonViewModel.uiState(sprintController, lessonState)

        assertEquals("冲刺结束后会显示评级，并决定是否优先进入错题回放。", uiState?.flowHint)
    }
}
