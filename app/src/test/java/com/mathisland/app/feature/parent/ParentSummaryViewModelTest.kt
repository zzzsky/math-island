package com.mathisland.app.feature.parent

import com.mathisland.app.ParentSummary
import org.junit.Assert.assertEquals
import org.junit.Test

class ParentSummaryViewModelTest {
    @Test
    fun uiState_formatsSummaryCardsForDisplay() {
        val uiState = ParentSummaryViewModel.uiState(
            ParentSummary(
                todayLearned = listOf("修桥加减法"),
                weakTopics = listOf("测量与图形"),
                streakDays = 4,
                recommendedIsland = "测量与图形岛"
            )
        )

        assertEquals("修桥加减法", uiState.todayLearnedText)
        assertEquals("测量与图形", uiState.weakTopicsText)
        assertEquals("4 天", uiState.streakText)
        assertEquals("测量与图形岛", uiState.recommendedIslandText)
    }
}
