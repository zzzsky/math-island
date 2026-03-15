package com.mathisland.app

import androidx.compose.runtime.Composable
import com.mathisland.app.navigation.TabletNavGraph
import com.mathisland.app.ui.adaptive.TabletScaffold
import com.mathisland.app.ui.adaptive.rememberWindowProfile

@Composable
fun MathIslandTabletApp() {
    MathIslandTheme {
        TabletScaffold(windowProfile = rememberWindowProfile()) {
            TabletNavGraph()
        }
    }
}
