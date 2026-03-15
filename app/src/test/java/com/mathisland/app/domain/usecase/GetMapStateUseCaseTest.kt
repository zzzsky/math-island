package com.mathisland.app.domain.usecase

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.data.progress.InMemoryProgressStore
import com.mathisland.app.data.progress.ProgressRepository
import com.mathisland.app.sampleIslands
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetMapStateUseCaseTest {
    private val controller = MathIslandGameController(sampleIslands())

    @Test
    fun unlockedAndCompletedProgress_isProjectedForMap() {
        val repository = ProgressRepository(
            store = InMemoryProgressStore(
                controller.initialState().copy(
                    unlockedIslandIds = setOf("calculation", "multiplication"),
                    completedLessonIds = setOf("calc-bridge"),
                    totalStars = 3
                )
            ),
            controller = controller
        )

        val state = GetMapStateUseCase(repository, controller).invoke()
        val calculation = state.islands.first { island -> island.id == "calculation" }
        val multiplication = state.islands.first { island -> island.id == "multiplication" }

        assertEquals(3, state.totalStars)
        assertTrue(calculation.unlocked)
        assertEquals(0.5f, calculation.progress)
        assertTrue(multiplication.unlocked)
        assertEquals(false, multiplication.lessons.first().completed)
    }
}
