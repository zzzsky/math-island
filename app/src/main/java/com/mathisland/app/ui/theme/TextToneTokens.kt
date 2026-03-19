package com.mathisland.app.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object TextToneTokens {
    @Composable
    fun high(onSurface: Color): Color = onSurface.copy(alpha = 0.90f)

    @Composable
    fun medium(onSurface: Color): Color = onSurface.copy(alpha = 0.84f)

    @Composable
    fun low(onSurface: Color): Color = onSurface.copy(alpha = 0.78f)

    @Composable
    fun supporting(onSurface: Color): Color = onSurface.copy(alpha = 0.72f)
}
