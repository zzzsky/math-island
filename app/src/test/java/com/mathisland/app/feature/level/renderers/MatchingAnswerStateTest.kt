package com.mathisland.app.feature.level.renderers

import com.mathisland.app.QuestionRendererType
import com.mathisland.app.domain.model.MatchingGroup
import com.mathisland.app.rendererTypeFor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MatchingAnswerStateTest {
    @Test
    fun encodedAnswer_supportsGroupedMatches() {
        val groups = listOf(
            MatchingGroup(
                title = "看场景选算法",
                leftItems = listOf("平均分苹果", "合并两堆贝壳"),
                rightItems = listOf("用加法", "用除法")
            ),
            MatchingGroup(
                title = "看工具选用途",
                leftItems = listOf("尺子", "秤"),
                rightItems = listOf("测重量", "测长度")
            )
        )

        val state = MatchingAnswerState()
            .selectLeft(0, 0)
            .assignTo(0, 1)
            .selectLeft(0, 1)
            .assignTo(0, 0)
            .selectLeft(1, 0)
            .assignTo(1, 1)
            .selectLeft(1, 1)
            .assignTo(1, 0)

        assertEquals(
            "平均分苹果=用除法,合并两堆贝壳=用加法||尺子=测长度,秤=测重量",
            state.encodedAnswer(groups)
        )
    }

    @Test
    fun groupedMatching_isCompleteOnlyWhenEveryGroupIsFilled() {
        val groups = listOf(
            MatchingGroup(
                title = "A",
                leftItems = listOf("甲", "乙"),
                rightItems = listOf("一", "二")
            ),
            MatchingGroup(
                title = "B",
                leftItems = listOf("丙"),
                rightItems = listOf("三")
            )
        )

        val incomplete = MatchingAnswerState()
            .selectLeft(0, 0)
            .assignTo(0, 0)
            .selectLeft(1, 0)
            .assignTo(1, 0)
        val complete = incomplete
            .selectLeft(0, 1)
            .assignTo(0, 1)

        assertTrue(!incomplete.isComplete(groups))
        assertTrue(complete.isComplete(groups))
    }

    @Test
    fun encodedAnswer_followsLeftItemOrder() {
        val state = MatchingAnswerState()
            .selectLeft(1)
            .assignTo(0)
            .selectLeft(0)
            .assignTo(2)
            .selectLeft(2)
            .assignTo(1)

        val encoded = state.encodedAnswer(
            leftItems = listOf("尺子", "秤", "时钟"),
            rightItems = listOf("重量", "时间", "长度")
        )

        assertEquals("尺子=长度,秤=重量,时钟=时间", encoded)
    }

    @Test
    fun assignTo_replacesPreviousTargetOccupant() {
        val state = MatchingAnswerState()
            .selectLeft(0)
            .assignTo(1)
            .selectLeft(1)
            .assignTo(1)

        assertEquals(mapOf(1 to 1), state.assignments)
    }

    @Test
    fun matchingRendererType_isMapped() {
        assertEquals(QuestionRendererType.MATCHING, rendererTypeFor("matching"))
        assertTrue(MatchingAnswerState().copy(assignmentsByGroup = mapOf(0 to mapOf(0 to 0, 1 to 1))).isComplete(2))
    }
}
