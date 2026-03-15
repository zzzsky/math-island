package com.mathisland.app.feature.map

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
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
                        islands = listOf(
                            MapTabletIslandUiState(
                                id = "calculation-island",
                                title = "计算岛",
                                subtitle = "加减法",
                                description = "口算与估算",
                                unlocked = true,
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
        composeRule.onNodeWithText("计算岛").assertIsDisplayed()
        composeRule.onNodeWithText("加减法 · 口算与估算").assertIsDisplayed()
        composeRule.onNodeWithTag("start-calc-bridge").assertIsDisplayed()
    }
}
