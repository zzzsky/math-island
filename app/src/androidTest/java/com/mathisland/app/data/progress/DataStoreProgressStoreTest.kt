package com.mathisland.app.data.progress

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mathisland.app.MathIslandGameController
import com.mathisland.app.domain.model.AppDestination
import com.mathisland.app.sampleIslands
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataStoreProgressStoreTest {
    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun saveAndLoad_roundTripsGameProgress() {
        val controller = MathIslandGameController(sampleIslands())
        val fileName = "progress-test-${System.nanoTime()}"
        val store = DataStoreProgressStore(context, fileName)
        val updated = controller.openMap(controller.initialState()).copy(totalStars = 11)

        store.save(updated)
        val restored = store.load(controller.initialState())

        assertEquals(AppDestination.MAP, restored.destination)
        assertEquals(11, restored.totalStars)
    }
}
