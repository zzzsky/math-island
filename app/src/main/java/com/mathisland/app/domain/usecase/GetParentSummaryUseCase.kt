package com.mathisland.app.domain.usecase

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.data.progress.ProgressRepository
import com.mathisland.app.domain.model.ParentSummary

class GetParentSummaryUseCase(
    private val repository: ProgressRepository,
    private val controller: MathIslandGameController
) {
    operator fun invoke(): ParentSummary = controller.parentSummary(repository.load())
}
