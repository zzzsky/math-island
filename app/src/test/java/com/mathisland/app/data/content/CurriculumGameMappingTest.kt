package com.mathisland.app.data.content

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CurriculumGameMappingTest {
    private val contentDir = sequenceOf(
        File("src/main/assets/content"),
        File("app/src/main/assets/content")
    ).first { candidate -> candidate.exists() }

    @Test
    fun curriculumMapsToSevenGameIslands_inCatalogOrder() {
        val curriculum = CurriculumRepository.loadFromFiles(contentDir)

        val gameIslands = curriculumToGameIslands(curriculum)

        assertEquals(
            listOf(
                "计算岛",
                "测量与图形岛",
                "乘法口诀岛",
                "平均分与除法岛",
                "大数岛",
                "分类岛",
                "综合挑战岛"
            ),
            gameIslands.map { island -> island.title }
        )
    }

    @Test
    fun challengeIsland_mapsDistinctLessonsWithOwnQuestionSets() {
        val curriculum = CurriculumRepository.loadFromFiles(contentDir)

        val challengeIsland = curriculumToGameIslands(curriculum)
            .first { island -> island.id == "challenge-island" }

        assertEquals(
            listOf("challenge-mixed-01", "challenge-sprint-01", "challenge-review-01"),
            challengeIsland.lessons.map { lesson -> lesson.id }
        )
        assertNotEquals(
            challengeIsland.lessons[0].questions.first().prompt,
            challengeIsland.lessons[1].questions.first().prompt
        )
    }
}
