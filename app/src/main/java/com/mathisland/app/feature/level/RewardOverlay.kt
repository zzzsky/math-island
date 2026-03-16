package com.mathisland.app.feature.level

import androidx.compose.runtime.Composable
import com.mathisland.app.feature.reward.RewardUiState

@Composable
fun RewardOverlay(
    state: RewardUiState,
    onContinue: () -> Unit,
    onSecondaryAction: (() -> Unit)? = null
) {
    com.mathisland.app.feature.reward.RewardTabletScreen(
        state = state,
        onContinue = onContinue,
        onSecondaryAction = onSecondaryAction
    )
}
