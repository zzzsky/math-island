package com.mathisland.app.ui.components.map

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

private val RouteGlow = Color(0x66F4D58D)
private val FocusRing = Color(0xFFF4D58D)

@Composable
fun RoutePainter(
    islandId: String,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
    artSource: MapArtSource = MapArtRegistry
) {
    val routeArt = MapArtRegistry.resolveRouteArt(highlighted, artSource)
    val routeAlpha by animateFloatAsState(
        targetValue = if (highlighted) 1f else 0.5f,
        animationSpec = tween(durationMillis = 600),
        label = "map-route-alpha-$islandId"
    )
    Box(
        modifier = modifier
            .height(6.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(999.dp))
            .background(FocusRing.copy(alpha = routeAlpha).compositeOver(RouteGlow))
    ) {
        Image(
            painter = routeArt.painter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}
