package com.mathisland.app.feature.level

import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.RewardSummary
import com.mathisland.app.feature.map.MapFeedbackKind
import com.mathisland.app.feature.map.mapReturnCopy

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
                val copy = mapReturnCopy(MapFeedbackKind.Replay)
                nextStepTitle = copy.summaryTitle
                nextStepBody = copy.summaryBody
                continueLabel = copy.summaryLabel
            }
            reward.newIslandTitle != null -> {
                val copy = mapReturnCopy(MapFeedbackKind.NewIsland)
                nextStepTitle = copy.summaryTitle
                nextStepBody = copy.summaryBody
                continueLabel = copy.summaryLabel
            }
            reward.newStickerName != null -> {
                val copy = mapReturnCopy(MapFeedbackKind.Chest)
                nextStepTitle = copy.summaryTitle
                nextStepBody = copy.summaryBody
                continueLabel = copy.summaryLabel
            }
            else -> {
                val copy = mapReturnCopy(MapFeedbackKind.Progress)
                nextStepTitle = copy.summaryTitle
                nextStepBody = copy.summaryBody
                continueLabel = copy.summaryLabel
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
