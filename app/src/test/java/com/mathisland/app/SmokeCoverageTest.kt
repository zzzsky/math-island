package com.mathisland.app

import com.mathisland.app.di.AppContainer
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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

    @Test
    fun staleMapPanelPath_isQuarantined_andDocsDescribeStableContracts() {
        val repoRoot = sequenceOf(
            File("."),
            File(".."),
            File("..", "..")
        ).first { candidate -> File(candidate, "README.md").exists() }

        val legacyPanel = File(repoRoot, "app/src/main/java/com/mathisland/app/feature/map/IslandDetailPanel.kt")
        if (legacyPanel.exists()) {
            val legacyPanelSource = legacyPanel.readText()
            assertTrue(legacyPanelSource.contains("@Deprecated("))
            assertFalse(legacyPanelSource.contains("testTag(\"panel-start-"))
        }

        val readme = File(repoRoot, "README.md").readText()
        assertTrue(readme.contains("panel-start-<lessonId>"))
        assertTrue(readme.contains("compatibility-backed"))

        val testingGuide = File(repoRoot, "docs/testing.md").readText()
        assertTrue(testingGuide.contains("panel-start-<lessonId>"))
    }
}
