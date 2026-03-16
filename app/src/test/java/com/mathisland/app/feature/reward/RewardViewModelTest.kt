package com.mathisland.app.feature.reward

import com.mathisland.app.RewardSummary
import com.mathisland.app.sampleIslands
import com.mathisland.app.MathIslandGameController
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class RewardViewModelTest {
    private val controller = MathIslandGameController(sampleIslands())

    @Test
    fun uiState_exposesPendingRewardAndTotalStars() {
        val progress = controller.initialState().copy(
            totalStars = 9,
            pendingReward = RewardSummary(
                lessonTitle = "修桥加减法",
                starsEarned = 3,
                correctAnswers = 3,
                totalQuestions = 3,
                newIslandTitle = "测量与图形岛",
                newStickerName = "Bridge Builder"
            )
        )

        val uiState = RewardViewModel.uiState(progress)

        assertNotNull(uiState)
        assertEquals("修桥加减法", uiState?.reward?.lessonTitle)
        assertEquals(9, uiState?.totalStars)
    }

    @Test
    fun uiState_buildsNextStepCopyForNewIslandUnlock() {
        val progress = controller.initialState().copy(
            totalStars = 9,
            pendingReward = RewardSummary(
                lessonTitle = "修桥加减法",
                starsEarned = 3,
                correctAnswers = 3,
                totalQuestions = 3,
                newIslandTitle = "测量与图形岛",
                newStickerName = null
            )
        )

        val uiState = RewardViewModel.uiState(progress)

        assertEquals("前往新岛", uiState?.continueLabel)
        assertEquals("下一步去测量与图形岛", uiState?.nextStepTitle)
    }
}
