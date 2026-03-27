package com.mathisland.app.feature.level

import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.domain.model.Question
import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState
import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class LevelStatusCardStateTest {
    private val baseQuestion = Question(
        prompt = "9 x 9 = ?",
        choices = listOf("81", "72", "99"),
        correctChoice = "81",
        hint = "先想想 9 个 9。",
        family = "challenge"
    )

    private val standardLesson = Lesson(
        id = "lesson-standard",
        islandId = "challenge",
        title = "普通闯关",
        focus = "综合挑战",
        summary = "正常节奏",
        questions = listOf(baseQuestion)
    )

    private val timedLesson = standardLesson.copy(
        id = "lesson-timed",
        title = "海图冲刺赛",
        timeLimitSeconds = 8
    )

    @Test
    fun attemptStatusCardState_defaultsToPreparationCopy() {
        val state = attemptStatusCardStateFor(
            lesson = standardLesson,
            feedback = null
        )

        assertEquals(LessonStatusTone.Neutral, state.tone)
        assertEquals("准备作答", state.subtitle)
        assertEquals("先看题目，再提交答案。", state.body)
    }

    @Test
    fun attemptStatusCardState_usesRetryCopyAfterIncorrectAnswer() {
        val state = attemptStatusCardStateFor(
            lesson = standardLesson,
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Incorrect,
                title = "再试一次",
                body = "先看题目线索，再重新判断一次。",
                submittedAnswer = "72"
            )
        )

        assertEquals(LessonStatusTone.Retry, state.tone)
        assertEquals("再次尝试", state.subtitle)
        assertEquals("先看题目线索，再重新判断一次。", state.body)
    }

    @Test
    fun attemptStatusCardState_usesConfirmedCopyAfterCorrectAnswer() {
        val state = attemptStatusCardStateFor(
            lesson = standardLesson,
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Correct,
                title = "答对了",
                body = "这题已经通过，继续往前推进。",
                submittedAnswer = "81"
            )
        )

        assertEquals(LessonStatusTone.Confirmed, state.tone)
        assertEquals("答案已确认", state.subtitle)
        assertEquals("马上进入下一题。", state.body)
    }

    @Test
    fun attemptStatusCardState_usesTimeoutExpiredCopyAfterTimerRunsOut() {
        val state = attemptStatusCardStateFor(
            lesson = timedLesson,
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.TimeoutExpired,
                title = "已超时",
                body = "本轮冲刺已经结束，这题按当前结果结算。",
                submittedAnswer = "72"
            )
        )

        assertEquals(LessonStatusTone.Warning, state.tone)
        assertEquals("本题已超时", state.subtitle)
        assertEquals("直接看下一题。", state.body)
    }

    @Test
    fun timerPressureCardState_highlightsFinalSprintWhenTimeIsLow() {
        val state = timerPressureCardStateFor(
            lesson = timedLesson,
            remainingSeconds = 2
        )

        assertEquals(LessonStatusTone.Warning, state.tone)
        assertEquals("最后冲刺", state.subtitle)
        assertEquals("只剩 00:02，先交最有把握的答案。", state.body)
    }

    @Test
    fun timerPressureCardState_skipsHalfwayStageForShortTimers() {
        val shortTimedLesson = timedLesson.copy(timeLimitSeconds = 4)

        val state = timerPressureCardStateFor(
            lesson = shortTimedLesson,
            remainingSeconds = 2
        )

        assertEquals(LessonStatusTone.Warning, state.tone)
        assertEquals("最后冲刺", state.subtitle)
    }

    @Test
    fun lessonStatusSurfaceColor_matchesExpectedTonePalette() {
        assertEquals(Color(0x0F000000), lessonStatusSurfaceColor(LessonStatusTone.Neutral))
        assertEquals(Color(0x1A9ADBC7), lessonStatusSurfaceColor(LessonStatusTone.Highlight))
        assertEquals(Color(0x1AF2B880), lessonStatusSurfaceColor(LessonStatusTone.Retry))
        assertEquals(Color(0x1A9ADBC7), lessonStatusSurfaceColor(LessonStatusTone.Confirmed))
        assertEquals(Color(0x1AD9D48A), lessonStatusSurfaceColor(LessonStatusTone.Warning))
    }
}
