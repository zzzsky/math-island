package com.mathisland.app.ui.components.map

import androidx.compose.foundation.Canvas
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

private val RouteGlow = Color(MapIllustrationTokens.RouteGlow)
private val FocusRing = Color(MapIllustrationTokens.RouteHighlight)
private val RouteInk = Color(MapIllustrationTokens.RouteInk)
private val RoutePaper = Color(MapIllustrationTokens.RoutePaper)

@Composable
fun RoutePainter(
    islandId: String,
    highlighted: Boolean,
    motionProgress: Float = 0f,
    modifier: Modifier = Modifier,
    artSource: MapArtSource = MapArtRegistry
) {
    val routeArt = MapArtRegistry.resolveRouteArt(highlighted, artSource)
    val routePainter = routeArt.slot.drawableResId?.let { painterResource(it) } ?: routeArt.painter
    val routeAlpha by animateFloatAsState(
        targetValue = if (highlighted) 1f else 0.5f,
        animationSpec = tween(durationMillis = 600),
        label = "map-route-alpha-$islandId"
    )
    val sweepProgress = if (highlighted) motionProgress.coerceIn(0f, 1f) else 0f
    Box(
        modifier = modifier
            .height(6.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(999.dp))
            .background(RoutePaper.copy(alpha = 0.22f))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = RouteInk.copy(alpha = 0.42f),
            )
            drawRect(
                color = FocusRing.copy(alpha = 0.20f + (routeAlpha * 0.18f)),
                style = Stroke(width = 1.6f)
            )
            drawLine(
                color = RouteGlow.copy(alpha = routeAlpha * 0.35f),
                start = Offset(size.width * 0.08f, size.height * 0.33f),
                end = Offset(size.width * 0.92f, size.height * 0.42f),
                strokeWidth = 1.2f,
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            drawLine(
                color = Color.White.copy(alpha = 0.12f + routeAlpha * 0.08f),
                start = Offset(size.width * 0.10f, size.height * 0.62f),
                end = Offset(size.width * 0.88f, size.height * 0.58f),
                strokeWidth = 0.9f,
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            if (sweepProgress > 0f) {
                val sweepStartX = size.width * 0.08f
                val sweepEndX = size.width * 0.92f
                val sweepStartY = size.height * 0.33f
                val sweepEndY = size.height * 0.42f
                val sweepHead = sweepProgress
                val sweepTail = (sweepHead - 0.18f).coerceAtLeast(0f)
                val head = Offset(
                    x = sweepStartX + ((sweepEndX - sweepStartX) * sweepHead),
                    y = sweepStartY + ((sweepEndY - sweepStartY) * sweepHead)
                )
                val tail = Offset(
                    x = sweepStartX + ((sweepEndX - sweepStartX) * sweepTail),
                    y = sweepStartY + ((sweepEndY - sweepStartY) * sweepTail)
                )
                drawLine(
                    color = FocusRing.copy(alpha = 0.42f + (sweepProgress * 0.36f)),
                    start = tail,
                    end = head,
                    strokeWidth = 2.8f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                drawCircle(
                    color = RouteGlow.copy(alpha = 0.34f + (sweepProgress * 0.36f)),
                    radius = 2.2f + (sweepProgress * 1.4f),
                    center = head
                )
            }
        }
        Image(
            painter = routePainter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.84f)
        )
    }
}
