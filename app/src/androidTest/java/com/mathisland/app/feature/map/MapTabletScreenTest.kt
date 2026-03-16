package com.mathisland.app.feature.map

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.mathisland.app.MathIslandTheme
import org.junit.Rule
import org.junit.Test

class MapTabletScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun mapScreen_showsIslandProgressAndLessonEntry() {
        composeRule.setContent {
            MathIslandTheme {
                MapTabletScreen(
                    state = MapTabletUiState(
                        totalStars = 3,
                        recommendedIslandId = "calculation-island",
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
                                        summary = "帮小海狸把桥板放回去，完成 3 道口算。",
                                        completed = false,
                                        enabled = true
                                    )
                                )
                            )
                        )
                    ),
                    onBackHome = {},
                    onOpenChest = {},
                    onStartLesson = {}
                )
            }
        }

        composeRule.onNodeWithText("总星星 3").assertIsDisplayed()
        composeRule.onNodeWithTag("select-island-calculation-island").assertIsDisplayed()
        composeRule.onNodeWithTag("panel-island-title").assertIsDisplayed()
        composeRule.onNodeWithTag("panel-primary-action").assertIsDisplayed()
    }

    @Test
    fun mapScreen_clickingIslandCardUpdatesDetailPanel() {
        composeRule.setContent {
            MathIslandTheme {
                MapTabletScreen(
                    state = MapTabletUiState(
                        totalStars = 3,
                        recommendedIslandId = "calculation-island",
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
                            ),
                            MapTabletIslandUiState(
                                id = "measurement-island",
                                title = "测量与图形岛",
                                subtitle = "长度与图形",
                                description = "米和厘米",
                                unlocked = true,
                                completed = false,
                                progress = 0.2f,
                                lessons = listOf(
                                    MapTabletLessonUiState(
                                        id = "measure-ruler",
                                        title = "尺子工坊",
                                        summary = "summary",
                                        completed = false,
                                        enabled = true
                                    )
                                )
                            )
                        )
                    ),
                    onBackHome = {},
                    onOpenChest = {},
                    onStartLesson = {}
                )
            }
        }

        composeRule.onNodeWithTag("select-island-calculation-island").assertIsDisplayed()
        composeRule.onNodeWithTag("select-island-measurement-island").performClick()
        composeRule.onNodeWithTag("panel-island-title").assertIsDisplayed()
        composeRule.onNodeWithTag("panel-island-title").assertTextEquals("测量与图形岛")
    }
}
