package com.mathisland.app.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.mathisland.app.ui.theme.SurfaceLevel

@Composable
fun StoryPanelCard(
    modifier: Modifier = Modifier,
    level: SurfaceLevel = SurfaceLevel.Primary,
    containerColor: Color? = null,
    borderColor: Color? = null,
    shape: Shape? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    SurfaceCard(
        modifier = modifier,
        level = level,
        containerColor = containerColor,
        borderColor = borderColor,
        shape = shape,
        content = content
    )
}
