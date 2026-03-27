package com.mathisland.app.feature.level.renderers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
internal fun RendererPanelStack(
    rendererTag: String,
    prompt: (@Composable () -> Unit)? = null,
    context: (@Composable () -> Unit)? = null,
    feedback: AnswerFeedbackUiState? = null,
    affordance: (@Composable () -> Unit)? = null,
    actions: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .testTag(rendererTag),
        verticalArrangement = Arrangement.spacedBy(RendererTokens.SectionGap)
    ) {
        prompt?.invoke()
        context?.invoke()
        if (feedback != null) {
            AnswerFeedbackBanner(state = feedback)
        }
        affordance?.invoke()
        actions()
    }
}
