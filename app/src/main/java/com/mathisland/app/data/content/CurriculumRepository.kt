package com.mathisland.app.data.content

import android.content.res.AssetManager
import com.mathisland.app.domain.model.CurriculumBundle
import com.mathisland.app.domain.model.CurriculumCatalog
import com.mathisland.app.domain.model.CurriculumIslandContent
import java.io.File
import kotlinx.serialization.json.Json

object CurriculumRepository {
    private val json = Json { ignoreUnknownKeys = true }

    fun loadFromFiles(contentDir: File): CurriculumBundle {
        require(contentDir.exists()) {
            "Content directory does not exist: ${contentDir.path}"
        }

        return load(
            catalogText = File(contentDir, "catalog.json").readText(),
            islandReader = { assetPath -> File(contentDir, assetPath).readText() }
        )
    }

    fun loadFromAssets(assetManager: AssetManager): CurriculumBundle =
        load(
            catalogText = assetManager.open("content/catalog.json").bufferedReader().use { it.readText() },
            islandReader = { assetPath ->
                assetManager.open("content/$assetPath").bufferedReader().use { it.readText() }
            }
        )

    private fun load(
        catalogText: String,
        islandReader: (String) -> String
    ): CurriculumBundle {
        val catalog = json.decodeFromString<CurriculumCatalog>(catalogText)
        val islands = catalog.islands.map { entry ->
            json.decodeFromString<CurriculumIslandContent>(islandReader(entry.assetPath))
        }
        return CurriculumBundle(
            catalog = catalog,
            islands = islands
        )
    }
}
