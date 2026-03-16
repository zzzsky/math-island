package com.mathisland.app.feature.home

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.data.progress.InMemoryProgressStore
import com.mathisland.app.data.progress.ProgressRepository
import com.mathisland.app.domain.model.ReviewTask
import com.mathisland.app.domain.usecase.GetHomeStateUseCase
import com.mathisland.app.sampleIslands
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeViewModelTest {
    private val controller = MathIslandGameController(sampleIslands())

    @Test
    fun uiState_wrapsHomeUseCaseResult() {
        val repository = ProgressRepository(
            store = InMemoryProgressStore(
                controller.initialState().copy(
                    totalStars = 6,
                    stickerNames = setOf("Bridge Builder"),
                    pendingReview = ReviewTask("calculation")
                )
            ),
            controller = controller
        )

        val uiState = HomeViewModel.uiState(GetHomeStateUseCase(repository, controller))

        assertEquals(6, uiState.totalStars)
        assertEquals(1, uiState.stickerCount)
        assertTrue(uiState.isReview)
    }
}
