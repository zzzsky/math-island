package com.mathisland.app.feature.level

import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.RewardSummary
import com.mathisland.app.feature.map.MapFeedbackKind
import com.mathisland.app.feature.map.mapReturnCopy

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
        val copy = when {
            reward.timedOut -> mapReturnCopy(MapFeedbackKind.Replay)
            reward.newIslandTitle != null -> mapReturnCopy(MapFeedbackKind.NewIsland)
            reward.newStickerName != null -> mapReturnCopy(MapFeedbackKind.Chest)
            else -> mapReturnCopy(MapFeedbackKind.Progress)
        }
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
