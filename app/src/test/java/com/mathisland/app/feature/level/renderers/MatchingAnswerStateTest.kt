package com.mathisland.app.feature.level.renderers

import com.mathisland.app.QuestionRendererType
import com.mathisland.app.domain.model.MatchingGroup
import com.mathisland.app.domain.model.MatchingRound
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
            state.encodeGroupsForSingleRound(groups)
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

        assertTrue(!incomplete.isGroupSetComplete(groups))
        assertTrue(complete.isGroupSetComplete(groups))
    }

    @Test
    fun encodedAnswer_supportsMultiRoundMatching() {
        val rounds = listOf(
            MatchingRound(
                title = "第一轮",
                prompt = "场景配算法",
                groups = listOf(
                    MatchingGroup(
                        title = "",
                        leftItems = listOf("平均分苹果", "合并两堆贝壳"),
                        rightItems = listOf("用加法", "用除法")
                    )
                )
            ),
            MatchingRound(
                title = "第二轮",
                prompt = "算法配作用",
                groups = listOf(
                    MatchingGroup(
                        title = "",
                        leftItems = listOf("用除法", "用加法"),
                        rightItems = listOf("求合起来一共多少", "求每份有多少")
                    )
                )
            )
        )

        val state = MatchingAnswerState()
            .selectLeft(0, 0)
            .assignTo(0, 1)
            .selectLeft(0, 1)
            .assignTo(0, 0)
            .advanceRound(rounds)
            .selectLeft(0, 0)
            .assignTo(0, 1)
            .selectLeft(0, 1)
            .assignTo(0, 0)

        assertEquals(
            "平均分苹果=用除法,合并两堆贝壳=用加法>>>用除法=求每份有多少,用加法=求合起来一共多少",
            state.encodeRounds(rounds)
        )
    }

    @Test
    fun advanceRound_movesOnlyAfterCurrentRoundCompletes() {
        val rounds = listOf(
            MatchingRound(
                title = "第一轮",
                prompt = "场景配算法",
                groups = listOf(
                    MatchingGroup(
                        title = "",
                        leftItems = listOf("平均分苹果"),
                        rightItems = listOf("用除法")
                    )
                )
            ),
            MatchingRound(
                title = "第二轮",
                prompt = "算法配作用",
                groups = listOf(
                    MatchingGroup(
                        title = "",
                        leftItems = listOf("用除法"),
                        rightItems = listOf("求每份有多少")
                    )
                )
            )
        )

        val incomplete = MatchingAnswerState()
        val complete = incomplete
            .selectLeft(0, 0)
            .assignTo(0, 0)

        assertEquals(0, incomplete.advanceRound(rounds).currentRoundIndex)
        assertEquals(1, complete.advanceRound(rounds).currentRoundIndex)
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
        assertTrue(
            MatchingAnswerState(
                assignmentsByRound = mapOf(0 to mapOf(0 to mapOf(0 to 0, 1 to 1)))
            ).isComplete(2)
        )
    }
}
