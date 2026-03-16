package com.mathisland.app.di

import android.content.Context
import com.mathisland.app.MathIslandGameController
import com.mathisland.app.data.progress.DataStoreProgressStore
import com.mathisland.app.data.progress.InMemoryProgressStore
import com.mathisland.app.data.progress.ProgressRepository
import com.mathisland.app.data.progress.ProgressStore
import com.mathisland.app.data.content.CurriculumRepository
import com.mathisland.app.data.content.curriculumToGameIslands
import com.mathisland.app.domain.usecase.GetHomeStateUseCase
import com.mathisland.app.domain.usecase.GetMapStateUseCase
import com.mathisland.app.domain.usecase.GetPendingReviewUseCase
import com.mathisland.app.domain.usecase.GetParentSummaryUseCase
import com.mathisland.app.domain.usecase.SubmitLessonResultUseCase
import com.mathisland.app.domain.model.CurriculumBundle
import com.mathisland.app.domain.model.Island
import java.io.File

data class AppContainer(
    val curriculumBundle: CurriculumBundle,
    val islands: List<Island>,
    val gameController: MathIslandGameController,
    val progressRepository: ProgressRepository,
    val getHomeStateUseCase: GetHomeStateUseCase,
    val getMapStateUseCase: GetMapStateUseCase,
    val getPendingReviewUseCase: GetPendingReviewUseCase,
    val getParentSummaryUseCase: GetParentSummaryUseCase,
    val submitLessonResultUseCase: SubmitLessonResultUseCase
) {
    companion object {
        fun fromContext(context: Context): AppContainer {
            val curriculum = CurriculumRepository.loadFromAssets(context.assets)
            return fromCurriculum(
                curriculum = curriculum,
                progressStore = DataStoreProgressStore(context)
            )
        }

        fun fromContentDir(contentDir: File): AppContainer {
            val curriculum = CurriculumRepository.loadFromFiles(contentDir)
            return fromCurriculum(
                curriculum = curriculum,
                progressStore = InMemoryProgressStore()
            )
        }

        fun fromIslands(
            curriculumBundle: CurriculumBundle,
            islands: List<Island>,
            progressStore: ProgressStore = InMemoryProgressStore()
        ): AppContainer {
            val controller = MathIslandGameController(islands)
            val repository = ProgressRepository(progressStore, controller)
            return AppContainer(
                curriculumBundle = curriculumBundle,
                islands = islands,
                gameController = controller,
                progressRepository = repository,
                getHomeStateUseCase = GetHomeStateUseCase(repository, controller),
                getMapStateUseCase = GetMapStateUseCase(repository, controller),
                getPendingReviewUseCase = GetPendingReviewUseCase(controller),
                getParentSummaryUseCase = GetParentSummaryUseCase(repository, controller),
                submitLessonResultUseCase = SubmitLessonResultUseCase(islands)
            )
        }

        private fun fromCurriculum(
            curriculum: CurriculumBundle,
            progressStore: ProgressStore
        ): AppContainer {
            val islands = curriculumToGameIslands(curriculum)
            return fromIslands(curriculum, islands, progressStore)
        }
    }
}
