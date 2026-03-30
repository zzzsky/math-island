package com.mathisland.app.feature.level

import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.RewardSummary
import com.mathisland.app.feature.map.mapReturnCopy
import com.mathisland.app.feature.map.rewardFeedbackKind
import com.mathisland.app.feature.map.toReturnResultStageState
import com.mathisland.app.ui.components.ReturnResultStageState
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
    val nextActionBody: String,
    val resultStageState: ReturnResultStageState? = null
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
            continueActionRole = copy.continueActionRole,
            nextStepLabel = copy.detailLabel,
            nextStepTitle = copy.summaryTitle,
            nextStepBody = copy.summaryBody,
            nextStepDetailTitle = copy.detailTitle,
            nextStepDetailBody = copy.detailBody,
            nextActionLabel = copy.actionLabel,
            nextActionTitle = copy.actionTitle,
            nextActionBody = copy.actionBody,
            resultStageState = copy.toReturnResultStageState()
        )
    }
}
