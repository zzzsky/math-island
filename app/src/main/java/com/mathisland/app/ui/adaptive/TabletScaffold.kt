package com.mathisland.app.ui.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@Composable
fun TabletScaffold(
    windowProfile: WindowProfile,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                contentDescription = "tablet-world-shell"
            }
    ) {
        when (windowProfile) {
            WindowProfile.TABLET_LANDSCAPE,
            WindowProfile.COMPACT -> content()
        }
    }
}
