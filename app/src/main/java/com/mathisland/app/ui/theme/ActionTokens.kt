package com.mathisland.app.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Immutable
enum class ActionRole {
    Primary,
    Secondary,
    Recommended,
    Completed,
    OutlinedSecondary,
}

@Immutable
data class ActionColorSet(
    val containerColor: Color,
    val contentColor: Color,
    val borderColor: Color = Color.Transparent,
)

object ActionTokens {
    val CornerRadius = 18.dp
    val HorizontalPadding = 18.dp
    val VerticalPadding = 12.dp

    fun colors(role: ActionRole): ActionColorSet = when (role) {
        ActionRole.Primary -> ActionColorSet(
            containerColor = TabletCoral,
            contentColor = TabletDeepWater,
        )

        ActionRole.Secondary -> ActionColorSet(
            containerColor = TabletSky,
            contentColor = TabletDeepWater,
        )

        ActionRole.Recommended -> ActionColorSet(
            containerColor = TabletSand,
            contentColor = TabletDeepWater,
        )

        ActionRole.Completed -> ActionColorSet(
            containerColor = TabletSeaweed,
            contentColor = TabletFoam,
        )

        ActionRole.OutlinedSecondary -> ActionColorSet(
            containerColor = Color.Transparent,
            contentColor = TabletFoam,
            borderColor = TabletSky.copy(alpha = 0.72f),
        )
    }
}
