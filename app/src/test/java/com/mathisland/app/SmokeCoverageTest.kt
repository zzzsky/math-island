package com.mathisland.app

import com.mathisland.app.di.AppContainer
import java.io.File
import org.junit.Assert.assertEquals
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
    fun stableContracts_areDocumented_andLegacyWrappersAreRemoved() {
        val repoRoot = sequenceOf(
            File("."),
            File(".."),
            File("..", "..")
        ).first { candidate -> File(candidate, "README.md").exists() }

        val removedPaths = listOf(
            "app/src/main/java/com/mathisland/app/feature/map/IslandDetailPanel.kt",
            "app/src/main/java/com/mathisland/app/feature/map/MapSceneCanvas.kt",
            "app/src/main/java/com/mathisland/app/MathIslandProgressStore.kt",
            "app/src/main/java/com/mathisland/app/feature/lesson/LessonAnswerPane.kt",
            "app/src/main/java/com/mathisland/app/feature/reward/RewardTabletScreen.kt",
            "app/src/main/java/com/mathisland/app/feature/common/TabletUi.kt"
        )
        removedPaths.forEach { path ->
            assertTrue(!File(repoRoot, path).exists())
        }

        val rendererFiles = listOf(
            "app/src/main/java/com/mathisland/app/feature/level/renderers/ChoiceQuestionPane.kt",
            "app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt",
            "app/src/main/java/com/mathisland/app/feature/level/renderers/RulerQuestionPane.kt"
        )
        rendererFiles.forEach { path ->
            assertTrue(File(repoRoot, path).exists())
        }

        val readme = File(repoRoot, "README.md").readText()
        assertTrue(readme.contains("panel-start-<lessonId>"))
        assertTrue(readme.contains("DataStore"))
        assertTrue(readme.contains("feature/level/renderers/*"))

        val testingGuide = File(repoRoot, "docs/testing.md").readText()
        assertTrue(testingGuide.contains("panel-start-<lessonId>"))
        assertTrue(testingGuide.contains("feature/level/renderers"))
    }
}
