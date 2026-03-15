package com.mathisland.app.domain.usecase

import com.mathisland.app.MathIslandGameController
import com.mathisland.app.data.progress.ProgressRepository

data class HomeState(
    val totalStars: Int,
    val stickerCount: Int,
    val nextLessonId: String?,
    val nextLessonTitle: String?,
    val nextLessonFocus: String?,
    val nextLessonSummary: String?,
    val isReview: Boolean
)

class GetHomeStateUseCase(
    private val repository: ProgressRepository,
    private val controller: MathIslandGameController
) {
    operator fun invoke(): HomeState {
        val progress = repository.load()
        val lesson = controller.recommendedLesson(progress)
        return HomeState(
            totalStars = progress.totalStars,
            stickerCount = progress.stickerNames.size,
            nextLessonId = lesson?.id,
            nextLessonTitle = lesson?.title,
            nextLessonFocus = lesson?.focus,
            nextLessonSummary = lesson?.summary,
            isReview = lesson?.isReview == true
        )
    }
}
