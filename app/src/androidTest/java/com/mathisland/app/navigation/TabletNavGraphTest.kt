package com.mathisland.app.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.mathisland.app.MathIslandTabletApp
import org.junit.Rule
import org.junit.Test

class TabletNavGraphTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun appStarts_withTabletStoryShell() {
        composeRule.setContent {
            MathIslandTabletApp()
        }

        composeRule.onNodeWithContentDescription("tablet-world-shell").assertIsDisplayed()
    }
}
