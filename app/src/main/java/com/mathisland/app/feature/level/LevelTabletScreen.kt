package com.mathisland.app.feature.level

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import com.mathisland.app.ui.components.ActionButton
import com.mathisland.app.ui.components.SurfaceCard
import com.mathisland.app.ui.theme.ActionRole
import com.mathisland.app.ui.theme.SurfaceLevel
import com.mathisland.app.ui.theme.SpacingTokens
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
    val isTimedLesson = lesson.timeLimitSeconds != null
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
                title = lessonFeedbackTitleFor(AnswerFeedbackKind.TimeoutExpired),
                body = "直接看下一题。",
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
        if (!inputEnabled || didExpire || (isTimedLesson && remainingSeconds == 0)) return@answerHandler
        val answeredCorrectly = answer == question.correctChoice
        inputEnabled = false
        feedbackState = if (answeredCorrectly) {
            AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Correct,
                title = lessonFeedbackTitleFor(AnswerFeedbackKind.Correct),
                body = "马上进入下一题。",
                submittedAnswer = answer
            )
        } else {
            AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Incorrect,
                title = lessonFeedbackTitleFor(AnswerFeedbackKind.Incorrect),
                body = question.hint.ifBlank { "先看提示，再判断答案。" },
                submittedAnswer = answer
            )
        }
        if (!answeredCorrectly) {
            scheduleFeedbackReset(delayMillis = LevelMotionTokens.RetryBannerWindowMillis)
            coroutineScope.launch {
                delay(LevelMotionTokens.RetryLockWindowMillis)
                if (!didExpire && (!isTimedLesson || remainingSeconds > 0)) {
                    inputEnabled = true
                }
            }
        }
        onAnswer(answer)
    }

    val supportRailState = levelSupportRailStateFor(
        state = state,
        remainingSeconds = remainingSeconds,
        feedback = feedbackState
    )

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
                LevelProgressHeroCard(
                    lesson = lesson,
                    questionIndex = state.questionIndex,
                    totalQuestions = state.totalQuestions,
                    supportState = supportRailState
                )

                LevelSupportRail(
                    state = supportRailState,
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                )

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
                    inputEnabled = inputEnabled,
                    isReview = lesson.isReview
                ),
                handleAnswer
            )
        }
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
