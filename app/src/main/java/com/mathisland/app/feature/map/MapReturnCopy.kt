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
        summaryTitle = "新主线已就位",
        summaryBody = "地图已经切到新岛，右侧会直接显示推荐课。",
        detailLabel = "回地图后",
        detailTitle = "先看新岛，再开始主线课",
        detailBody = "焦点会落到新岛，右侧面板会直接给出下一节主线课。",
        actionLabel = "优先动作",
        actionTitle = "开始主线课",
        actionBody = "先看新岛，再直接开始当前推荐的主线课程。",
        continueCtaLabel = "回地图看新岛",
        listBadge = "主线推荐",
        listBody = "下一节主线课已就位"
    )
    MapFeedbackKind.Chest -> MapReturnCopy(
        summaryLabel = "先看收藏",
        summaryTitle = "宝箱收藏已更新",
        summaryBody = "先打开宝箱，再回到当前推荐课。",
        detailLabel = "回地图后",
        detailTitle = "先开宝箱，再回到课程",
        detailBody = "宝箱入口会先被强调，当前推荐课仍然保留在右侧面板。",
        actionLabel = "优先动作",
        actionTitle = "先开宝箱",
        actionBody = "先看收藏更新，再回到当前推荐课程。",
        continueCtaLabel = "回地图开宝箱",
        listBadge = "宝箱优先",
        listBody = "先看收藏，再回到当前课程"
    )
    MapFeedbackKind.Replay -> MapReturnCopy(
        summaryLabel = "先做回放",
        summaryTitle = "回放路线已就位",
        summaryBody = "先处理回放，再决定是否重新冲刺。",
        detailLabel = "回地图后",
        detailTitle = "先回放，再决定是否重试",
        detailBody = "地图会先落到回放路线，右侧面板会优先给出复习或回放课程。",
        actionLabel = "优先动作",
        actionTitle = "先做回放",
        actionBody = "先完成回放或复习，再决定是否重新发起冲刺。",
        continueCtaLabel = "回地图做回放",
        listBadge = "回放优先",
        listBody = "先做回放，再决定是否继续冲刺"
    )
    MapFeedbackKind.Progress -> MapReturnCopy(
        summaryLabel = "继续推进",
        summaryTitle = "当前推荐已就位",
        summaryBody = "继续当前推荐课，地图和面板已经对齐。",
        detailLabel = "回地图后",
        detailTitle = "直接继续当前推荐课",
        detailBody = "地图焦点和右侧主按钮已经对齐到当前推荐路线。",
        actionLabel = "优先动作",
        actionTitle = "继续当前课程",
        actionBody = "直接进入当前推荐路线，不需要先切别的入口。",
        continueCtaLabel = "回地图继续",
        listBadge = "继续推进",
        listBody = "当前推荐课程已经整理好"
    )
}
