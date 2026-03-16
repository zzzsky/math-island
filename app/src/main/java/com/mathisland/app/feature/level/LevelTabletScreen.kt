package com.mathisland.app.feature.level

import androidx.compose.runtime.Composable

@Composable
fun LevelTabletScreen(
    state: LevelUiState,
    onQuit: () -> Unit,
    answerPane: @Composable () -> Unit,
    onExpire: (() -> Unit)? = null
) {
    com.mathisland.app.feature.lesson.LessonTabletScreen(
        state = state,
        onQuit = onQuit,
        answerPane = answerPane,
        onExpire = onExpire
    )
}
