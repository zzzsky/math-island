package com.mathisland.app.feature.chest

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
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

        composeRule.onNodeWithTag("chest-screen").assertIsDisplayed()
        composeRule.onNodeWithTag("chest-header-panel").assertIsDisplayed()
        composeRule.onNodeWithTag("chest-sticker-grid").assertIsDisplayed()
        composeRule.onNodeWithTag("chest-sticker-card-0").assertIsDisplayed()
        composeRule.onNodeWithText("宝箱收藏").assertIsDisplayed()
        composeRule.onNodeWithText("灯塔徽章").assertIsDisplayed()
        composeRule.onNodeWithText("累计星星 9 · 收集到 2 张岛屿贴纸").assertIsDisplayed()
        composeRule.onNodeWithTag("chest-open-map").assertIsDisplayed()
    }

    @Test
    fun chestScreen_showsEmptyStateWhenNoStickersAreUnlocked() {
        composeRule.setContent {
            MathIslandTheme {
                ChestTabletScreen(
                    state = ChestViewModel.uiState(
                        stickers = emptyList(),
                        totalStars = 0
                    ),
                    onBackHome = {},
                    onOpenMap = {}
                )
            }
        }

        composeRule.onNodeWithTag("chest-screen").assertIsDisplayed()
        composeRule.onNodeWithTag("chest-header-panel").assertIsDisplayed()
        composeRule.onNodeWithTag("chest-empty-state-card").assertIsDisplayed()
        composeRule.onNodeWithText("宝箱收藏").assertIsDisplayed()
        composeRule.onNodeWithText("还没有贴纸").assertIsDisplayed()
        composeRule.onNodeWithText("先去完成一整座主岛").assertIsDisplayed()
        composeRule.onNodeWithText("每清空一座岛，就会在这里点亮一张新的纪念贴纸。").assertIsDisplayed()
        composeRule.onNodeWithTag("chest-open-map").assertIsDisplayed()
    }
}
