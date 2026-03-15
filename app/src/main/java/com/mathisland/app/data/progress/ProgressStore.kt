package com.mathisland.app.data.progress

import com.mathisland.app.GameProgress
import com.mathisland.app.MathIslandProgressStore

interface ProgressStore {
    fun load(initial: GameProgress): GameProgress
    fun save(state: GameProgress)
}

class SharedPreferencesProgressStore(
    private val delegate: MathIslandProgressStore
) : ProgressStore {
    override fun load(initial: GameProgress): GameProgress = delegate.load(initial)

    override fun save(state: GameProgress) {
        delegate.save(state)
    }
}

class InMemoryProgressStore(
    private var state: GameProgress? = null
) : ProgressStore {
    override fun load(initial: GameProgress): GameProgress = state ?: initial

    override fun save(state: GameProgress) {
        this.state = state
    }
}
