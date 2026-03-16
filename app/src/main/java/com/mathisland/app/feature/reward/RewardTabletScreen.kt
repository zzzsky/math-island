package com.mathisland.app.feature.reward

import androidx.compose.runtime.Composable
import com.mathisland.app.feature.level.RewardOverlay

@Deprecated(
    message = "Use feature.level.RewardOverlay instead.",
    replaceWith = ReplaceWith("com.mathisland.app.feature.level.RewardOverlay(state, onContinue, onSecondaryAction)")
)
@Composable
fun RewardTabletScreen(
    state: RewardUiState,
    onContinue: () -> Unit,
    onSecondaryAction: (() -> Unit)? = null
) {
    RewardOverlay(
        state = state,
        onContinue = onContinue,
        onSecondaryAction = onSecondaryAction
    )
}
