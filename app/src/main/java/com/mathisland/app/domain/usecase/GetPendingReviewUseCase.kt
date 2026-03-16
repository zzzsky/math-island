package com.mathisland.app.domain.usecase

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.Lesson

class GetPendingReviewUseCase(
    private val controller: MathIslandGameController
) {
    operator fun invoke(state: GameProgress): Lesson? = controller.pendingReviewLesson(state)
}
