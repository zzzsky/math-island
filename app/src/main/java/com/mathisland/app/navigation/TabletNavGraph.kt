package com.mathisland.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.mathisland.app.MathIslandApp
import com.mathisland.app.di.AppContainer
import com.mathisland.app.domain.model.AppDestination
import com.mathisland.app.domain.model.CurriculumBundle
import com.mathisland.app.domain.model.CurriculumCatalog
import com.mathisland.app.feature.chest.ChestTabletScreen
import com.mathisland.app.feature.chest.ChestViewModel
import com.mathisland.app.feature.home.HomeTabletScreen
import com.mathisland.app.feature.home.HomeViewModel
import com.mathisland.app.feature.level.LevelAnswerPane
import com.mathisland.app.feature.level.LevelTabletScreen
import com.mathisland.app.feature.level.LevelViewModel
import com.mathisland.app.feature.level.RewardOverlay
import com.mathisland.app.feature.level.RewardViewModel
import com.mathisland.app.feature.map.MapTabletScreen
import com.mathisland.app.feature.map.MapViewModel
import com.mathisland.app.feature.parent.ParentGateScreen
import com.mathisland.app.feature.parent.ParentGateViewModel
import com.mathisland.app.feature.parent.ParentSummaryTabletScreen
import com.mathisland.app.feature.parent.ParentSummaryViewModel
import com.mathisland.app.sampleIslands

@Composable
fun TabletNavGraph() {
    val context = LocalContext.current.applicationContext
    val container = remember(context) {
        runCatching {
            AppContainer.fromContext(context)
        }.getOrElse {
            AppContainer.fromIslands(
                curriculumBundle = CurriculumBundle(
                    catalog = CurriculumCatalog(islands = emptyList()),
                    islands = emptyList()
                ),
                islands = sampleIslands(),
                progressStore = com.mathisland.app.data.progress.InMemoryProgressStore()
            )
        }
    }
    val routeState = rememberTabletRouteState(container)
    val controller = remember(container) { container.gameController }
    val homeState = remember(routeState.progress) { HomeViewModel.uiState(container.getHomeStateUseCase) }
    val parentSummaryState = remember(routeState.progress) {
        ParentSummaryViewModel.uiState(container.getParentSummaryUseCase())
    }

    MathIslandApp {
        when (routeState.destination) {
            AppDestination.HOME -> HomeTabletScreen(
                state = homeState,
                onContinue = routeState::continueAdventure,
                onOpenMap = routeState::openMap,
                onOpenChest = routeState::openChest,
                onOpenParent = routeState::openParentGate
            )

            AppDestination.MAP -> MapTabletScreen(
                state = MapViewModel.uiState(controller, routeState.progress, routeState.pendingMapFeedback),
                onBackHome = routeState::goHome,
                onOpenChest = routeState::openChest,
                onStartLesson = routeState::startLesson,
                onConsumeFeedback = routeState::consumeMapFeedback
            )

            AppDestination.CHEST -> ChestTabletScreen(
                state = ChestViewModel.uiState(routeState.progress),
                onBackHome = routeState::goHome,
                onOpenMap = routeState::openMap
            )

            AppDestination.LESSON -> {
                val levelState = LevelViewModel.uiState(controller, routeState.progress)
                if (levelState != null) {
                    LevelTabletScreen(
                        state = levelState,
                        onQuit = routeState::openMap,
                        onExpire = routeState::expireLesson,
                        answerPane = {
                            LevelAnswerPane(
                                question = levelState.question,
                                onAnswer = routeState::answer
                            )
                        }
                    )
                }
            }

            AppDestination.REWARD -> {
                val rewardState = RewardViewModel.uiState(routeState.progress)
                if (rewardState != null) {
                    RewardOverlay(
                        state = rewardState,
                        onContinue = routeState::claimReward,
                        onSecondaryAction = rewardState.reward.secondaryActionLessonId?.let { lessonId ->
                            { routeState.startLesson(lessonId) }
                        }
                    )
                }
            }

            AppDestination.PARENT_GATE -> ParentGateScreen(
                state = ParentGateViewModel.uiState(),
                onAnswer = routeState::submitParentGateAnswer,
                onBackHome = routeState::goHome
            )

            AppDestination.PARENT_SUMMARY -> ParentSummaryTabletScreen(
                state = parentSummaryState,
                onBackHome = routeState::closeParentSummary
            )
        }
    }
}
