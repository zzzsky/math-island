package com.mathisland.app.feature.map

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.feature.island.IslandOverlaySheet
import com.mathisland.app.feature.island.IslandViewModel
import com.mathisland.app.ui.components.IslandMapCanvas
import com.mathisland.app.ui.components.TabletChipLabel
import com.mathisland.app.ui.theme.TabletDeepWater
import kotlinx.coroutines.delay

private val MapSeaweed = Color(0xFF4B6F44)

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onBackHome) {
                    Text("返回首页")
                }
                Box(
                    modifier = Modifier
                        .scale(chestScale)
                        .testTag("map-open-chest-container")
                ) {
                    Button(
                        modifier = Modifier.testTag("map-open-chest"),
                        onClick = {
                            if (state.feedback != null && feedbackKey != null) {
                                dismissedFeedbackKey = feedbackKey
                                onConsumeFeedback()
                            }
                            onOpenChest()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("打开宝箱")
                    }
                    if (activeFeedback?.chestReady == true) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = chestPulseAlpha),
                                    shape = RoundedCornerShape(999.dp)
                                )
                                .testTag("map-open-chest-pulse")
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .scale(starsScale)
                        .testTag("map-total-stars-pill"),
                    colors = CardDefaults.cardColors(containerColor = Color(0x402C647A)),
                    shape = RoundedCornerShape(999.dp)
                ) {
                    Text(
                        text = "总星星 $displayedStars",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .testTag("map-total-stars")
                    )
                }
            }

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
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedIslandId = island.id
                                if (state.feedback != null && feedbackKey != null) {
                                    dismissedFeedbackKey = feedbackKey
                                    onConsumeFeedback()
                                }
                            }
                            .testTag("select-island-${island.id}"),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                island.id == selectedIsland?.id -> Color(0xFF215A6D)
                                island.unlocked -> Color(0xCC18475A)
                                else -> Color(0x6618475A)
                            }
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = island.title,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = island.subtitle,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
                                    )
                                }
                                TabletChipLabel(
                                    text = when {
                                        island.id == selectedIsland?.id -> "当前焦点"
                                        island.completed -> "已完成"
                                        island.unlocked -> "已解锁"
                                        else -> "等待前岛完成"
                                    }
                                )
                            }

                            LinearProgressIndicator(
                                progress = { island.progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(999.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = Color.White.copy(alpha = 0.14f)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                island.lessons.forEach { lesson ->
                                    Button(
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("start-${lesson.id}"),
                                        onClick = { onStartLesson(lesson.id) },
                                        enabled = lesson.enabled,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (lesson.completed) {
                                                MapSeaweed
                                            } else {
                                                MaterialTheme.colorScheme.primary
                                            },
                                            contentColor = if (lesson.completed) {
                                                Color.White
                                            } else {
                                                TabletDeepWater
                                            }
                                        )
                                    ) {
                                        Text(if (lesson.completed) "再次练习" else "开始")
                                    }
                                }
                            }
                        }
                    }
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
