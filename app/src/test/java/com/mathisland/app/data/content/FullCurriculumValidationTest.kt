package com.mathisland.app.data.content

import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Test

class FullCurriculumValidationTest {
    private val contentDir = sequenceOf(
        File("src/main/assets/content"),
        File("app/src/main/assets/content")
    ).first { candidate -> candidate.exists() }

    @Test
    fun everyKnowledgePoint_hasAtLeastOnePlayableLevel() {
        val curriculum = CurriculumRepository.loadFromFiles(contentDir)
        val report = validateCurriculumCoverage(curriculum)

        assertTrue(report.missingKnowledgePoints.isEmpty())
    }
}
