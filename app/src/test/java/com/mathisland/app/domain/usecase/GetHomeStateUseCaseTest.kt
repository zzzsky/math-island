package com.mathisland.app.domain.usecase

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.ReviewTask
import com.mathisland.app.data.progress.InMemoryProgressStore
import com.mathisland.app.data.progress.ProgressRepository
import com.mathisland.app.sampleIslands
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetHomeStateUseCaseTest {
    private val controller = MathIslandGameController(sampleIslands())

    @Test
    fun pendingReview_isReflectedInHomeState() {
        val repository = ProgressRepository(
            store = InMemoryProgressStore(
                controller.initialState().copy(
                    totalStars = 5,
                    stickerNames = setOf("Bridge Builder"),
                    pendingReview = ReviewTask("calculation")
                )
            ),
            controller = controller
        )

        val state = GetHomeStateUseCase(repository, controller).invoke()

        assertEquals(5, state.totalStars)
        assertEquals(1, state.stickerCount)
        assertEquals("小海鸥求助", state.nextLessonTitle)
        assertTrue(state.isReview)
    }
}
