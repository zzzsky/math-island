package com.mathisland.app.feature.lesson

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.feature.level.LevelUiState
import com.mathisland.app.feature.level.LevelViewModel

@Deprecated(
    message = "Use feature.level.LevelViewModel instead.",
    replaceWith = ReplaceWith("com.mathisland.app.feature.level.LevelViewModel")
)
typealias LessonUiState = LevelUiState

@Deprecated(
    message = "Use feature.level.LevelViewModel instead.",
    replaceWith = ReplaceWith("com.mathisland.app.feature.level.LevelViewModel")
)
object LessonViewModel {
    fun uiState(
        controller: MathIslandGameController,
        progress: GameProgress
    ): LessonUiState? = LevelViewModel.uiState(controller, progress)
}
