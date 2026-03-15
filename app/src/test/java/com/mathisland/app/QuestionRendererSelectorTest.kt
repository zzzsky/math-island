package com.mathisland.app

import org.junit.Assert.assertEquals
import org.junit.Test

class QuestionRendererSelectorTest {
    @Test
    fun questionFamilies_mapToExpectedRendererTypes() {
        assertEquals(QuestionRendererType.CHOICE, rendererTypeFor("calculation"))
        assertEquals(QuestionRendererType.RULER, rendererTypeFor("measurement"))
        assertEquals(QuestionRendererType.CHANT, rendererTypeFor("multiplication"))
        assertEquals(QuestionRendererType.GROUP, rendererTypeFor("division"))
        assertEquals(QuestionRendererType.SORT, rendererTypeFor("big-number"))
        assertEquals(QuestionRendererType.GROUP, rendererTypeFor("classification"))
        assertEquals(QuestionRendererType.NUMBER_PAD, rendererTypeFor("challenge"))
    }
}
