package com.mathisland.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mathisland.app.MathIslandGameController
import com.mathisland.app.di.AppContainer
import com.mathisland.app.domain.model.AppDestination
import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.RewardSummary
import com.mathisland.app.feature.map.MapFeedbackUiState

interface TabletRouteState {
    val destination: AppDestination
    val progress: GameProgress
    val pendingMapFeedback: MapFeedbackUiState?

    fun continueAdventure()
    fun openMap()
    fun openChest()
    fun openParentGate()
    fun goHome()
    fun startLesson(lessonId: String)
    fun answer(choice: String)
    fun expireLesson()
    fun claimReward()
    fun submitParentGateAnswer(answer: String)
    fun closeParentSummary()
    fun consumeMapFeedback()
}

@Composable
fun rememberTabletRouteState(container: AppContainer): TabletRouteState {
    val controller = remember(container) { container.gameController }
    val repository = remember(container) { container.progressRepository }
    return remember(container, controller, repository) {
        RememberedTabletRouteState(
            controller = controller,
            initialProgress = repository.load(),
            saveProgress = repository::save
        )
    }
}

@Stable
private class RememberedTabletRouteState(
    private val controller: MathIslandGameController,
    initialProgress: GameProgress,
    private val saveProgress: (GameProgress) -> Unit
) : TabletRouteState {
    override var progress by mutableStateOf(initialProgress)
        private set

    override var pendingMapFeedback by mutableStateOf<MapFeedbackUiState?>(null)
        private set

    override val destination: AppDestination
        get() = progress.destination

    override fun continueAdventure() = updateProgress(controller.continueAdventure(progress))

    override fun openMap() {
        pendingMapFeedback = null
        updateProgress(controller.openMap(progress))
    }

    override fun openChest() {
        pendingMapFeedback = null
        updateProgress(controller.openChest(progress))
    }

    override fun openParentGate() = updateProgress(controller.openParentGate(progress))

    override fun goHome() {
        pendingMapFeedback = null
        updateProgress(controller.goHome(progress))
    }

    override fun startLesson(lessonId: String) {
        pendingMapFeedback = null
        updateProgress(controller.startLesson(progress, lessonId))
    }

    override fun answer(choice: String) = updateProgress(controller.answer(progress, choice))

    override fun expireLesson() = updateProgress(controller.expireLesson(progress))

    override fun claimReward() {
        pendingMapFeedback = progress.pendingReward?.toMapFeedback()
        updateProgress(controller.claimReward(progress))
    }

    override fun submitParentGateAnswer(answer: String) =
        updateProgress(controller.submitParentGateAnswer(progress, answer))

    override fun closeParentSummary() = updateProgress(controller.closeParentSummary(progress))

    override fun consumeMapFeedback() {
        pendingMapFeedback = null
    }

    private fun updateProgress(nextState: GameProgress) {
        progress = nextState
        saveProgress(nextState)
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
