package com.mathisland.app.feature.chest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mathisland.app.ui.theme.SpacingTokens

@Composable
fun StickerCollectionGrid(
    stickers: List<String>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
    ) {
        stickers.forEach { sticker ->
            StickerCard(
                sticker = sticker,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
