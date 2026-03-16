package com.mathisland.app.domain.usecase

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.data.content.CurriculumRepository
import com.mathisland.app.data.content.curriculumToGameIslands
import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.ReviewTask
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class GetPendingReviewUseCaseTest {
    private val controller = MathIslandGameController(
        curriculumToGameIslands(
            CurriculumRepository.loadFromFiles(
                sequenceOf(
                    File("src/main/assets/content"),
                    File("app/src/main/assets/content")
                ).first { candidate -> candidate.exists() }
            )
        )
    )
    private val useCase = GetPendingReviewUseCase(controller)

    @Test
    fun challengePendingReview_prefersChallengeReplayLesson() {
        val state = controller.initialState().copy(
            unlockedIslandIds = controller.islands.map { island -> island.id }.toSet(),
            pendingReview = ReviewTask("challenge")
        )

        val lesson = useCase(state)

        assertEquals("challenge-review-01", lesson?.id)
        assertEquals("错题回放站", lesson?.title)
    }

    @Test
    fun calculationPendingReview_returnsSeagullLesson() {
        val state = controller.initialState().copy(
            pendingReview = ReviewTask("calculation")
        )

        val lesson = useCase(state)

        assertNotNull(lesson)
        assertEquals("review-calculation", lesson?.id)
    }
}
