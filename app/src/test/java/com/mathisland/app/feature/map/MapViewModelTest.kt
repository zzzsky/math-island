package com.mathisland.app.feature.map

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.domain.model.AppDestination
import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.Island
import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.domain.model.Question
import com.mathisland.app.domain.model.ReviewTask
import org.junit.Assert.assertEquals
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
