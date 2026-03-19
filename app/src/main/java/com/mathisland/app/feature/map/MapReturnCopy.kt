package com.mathisland.app.feature.map

data class MapReturnCopy(
    val summaryLabel: String,
    val summaryTitle: String,
    val summaryBody: String,
    val listBadge: String,
    val listBody: String
)

fun mapReturnCopy(kind: MapFeedbackKind): MapReturnCopy = when (kind) {
    MapFeedbackKind.NewIsland -> MapReturnCopy(
        summaryLabel = "主线继续",
        summaryTitle = "新主线已就位",
        summaryBody = "地图已经切到新岛，右侧会直接显示推荐课。",
        listBadge = "主线推荐",
        listBody = "下一节主线课已就位"
    )
    MapFeedbackKind.Chest -> MapReturnCopy(
        summaryLabel = "先看收藏",
        summaryTitle = "宝箱收藏已更新",
        summaryBody = "先打开宝箱，再回到当前推荐课。",
        listBadge = "宝箱优先",
        listBody = "先看收藏，再回到当前课程"
    )
    MapFeedbackKind.Replay -> MapReturnCopy(
        summaryLabel = "先做回放",
        summaryTitle = "回放路线已就位",
        summaryBody = "先处理回放，再决定是否重新冲刺。",
        listBadge = "回放优先",
        listBody = "先做回放，再决定是否继续冲刺"
    )
    MapFeedbackKind.Progress -> MapReturnCopy(
        summaryLabel = "继续推进",
        summaryTitle = "当前推荐已就位",
        summaryBody = "继续当前推荐课，地图和面板已经对齐。",
        listBadge = "继续推进",
        listBody = "当前推荐课程已经整理好"
    )
}
