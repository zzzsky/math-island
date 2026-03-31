package com.mathisland.app.feature.level.renderers

import com.mathisland.app.QuestionRendererType
import com.mathisland.app.rendererTypeFor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MultiStepAnswerStateTest {
    @Test
    fun encodedAnswer_followsStepOrder() {
        val state = MultiStepAnswerState()
            .advance("平均分给 3 只小猴", 2)
            .advance("每只 4 个", 2)

        assertEquals("平均分给 3 只小猴,每只 4 个", state.encodedAnswer(2))
    }

    @Test
    fun reset_clearsRecordedSteps() {
        val state = MultiStepAnswerState()
            .advance("平均分给 3 只小猴", 2)
            .reset()

        assertEquals(emptyList<String>(), state.answers)
    }

    @Test
    fun multiStepRendererType_isMapped() {
        assertEquals(QuestionRendererType.MULTI_STEP, rendererTypeFor("multi-step"))
        assertTrue(MultiStepAnswerState(listOf("a", "b")).isComplete(2))
    }
}
