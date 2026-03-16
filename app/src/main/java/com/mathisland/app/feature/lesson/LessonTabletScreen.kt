package com.mathisland.app.feature.lesson

import androidx.compose.runtime.Composable
import com.mathisland.app.feature.level.LevelTabletScreen

@Deprecated(
    message = "Use feature.level.LevelTabletScreen instead.",
    replaceWith = ReplaceWith("com.mathisland.app.feature.level.LevelTabletScreen(state, onQuit, answerPane, onExpire)")
)
@Composable
fun LessonTabletScreen(
    state: LessonUiState,
    onQuit: () -> Unit,
    answerPane: @Composable () -> Unit,
    onExpire: (() -> Unit)? = null
) {
    LevelTabletScreen(
        state = state,
        onQuit = onQuit,
        answerPane = answerPane,
        onExpire = onExpire
    )
}
