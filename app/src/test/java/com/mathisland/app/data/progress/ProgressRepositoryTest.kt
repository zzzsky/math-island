package com.mathisland.app.data.progress

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.domain.model.AppDestination
import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.sampleIslands
import org.junit.Assert.assertEquals
import org.junit.Test

class ProgressRepositoryTest {
    private val controller = MathIslandGameController(sampleIslands())
    private val store = InMemoryProgressStore(controller.initialState())
    private val repository = ProgressRepository(store, controller)

    @Test
    fun saveAndLoad_roundTripsGameProgress() {
        val updated = controller.openMap(controller.initialState()).copy(totalStars = 7)

        repository.save(updated)
        val restored = repository.load()

        assertEquals(AppDestination.MAP, restored.destination)
        assertEquals(7, restored.totalStars)
    }
}
