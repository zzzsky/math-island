package com.mathisland.app.feature.parent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ParentGateScreen(
    state: ParentGateUiState,
    onAnswer: (String) -> Unit,
    onBackHome: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ParentGatePanel(
            state = state,
            onAnswer = onAnswer,
            onBackHome = onBackHome,
            modifier = Modifier.fillMaxWidth(0.56f)
        )
    }
}
