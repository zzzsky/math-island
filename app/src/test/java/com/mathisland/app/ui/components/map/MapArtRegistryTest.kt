package com.mathisland.app.ui.components.map

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class MapArtRegistryTest {
    @Test
    fun resolveIslandArt_usesCurriculumIslandIdAndFallbacks() {
        val basePainter = TestPainter("measurement-base")
        val spec = MapArtRegistry.resolveIslandArt(
            "measurement-geometry-island",
            source = FakeMapArtSource(
                painters = mapOf(
                    islandBaseSlotKey("measurement-geometry-island") to basePainter
                )
            )
        )
        assertEquals("measurement_geometry_island", spec.normalizedKey)
        assertSame(basePainter, spec.baseArt)
    }

    @Test
    fun resolveDecorationSlots_exposesCloudWaveAndSparkKeys() {
        val slots = MapArtRegistry.decorationSlots()
        assertTrue(slots.any { it.startsWith("map_cloud_") })
        assertTrue(slots.any { it.startsWith("map_wave_") })
        assertTrue(slots.any { it.startsWith("map_spark_") })
    }

    @Test
    fun resolveIslandArt_prefersVectorThenWebpThenPng() {
        assertEquals(
            listOf(SourceType.Vector, SourceType.WebP, SourceType.Png),
            MapArtRegistry.defaultSourcePriority
        )
    }

    @Test
    fun resolveSharedArt_exposesSeaBackdropAndRouteKeys() {
        assertEquals("map_sea_backdrop", MapArtRegistry.resolveSeaBackdrop().key)
        assertEquals("route_segment_default", MapArtRegistry.resolveRouteArt(highlighted = false).key)
        assertEquals("route_segment_highlight", MapArtRegistry.resolveRouteArt(highlighted = true).key)
    }

    @Test
    fun resolveIslandArt_singleMissingAssetStillReturnsTestableFallbacks() {
        val presentIcon = TestPainter("measurement-icon")
        val fakeSource = FakeMapArtSource(
            painters = mapOf(
                islandIconSlotKey("measurement-geometry-island") to presentIcon
            ),
            missingSlots = setOf(islandBaseSlotKey("measurement-geometry-island"))
        )

        val spec = MapArtRegistry.resolveIslandArt(
            "measurement-geometry-island",
            source = fakeSource
        )
        assertNotSame(presentIcon, spec.baseArt)
        assertSame(presentIcon, spec.iconArt)
    }
}

private class FakeMapArtSource(
    private val painters: Map<String, Painter>,
    private val missingSlots: Set<String> = emptySet()
) : MapArtSource {
    override fun resolve(slot: ArtSlotSpec): Painter? =
        if (missingSlots.contains(slot.key)) null else painters[slot.key]

    override fun decorationSlots(): List<String> = decorationSlotPrefixes()
}

private class TestPainter(
    private val label: String
) : Painter() {
    override val intrinsicSize: Size = Size.Unspecified

    override fun DrawScope.onDraw() {
        drawRect(Color.Magenta)
    }

    override fun toString(): String = label
}
