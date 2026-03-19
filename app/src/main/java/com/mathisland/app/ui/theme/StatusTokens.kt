package com.mathisland.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
enum class StatusVariant {
    Neutral,
    Recommended,
    Success,
    Caution,
    Highlight,
}

@Immutable
data class StatusColorSet(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color = Color.Transparent,
)

object StatusTokens {
    val HorizontalPadding = SpacingTokens.Sm
    val VerticalPadding = SpacingTokens.Xs

    fun colors(variant: StatusVariant): StatusColorSet = when (variant) {
        StatusVariant.Neutral -> StatusColorSet(
            containerColor = Color.White.copy(alpha = 0.10f),
            contentColor = TabletFoam.copy(alpha = 0.88f),
        )

        StatusVariant.Recommended -> StatusColorSet(
            containerColor = TabletSand.copy(alpha = 0.22f),
            contentColor = TabletSand,
            borderColor = TabletSand.copy(alpha = 0.28f),
        )

        StatusVariant.Success -> StatusColorSet(
            containerColor = TabletMint.copy(alpha = 0.20f),
            contentColor = TabletFoam,
            borderColor = TabletMint.copy(alpha = 0.26f),
        )

        StatusVariant.Caution -> StatusColorSet(
            containerColor = TabletCoral.copy(alpha = 0.16f),
            contentColor = TabletSand,
            borderColor = TabletCoral.copy(alpha = 0.24f),
        )

        StatusVariant.Highlight -> StatusColorSet(
            containerColor = TabletSky.copy(alpha = 0.18f),
            contentColor = TabletFoam,
            borderColor = TabletSky.copy(alpha = 0.26f),
        )
    }
}
