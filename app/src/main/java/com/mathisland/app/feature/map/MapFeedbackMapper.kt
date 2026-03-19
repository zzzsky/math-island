package com.mathisland.app.feature.map

import com.mathisland.app.domain.model.RewardSummary

fun rewardToMapFeedback(reward: RewardSummary): MapFeedbackUiState? {
    val title = when {
        reward.newIslandTitle != null -> "新岛已解锁"
        reward.newStickerName != null -> "宝箱有新收藏"
        reward.timedOut -> "回放路线已整理"
        reward.starsEarned > 0 -> "星星增加"
        else -> null
    } ?: return null

    val body = buildList {
        reward.newIslandTitle?.let { islandTitle -> add("$islandTitle 已开放") }
        reward.newStickerName?.let { stickerName -> add("$stickerName 已收入宝箱") }
        if (reward.timedOut) {
            add("综合挑战的回放与推荐路线已经整理好。")
        }
        if (reward.starsEarned > 0) {
            add("累计获得 ${reward.starsEarned} 颗星星。")
        }
    }.joinToString("，")

    val kind = when {
        reward.newIslandTitle != null -> MapFeedbackKind.NewIsland
        reward.newStickerName != null -> MapFeedbackKind.Chest
        reward.timedOut -> MapFeedbackKind.Replay
        else -> MapFeedbackKind.Progress
    }

    val summaryLabel = when (kind) {
        MapFeedbackKind.NewIsland -> "继续主线"
        MapFeedbackKind.Chest -> "打开宝箱"
        MapFeedbackKind.Replay -> "回放优先"
        MapFeedbackKind.Progress -> "继续航线"
    }

    return MapFeedbackUiState(
        kind = kind,
        title = title,
        body = body,
        highlightedIslandId = reward.newIslandId,
        starsEarned = reward.starsEarned,
        chestReady = reward.newStickerName != null,
        summaryLabel = summaryLabel,
        summaryTitle = reward.mapFeedbackSummaryTitle(),
        summaryBody = reward.mapFeedbackSummaryBody()
    )
}

internal fun RewardSummary.mapFeedbackSummaryTitle(): String =
    when {
        timedOut -> "先看回放，再决定是否重新冲刺"
        newIslandTitle != null -> "新主线已经准备好"
        newStickerName != null -> "宝箱里有新的收藏"
        gradeLabel != null -> gradeLabel
        else -> "继续当前推荐航线"
    }

internal fun RewardSummary.mapFeedbackSummaryBody(): String =
    when {
        timedOut -> "地图会优先保留综合挑战的回放与练习入口，方便你先消化本轮结果。"
        newIslandTitle != null -> "$newIslandTitle 已经成为当前焦点，右侧面板会直接显示下一节推荐课程。"
        newStickerName != null -> "宝箱按钮会高亮提示新贴纸，回地图后可以直接打开查看。"
        gradeDescription != null -> gradeDescription
        else -> "地图会保留当前推荐路线，继续按钮对应的下一步已经在右侧面板准备好。"
    }
