package com.mathisland.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun MathIslandTheme(content: @Composable () -> Unit) {
    val colors = darkColorScheme(
        primary = TabletSand,
        secondary = TabletCoral,
        tertiary = TabletSeaweed,
        background = TabletOcean,
        surface = TabletDeepWater,
        onPrimary = TabletDeepWater,
        onSecondary = TabletFoam,
        onBackground = TabletFoam,
        onSurface = TabletFoam
    )

    MaterialTheme(
        colorScheme = colors,
        typography = MathIslandTypography,
        content = content
    )
}
