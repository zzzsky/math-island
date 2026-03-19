package com.mathisland.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.surfaceBorder
import com.mathisland.app.ui.theme.surfaceColor
import com.mathisland.app.ui.theme.surfaceShape

@Composable
fun SurfaceCard(
    modifier: Modifier = Modifier,
    level: SurfaceLevel,
    containerColor: Color? = null,
    borderColor: Color? = null,
    shape: Shape? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val resolvedShape = shape ?: surfaceShape(level)
    val resolvedBorder = borderColor ?: surfaceBorder(level)
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor ?: surfaceColor(level)),
        shape = resolvedShape,
        border = BorderStroke(1.dp, resolvedBorder),
        content = content
    )
}
