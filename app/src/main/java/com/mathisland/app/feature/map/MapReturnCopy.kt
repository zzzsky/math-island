package com.mathisland.app.feature.map

data class MapReturnCopy(
    val summaryLabel: String,
    val summaryTitle: String,
    val summaryBody: String,
    val detailLabel: String,
    val detailTitle: String,
    val detailBody: String,
    val actionLabel: String,
    val actionTitle: String,
    val actionBody: String,
    val continueCtaLabel: String,
    val listBadge: String,
    val listBody: String
)

fun mapReturnCopy(kind: MapFeedbackKind): MapReturnCopy = when (kind) {
    MapFeedbackKind.NewIsland -> MapReturnCopy(
        summaryLabel = "主线继续",
        summaryTitle = "新岛可出发",
        summaryBody = "地图已经切到新岛，推荐主线课已经对齐。",
        detailLabel = "回地图后",
        detailTitle = "先看新岛",
        detailBody = "焦点会落到新岛，右侧会直接给出主线课。",
        actionLabel = "优先动作",
        actionTitle = "开始主线课",
        actionBody = "看完新岛后，直接开始当前推荐主线课。",
        continueCtaLabel = "回地图看新岛",
        listBadge = "主线推荐",
        listBody = "主线课已就位"
    )
    MapFeedbackKind.Chest -> MapReturnCopy(
        summaryLabel = "先看收藏",
        summaryTitle = "宝箱可打开",
        summaryBody = "先看收藏更新，再回到当前推荐课。",
        detailLabel = "回地图后",
        detailTitle = "先开宝箱",
        detailBody = "宝箱入口会先被强调，课程仍然保留在右侧。",
        actionLabel = "优先动作",
        actionTitle = "先开宝箱",
        actionBody = "先看这次收藏更新，再回到当前推荐课。",
        continueCtaLabel = "回地图开宝箱",
        listBadge = "宝箱优先",
        listBody = "先看收藏更新"
    )
    MapFeedbackKind.Replay -> MapReturnCopy(
        summaryLabel = "先做回放",
        summaryTitle = "回放已就位",
        summaryBody = "先做回放，再决定是否重新冲刺。",
        detailLabel = "回地图后",
        detailTitle = "先做回放",
        detailBody = "地图会先落到回放路线，右侧优先给出复习课。",
        actionLabel = "优先动作",
        actionTitle = "先做回放",
        actionBody = "先完成回放或复习，再决定是否重开冲刺。",
        continueCtaLabel = "回地图做回放",
        listBadge = "回放优先",
        listBody = "先做回放"
    )
    MapFeedbackKind.Progress -> MapReturnCopy(
        summaryLabel = "继续推进",
        summaryTitle = "当前课已就位",
        summaryBody = "当前推荐课已经对齐，可以直接继续。",
        detailLabel = "回地图后",
        detailTitle = "直接继续",
        detailBody = "地图焦点和右侧主按钮已经对齐到当前路线。",
        actionLabel = "优先动作",
        actionTitle = "继续当前课程",
        actionBody = "直接进入当前推荐课，不需要先切别的入口。",
        continueCtaLabel = "回地图继续",
        listBadge = "继续推进",
        listBody = "当前课程已整理好"
    )
}
