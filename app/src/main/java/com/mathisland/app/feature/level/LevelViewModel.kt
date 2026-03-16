package com.mathisland.app.feature.level

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.domain.model.GameProgress

typealias LevelUiState = com.mathisland.app.feature.lesson.LessonUiState

object LevelViewModel {
    fun uiState(
        controller: MathIslandGameController,
        progress: GameProgress
    ): LevelUiState? = com.mathisland.app.feature.lesson.LessonViewModel.uiState(controller, progress)
}
