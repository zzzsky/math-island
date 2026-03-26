package com.mathisland.app.feature.level.renderers

import com.mathisland.app.ui.theme.ActionRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RendererActionStateTest {
    private val timeoutExpiredKind = AnswerFeedbackKind.valueOf("TimeoutExpired")

    @Test
    fun readyState_keepsDefaultLabelAndRole() {
        val state = rendererActionStateFor(
            feedback = null,
            inputEnabled = true
        )

        assertTrue(state.enabled)
        assertEquals("选择这个答案", state.resolveLabel("选择这个答案"))
        assertEquals(ActionRole.Primary, state.resolveRole(ActionRole.Primary))
        assertEquals("准备作答", state.sectionTitle())
        assertEquals("先看题目，再提交这次答案。", state.sectionBody())
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
        assertEquals("正在重试", state.sectionTitle())
        assertEquals("先看提示，再判断一次。", state.sectionBody())
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
        assertEquals("已确认", state.sectionTitle())
        assertEquals("答案已确认，马上进入下一题。", state.sectionBody())
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
        assertEquals("正在检查", state.sectionTitle())
        assertEquals("这次提交正在检查，请稍等片刻。", state.sectionBody())
    }

    @Test
    fun timeoutExpiredState_isDisabledAndTerminal() {
        val state = rendererActionStateFor(
            feedback = AnswerFeedbackUiState(
                kind = timeoutExpiredKind,
                title = "已超时",
                body = "本题时间已到"
            ),
            inputEnabled = false
        )

        assertFalse(state.enabled)
        assertEquals("已超时", state.resolveLabel("选择这个答案"))
        assertEquals(ActionRole.Secondary, state.resolveRole(ActionRole.Primary))
        assertEquals("已超时", state.sectionTitle())
        assertEquals("本题时间已到，请直接进入下一题。", state.sectionBody())
    }
}
