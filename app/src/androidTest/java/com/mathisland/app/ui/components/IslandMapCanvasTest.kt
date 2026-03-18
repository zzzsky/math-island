package com.mathisland.app.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.feature.map.MapTabletIslandUiState
import com.mathisland.app.feature.map.MapTabletLessonUiState
import com.mathisland.app.ui.components.map.ArtSlotSpec
import com.mathisland.app.ui.components.map.MapArtRegistry
import com.mathisland.app.ui.components.map.MapArtSource
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class IslandMapCanvasTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun islandMapCanvas_preservesExistingNodeContracts() {
        var selected: String? = null

        composeRule.setContent {
            MathIslandTheme {
                IslandMapCanvas(
                    islands = listOf(
                        MapTabletIslandUiState(
                            id = "calculation-island",
                            title = "计算岛",
                            subtitle = "加减法",
                            description = "口算与估算",
                            unlocked = true,
                            completed = false,
                            progress = 0.5f,
                            lessons = listOf(
                                MapTabletLessonUiState(
                                    id = "calc-bridge",
                                    title = "修桥加减法",
                                    summary = "summary",
                                    completed = false,
                                    enabled = true
                                )
                            )
                        )
                    ),
                    selectedIslandId = "calculation-island",
                    onSelectIsland = { selected = it },
                    artSource = FakeFallbackMapArtSource()
                )
            }
        }

        composeRule.onNodeWithTag("map-scene-canvas").assertIsDisplayed()
        composeRule.onNodeWithTag("map-node-calculation-island").performClick()
        assertEquals("calculation-island", selected)
    }

    @Test
    fun islandMapCanvas_highlightsRouteWhenSpecified() {
        composeRule.setContent {
            MathIslandTheme {
                IslandMapCanvas(
                    islands = listOf(
                        MapTabletIslandUiState(
                            id = "calculation-island",
                            title = "计算岛",
                            subtitle = "加减法",
                            description = "口算与估算",
                            unlocked = true,
                            completed = false,
                            progress = 0.5f,
                            lessons = emptyList()
                        ),
                        MapTabletIslandUiState(
                            id = "measurement-geometry-island",
                            title = "测量与图形岛",
                            subtitle = "尺子",
                            description = "长度",
                            unlocked = true,
                            completed = false,
                            progress = 0.2f,
                            lessons = emptyList()
                        )
                    ),
                    selectedIslandId = "calculation-island",
                    highlightedIslandId = "measurement-geometry-island",
                    onSelectIsland = {},
                    artSource = FakeFallbackMapArtSource()
                )
            }
        }

        composeRule.onNodeWithTag("map-route-highlight-measurement-geometry-island").assertIsDisplayed()
        composeRule.onNodeWithTag("map-node-highlight-measurement-geometry-island").assertIsDisplayed()
    }

    @Test
    fun islandMapCanvas_keepsNodeBoundsWhenHighlightToggles() {
        var highlightedIslandId by mutableStateOf<String?>(null)

        composeRule.setContent {
            MathIslandTheme {
                IslandMapCanvas(
                    islands = sampleIslands(),
                    selectedIslandId = "calculation-island",
                    highlightedIslandId = highlightedIslandId,
                    onSelectIsland = {},
                    artSource = FakeFallbackMapArtSource()
                )
            }
        }
        val idleBounds = composeRule
            .onNodeWithTag("map-node-measurement-geometry-island")
            .fetchSemanticsNode()
            .boundsInRoot

        composeRule.runOnIdle {
            highlightedIslandId = "measurement-geometry-island"
        }
        val highlightedBounds = composeRule
            .onNodeWithTag("map-node-measurement-geometry-island")
            .fetchSemanticsNode()
            .boundsInRoot

        assertEquals(idleBounds, highlightedBounds)
    }

    @Test
    fun islandMapCanvas_rendersSameNodeTagsWithFallbackAndAssetBackedRegistries() {
        var artSource: MapArtSource by mutableStateOf(FakeFallbackMapArtSource())

        composeRule.setContent {
            MathIslandTheme {
                IslandMapCanvas(
                    islands = sampleIslands(),
                    selectedIslandId = "calculation-island",
                    onSelectIsland = {},
                    artSource = artSource
                )
            }
        }
        composeRule.onNodeWithTag("map-node-calculation-island").assertIsDisplayed()
        composeRule.onNodeWithTag("map-node-measurement-geometry-island").assertIsDisplayed()

        composeRule.runOnIdle {
            artSource = FakeAssetBackedMapArtSource()
        }
        composeRule.onNodeWithTag("map-node-calculation-island").assertIsDisplayed()
        composeRule.onNodeWithTag("map-node-measurement-geometry-island").assertIsDisplayed()
    }

    @Test
    fun islandMapCanvas_clickingIslandStillCallsSelectionHandler() {
        var selected: String? = null
        composeRule.setContent {
            MathIslandTheme {
                IslandMapCanvas(
                    islands = sampleIslands(),
                    selectedIslandId = "calculation-island",
                    onSelectIsland = { selected = it },
                    artSource = FakeFallbackMapArtSource()
                )
            }
        }
        composeRule.onNodeWithTag("map-node-measurement-geometry-island").performClick()
        assertEquals("measurement-geometry-island", selected)
    }

    @Test
    fun islandMapCanvas_keepsHighlightContainerTagsWithFallbackArt() {
        composeRule.setContent {
            MathIslandTheme {
                IslandMapCanvas(
                    islands = sampleIslands(),
                    selectedIslandId = "calculation-island",
                    highlightedIslandId = "measurement-geometry-island",
                    onSelectIsland = {},
                    artSource = FakeFallbackMapArtSource()
                )
            }
        }
        composeRule.onNodeWithTag("map-node-highlight-measurement-geometry-island").assertIsDisplayed()
        composeRule.onNodeWithTag("map-route-highlight-measurement-geometry-island").assertIsDisplayed()
    }

    @Test
    fun islandMapCanvas_keepsHighlightContainerTagsWithAssetBackedArt() {
        composeRule.setContent {
            MathIslandTheme {
                IslandMapCanvas(
                    islands = sampleIslands(),
                    selectedIslandId = "calculation-island",
                    highlightedIslandId = "measurement-geometry-island",
                    onSelectIsland = {},
                    artSource = FakeAssetBackedMapArtSource()
                )
            }
        }
        composeRule.onNodeWithTag("map-node-highlight-measurement-geometry-island").assertIsDisplayed()
        composeRule.onNodeWithTag("map-route-highlight-measurement-geometry-island").assertIsDisplayed()
    }

    @Test
    fun islandMapCanvas_keepsNodeBoundsWithSingleMissingAssetFallback() {
        composeRule.setContent {
            MathIslandTheme {
                IslandMapCanvas(
                    islands = sampleIslands(),
                    selectedIslandId = "calculation-island",
                    onSelectIsland = {},
                    artSource = FakeMissingBaseAssetMapArtSource()
                )
            }
        }
        composeRule.onNodeWithTag("map-node-measurement-geometry-island").assertIsDisplayed()
        composeRule.onNodeWithTag("map-node-measurement-geometry-island").performClick()
    }

    @Test
    fun islandMapCanvas_rendersWithProductionDrawableRegistry() {
        composeRule.setContent {
            MathIslandTheme {
                IslandMapCanvas(
                    islands = sampleIslands(),
                    selectedIslandId = "calculation-island",
                    highlightedIslandId = "measurement-geometry-island",
                    onSelectIsland = {},
                    artSource = MapArtRegistry
                )
            }
        }

        composeRule.onNodeWithTag("map-node-calculation-island").assertIsDisplayed()
        composeRule.onNodeWithTag("map-node-highlight-measurement-geometry-island").assertIsDisplayed()
        composeRule.onNodeWithTag("map-route-highlight-measurement-geometry-island").assertIsDisplayed()
    }

    private fun sampleIslands() = listOf(
        MapTabletIslandUiState(
            id = "calculation-island",
            title = "计算岛",
            subtitle = "加减法",
            description = "口算与估算",
            unlocked = true,
            completed = false,
            progress = 0.5f,
            lessons = listOf(
                MapTabletLessonUiState(
                    id = "calc-bridge",
                    title = "修桥加减法",
                    summary = "summary",
                    completed = false,
                    enabled = true
                )
            )
        ),
        MapTabletIslandUiState(
            id = "measurement-geometry-island",
            title = "测量与图形岛",
            subtitle = "尺子",
            description = "长度",
            unlocked = true,
            completed = false,
            progress = 0.2f,
            lessons = emptyList()
        )
    )
}

private class FakeFallbackMapArtSource : MapArtSource {
    override fun resolve(slot: ArtSlotSpec): Painter? = null

    override fun decorationSlots(): List<String> = emptyList()
}

private class FakeAssetBackedMapArtSource : MapArtSource {
    override fun resolve(slot: ArtSlotSpec): Painter? = TestPainter(slot.key)

    override fun decorationSlots(): List<String> = listOf("map_cloud_01", "map_wave_01", "map_spark_01")
}

private class FakeMissingBaseAssetMapArtSource : MapArtSource {
    override fun resolve(slot: ArtSlotSpec): Painter? =
        if (slot.key == "island_measurement_geometry_island_base") null else TestPainter(slot.key)

    override fun decorationSlots(): List<String> = emptyList()
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
