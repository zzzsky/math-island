package com.mathisland.app.feature.level.renderers

import com.mathisland.app.QuestionRendererType
import com.mathisland.app.rendererTypeFor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FillBlankAnswerStateTest {
    @Test
    fun encodedAnswer_followsSlotOrder() {
        val state = FillBlankAnswerState()
            .selectOption(1)
            .assignTo(0)
            .selectOption(0)
            .assignTo(1)

        val encoded = state.encodedAnswer(
            options = listOf("200", "100", "20"),
            slotCount = 2
        )

        assertEquals("100,200", encoded)
    }

    @Test
    fun assignTo_replacesPreviousSlotOccupant() {
        val state = FillBlankAnswerState()
            .selectOption(0)
            .assignTo(0)
            .selectOption(1)
            .assignTo(0)

        assertEquals(mapOf(0 to 1), state.assignments)
    }

    @Test
    fun fillBlankRendererType_isMapped() {
        assertEquals(QuestionRendererType.FILL_BLANK, rendererTypeFor("fill-blank"))
        assertTrue(FillBlankAnswerState(assignments = mapOf(0 to 0, 1 to 1)).isComplete(2))
    }
}
