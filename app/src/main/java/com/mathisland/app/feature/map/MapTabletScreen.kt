package com.mathisland.app.feature.map

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.island.IslandOverlaySheet
import com.mathisland.app.feature.island.IslandViewModel
import com.mathisland.app.ui.components.IslandMapCanvas
import kotlinx.coroutines.delay

@Composable
fun MapTabletScreen(
    state: MapTabletUiState,
    onBackHome: () -> Unit,
    onOpenChest: () -> Unit,
    onStartLesson: (String) -> Unit,
    onConsumeFeedback: () -> Unit = {}
) {
    var selectedIslandId by remember(state.recommendedIslandId, state.islands) {
        mutableStateOf(state.recommendedIslandId)
    }
    val feedbackKey = state.feedback?.let { feedback ->
        listOf(
            feedback.title,
            feedback.body,
            feedback.highlightedIslandId.orEmpty(),
            feedback.starsEarned.toString(),
            feedback.chestReady.toString(),
            state.totalStars.toString()
        ).joinToString("|")
    }
    var dismissedFeedbackKey by remember { mutableStateOf<String?>(null) }
    val activeFeedback = state.feedback?.takeIf { feedbackKey != null && dismissedFeedbackKey != feedbackKey }
    var animatedStarsTarget by remember(state.totalStars) { mutableIntStateOf(state.totalStars) }
    var starsEmphasis by remember { mutableStateOf(false) }
    var chestPulse by remember { mutableStateOf(false) }
    val displayedStars by animateIntAsState(
        targetValue = animatedStarsTarget,
        animationSpec = tween(durationMillis = 600),
        label = "map-total-stars"
    )
    val starsScale by animateFloatAsState(
        targetValue = if (starsEmphasis) 1.08f else 1f,
        animationSpec = tween(durationMillis = 180),
        label = "map-stars-pill"
    )
    val chestScale by animateFloatAsState(
        targetValue = if (chestPulse) 1.06f else 1f,
        animationSpec = tween(durationMillis = 160),
        label = "map-chest-pulse"
    )
    val chestPulseAlpha by animateFloatAsState(
        targetValue = if (chestPulse) 1f else 0.35f,
        animationSpec = tween(durationMillis = 160),
        label = "map-chest-pulse-alpha"
    )

    LaunchedEffect(feedbackKey, dismissedFeedbackKey) {
        val feedback = activeFeedback ?: run {
            animatedStarsTarget = state.totalStars
            starsEmphasis = false
            chestPulse = false
            return@LaunchedEffect
        }
        feedback.highlightedIslandId?.let { selectedIslandId = it }
        animatedStarsTarget = (state.totalStars - feedback.starsEarned).coerceAtLeast(0)
        if (feedback.starsEarned > 0) {
            delay(80)
            starsEmphasis = true
            animatedStarsTarget = state.totalStars
            delay(360)
            starsEmphasis = false
        } else {
            starsEmphasis = false
        }
        delay(if (feedback.starsEarned > 0) 1360 else 1800)
        onConsumeFeedback()
    }

    LaunchedEffect(feedbackKey, dismissedFeedbackKey, activeFeedback?.chestReady) {
        val feedback = activeFeedback
        if (feedback?.chestReady != true) {
            chestPulse = false
            return@LaunchedEffect
        }
        repeat(3) {
            chestPulse = true
            delay(150)
            chestPulse = false
            delay(120)
        }
    }

    val selectedIsland = state.islands.firstOrNull { island -> island.id == selectedIslandId }
        ?: state.islands.firstOrNull()

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MapTopBar(
                totalStars = displayedStars,
                starsScale = starsScale,
                chestScale = chestScale,
                chestPulseAlpha = chestPulseAlpha,
                showChestPulse = activeFeedback?.chestReady == true,
                onBackHome = onBackHome,
                onOpenChest = {
                    if (state.feedback != null && feedbackKey != null) {
                        dismissedFeedbackKey = feedbackKey
                        onConsumeFeedback()
                    }
                    onOpenChest()
                }
            )

            activeFeedback?.let { feedback ->
                MapProgressFeedback(feedback = feedback)
            }

            IslandMapCanvas(
                islands = state.islands,
                selectedIslandId = selectedIsland?.id,
                highlightedIslandId = activeFeedback?.highlightedIslandId,
                onSelectIsland = { islandId ->
                    selectedIslandId = islandId
                    if (state.feedback != null && feedbackKey != null) {
                        dismissedFeedbackKey = feedbackKey
                        onConsumeFeedback()
                    }
                }
            )

            selectedIsland?.let { island ->
                Text(
                    text = "当前查看 ${island.title}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MapScreenTokens.SelectedTitle,
                    modifier = Modifier.testTag("map-selected-title")
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 220.dp)
                    .testTag("map-islands-list"),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(state.islands) { island ->
                    MapIslandListCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        island = island,
                        selected = island.id == selectedIsland?.id,
                        onSelect = {
                            selectedIslandId = island.id
                            if (state.feedback != null && feedbackKey != null) {
                                dismissedFeedbackKey = feedbackKey
                                onConsumeFeedback()
                            }
                        }
                    )
                }
            }
        }

        selectedIsland?.let {
            Column(modifier = Modifier.weight(0.95f)) {
                IslandOverlaySheet(
                    state = IslandViewModel.uiState(
                        mapState = state,
                        selectedIslandId = selectedIslandId
                    ),
                    onStartLesson = onStartLesson
                )
            }
        }
    }
}
