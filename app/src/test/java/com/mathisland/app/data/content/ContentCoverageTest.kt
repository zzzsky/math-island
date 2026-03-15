package com.mathisland.app.data.content

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Test

class ContentCoverageTest {
    private val contentDir = sequenceOf(
        File("src/main/assets/content"),
        File("app/src/main/assets/content")
    ).first { candidate -> candidate.exists() }

    @Test
    fun catalog_declaresSevenIslands() {
        val curriculum = CurriculumRepository.loadFromFiles(contentDir)

        assertEquals(7, curriculum.catalog.islands.size)
    }

    @Test
    fun approvedSourceUnits_areFullyCovered() {
        val curriculum = CurriculumRepository.loadFromFiles(contentDir)

        assertEquals(
            setOf("二上-1", "二上-2", "二上-3-4-7", "二上-5", "二下-1", "二下-2-5", "二下-3", "二下-4", "二下-6", "二下-7"),
            ContentCoverage.coveredSourceUnits(curriculum)
        )
    }
}
