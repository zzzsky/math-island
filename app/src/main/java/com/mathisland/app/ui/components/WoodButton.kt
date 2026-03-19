package com.mathisland.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.TabletDeepWater
import com.mathisland.app.ui.theme.TabletFoam
import com.mathisland.app.ui.theme.TabletSand
import com.mathisland.app.ui.theme.TabletSeaweed
import com.mathisland.app.ui.theme.TabletSky

@Composable
fun WoodButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color,
    contentColor: Color = TabletDeepWater,
    role: ActionRole? = null,
) {
    ActionButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        role = role ?: inferActionRole(containerColor, contentColor),
        containerColor = containerColor,
        contentColor = contentColor,
    )
}

private fun inferActionRole(
    containerColor: Color,
    contentColor: Color,
): ActionRole = when {
    contentColor == TabletFoam || containerColor == TabletSeaweed -> ActionRole.Completed
    containerColor == TabletSand -> ActionRole.Recommended
    containerColor == TabletSky -> ActionRole.Secondary
    else -> ActionRole.Primary
}
