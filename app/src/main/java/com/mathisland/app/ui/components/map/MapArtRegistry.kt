package com.mathisland.app.ui.components.map

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import com.mathisland.app.R

object MapArtRegistry : MapArtSource {
    val defaultSourcePriority = listOf(SourceType.Vector, SourceType.WebP, SourceType.Png)

    fun resolveSeaBackdrop(source: MapArtSource = this): SeaArtSpec =
        SeaArtSpec(
            slot = slotSpec(
                key = seaBackdropSlotKey(),
                size = MapIllustrationTokens.SeaBackdropSpecSize
            ),
            painter = resolvePainter(source, slotSpec(seaBackdropSlotKey(), MapIllustrationTokens.SeaBackdropSpecSize))
        )

    fun resolveRouteArt(highlighted: Boolean, source: MapArtSource = this): RouteArtSpec {
        val slotKey = routeSegmentSlotKey(highlighted)
        return RouteArtSpec(
            slot = slotSpec(
                key = slotKey,
                size = null
            ),
            highlighted = highlighted,
            painter = resolvePainter(source, slotSpec(slotKey, null))
        )
    }

    fun resolveIslandArt(islandId: String, source: MapArtSource = this): IslandArtSpec {
        val normalizedKey = normalizeIslandArtKey(islandId)
        val baseSlot = slotSpec(islandBaseSlotKey(islandId), MapIllustrationTokens.IslandBaseSpecSize)
        val iconSlot = slotSpec(islandIconSlotKey(islandId), MapIllustrationTokens.IslandIconSpecSize)
        val lockedOverlaySlot = slotSpec(islandLockedOverlaySlotKey(), MapIllustrationTokens.StateOverlaySpecSize)
        val unlockedTintSlot = slotSpec(islandUnlockedTintSlotKey(), MapIllustrationTokens.StateOverlaySpecSize)
        val focusedRingSlot = slotSpec(islandFocusedRingSlotKey(), MapIllustrationTokens.StateOverlaySpecSize)
        val completedBadgeSlot = slotSpec(
            islandCompletedBadgeSlotKey(),
            MapIllustrationTokens.StateOverlaySpecSize,
            alignment = MapIllustrationTokens.BadgeAlignment
        )

        return IslandArtSpec(
            normalizedKey = normalizedKey,
            baseSlot = baseSlot,
            iconSlot = iconSlot,
            lockedOverlaySlot = lockedOverlaySlot,
            unlockedTintSlot = unlockedTintSlot,
            focusedRingSlot = focusedRingSlot,
            completedBadgeSlot = completedBadgeSlot,
            decorationSlots = decorationSlots(),
            baseArt = resolvePainter(source, baseSlot),
            iconArt = resolvePainter(source, iconSlot),
            lockedOverlay = resolvePainter(source, lockedOverlaySlot),
            unlockedTint = resolvePainter(source, unlockedTintSlot),
            focusedRing = resolvePainter(source, focusedRingSlot),
            completedBadge = resolvePainter(source, completedBadgeSlot)
        )
    }

    override fun resolve(slot: ArtSlotSpec): Painter? = fallbackPainter(slot.key)

    override fun decorationSlots(): List<String> =
        decorationSlotPrefixes()

    private fun slotSpec(
        key: String,
        size: androidx.compose.ui.unit.IntSize?,
        alignment: androidx.compose.ui.Alignment = MapIllustrationTokens.IslandArtAlignment
    ) = ArtSlotSpec(
        key = key,
        sourcePriority = defaultSourcePriority,
        size = size,
        alignment = alignment,
        contentScale = MapIllustrationTokens.ArtContentScale,
        drawableResId = drawableResIdOrNull(key)
    )

    private fun resolvePainter(source: MapArtSource, slot: ArtSlotSpec): Painter =
        source.resolve(slot) ?: fallbackPainter(slot.key)

    private fun fallbackPainter(key: String): Painter =
        ColorPainter(colorFor(key))

    private fun colorFor(key: String): Color {
        val hash = key.hashCode()
        val red = 120 + (hash and 0x3F)
        val green = 120 + ((hash shr 6) and 0x3F)
        val blue = 120 + ((hash shr 12) and 0x3F)
        return Color(red, green, blue, 255)
    }

    @DrawableRes
    private fun drawableResIdOrNull(key: String): Int? =
        runCatching {
            R.drawable::class.java.getField(key).getInt(null)
        }.getOrNull()?.takeIf { it != 0 }
}
