package com.mathisland.app.feature.parent

import com.mathisland.app.domain.model.ParentSummary

data class ParentSummaryUiState(
    val todayLearnedText: String,
    val weakTopicsText: String,
    val streakText: String,
    val recommendedIslandText: String
)

object ParentSummaryViewModel {
    fun uiState(summary: ParentSummary): ParentSummaryUiState = ParentSummaryUiState(
        todayLearnedText = summary.todayLearned.joinToString("、").ifEmpty { "今天还没有新进度" },
        weakTopicsText = summary.weakTopics.joinToString("、").ifEmpty { "暂无薄弱项" },
        streakText = "${summary.streakDays} 天",
        recommendedIslandText = summary.recommendedIsland
    )
}
