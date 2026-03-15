package com.mathisland.app.domain.usecase

import com.mathisland.app.GameProgress
import com.mathisland.app.Lesson
import com.mathisland.app.MathIslandGameController

class GetPendingReviewUseCase(
    private val controller: MathIslandGameController
) {
    operator fun invoke(state: GameProgress): Lesson? = controller.pendingReviewLesson(state)
}
