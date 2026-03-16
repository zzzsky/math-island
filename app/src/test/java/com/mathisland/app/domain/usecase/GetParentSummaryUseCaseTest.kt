package com.mathisland.app.domain.usecase

import com.mathisland.app.data.content.CurriculumRepository
import com.mathisland.app.data.content.curriculumToGameIslands
import com.mathisland.app.data.progress.InMemoryProgressStore
import com.mathisland.app.data.progress.ProgressRepository
import com.mathisland.app.domain.model.ReviewTask
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Test

class GetParentSummaryUseCaseTest {
    private val controller = com.mathisland.app.MathIslandGameController(
        curriculumToGameIslands(
            CurriculumRepository.loadFromFiles(
                sequenceOf(
                    File("src/main/assets/content"),
                    File("app/src/main/assets/content")
                ).first { candidate -> candidate.exists() }
            )
        )
    )
    private val repository = ProgressRepository(InMemoryProgressStore(), controller)
    private val useCase = GetParentSummaryUseCase(repository, controller)

    @Test
    fun returnsTodayWeakTopicStreakAndRecommendation() {
        repository.save(
            controller.initialState().copy(
                completedLessonIds = setOf("calc-bridge"),
                todayLessonTitles = listOf("修桥加减法"),
                streakDays = 4,
                pendingReview = ReviewTask("measurement"),
                unlockedIslandIds = setOf("calculation-island", "measurement-geometry-island")
            )
        )

        val summary = useCase()

        assertEquals(listOf("修桥加减法"), summary.todayLearned)
        assertEquals(listOf("测量与图形"), summary.weakTopics)
        assertEquals(4, summary.streakDays)
        assertEquals("测量与图形岛", summary.recommendedIsland)
    }
}
