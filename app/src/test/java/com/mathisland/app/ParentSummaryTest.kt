package com.mathisland.app

import org.junit.Assert.assertEquals
import org.junit.Test

class ParentSummaryTest {
    private val controller = MathIslandGameController(sampleIslands())

    @Test
    fun parentSummary_reportsTodayLearnedWeakTopicsStreakAndRecommendation() {
        val state = controller.initialState().copy(
            completedLessonIds = setOf("calc-carry-01"),
            todayLessonTitles = listOf("修桥进位关"),
            streakDays = 3,
            pendingReview = ReviewTask("measurement"),
            unlockedIslandIds = setOf("calculation-island", "measurement-geometry-island")
        )

        val summary = controller.parentSummary(state)

        assertEquals(listOf("修桥进位关"), summary.todayLearned)
        assertEquals(listOf("测量与图形"), summary.weakTopics)
        assertEquals(3, summary.streakDays)
        assertEquals("测量与图形岛", summary.recommendedIsland)
    }
}
