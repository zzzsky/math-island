package com.mathisland.app.ui.components.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.StrokeCap
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
                    colors = listOf(
                        Color(MapIllustrationTokens.SeaBackdropBase),
                        Color(MapIllustrationTokens.SeaBackdropMid),
                        Color(MapIllustrationTokens.SeaBackdropLight)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(MapIllustrationTokens.SeaBackdropWash),
                            Color.Transparent
                        )
                    )
                )
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            val waveStroke = 4.dp.toPx()
            val lines = listOf(0.18f, 0.34f, 0.56f, 0.74f)
            lines.forEachIndexed { index, ratio ->
                val y = size.height * ratio
                drawLine(
                    color = Color.White.copy(alpha = 0.08f + index * 0.015f),
                    start = Offset(size.width * 0.06f, y),
                    end = Offset(size.width * 0.94f, y + (index % 2) * 5f),
                    strokeWidth = waveStroke,
                    cap = StrokeCap.Round
                )
            }
            drawCircle(
                color = Color.White.copy(alpha = 0.06f),
                radius = size.minDimension * 0.22f,
                center = Offset(size.width * 0.18f, size.height * 0.18f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.04f),
                radius = size.minDimension * 0.30f,
                center = Offset(size.width * 0.80f, size.height * 0.24f)
            )
        }
        Image(
            painter = seaArt.painter,
            contentDescription = null,
            contentScale = seaArt.slot.contentScale,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.88f)
        )
    }
}
