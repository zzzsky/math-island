package com.mathisland.app
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mathisland.app.di.AppContainer
import com.mathisland.app.domain.model.CurriculumBundle
import com.mathisland.app.domain.model.CurriculumCatalog
import com.mathisland.app.feature.chest.ChestTabletScreen
import com.mathisland.app.feature.chest.ChestViewModel
import com.mathisland.app.feature.home.HomeTabletScreen
import com.mathisland.app.feature.home.HomeViewModel
import com.mathisland.app.feature.lesson.LessonAnswerPane
import com.mathisland.app.feature.lesson.LessonTabletScreen
import com.mathisland.app.feature.lesson.LessonViewModel
import com.mathisland.app.feature.map.MapFeedbackUiState
import com.mathisland.app.feature.map.MapTabletScreen
import com.mathisland.app.feature.map.MapViewModel
import com.mathisland.app.feature.parent.ParentGateScreen as ParentGateFeatureScreen
import com.mathisland.app.feature.parent.ParentGateViewModel
import com.mathisland.app.feature.parent.ParentSummaryTabletScreen
import com.mathisland.app.feature.parent.ParentSummaryViewModel
import com.mathisland.app.feature.reward.RewardTabletScreen
import com.mathisland.app.feature.reward.RewardViewModel

private val Ocean = Color(0xFF0F4C5C)
private val DeepWater = Color(0xFF114B5F)
private val Foam = Color(0xFFF3FAF8)
private val Sand = Color(0xFFF4D58D)
private val Coral = Color(0xFFEE964B)
private val Seaweed = Color(0xFF4B6F44)
private val Sky = Color(0xFF8ECae6)
private val Mint = Color(0xFF9ADBC7)

@Composable
fun MathIslandTheme(content: @Composable () -> Unit) {
    val colors = darkColorScheme(
        primary = Sand,
        secondary = Coral,
        tertiary = Seaweed,
        background = Ocean,
        surface = DeepWater,
        onPrimary = DeepWater,
        onSecondary = Color.White,
        onBackground = Foam,
        onSurface = Foam
    )

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

@Composable
fun MathIslandApp() {
    val context = LocalContext.current.applicationContext
    val container = remember(context) {
        runCatching {
            AppContainer.fromContext(context)
        }.getOrElse {
            val sampleIslands = sampleIslands()
            AppContainer.fromIslands(
                curriculumBundle = CurriculumBundle(
                    catalog = CurriculumCatalog(islands = emptyList()),
                    islands = emptyList()
                ),
                islands = sampleIslands,
                progressStore = com.mathisland.app.data.progress.InMemoryProgressStore()
            )
        }
    }
    val controller = remember(container) { container.gameController }
    val progressRepository = remember(container) { container.progressRepository }
    val getHomeStateUseCase = remember(container) { container.getHomeStateUseCase }
    val getParentSummaryUseCase = remember(container) { container.getParentSummaryUseCase }
    var state by remember(container) { mutableStateOf(progressRepository.load()) }
    var pendingMapFeedback by remember(container) { mutableStateOf<MapFeedbackUiState?>(null) }

    fun updateState(nextState: GameProgress) {
        state = nextState
        progressRepository.save(nextState)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surface,
                            Color(0xFF173B4F)
                        )
                    )
                )
                .padding(horizontal = 28.dp, vertical = 20.dp)
        ) {
            val homeState = remember(state) { HomeViewModel.uiState(getHomeStateUseCase) }
            val parentSummary = remember(state) { ParentSummaryViewModel.uiState(getParentSummaryUseCase()) }
            when (state.destination) {
                AppDestination.HOME -> HomeTabletScreen(
                    state = homeState,
                    onContinue = { updateState(controller.continueAdventure(state)) },
                    onOpenMap = { updateState(controller.openMap(state)) },
                    onOpenChest = { updateState(controller.openChest(state)) },
                    onOpenParent = { updateState(controller.openParentGate(state)) }
                )

                AppDestination.MAP -> MapTabletScreen(
                    state = MapViewModel.uiState(controller, state, pendingMapFeedback),
                    onBackHome = {
                        pendingMapFeedback = null
                        updateState(controller.goHome(state))
                    },
                    onOpenChest = {
                        pendingMapFeedback = null
                        updateState(controller.openChest(state))
                    },
                    onStartLesson = { lessonId ->
                        pendingMapFeedback = null
                        updateState(controller.startLesson(state, lessonId))
                    },
                    onConsumeFeedback = { pendingMapFeedback = null }
                )

                AppDestination.CHEST -> ChestTabletScreen(
                    state = ChestViewModel.uiState(state),
                    onBackHome = { updateState(controller.goHome(state)) },
                    onOpenMap = { updateState(controller.openMap(state)) }
                )

                AppDestination.LESSON -> {
                    val lessonState = LessonViewModel.uiState(controller, state)
                    if (lessonState != null) {
                        LessonTabletScreen(
                            state = lessonState,
                            onQuit = { updateState(controller.openMap(state)) },
                            onExpire = { updateState(controller.expireLesson(state)) },
                            answerPane = {
                                LessonAnswerPane(
                                    question = lessonState.question,
                                    onAnswer = { choice -> updateState(controller.answer(state, choice)) }
                                )
                            }
                        )
                    }
                }

                AppDestination.REWARD -> {
                    val rewardState = RewardViewModel.uiState(state)
                    if (rewardState != null) {
                        RewardTabletScreen(
                            state = rewardState,
                            onContinue = {
                                pendingMapFeedback = state.pendingReward?.toMapFeedback()
                                updateState(controller.claimReward(state))
                            },
                            onSecondaryAction = rewardState.reward.secondaryActionLessonId?.let { lessonId ->
                                { updateState(controller.startLesson(state, lessonId)) }
                            }
                        )
                    }
                }

                AppDestination.PARENT_GATE -> ParentGateFeatureScreen(
                    state = ParentGateViewModel.uiState(),
                    onAnswer = { answer -> updateState(controller.submitParentGateAnswer(state, answer)) },
                    onBackHome = { updateState(controller.goHome(state)) }
                )

                AppDestination.PARENT_SUMMARY -> ParentSummaryTabletScreen(
                    state = parentSummary,
                    onBackHome = { updateState(controller.closeParentSummary(state)) }
                )
            }
        }
    }
}

@Preview(widthDp = 1280, heightDp = 800)
@Composable
private fun MathIslandAppPreview() {
    MathIslandTheme {
        MathIslandApp()
    }
}

private fun RewardSummary.toMapFeedback(): MapFeedbackUiState? {
    val title = when {
        newIslandTitle != null -> "新岛已解锁"
        newStickerName != null -> "宝箱有新收藏"
        starsEarned > 0 -> "星星增加"
        else -> null
    } ?: return null

    val body = buildList {
        newIslandTitle?.let { islandTitle -> add("$islandTitle 已开放") }
        newStickerName?.let { stickerName -> add("$stickerName 已收入宝箱") }
        if (starsEarned > 0) {
            add("累计获得 $starsEarned 颗星星。")
        }
    }.joinToString("，")

    return MapFeedbackUiState(
        title = title,
        body = body,
        highlightedIslandId = newIslandId,
        starsEarned = starsEarned,
        chestReady = newStickerName != null
    )
}
