package com.mathisland.app.feature.level

import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.RewardSummary
import com.mathisland.app.feature.map.mapReturnCopy
import com.mathisland.app.feature.map.rewardFeedbackKind

data class RewardOverlayUiState(
    val reward: RewardSummary,
    val totalStars: Int,
    val continueLabel: String,
    val nextStepLabel: String,
    val nextStepTitle: String,
    val nextStepBody: String,
    val nextStepDetailTitle: String,
    val nextStepDetailBody: String
)

object RewardViewModel {
    fun uiState(progress: GameProgress): RewardOverlayUiState? {
        val reward = progress.pendingReward ?: return null
        val copy = mapReturnCopy(rewardFeedbackKind(reward))
        return RewardOverlayUiState(
            reward = reward,
            totalStars = progress.totalStars,
            continueLabel = copy.summaryLabel,
            nextStepLabel = copy.detailLabel,
            nextStepTitle = copy.summaryTitle,
            nextStepBody = copy.summaryBody,
            nextStepDetailTitle = copy.detailTitle,
            nextStepDetailBody = copy.detailBody
        )
    }
}
