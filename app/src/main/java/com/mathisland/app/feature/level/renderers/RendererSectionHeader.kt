package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
internal fun RendererSectionHeader(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.testTag("renderer-action-header")
    ) {
        RendererGuidanceCard(
            title = title,
            body = body
        )
    }
}
