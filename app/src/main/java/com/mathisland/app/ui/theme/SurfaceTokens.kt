package com.mathisland.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class SurfaceLevel {
    Page,
    Primary,
    Secondary
}

object SurfaceTokens {
    val PageColor = Color(0xCC123847)
    val PrimaryColor = TabletPanelSurface
    val SecondaryColor = TabletInfoSurface

    val PageBorder = Color.White.copy(alpha = 0.06f)
    val PrimaryBorder = TabletSand.copy(alpha = 0.18f)
    val SecondaryBorder = Color.White.copy(alpha = 0.10f)

    val PageShape = RoundedCornerShape(28.dp)
    val PrimaryShape = RoundedCornerShape(28.dp)
    val SecondaryShape = RoundedCornerShape(24.dp)
}

fun surfaceColor(level: SurfaceLevel): Color = when (level) {
    SurfaceLevel.Page -> SurfaceTokens.PageColor
    SurfaceLevel.Primary -> SurfaceTokens.PrimaryColor
    SurfaceLevel.Secondary -> SurfaceTokens.SecondaryColor
}

fun surfaceBorder(level: SurfaceLevel): Color = when (level) {
    SurfaceLevel.Page -> SurfaceTokens.PageBorder
    SurfaceLevel.Primary -> SurfaceTokens.PrimaryBorder
    SurfaceLevel.Secondary -> SurfaceTokens.SecondaryBorder
}

fun surfaceShape(level: SurfaceLevel) = when (level) {
    SurfaceLevel.Page -> SurfaceTokens.PageShape
    SurfaceLevel.Primary -> SurfaceTokens.PrimaryShape
    SurfaceLevel.Secondary -> SurfaceTokens.SecondaryShape
}
