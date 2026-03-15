package com.mathisland.app.feature.chest

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.sampleIslands
import org.junit.Assert.assertEquals
import org.junit.Test

class ChestViewModelTest {
    private val controller = MathIslandGameController(sampleIslands())

    @Test
    fun uiState_readsStickerAndStarCountsFromProgress() {
        val progress = controller.initialState().copy(
            totalStars = 9,
            stickerNames = setOf("Bridge Builder", "Ruler Ranger")
        )

        val uiState = ChestViewModel.uiState(progress)

        assertEquals(9, uiState.totalStars)
        assertEquals(2, uiState.stickerCount)
    }
}
