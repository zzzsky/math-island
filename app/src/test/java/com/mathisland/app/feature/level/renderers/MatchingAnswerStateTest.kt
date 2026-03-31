package com.mathisland.app.feature.level.renderers

import com.mathisland.app.QuestionRendererType
import com.mathisland.app.rendererTypeFor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MatchingAnswerStateTest {
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
        assertTrue(MatchingAnswerState().copy(assignments = mapOf(0 to 0, 1 to 1)).isComplete(2))
    }
}
