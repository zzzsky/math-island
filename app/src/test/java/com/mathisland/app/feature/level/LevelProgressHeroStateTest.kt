package com.mathisland.app.feature.level

import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.domain.model.Question
import com.mathisland.app.feature.level.renderers.AnswerFeedbackKind
import com.mathisland.app.feature.level.renderers.AnswerFeedbackUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LevelProgressHeroStateTest {
    private val baseQuestion = Question(
        prompt = "26 + 18 = ?",
        choices = listOf("44", "45", "34"),
        correctChoice = "44",
        hint = "先算个位，再算十位。",
        family = "calculation"
    )

    private val standardLesson = Lesson(
        id = "calculation-01",
        islandId = "calculation",
        title = "海湾加法课",
        focus = "加法",
        summary = "2 题热身",
        questions = listOf(baseQuestion)
    )

    @Test
    fun heroState_forUntimedLessonOmitsTimerArtifacts() {
        val state = levelProgressHeroStateFor(
            state = LevelUiState(
                lesson = standardLesson,
                question = baseQuestion,
                questionIndex = 0,
                totalQuestions = 2,
                totalStars = 8,
                flowHint = "完成本节后会先结算星星，再回地图继续探索。"
            ),
            remainingSeconds = 0,
            feedback = null
        )

        assertEquals("完成本节后会先结算星星，再回地图继续探索。", state.routeSummary)
        assertEquals("总星星 8", state.trailingSummary)
        assertNull(state.timerChipText)
        assertNull(state.timerNote)
        assertEquals("加法", state.heroBadgeText)
        assertEquals("当前路线", state.routeBadgeText)
    }

    @Test
    fun heroState_forTimedLessonIncludesTimerArtifacts() {
        val state = levelProgressHeroStateFor(
            state = LevelUiState(
                lesson = Lesson(
                    id = "challenge-sprint-01",
                    islandId = "challenge",
                    title = "海图冲刺赛",
                    focus = "综合挑战",
                    summary = "3 题限时冲刺",
                    questions = listOf(baseQuestion),
                    timeLimitSeconds = 8
                ),
                question = baseQuestion,
                questionIndex = 0,
                totalQuestions = 3,
                totalStars = 12,
                flowHint = "冲刺结束后会显示评级，并决定是否优先进入错题回放。"
            ),
            remainingSeconds = 5,
            feedback = null
        )

        assertEquals("冲刺结束后会显示评级，并决定是否优先进入错题回放。", state.routeSummary)
        assertEquals("限时 00:05", state.timerChipText)
        assertEquals("超时会直接结算，本题不计通关。", state.timerNote)
        assertEquals("综合挑战", state.heroBadgeText)
        assertEquals("冲刺提示", state.routeBadgeText)
    }

    @Test
    fun heroState_forRetryPromotesRetryHeroAndRouteBadges() {
        val state = levelProgressHeroStateFor(
            state = LevelUiState(
                lesson = standardLesson,
                question = baseQuestion,
                questionIndex = 0,
                totalQuestions = 2,
                totalStars = 8,
                flowHint = "完成本节后会先结算星星，再回地图继续探索。"
            ),
            remainingSeconds = 0,
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Incorrect,
                title = "再次尝试",
                body = "先看提示，再判断答案。",
                submittedAnswer = "72"
            )
        )

        assertEquals("再次尝试", state.heroBadgeText)
        assertEquals("重试提示", state.routeBadgeText)
    }
}
