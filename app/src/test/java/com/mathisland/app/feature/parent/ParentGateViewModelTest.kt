package com.mathisland.app.feature.parent

import org.junit.Assert.assertEquals
import org.junit.Test

class ParentGateViewModelTest {
    @Test
    fun uiState_exposesGatePromptAndChoices() {
        val uiState = ParentGateViewModel.uiState()

        assertEquals("家长入口", uiState.chipLabel)
        assertEquals("请先完成一道口算", uiState.title)
        assertEquals("8 + 7 = ?", uiState.question)
        assertEquals(listOf("14", "15", "16"), uiState.answers)
    }
}
