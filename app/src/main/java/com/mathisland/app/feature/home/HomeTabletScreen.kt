package com.mathisland.app.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mathisland.app.domain.usecase.HomeState
import com.mathisland.app.ui.theme.SpacingTokens

@Composable
fun HomeTabletScreen(
    state: HomeState,
    onContinue: () -> Unit,
    onOpenMap: () -> Unit,
    onOpenChest: () -> Unit,
    onOpenParent: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Xl)
    ) {
        HomeHeroPanel(
            state = state,
            modifier = Modifier.weight(1.3f)
        )
        HomeActionColumn(
            state = state,
            modifier = Modifier.weight(0.9f),
            onContinue = onContinue,
            onOpenMap = onOpenMap,
            onOpenChest = onOpenChest,
            onOpenParent = onOpenParent,
        )
    }
}
