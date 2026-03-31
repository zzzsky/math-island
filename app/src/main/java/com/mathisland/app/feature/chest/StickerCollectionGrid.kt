package com.mathisland.app.feature.chest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.mathisland.app.ui.theme.SpacingTokens

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StickerCollectionGrid(
    stickers: List<String>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .testTag("chest-sticker-grid"),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Md),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md),
        maxItemsInEachRow = 2
    ) {
        stickers.forEachIndexed { index, sticker ->
            StickerCard(
                sticker = sticker,
                index = index,
                modifier = Modifier.fillMaxWidth(0.48f)
            )
        }
    }
}
