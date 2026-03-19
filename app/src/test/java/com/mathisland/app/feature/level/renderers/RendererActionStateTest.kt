package com.mathisland.app.feature.level.renderers

import com.mathisland.app.ui.theme.ActionRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RendererActionStateTest {
    @Test
    fun readyState_keepsDefaultLabelAndRole() {
        val state = rendererActionStateFor(
            feedback = null,
            inputEnabled = true
        )

        assertTrue(state.enabled)
        assertEquals("选择这个答案", state.resolveLabel("选择这个答案"))
        assertEquals(ActionRole.Primary, state.resolveRole(ActionRole.Primary))
    }

    @Test
    fun retryState_swapsToRetryLabelAndSecondaryRole() {
        val state = rendererActionStateFor(
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Incorrect,
                title = "再试一次",
                body = "提示"
            ),
            inputEnabled = true
        )

        assertTrue(state.enabled)
        assertEquals("再试一次", state.resolveLabel("选择这个答案"))
        assertEquals(ActionRole.Secondary, state.resolveRole(ActionRole.Primary))
    }

    @Test
    fun confirmedState_usesCompletedRoleAndDisablesInput() {
        val state = rendererActionStateFor(
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Correct,
                title = "答对了",
                body = "继续推进"
            ),
            inputEnabled = false
        )

        assertFalse(state.enabled)
        assertEquals("已确认", state.resolveLabel("选择这个答案"))
        assertEquals(ActionRole.Completed, state.resolveRole(ActionRole.Primary))
    }

    @Test
    fun lockedState_preservesLabelButDisablesInput() {
        val state = rendererActionStateFor(
            feedback = AnswerFeedbackUiState(
                kind = AnswerFeedbackKind.Incorrect,
                title = "再试一次",
                body = "提示"
            ),
            inputEnabled = false
        )

        assertFalse(state.enabled)
        assertEquals("选择这个答案", state.resolveLabel("选择这个答案"))
        assertEquals(ActionRole.Secondary, state.resolveRole(ActionRole.Primary))
    }
}
