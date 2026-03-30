package com.mathisland.app.feature.level

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.domain.model.RewardSummary
import com.mathisland.app.sampleIslands
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
                newIslandId = null,
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
                newIslandId = null,
                newIslandTitle = "测量与图形岛",
                newStickerName = null
            )
        )

        val uiState = RewardViewModel.uiState(progress)

        assertEquals("主线继续", uiState?.continueLabel)
        assertEquals("回地图看新岛", uiState?.continueCtaLabel)
        assertEquals("回地图后", uiState?.nextStepLabel)
        assertEquals("新岛可出发", uiState?.nextStepTitle)
        assertEquals("先看新岛", uiState?.nextStepDetailTitle)
        assertEquals("优先动作", uiState?.nextActionLabel)
        assertEquals("开始主线课", uiState?.nextActionTitle)
    }

    @Test
    fun uiState_buildsNextStepCopyForChestReward() {
        val progress = controller.initialState().copy(
            totalStars = 9,
            pendingReward = RewardSummary(
                lessonTitle = "修桥加减法",
                starsEarned = 3,
                correctAnswers = 3,
                totalQuestions = 3,
                newIslandId = null,
                newIslandTitle = null,
                newStickerName = "Bridge Builder"
            )
        )

        val uiState = RewardViewModel.uiState(progress)

        assertEquals("先看收藏", uiState?.continueLabel)
        assertEquals("回地图开宝箱", uiState?.continueCtaLabel)
        assertEquals("宝箱可打开", uiState?.nextStepTitle)
        assertEquals("先开宝箱", uiState?.nextStepDetailTitle)
        assertEquals("先开宝箱", uiState?.nextActionTitle)
    }

    @Test
    fun uiState_buildsNextStepCopyForReplayFlow() {
        val progress = controller.initialState().copy(
            totalStars = 9,
            pendingReward = RewardSummary(
                lessonTitle = "海图冲刺赛",
                starsEarned = 0,
                correctAnswers = 1,
                totalQuestions = 3,
                newIslandId = null,
                newIslandTitle = null,
                newStickerName = null,
                timedOut = true,
                gradeLabel = "整备评级",
                gradeDescription = "先回放再发起下一轮冲刺。"
            )
        )

        val uiState = RewardViewModel.uiState(progress)

        assertEquals("先做回放", uiState?.continueLabel)
        assertEquals("回地图做回放", uiState?.continueCtaLabel)
        assertEquals("回放已就位", uiState?.nextStepTitle)
        assertEquals("先做回放", uiState?.nextStepDetailTitle)
        assertEquals("先做回放", uiState?.nextActionTitle)
    }
}
