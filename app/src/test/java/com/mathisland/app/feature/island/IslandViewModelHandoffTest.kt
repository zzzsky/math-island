package com.mathisland.app.feature.island

import com.mathisland.app.feature.map.MapFeedbackKind
import com.mathisland.app.feature.map.MapFeedbackUiState
import com.mathisland.app.feature.map.MapTabletIslandUiState
import com.mathisland.app.feature.map.MapTabletLessonUiState
import com.mathisland.app.feature.map.MapTabletUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class IslandViewModelHandoffTest {
    @Test
    fun uiState_showsChestHandoffOnRecommendedIslandWhenNoExplicitHighlight() {
        val state = IslandViewModel.uiState(
            mapState = MapTabletUiState(
                totalStars = 6,
                recommendedIslandId = "calculation-island",
                feedback = MapFeedbackUiState(
                    kind = MapFeedbackKind.Chest,
                    title = "宝箱有新收藏",
                    body = "Bridge Builder 已收入宝箱。",
                    summaryLabel = "打开宝箱",
                    summaryTitle = "宝箱里有新的收藏",
                    summaryBody = "回地图后可以直接打开查看。",
                    actionLabel = "优先动作",
                    actionTitle = "先开宝箱",
                    actionBody = "先看收藏更新，再回到当前推荐课程。"
                ),
                islands = listOf(
                    island(id = "calculation-island", title = "计算岛"),
                    island(id = "measurement-island", title = "测量与图形岛")
                )
            ),
            selectedIslandId = null
        )

        assertEquals("计算岛", state.island.title)
        assertEquals("打开宝箱", state.handoffLabel)
        assertEquals("先开宝箱", state.handoffDetailTitle)
        assertEquals("先开宝箱", state.handoffActionTitle)
        assertEquals("新收藏", state.handoffStageState?.spotlightLabel)
    }

    @Test
    fun uiState_hidesHandoffOnNonHighlightedIsland() {
        val state = IslandViewModel.uiState(
            mapState = MapTabletUiState(
                totalStars = 6,
                recommendedIslandId = "measurement-island",
                feedback = MapFeedbackUiState(
                    kind = MapFeedbackKind.NewIsland,
                    title = "新岛已解锁",
                    body = "测量与图形岛已开放。",
                    highlightedIslandId = "measurement-island",
                    summaryLabel = "继续主线",
                    summaryTitle = "新主线已经准备好",
                    summaryBody = "右侧会直接显示推荐课程。",
                    actionLabel = "优先动作",
                    actionTitle = "开始主线课",
                    actionBody = "先看新岛，再直接开始当前推荐的主线课程。"
                ),
                islands = listOf(
                    island(id = "calculation-island", title = "计算岛"),
                    island(id = "measurement-island", title = "测量与图形岛")
                )
            ),
            selectedIslandId = "calculation-island"
        )

        assertEquals("计算岛", state.island.title)
        assertNull(state.handoffLabel)
        assertNull(state.handoffTitle)
        assertNull(state.handoffBody)
        assertNull(state.handoffDetailTitle)
        assertNull(state.handoffActionTitle)
        assertNull(state.handoffStageState)
    }

    private fun island(id: String, title: String) = MapTabletIslandUiState(
        id = id,
        title = title,
        subtitle = "subtitle",
        description = "description",
        unlocked = true,
        completed = false,
        progress = 0.5f,
        lessons = listOf(
            MapTabletLessonUiState(
                id = "$id-lesson-01",
                title = "课程 1",
                summary = "summary",
                completed = false,
                enabled = true
            )
        )
    )
}
