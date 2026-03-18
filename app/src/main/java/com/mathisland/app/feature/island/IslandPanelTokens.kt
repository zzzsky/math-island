package com.mathisland.app.feature.island

import androidx.compose.ui.graphics.Color
import com.mathisland.app.ui.components.map.MapIllustrationTokens
import com.mathisland.app.ui.theme.TabletDeepWater

object IslandPanelTokens {
    val OverlaySurface = Color(0xCC173C4C)
    val HeaderSurface = Color(0x662C647A)
    val StorySurface = Color(0x52203E4C)
    val LessonSurface = Color(0x66203E4C)
    val LessonCompletedSurface = Color(0x7A355E42)
    val LessonRecommendedSurface = Color(0x7A2B5D69)
    val HeaderBorder = Color(MapIllustrationTokens.RouteHighlight).copy(alpha = 0.46f)
    val StoryBorder = Color.White.copy(alpha = 0.08f)
    val LessonBorder = Color.White.copy(alpha = 0.1f)
    val RecommendedBorder = Color(MapIllustrationTokens.RouteHighlight).copy(alpha = 0.52f)
    val CompletedBorder = Color(0xFF7EB37C).copy(alpha = 0.58f)
    val IconBadge = Color(MapIllustrationTokens.RoutePaper).copy(alpha = 0.95f)
    val ProgressTrack = Color.White.copy(alpha = 0.12f)
    val ProgressFill = Color(MapIllustrationTokens.RouteHighlight)
    val DescriptionText = Color.White.copy(alpha = 0.84f)
    val SummaryText = Color.White.copy(alpha = 0.76f)
    val RecommendedButton = Color(MapIllustrationTokens.RouteHighlight)
    val CompletedButton = Color(0xFF4B6F44)
    val DefaultButton = Color(0xFF88C8D8)
    val ButtonContent = TabletDeepWater
}
