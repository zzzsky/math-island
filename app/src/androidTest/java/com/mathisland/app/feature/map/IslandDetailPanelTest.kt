package com.mathisland.app.feature.map

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mathisland.app.MathIslandTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class IslandDetailPanelTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun panel_showsIslandSummaryAndStartsRecommendedLesson() {
        var startedLessonId: String? = null

        composeRule.setContent {
            MathIslandTheme {
                IslandDetailPanel(
                    island = MapTabletIslandUiState(
                        id = "measurement-island",
                        title = "测量与图形岛",
                        subtitle = "长度与图形",
                        description = "米和厘米的尺子工坊",
                        unlocked = true,
                        completed = false,
                        progress = 0.5f,
                        lessons = listOf(
                            MapTabletLessonUiState(
                                id = "measure-ruler-01",
                                title = "尺子工坊",
                                summary = "学习米和厘米。",
                                completed = false,
                                enabled = true
                            ),
                            MapTabletLessonUiState(
                                id = "geometry-shape-01",
                                title = "拼图山谷",
                                summary = "认识图形。",
                                completed = true,
                                enabled = true
                            )
                        )
                    ),
                    onStartLesson = { lessonId -> startedLessonId = lessonId }
                )
            }
        }

        composeRule.onNodeWithTag("panel-island-title").assertIsDisplayed()
        composeRule.onNodeWithText("测量与图形岛").assertIsDisplayed()
        composeRule.onNodeWithTag("panel-primary-action").performClick()

        assertEquals("measure-ruler-01", startedLessonId)
    }
}
