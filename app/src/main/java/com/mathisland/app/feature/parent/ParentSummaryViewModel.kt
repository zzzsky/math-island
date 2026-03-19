package com.mathisland.app.feature.parent

import com.mathisland.app.domain.model.ParentSummary

data class ParentSummaryUiState(
    val todayLearnedText: String,
    val weakTopicsText: String,
    val streakText: String,
    val recommendedIslandText: String,
    val heroTitle: String = "今日学习总览",
    val heroBody: String = "",
    val todayCountText: String = "0 项",
    val weakTopicCountText: String = "0 项",
)

object ParentSummaryViewModel {
    fun uiState(summary: ParentSummary): ParentSummaryUiState {
        val todayCount = summary.todayLearned.size
        val weakCount = summary.weakTopics.size
        val todayLearnedText = summary.todayLearned.joinToString("、").ifEmpty { "今天还没有新进度" }
        val weakTopicsText = summary.weakTopics.joinToString("、").ifEmpty { "暂无薄弱项" }
        return ParentSummaryUiState(
            todayLearnedText = todayLearnedText,
            weakTopicsText = weakTopicsText,
            streakText = "${summary.streakDays} 天",
            recommendedIslandText = summary.recommendedIsland,
            heroTitle = when {
                todayCount == 0 -> "今天还没有新的学习进度"
                todayCount == 1 -> "今天完成了 1 个学习节点"
                else -> "今天完成了 $todayCount 个学习节点"
            },
            heroBody = buildString {
                append("已连续学习 ${summary.streakDays} 天")
                if (summary.recommendedIsland.isNotBlank()) {
                    append("，建议下一步优先查看 ${summary.recommendedIsland}")
                }
                if (weakCount > 0) {
                    append("，并关注 ${weakCount} 个薄弱知识点。")
                } else {
                    append("，当前没有新增薄弱项。")
                }
            },
            todayCountText = "${todayCount} 项",
            weakTopicCountText = "${weakCount} 项"
        )
    }
}
