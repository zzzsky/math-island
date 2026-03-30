package com.mathisland.app.feature.map

import com.mathisland.app.ui.components.ReturnResultStageState

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
    val detailBody: String = summaryBody,
    val actionLabel: String = "优先动作",
    val actionTitle: String = summaryTitle,
    val actionBody: String = detailBody,
    val stageState: ReturnResultStageState? = null
)
