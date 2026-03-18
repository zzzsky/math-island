package com.mathisland.app.ui.components.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun SeaBackdropPainter(
    modifier: Modifier = Modifier,
    artSource: MapArtSource = MapArtRegistry
) {
    val seaArt = MapArtRegistry.resolveSeaBackdrop(artSource)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF0F4C5C), Color(0xFF1B6B83), Color(0xFF77B8D8))
                )
            )
    ) {
        Image(
            painter = seaArt.painter,
            contentDescription = null,
            contentScale = seaArt.slot.contentScale,
            modifier = Modifier.fillMaxSize()
        )
    }
}
