package com.mathisland.app.feature.chest

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mathisland.app.MathIslandTheme
import org.junit.Rule
import org.junit.Test

class ChestTabletScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun chestScreen_showsUnlockedStickerAndSummary() {
        composeRule.setContent {
            MathIslandTheme {
                ChestTabletScreen(
                    state = ChestViewModel.uiState(
                        stickers = listOf("灯塔徽章", "Bridge Builder"),
                        totalStars = 9
                    ),
                    onBackHome = {},
                    onOpenMap = {}
                )
            }
        }

        composeRule.onNodeWithText("宝箱收藏").assertIsDisplayed()
        composeRule.onNodeWithText("灯塔徽章").assertIsDisplayed()
        composeRule.onNodeWithText("累计星星 9 · 收集到 2 张岛屿贴纸").assertIsDisplayed()
    }
}
