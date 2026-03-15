package com.mathisland.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CurriculumCatalog(
    val islands: List<CurriculumCatalogEntry>
)

@Serializable
data class CurriculumCatalogEntry(
    val id: String,
    val title: String,
    val assetPath: String,
    val sourceUnits: List<String>
)

@Serializable
data class CurriculumIslandContent(
    val id: String,
    val title: String,
    val subtitle: String,
    val sourceUnits: List<String>,
    val knowledgePoints: List<String>,
    val exampleTypes: List<String>,
    val lessons: List<CurriculumLesson>
)

@Serializable
data class CurriculumLesson(
    val id: String,
    val title: String,
    val summary: String,
    val questionFamily: String
)

data class CurriculumBundle(
    val catalog: CurriculumCatalog,
    val islands: List<CurriculumIslandContent>
)
