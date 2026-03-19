package com.mathisland.app.feature.level

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.components.SurfaceCard
import com.mathisland.app.ui.components.TabletChipLabel
import com.mathisland.app.ui.components.TabletInfoCard
import com.mathisland.app.ui.theme.SurfaceLevel
import kotlinx.coroutines.delay

@Composable
fun LevelTabletScreen(
    state: LevelUiState,
    onQuit: () -> Unit,
    answerPane: @Composable () -> Unit,
    onExpire: (() -> Unit)? = null
) {
    val lesson = state.lesson
    val question = state.question
    var remainingSeconds by remember(lesson.id) {
        mutableIntStateOf(lesson.timeLimitSeconds ?: 0)
    }
    var didExpire by remember(lesson.id) { mutableStateOf(false) }

    LaunchedEffect(lesson.id, lesson.timeLimitSeconds) {
        val limitSeconds = lesson.timeLimitSeconds ?: return@LaunchedEffect
        remainingSeconds = limitSeconds
        while (!didExpire && remainingSeconds > 0) {
            delay(1_000)
            remainingSeconds -= 1
        }
        if (!didExpire && remainingSeconds == 0) {
            didExpire = true
            onExpire?.invoke()
        }
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        SurfaceCard(
            modifier = Modifier.weight(1f),
            level = SurfaceLevel.Page,
            containerColor = Color(0xCC153C4A)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TabletChipLabel(text = if (lesson.isReview) "小海鸥求助" else lesson.focus)
                    if (lesson.timeLimitSeconds != null) {
                        TabletChipLabel(
                            text = "限时 ${formatCountdown(remainingSeconds)}",
                            modifier = Modifier.testTag("lesson-timer")
                        )
                    } else {
                        Text("总星星 ${state.totalStars}")
                    }
                }
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                LinearProgressIndicator(
                    progress = { (state.questionIndex + 1).toFloat() / state.totalQuestions.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(999.dp)),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = Color.White.copy(alpha = 0.12f)
                )
                Text(
                    text = "第 ${state.questionIndex + 1} / ${state.totalQuestions} 题",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                if (lesson.timeLimitSeconds != null) {
                    Text(
                        text = "倒计时结束会直接结算，本轮作为冲刺练习不计通关。",
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.testTag("lesson-timer-note")
                    )
                }
                TabletInfoCard(
                    title = "流程提示",
                    subtitle = "完成后会发生什么",
                    body = state.flowHint,
                    modifier = Modifier.testTag("lesson-flow-hint")
                )
                TabletInfoCard(
                    title = "题目",
                    subtitle = question.prompt,
                    body = question.hint
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = onQuit,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF40697A))
                ) {
                    Text("返回地图")
                }
            }
        }

        Column(
            modifier = Modifier.weight(0.95f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            answerPane()
        }
    }
}

private fun formatCountdown(totalSeconds: Int): String {
    val safeSeconds = totalSeconds.coerceAtLeast(0)
    val minutes = safeSeconds / 60
    val seconds = safeSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
