package com.mathisland.app.feature.lesson

import com.mathisland.app.MathIslandGameController
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
}
