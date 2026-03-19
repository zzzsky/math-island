package com.mathisland.app.feature.map

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.domain.model.AppDestination
import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.Island
import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.domain.model.Question
import com.mathisland.app.domain.model.ReviewTask
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MapViewModelTest {
    @Test
    fun uiState_prefersFirstUnlockedUnfinishedIslandAsDefaultSelection() {
        val controller = MathIslandGameController(
            islands = listOf(
                island(
                    id = "calculation-island",
                    title = "计算岛",
                    lessonId = "calc-01"
                ),
                island(
                    id = "measurement-island",
                    title = "测量与图形岛",
                    lessonId = "measure-01"
                )
            )
        )

        val state = MapViewModel.uiState(
            controller = controller,
            progress = progress(
                unlockedIslandIds = setOf("calculation-island", "measurement-island"),
                completedLessonIds = setOf("calc-01")
            )
        )

        assertEquals("measurement-island", state.recommendedIslandId)
    }

    @Test
    fun uiState_marksCompletedIslands() {
        val controller = MathIslandGameController(
            islands = listOf(
                island(
                    id = "calculation-island",
                    title = "计算岛",
                    lessonId = "calc-01"
                )
            )
        )

        val state = MapViewModel.uiState(
            controller = controller,
            progress = progress(
                unlockedIslandIds = setOf("calculation-island"),
                completedLessonIds = setOf("calc-01")
            )
        )

        assertEquals(true, state.islands.single().completed)
    }

    @Test
    fun uiState_marksNewIslandHandoffOnHighlightedIsland() {
        val controller = MathIslandGameController(
            islands = listOf(
                island(id = "calculation-island", title = "计算岛", lessonId = "calc-01"),
                island(id = "measurement-island", title = "测量与图形岛", lessonId = "measure-01")
            )
        )

        val state = MapViewModel.uiState(
            controller = controller,
            progress = progress(
                unlockedIslandIds = setOf("calculation-island", "measurement-island"),
                completedLessonIds = setOf("calc-01")
            ),
            feedback = MapFeedbackUiState(
                kind = MapFeedbackKind.NewIsland,
                title = "新岛已解锁",
                body = "测量与图形岛已开放。",
                highlightedIslandId = "measurement-island",
                summaryLabel = "主线继续"
            )
        )

        val highlighted = state.islands.single { it.id == "measurement-island" }
        assertEquals("主线推荐", highlighted.handoffBadge)
        assertEquals("下一节主线课已就位", highlighted.handoffBody)
    }

    @Test
    fun uiState_marksChestHandoffOnRecommendedIsland() {
        val controller = MathIslandGameController(
            islands = listOf(
                island(id = "calculation-island", title = "计算岛", lessonId = "calc-01")
            )
        )

        val state = MapViewModel.uiState(
            controller = controller,
            progress = progress(
                unlockedIslandIds = setOf("calculation-island"),
                completedLessonIds = emptySet()
            ),
            feedback = MapFeedbackUiState(
                kind = MapFeedbackKind.Chest,
                title = "宝箱有新收藏",
                body = "Bridge Builder 已收入宝箱。",
                summaryLabel = "先看收藏"
            )
        )

        val island = state.islands.single()
        assertEquals("宝箱优先", island.handoffBadge)
        assertEquals("先看收藏，再回到当前课程", island.handoffBody)
    }

    @Test
    fun uiState_leavesOtherIslandsNeutralWhenHandoffTargetsDifferentIsland() {
        val controller = MathIslandGameController(
            islands = listOf(
                island(id = "calculation-island", title = "计算岛", lessonId = "calc-01"),
                island(id = "measurement-island", title = "测量与图形岛", lessonId = "measure-01")
            )
        )

        val state = MapViewModel.uiState(
            controller = controller,
            progress = progress(
                unlockedIslandIds = setOf("calculation-island", "measurement-island"),
                completedLessonIds = setOf("calc-01")
            ),
            feedback = MapFeedbackUiState(
                kind = MapFeedbackKind.NewIsland,
                title = "新岛已解锁",
                body = "测量与图形岛已开放。",
                highlightedIslandId = "measurement-island",
                summaryLabel = "主线继续"
            )
        )

        val neutral = state.islands.single { it.id == "calculation-island" }
        assertNull(neutral.handoffBadge)
        assertNull(neutral.handoffBody)
    }

    private fun island(
        id: String,
        title: String,
        lessonId: String
    ) = Island(
        id = id,
        title = title,
        subtitle = "副标题",
        description = "描述",
        rewardSticker = "$id-sticker",
        lessons = listOf(
            Lesson(
                id = lessonId,
                islandId = id,
                title = "$title-课程",
                focus = "focus",
                summary = "summary",
                questions = listOf(
                    Question(
                        prompt = "1 + 1 = ?",
                        choices = listOf("2"),
                        correctChoice = "2",
                        hint = "hint",
                        family = "calculation"
                    )
                )
            )
        )
    )

    private fun progress(
        unlockedIslandIds: Set<String>,
        completedLessonIds: Set<String>
    ) = GameProgress(
        destination = AppDestination.MAP,
        unlockedIslandIds = unlockedIslandIds,
        completedLessonIds = completedLessonIds,
        totalStars = 0,
        stickerNames = emptySet(),
        activeLessonId = null,
        activeQuestionIndex = 0,
        correctAnswersInLesson = 0,
        lastWrongFamily = null,
        consecutiveWrongCount = 0,
        scheduledReviewFamily = null,
        pendingReview = null,
        todayLessonTitles = emptyList(),
        streakDays = 0,
        lastStudyDayEpoch = null,
        pendingReward = null
    )
}
