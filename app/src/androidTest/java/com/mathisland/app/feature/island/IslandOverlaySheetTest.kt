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

    @Test
    fun overlay_showsHandoffDetailCardWhenProvided() {
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
                                )
                            )
                        ),
                        handoffKind = com.mathisland.app.feature.map.MapFeedbackKind.NewIsland,
                        handoffLabel = "主线继续",
                        handoffTitle = "新主线已就位",
                        handoffBody = "地图已经切到新岛，右侧会直接显示推荐课。",
                        handoffDetailLabel = "回地图后",
                        handoffDetailTitle = "先看新岛，再开始主线课",
                        handoffDetailBody = "焦点会落到新岛，右侧面板会直接给出下一节主线课。",
                        handoffActionLabel = "优先动作",
                        handoffActionTitle = "开始主线课",
                        handoffActionBody = "先看新岛，再直接开始当前推荐的主线课程。",
                        primaryLessonId = "measure-ruler-01",
                        primaryActionLabel = "开始主线 尺子工坊"
                    ),
                    onStartLesson = {}
                )
            }
        }

        composeRule.onNodeWithTag("island-handoff-card").assertIsDisplayed()
        composeRule.onNodeWithTag("island-handoff-kind-pill").assertIsDisplayed()
        composeRule.onNodeWithTag("island-handoff-detail-card").assertIsDisplayed()
        composeRule.waitUntil(5_000) {
            runCatching {
                composeRule.onNodeWithTag("island-handoff-action-card").assertIsDisplayed()
                true
            }.getOrDefault(false)
        }
        composeRule.waitUntil(5_000) {
            runCatching {
                composeRule.onAllNodesWithTag("island-handoff-action-pill")
                    .fetchSemanticsNodes().size == 1
            }.getOrDefault(false)
        }
        composeRule.onNodeWithTag("island-handoff-action-card").assertIsDisplayed()
        composeRule.onAllNodesWithTag("island-handoff-action-pill").assertCountEquals(1)
    }
}
