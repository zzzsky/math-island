package com.mathisland.app.feature.island

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import com.mathisland.app.MathIslandTheme
import com.mathisland.app.feature.map.MapTabletIslandUiState
import com.mathisland.app.feature.map.MapTabletLessonUiState
import org.junit.Rule
import org.junit.Test

class IslandOverlaySheetTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun overlay_showsPrimaryActionAndLessonStatuses() {
        composeRule.setContent {
            MathIslandTheme {
                IslandOverlaySheet(
                    state = IslandUiState(
                        island = MapTabletIslandUiState(
                            id = "measurement-island",
                            title = "测量与图形岛",
                            subtitle = "长度与图形",
                            description = "米和厘米",
                            unlocked = true,
                            completed = false,
                            progress = 0.5f,
                            lessons = listOf(
                                MapTabletLessonUiState(
                                    id = "measure-ruler-01",
                                    title = "尺子工坊",
                                    summary = "用尺子量一量",
                                    completed = false,
                                    enabled = true
                                ),
                                MapTabletLessonUiState(
                                    id = "geometry-shape-01",
                                    title = "图形搭搭乐",
                                    summary = "认认角和边",
                                    completed = true,
                                    enabled = true
                                )
                            )
                        ),
                        primaryLessonId = "measure-ruler-01",
                        primaryActionLabel = "继续 尺子工坊"
                    ),
                    onStartLesson = {}
                )
            }
        }

        composeRule.onNodeWithTag("island-overlay-sheet").assertIsDisplayed()
        composeRule.onNodeWithText("测量与图形岛").assertIsDisplayed()
        composeRule.onAllNodesWithTag("island-primary-action").assertCountEquals(1)
        composeRule.onNodeWithText("尺子工坊").assertIsDisplayed()
        composeRule.onNodeWithTag("panel-lessons-list")
            .performScrollToNode(hasTestTag("panel-start-geometry-shape-01"))
        composeRule.onNodeWithText("图形搭搭乐").assertIsDisplayed()
        composeRule.onAllNodesWithTag("panel-start-geometry-shape-01").assertCountEquals(1)
    }
}
