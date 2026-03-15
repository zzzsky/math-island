package com.mathisland.app.ui.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

enum class WindowProfile {
    TABLET_LANDSCAPE,
    COMPACT
}

@Composable
fun rememberWindowProfile(): WindowProfile {
    val configuration = LocalConfiguration.current
    return remember(configuration.screenWidthDp, configuration.screenHeightDp) {
        if (configuration.screenWidthDp >= 1000 && configuration.screenWidthDp > configuration.screenHeightDp) {
            WindowProfile.TABLET_LANDSCAPE
        } else {
            WindowProfile.COMPACT
        }
    }
}
