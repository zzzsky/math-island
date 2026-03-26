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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.mathisland.app.ui.theme.TextToneTokens
import com.mathisland.app.ui.theme.TypographyTokens
import com.mathisland.app.feature.level.renderers.RendererActionState
import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState
import com.mathisland.app.feature.level.renderers.rendererActionStateFor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LevelTabletScreen(
    state: LevelUiState,
    onQuit: () -> Unit,
    onAnswer: (String) -> Unit,
    answerPane: @Composable (AnswerFeedbackUiState?, RendererActionState, (String) -> Unit) -> Unit,
    onExpire: (() -> Unit)? = null
) {
    val lesson = state.lesson
    val question = state.question
    var remainingSeconds by remember(lesson.id) {
        mutableIntStateOf(lesson.timeLimitSeconds ?: 0)
    }
    var didExpire by remember(lesson.id) { mutableStateOf(false) }
    var feedbackState by remember(lesson.id) { mutableStateOf(state.initialFeedback) }
    var inputEnabled by remember(lesson.id, state.questionIndex) { mutableStateOf(true) }
    var feedbackResetJob by remember(lesson.id) { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()

    fun scheduleFeedbackReset(delayMillis: Long) {
        feedbackResetJob?.cancel()
        feedbackResetJob = coroutineScope.launch {
            delay(delayMillis)
            feedbackState = state.initialFeedback
            feedbackResetJob = null
        }
    }

    LaunchedEffect(lesson.id, lesson.timeLimitSeconds) {
        didExpire = false
        feedbackResetJob?.cancel()
        feedbackResetJob = null
        inputEnabled = true
        feedbackState = state.initialFeedback
        val limitSeconds = lesson.timeLimitSeconds ?: return@LaunchedEffect
        remainingSeconds = limitSeconds
        while (!didExpire && remainingSeconds > 0) {
            delay(1_000)
            remainingSeconds -= 1
        }
        if (!didExpire && remainingSeconds == 0) {
            didExpire = true
            feedbackResetJob?.cancel()
            feedbackResetJob = null
            inputEnabled = false
            feedbackState = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.TimeoutExpired,
                title = "已超时",
                body = "本轮冲刺已经结束，这题按当前结果结算。",
                submittedAnswer = feedbackState?.submittedAnswer
            )
            onExpire?.invoke()
        }
    }

    LaunchedEffect(lesson.id, state.questionIndex, question.prompt) {
        feedbackResetJob?.cancel()
        feedbackResetJob = null
        inputEnabled = true
        feedbackState = state.initialFeedback
    }

    DisposableEffect(lesson.id, state.questionIndex, question.prompt) {
        onDispose {
            feedbackResetJob?.cancel()
            feedbackResetJob = null
        }
    }

    val handleAnswer: (String) -> Unit = answerHandler@{ answer ->
        if (!inputEnabled || didExpire || remainingSeconds == 0) return@answerHandler
        val answeredCorrectly = answer == question.correctChoice
        inputEnabled = false
        feedbackState = if (answeredCorrectly) {
            AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Correct,
                title = "答对了",
                body = if (lesson.timeLimitSeconds != null) {
                    "保持这个节奏，马上进入下一题。"
                } else {
                    "这题已经通过，继续往前推进。"
                },
                submittedAnswer = answer
            )
        } else {
            AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Incorrect,
                title = "再试一次",
                body = question.hint.ifBlank { "看看题目线索，再重新判断一次。" },
                submittedAnswer = answer
            )
        }
        if (!answeredCorrectly) {
            scheduleFeedbackReset(delayMillis = LevelMotionTokens.RetryBannerWindowMillis)
            coroutineScope.launch {
                delay(LevelMotionTokens.RetryLockWindowMillis)
                if (!didExpire && remainingSeconds > 0) {
                    inputEnabled = true
                }
            }
        }
        onAnswer(answer)
    }

    val attemptStatus = attemptStatusCardStateFor(
        lesson = lesson,
        feedback = feedbackState
    )
    val timerStatus = if (lesson.timeLimitSeconds != null) {
        timerPressureCardStateFor(
            lesson = lesson,
            remainingSeconds = remainingSeconds
        )
    } else {
        null
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
                                    style = TypographyTokens.SupportingLabel,
                                    color = TextToneTokens.high(MaterialTheme.colorScheme.onSurface)
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
                            style = TypographyTokens.BodySecondary,
                            color = TextToneTokens.medium(MaterialTheme.colorScheme.onSurface)
                        )
                        if (lesson.timeLimitSeconds != null) {
                            Text(
                                text = "倒计时结束会直接结算，本轮作为冲刺练习不计通关。",
                                style = TypographyTokens.BodyPrimary,
                                color = TextToneTokens.supporting(MaterialTheme.colorScheme.onSurface),
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
                        title = attemptStatus.title,
                        subtitle = attemptStatus.subtitle,
                        body = attemptStatus.body,
                        accentColor = lessonStatusAccentColor(attemptStatus.tone),
                        modifier = Modifier.testTag("lesson-attempt-status")
                    )
                    if (timerStatus != null) {
                        TabletInfoCard(
                            title = timerStatus.title,
                            subtitle = timerStatus.subtitle,
                            body = timerStatus.body,
                            accentColor = lessonStatusAccentColor(timerStatus.tone),
                            modifier = Modifier.testTag("lesson-timer-status")
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
            answerPane(
                feedbackState,
                rendererActionStateFor(
                    feedback = feedbackState,
                    inputEnabled = inputEnabled
                ),
                handleAnswer
            )
        }
    }
}

@Composable
private fun lessonStatusAccentColor(tone: LessonStatusTone): Color {
    return when (tone) {
        LessonStatusTone.Neutral -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.24f)
        LessonStatusTone.Highlight -> Color(0x809ADBC7)
        LessonStatusTone.Retry -> Color(0x80F2B880)
        LessonStatusTone.Confirmed -> Color(0x809ADBC7)
        LessonStatusTone.Warning -> Color(0x80D9D48A)
    }
}

@Composable
fun LevelTabletScreen(
    state: LevelUiState,
    onQuit: () -> Unit,
    answerPane: @Composable () -> Unit,
    onExpire: (() -> Unit)? = null
) {
    LevelTabletScreen(
        state = state,
        onQuit = onQuit,
        onAnswer = {},
        answerPane = { _, _, _ -> answerPane() },
        onExpire = onExpire
    )
}

private fun formatCountdown(totalSeconds: Int): String {
    val safeSeconds = totalSeconds.coerceAtLeast(0)
    val minutes = safeSeconds / 60
    val seconds = safeSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
