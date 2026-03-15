package com.mathisland.app.feature.reward

import com.mathisland.app.GameProgress
import com.mathisland.app.RewardSummary

data class RewardUiState(
    val reward: RewardSummary,
    val totalStars: Int
)

object RewardViewModel {
    fun uiState(progress: GameProgress): RewardUiState? {
        val reward = progress.pendingReward ?: return null
        return RewardUiState(
            reward = reward,
            totalStars = progress.totalStars
        )
    }
}
