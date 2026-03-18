package com.mathisland.app.ui.components.map

import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntSize

internal object MapIllustrationTokens {
    val OverlayOrder = listOf("base", "state", "focus", "badge", "label")

    val SeaBackdropSpecSize = IntSize(1920, 1200)
    val IslandBaseSpecSize = IntSize(640, 480)
    val StateOverlaySpecSize = IntSize(256, 256)
    val RouteSegmentLongEdge = 512
    val IslandIconSpecSize = IntSize(96, 96)

    val ArtContentScale = ContentScale.Fit
    val IslandArtAlignment = Alignment.Center
    val BadgeAlignment = Alignment.TopEnd

    const val SeaBackdropKey = "map_sea_backdrop"
    const val RouteDefaultKey = "route_segment_default"
    const val RouteHighlightKey = "route_segment_highlight"
    const val IslandLockedOverlayKey = "island_locked_overlay"
    const val IslandUnlockedTintKey = "island_unlocked_tint"
    const val IslandFocusedRingKey = "island_focused_ring"
    const val IslandCompletedBadgeKey = "island_completed_badge"
    const val CloudPrefix = "map_cloud_"
    const val WavePrefix = "map_wave_"
    const val SparkPrefix = "map_spark_"
}

internal fun normalizeIslandArtKey(islandId: String): String =
    islandId.lowercase().replace('-', '_')

internal fun seaBackdropSlotKey(): String = MapIllustrationTokens.SeaBackdropKey

internal fun routeSegmentSlotKey(highlighted: Boolean): String =
    if (highlighted) MapIllustrationTokens.RouteHighlightKey else MapIllustrationTokens.RouteDefaultKey

internal fun islandBaseSlotKey(islandId: String): String =
    "island_${normalizeIslandArtKey(islandId)}_base"

internal fun islandIconSlotKey(islandId: String): String =
    "island_${normalizeIslandArtKey(islandId)}_icon"

internal fun islandLockedOverlaySlotKey(): String = MapIllustrationTokens.IslandLockedOverlayKey

internal fun islandUnlockedTintSlotKey(): String = MapIllustrationTokens.IslandUnlockedTintKey

internal fun islandFocusedRingSlotKey(): String = MapIllustrationTokens.IslandFocusedRingKey

internal fun islandCompletedBadgeSlotKey(): String = MapIllustrationTokens.IslandCompletedBadgeKey

internal fun decorationSlotPrefixes(): List<String> = listOf(
    MapIllustrationTokens.CloudPrefix,
    MapIllustrationTokens.WavePrefix,
    MapIllustrationTokens.SparkPrefix
)
