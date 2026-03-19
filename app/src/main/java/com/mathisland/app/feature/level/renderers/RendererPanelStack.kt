package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
internal fun RendererPanelStack(
    rendererTag: String,
    context: (@Composable () -> Unit)? = null,
    feedback: AnswerFeedbackUiState? = null,
    affordance: (@Composable () -> Unit)? = null,
    actions: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(rendererTag),
        verticalArrangement = Arrangement.spacedBy(RendererTokens.SectionGap)
    ) {
        context?.invoke()
        if (feedback != null) {
            AnswerFeedbackBanner(state = feedback)
        }
        affordance?.invoke()
        actions()
    }
}
