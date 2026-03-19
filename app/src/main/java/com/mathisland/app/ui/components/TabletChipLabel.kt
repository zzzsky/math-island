package com.mathisland.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mathisland.app.ui.theme.StatusVariant

@Composable
fun TabletChipLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    StatusChip(
        text = text,
        modifier = modifier,
        variant = StatusVariant.Neutral
    )
}
