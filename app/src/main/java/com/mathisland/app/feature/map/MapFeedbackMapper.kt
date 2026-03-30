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

    val copy = mapReturnCopy(kind)
    val summaryLabel = copy.summaryLabel

    return MapFeedbackUiState(
        kind = kind,
        title = title,
        body = body,
        highlightedIslandId = reward.newIslandId,
        starsEarned = reward.starsEarned,
        chestReady = reward.newStickerName != null,
        summaryLabel = summaryLabel,
        summaryTitle = reward.mapFeedbackSummaryTitle(),
        summaryBody = reward.mapFeedbackSummaryBody(),
        detailLabel = copy.detailLabel,
        detailTitle = copy.detailTitle,
        detailBody = copy.detailBody,
        actionLabel = copy.actionLabel,
        actionTitle = copy.actionTitle,
        actionBody = copy.actionBody
    )
}

internal fun RewardSummary.mapFeedbackSummaryTitle(): String =
    when {
        timedOut -> mapReturnCopy(MapFeedbackKind.Replay).summaryTitle
        newIslandTitle != null -> mapReturnCopy(MapFeedbackKind.NewIsland).summaryTitle
        newStickerName != null -> mapReturnCopy(MapFeedbackKind.Chest).summaryTitle
        gradeLabel != null -> gradeLabel
        else -> mapReturnCopy(MapFeedbackKind.Progress).summaryTitle
    }

internal fun RewardSummary.mapFeedbackSummaryBody(): String =
    when {
        timedOut -> mapReturnCopy(MapFeedbackKind.Replay).summaryBody
        newIslandTitle != null -> mapReturnCopy(MapFeedbackKind.NewIsland).summaryBody
        newStickerName != null -> mapReturnCopy(MapFeedbackKind.Chest).summaryBody
        gradeDescription != null -> gradeDescription
        else -> mapReturnCopy(MapFeedbackKind.Progress).summaryBody
    }
