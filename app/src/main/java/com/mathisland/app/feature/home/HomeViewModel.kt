package com.mathisland.app.feature.home

import com.mathisland.app.domain.usecase.GetHomeStateUseCase
import com.mathisland.app.domain.usecase.HomeState

object HomeViewModel {
    fun uiState(useCase: GetHomeStateUseCase): HomeState = useCase()
}
