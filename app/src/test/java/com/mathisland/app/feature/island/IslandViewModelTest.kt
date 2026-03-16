package com.mathisland.app.feature.island

import com.mathisland.app.feature.map.MapTabletIslandUiState
import com.mathisland.app.feature.map.MapTabletLessonUiState
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
