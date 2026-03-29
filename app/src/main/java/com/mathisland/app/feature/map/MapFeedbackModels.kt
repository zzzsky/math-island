package com.mathisland.app.feature.map

enum class MapFeedbackKind {
    NewIsland,
    Chest,
    Replay,
    Progress
}

data class MapFeedbackUiState(
    val kind: MapFeedbackKind = MapFeedbackKind.Progress,
    val title: String,
    val body: String,
    val highlightedIslandId: String? = null,
    val starsEarned: Int = 0,
    val chestReady: Boolean = false,
    val summaryTitle: String = title,
    val summaryBody: String = body,
    val summaryLabel: String = "回到地图",
    val detailLabel: String = "回地图后",
    val detailTitle: String = summaryTitle,
    val detailBody: String = summaryBody
)
