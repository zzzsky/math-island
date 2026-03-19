package com.mathisland.app.feature.island

import com.mathisland.app.feature.map.MapTabletIslandUiState
import com.mathisland.app.feature.map.MapTabletLessonUiState
import com.mathisland.app.feature.map.MapFeedbackKind
import com.mathisland.app.feature.map.MapFeedbackUiState
import com.mathisland.app.feature.map.MapTabletUiState
import org.junit.Assert.assertEquals
import org.junit.Test

class IslandViewModelTest {
    @Test
    fun uiState_prefersRecommendedIslandWhenNoExplicitSelection() {
        val state = IslandViewModel.uiState(
            mapState = MapTabletUiState(
                totalStars = 6,
                recommendedIslandId = "measurement-island",
                islands = listOf(
                    island(id = "calculation-island", title = "计算岛"),
                    island(id = "measurement-island", title = "测量与图形岛")
                )
            ),
            selectedIslandId = null
        )

        assertEquals("measurement-island", state.island.id)
        assertEquals("测量与图形岛", state.island.title)
    }

    @Test
    fun uiState_usesExplicitSelectionWhenProvided() {
        val state = IslandViewModel.uiState(
            mapState = MapTabletUiState(
                totalStars = 6,
                recommendedIslandId = "measurement-island",
                islands = listOf(
                    island(id = "calculation-island", title = "计算岛"),
                    island(id = "measurement-island", title = "测量与图形岛")
                )
            ),
            selectedIslandId = "calculation-island"
        )

        assertEquals("calculation-island", state.island.id)
        assertEquals("计算岛", state.island.title)
    }

    @Test
    fun uiState_usesChestPrimaryActionWhenChestHandoffActive() {
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
                    summaryBody = "回地图后可以直接打开查看。"
                ),
                islands = listOf(
                    island(id = "calculation-island", title = "计算岛")
                )
            ),
            selectedIslandId = null
        )

        assertEquals(IslandPrimaryActionMode.OpenChest, state.primaryActionMode)
        assertEquals("先打开宝箱", state.primaryActionLabel)
    }

    @Test
    fun uiState_prefersReviewLessonDuringReplayHandoff() {
        val state = IslandViewModel.uiState(
            mapState = MapTabletUiState(
                totalStars = 6,
                recommendedIslandId = "challenge-island",
                feedback = MapFeedbackUiState(
                    kind = MapFeedbackKind.Replay,
                    title = "回放路线已整理",
                    body = "综合挑战的回放与推荐路线已经整理好。",
                    summaryLabel = "回放优先",
                    summaryTitle = "先看回放，再决定是否重新冲刺",
                    summaryBody = "地图会优先保留综合挑战的回放与练习入口。"
                ),
                islands = listOf(
                    MapTabletIslandUiState(
                        id = "challenge-island",
                        title = "挑战岛",
                        subtitle = "综合挑战",
                        description = "挑战",
                        unlocked = true,
                        completed = false,
                        progress = 0.5f,
                        lessons = listOf(
                            MapTabletLessonUiState(
                                id = "challenge-sprint-01",
                                title = "海图冲刺赛",
                                summary = "冲刺",
                                completed = false,
                                enabled = true
                            ),
                            MapTabletLessonUiState(
                                id = "challenge-review-01",
                                title = "错题回放站",
                                summary = "回放",
                                completed = false,
                                enabled = true
                            )
                        )
                    )
                )
            ),
            selectedIslandId = null
        )

        assertEquals(IslandPrimaryActionMode.StartLesson, state.primaryActionMode)
        assertEquals("challenge-review-01", state.primaryLessonId)
        assertEquals("先做回放 错题回放站", state.primaryActionLabel)
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
