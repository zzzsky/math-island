package com.mathisland.app.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.feature.map.MapTabletIslandUiState
import com.mathisland.app.feature.map.MapTabletLessonUiState
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
                    onSelectIsland = { selected = it }
                )
            }
        }

        composeRule.onNodeWithTag("map-scene-canvas").assertIsDisplayed()
        composeRule.onNodeWithTag("map-node-calculation-island").performClick()
        assertEquals("calculation-island", selected)
    }
}
