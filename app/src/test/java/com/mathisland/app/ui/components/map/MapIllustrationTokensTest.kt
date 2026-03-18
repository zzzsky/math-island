package com.mathisland.app.ui.components.map

import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntSize
import org.junit.Assert.assertEquals
import org.junit.Test

class MapIllustrationTokensTest {
    @Test
    fun normalizeIslandArtKey_usesCurriculumIslandIdWithoutAliases() {
        assertEquals(
            "measurement_geometry_island",
            normalizeIslandArtKey("measurement-geometry-island")
        )
    }

    @Test
    fun overlayOrder_matchesApprovedSpec() {
        assertEquals(
            listOf("base", "state", "focus", "badge", "label"),
            MapIllustrationTokens.OverlayOrder
        )
    }

    @Test
    fun illustrationContracts_matchApprovedSizesAndAnchors() {
        assertEquals(IntSize(1920, 1200), MapIllustrationTokens.SeaBackdropSpecSize)
        assertEquals(IntSize(640, 480), MapIllustrationTokens.IslandBaseSpecSize)
        assertEquals(IntSize(256, 256), MapIllustrationTokens.StateOverlaySpecSize)
        assertEquals(512, MapIllustrationTokens.RouteSegmentLongEdge)
        assertEquals(IntSize(96, 96), MapIllustrationTokens.IslandIconSpecSize)
        assertEquals(ContentScale.Fit, MapIllustrationTokens.ArtContentScale)
        assertEquals(Alignment.Center, MapIllustrationTokens.IslandArtAlignment)
        assertEquals(Alignment.TopEnd, MapIllustrationTokens.BadgeAlignment)
    }
}
