package com.mathisland.app.feature.level

import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.RewardSummary

data class RewardOverlayUiState(
    val reward: RewardSummary,
    val totalStars: Int,
    val continueLabel: String,
    val nextStepTitle: String,
    val nextStepBody: String
)

object RewardViewModel {
    fun uiState(progress: GameProgress): RewardOverlayUiState? {
        val reward = progress.pendingReward ?: return null
        val nextStepTitle: String
        val nextStepBody: String
        val continueLabel: String

        when {
            reward.timedOut -> {
                nextStepTitle = "下一步先回放再冲刺"
                nextStepBody = "先回地图查看错题回放站，再决定是否立刻再试一次冲刺。"
                continueLabel = "回地图看回放"
            }
            reward.newIslandTitle != null -> {
                nextStepTitle = "下一步去${reward.newIslandTitle}"
                nextStepBody = "地图会自动切到新岛，继续下一条主线课程。"
                continueLabel = "前往新岛"
            }
            reward.newStickerName != null -> {
                nextStepTitle = "下一步查看宝箱收藏"
                nextStepBody = "这张新贴纸已经收入宝箱，回地图后可以立刻打开查看。"
                continueLabel = "回地图开宝箱"
            }
            else -> {
                nextStepTitle = "下一步继续当前岛屿"
                nextStepBody = "回地图后会保留当前推荐课程，继续往下推进。"
                continueLabel = "回到地图"
            }
        }
        return RewardOverlayUiState(
            reward = reward,
            totalStars = progress.totalStars,
            continueLabel = continueLabel,
            nextStepTitle = nextStepTitle,
            nextStepBody = nextStepBody
        )
    }
}
