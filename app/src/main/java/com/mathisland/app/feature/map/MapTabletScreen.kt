package com.mathisland.app.feature.map

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.TypographyTokens
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

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
    val motionProgress = remember { Animatable(0f) }
    val motionValue = motionProgress.value
    val feedbackStars = activeFeedback?.starsEarned ?: 0
    val displayedStars = when {
        feedbackStars > 0 -> (state.totalStars - feedbackStars + (feedbackStars * motionValue).roundToInt())
            .coerceAtMost(state.totalStars)
        else -> state.totalStars
    }
    val starsScale = if (feedbackStars > 0) 1f + (motionValue * 0.12f) else 1f
    val chestScale = if (activeFeedback?.chestReady == true) 1f + (motionValue * 0.08f) else 1f
    val chestPulseAlpha = if (activeFeedback?.chestReady == true) 0.36f + (motionValue * 0.64f) else 0.35f

    LaunchedEffect(feedbackKey, dismissedFeedbackKey) {
        val feedback = activeFeedback ?: run {
            motionProgress.stop()
            motionProgress.snapTo(0f)
            return@LaunchedEffect
        }
        feedback.highlightedIslandId?.let { selectedIslandId = it }
        motionProgress.stop()
        motionProgress.snapTo(0f)
        motionProgress.animateTo(1f, tween(durationMillis = 260, easing = FastOutSlowInEasing))
        delay(420)
        onConsumeFeedback()
    }

    val selectedIsland = state.islands.firstOrNull { island -> island.id == selectedIslandId }
        ?: state.islands.firstOrNull()
    val preferredOverlayIslandId = activeFeedback?.highlightedIslandId ?: selectedIslandId

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Lg)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Md)
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
                MapProgressFeedback(feedback = feedback, motionProgress = motionValue)
                MapReturnSummaryCard(
                    feedback = feedback,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            IslandMapCanvas(
                islands = state.islands,
                selectedIslandId = selectedIsland?.id,
                highlightedIslandId = activeFeedback?.highlightedIslandId,
                motionProgress = motionValue,
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
                    style = TypographyTokens.FeatureTitle,
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
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
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
                        selectedIslandId = preferredOverlayIslandId
                    ),
                    onStartLesson = onStartLesson
                )
            }
        }
    }
}
