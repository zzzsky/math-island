package com.mathisland.app.domain.usecase

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.ParentSummary
import com.mathisland.app.data.progress.ProgressRepository

class GetParentSummaryUseCase(
    private val repository: ProgressRepository,
    private val controller: MathIslandGameController
) {
    operator fun invoke(): ParentSummary = controller.parentSummary(repository.load())
}
