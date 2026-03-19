package com.mathisland.app.feature.level

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.components.StatusChip
import com.mathisland.app.ui.components.SurfaceCard
import com.mathisland.app.ui.components.TabletInfoCard
import com.mathisland.app.ui.components.StoryPanelCard
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.RadiusTokens
import com.mathisland.app.ui.theme.StatusVariant
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.SpacingTokens
import com.mathisland.app.ui.theme.TypographyTokens
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
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.Lg)
    ) {
        SurfaceCard(
            modifier = Modifier.weight(1f),
            level = SurfaceLevel.Page,
            containerColor = Color(0xC31A4150)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(SpacingTokens.Xxl),
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
            ) {
                StoryPanelCard(
                    modifier = Modifier.fillMaxWidth(),
                    level = SurfaceLevel.Secondary,
                    containerColor = Color.White.copy(alpha = 0.05f),
                    shape = RadiusTokens.CardLg
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = SpacingTokens.Lg, vertical = SpacingTokens.Md),
                        verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatusChip(
                                text = if (lesson.isReview) "小海鸥求助" else lesson.focus,
                                variant = if (lesson.isReview) StatusVariant.Caution else StatusVariant.Highlight
                            )
                            if (lesson.timeLimitSeconds != null) {
                                StatusChip(
                                    text = "限时 ${formatCountdown(remainingSeconds)}",
                                    modifier = Modifier.testTag("lesson-timer"),
                                    variant = StatusVariant.Caution
                                )
                            } else {
                                Text(
                                    text = "总星星 ${state.totalStars}",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                                )
                            }
                        }
                        Text(
                            text = lesson.title,
                            style = TypographyTokens.SectionTitle,
                            fontWeight = FontWeight.Bold
                        )
                        LinearProgressIndicator(
                            progress = { (state.questionIndex + 1).toFloat() / state.totalQuestions.toFloat() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RadiusTokens.Pill),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = Color.White.copy(alpha = 0.12f)
                        )
                        Text(
                            text = "第 ${state.questionIndex + 1} / ${state.totalQuestions} 题",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f)
                        )
                        if (lesson.timeLimitSeconds != null) {
                            Text(
                                text = "倒计时结束会直接结算，本轮作为冲刺练习不计通关。",
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.testTag("lesson-timer-note")
                            )
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)) {
                    StatusChip(
                        text = "线索与问题",
                        variant = StatusVariant.Neutral
                    )
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
                }

                Spacer(modifier = Modifier.weight(1f))
                ActionButton(
                    text = "返回地图",
                    onClick = onQuit,
                    role = ActionRole.Secondary,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        Column(
            modifier = Modifier.weight(0.95f),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.Sm)
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
