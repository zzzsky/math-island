package com.mathisland.app.feature.level

import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.RewardSummary
import com.mathisland.app.feature.map.MapFeedbackKind
import com.mathisland.app.feature.map.mapReturnCopy
import com.mathisland.app.feature.map.rewardFeedbackKind
import com.mathisland.app.ui.theme.ActionRole

data class RewardOverlayUiState(
    val reward: RewardSummary,
    val totalStars: Int,
    val continueLabel: String,
    val continueCtaLabel: String,
    val continueActionRole: ActionRole,
    val nextStepLabel: String,
    val nextStepTitle: String,
    val nextStepBody: String,
    val nextStepDetailTitle: String,
    val nextStepDetailBody: String,
    val nextActionLabel: String,
    val nextActionTitle: String,
    val nextActionBody: String
)

object RewardViewModel {
    fun uiState(progress: GameProgress): RewardOverlayUiState? {
        val reward = progress.pendingReward ?: return null
        val kind = rewardFeedbackKind(reward)
        val copy = mapReturnCopy(kind)
        return RewardOverlayUiState(
            reward = reward,
            totalStars = progress.totalStars,
            continueLabel = copy.summaryLabel,
            continueCtaLabel = copy.continueCtaLabel,
            continueActionRole = continueActionRole(kind),
            nextStepLabel = copy.detailLabel,
            nextStepTitle = copy.summaryTitle,
            nextStepBody = copy.summaryBody,
            nextStepDetailTitle = copy.detailTitle,
            nextStepDetailBody = copy.detailBody,
            nextActionLabel = copy.actionLabel,
            nextActionTitle = copy.actionTitle,
            nextActionBody = copy.actionBody
        )
    }

    private fun continueActionRole(kind: MapFeedbackKind): ActionRole = when (kind) {
        MapFeedbackKind.NewIsland -> ActionRole.Primary
        MapFeedbackKind.Chest -> ActionRole.Recommended
        MapFeedbackKind.Replay -> ActionRole.Secondary
        MapFeedbackKind.Progress -> ActionRole.Primary
    }
}
