package com.mathisland.app.ui.components.map

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntSize

enum class SourceType {
    Vector,
    WebP,
    Png
}

data class ArtSlotSpec(
    val key: String,
    val sourcePriority: List<SourceType>,
    val size: IntSize? = null,
    val alignment: Alignment = MapIllustrationTokens.IslandArtAlignment,
    val contentScale: ContentScale = MapIllustrationTokens.ArtContentScale,
    val drawableResId: Int? = null
)

data class SeaArtSpec(
    val slot: ArtSlotSpec,
    val painter: Painter
) {
    val key: String get() = slot.key
}

data class RouteArtSpec(
    val slot: ArtSlotSpec,
    val highlighted: Boolean,
    val painter: Painter
) {
    val key: String get() = slot.key
}

data class IslandArtSpec(
    val normalizedKey: String,
    val baseSlot: ArtSlotSpec,
    val iconSlot: ArtSlotSpec,
    val lockedOverlaySlot: ArtSlotSpec,
    val unlockedTintSlot: ArtSlotSpec,
    val focusedRingSlot: ArtSlotSpec,
    val completedBadgeSlot: ArtSlotSpec,
    val decorationSlots: List<String>,
    val baseArt: Painter,
    val iconArt: Painter,
    val lockedOverlay: Painter,
    val unlockedTint: Painter,
    val focusedRing: Painter,
    val completedBadge: Painter,
)

interface MapArtSource {
    fun resolve(slot: ArtSlotSpec): Painter?
    fun decorationSlots(): List<String>
}
