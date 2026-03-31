package com.mathisland.app.feature.chest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.verticalScroll
import com.mathisland.app.ui.theme.SpacingTokens

@Composable
fun ChestTabletScreen(
    state: ChestUiState,
    onBackHome: () -> Unit,
    onOpenMap: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .testTag("chest-screen"),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
    ) {
        ChestHeaderPanel(
            summaryText = state.summaryText,
            onBackHome = onBackHome,
            onOpenMap = onOpenMap,
        )
        if (state.stickers.isEmpty()) {
            ChestEmptyStateCard()
        } else {
            StickerCollectionGrid(stickers = state.stickers)
        }
    }
}
