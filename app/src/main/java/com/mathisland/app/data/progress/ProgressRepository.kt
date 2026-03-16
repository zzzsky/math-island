package com.mathisland.app.data.progress

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.domain.model.GameProgress

class ProgressRepository(
    private val store: ProgressStore,
    private val controller: MathIslandGameController
) {
    fun load(): GameProgress = store.load(controller.initialState())

    fun save(state: GameProgress) {
        store.save(state)
    }

    fun update(transform: (GameProgress) -> GameProgress): GameProgress {
        val updated = transform(load())
        save(updated)
        return updated
    }
}
