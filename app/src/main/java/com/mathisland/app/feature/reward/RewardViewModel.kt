package com.mathisland.app.feature.reward

import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.feature.level.RewardOverlayUiState

@Deprecated(
    message = "Use feature.level.RewardViewModel instead.",
    replaceWith = ReplaceWith("com.mathisland.app.feature.level.RewardViewModel")
)
typealias RewardUiState = RewardOverlayUiState

@Deprecated(
    message = "Use feature.level.RewardViewModel instead.",
    replaceWith = ReplaceWith("com.mathisland.app.feature.level.RewardViewModel")
)
object RewardViewModel {
    fun uiState(progress: GameProgress): RewardUiState? =
        com.mathisland.app.feature.level.RewardViewModel.uiState(progress)
}
