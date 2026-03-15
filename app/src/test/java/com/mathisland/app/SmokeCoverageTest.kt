package com.mathisland.app

import com.mathisland.app.di.AppContainer
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SmokeCoverageTest {
    private val contentDir = sequenceOf(
        File("src/main/assets/content"),
        File("app/src/main/assets/content")
    ).first { candidate -> candidate.exists() }

    @Test
    fun appContainer_buildsCoreGameDependencies() {
        val container = AppContainer.fromContentDir(contentDir)

        assertEquals(7, container.islands.size)
        assertNotNull(container.getHomeStateUseCase)
        assertNotNull(container.getMapStateUseCase)
        assertNotNull(container.getPendingReviewUseCase)
        assertNotNull(container.getParentSummaryUseCase)
        assertNotNull(container.submitLessonResultUseCase)
        assertNotNull(container.gameController.recommendedLesson(container.gameController.initialState()))
    }
}
