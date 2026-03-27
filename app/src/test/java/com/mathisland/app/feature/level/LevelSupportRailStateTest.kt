package com.mathisland.app.feature.level

import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.domain.model.Question
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LevelSupportRailStateTest {
    private val baseQuestion = Question(
        prompt = "26 + 18 = ?",
        choices = listOf("44", "45", "34"),
        correctChoice = "44",
        hint = "先算个位，再算十位。",
        family = "calculation"
    )

    @Test
    fun supportRailState_forUntimedLessonOmitsTimerArtifacts() {
        val state = levelSupportRailStateFor(
            state = LevelUiState(
                lesson = Lesson(
                    id = "calculation-01",
                    islandId = "calculation",
                    title = "海湾加法课",
                    focus = "加法",
                    summary = "2 题热身",
                    questions = listOf(baseQuestion)
                ),
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
        assertEquals(
            listOf("lesson-attempt-status", "lesson-next-step-card", "lesson-question-card"),
            state.cards.map { it.tag }
        )
        assertEquals("现在处理", state.cards[1].badgeText)
        assertEquals("先看题目", state.cards[2].badgeText)
    }

    @Test
    fun supportRailState_forTimedLessonIncludesTimerArtifacts() {
        val state = levelSupportRailStateFor(
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
        assertEquals("倒计时结束会直接结算，本轮不计通关。", state.timerNote)
        assertEquals(
            listOf("lesson-attempt-status", "lesson-next-step-card", "lesson-timer-status", "lesson-question-card"),
            state.cards.map { it.tag }
        )
        assertEquals("现在处理", state.cards[1].badgeText)
        assertEquals("先看题目", state.cards[3].badgeText)
    }
}
