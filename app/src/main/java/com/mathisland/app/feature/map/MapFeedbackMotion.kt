package com.mathisland.app.feature.map

import androidx.compose.ui.graphics.Color
import com.mathisland.app.domain.model.RewardSummary
import com.mathisland.app.ui.theme.StatusVariant

internal data class MapFeedbackMotionSpec(
    val accent: Color,
    val badgeVariant: StatusVariant,
    val chipRevealAt: Float,
    val summaryRevealAt: Float,
    val spotlightRevealAt: Float,
    val detailRevealAt: Float,
    val supportingRevealAt: Float,
    val trailingRevealAt: Float,
    val cardScaleBoost: Float
)

internal fun MapFeedbackKind.motionSpec(): MapFeedbackMotionSpec =
    when (this) {
        MapFeedbackKind.NewIsland -> MapFeedbackMotionSpec(
            accent = Color(0xFFF2D48B),
            badgeVariant = StatusVariant.Recommended,
            chipRevealAt = 0.03f,
            summaryRevealAt = 0.06f,
            spotlightRevealAt = 0.18f,
            detailRevealAt = 0.34f,
            supportingRevealAt = 0.48f,
            trailingRevealAt = 0.58f,
            cardScaleBoost = 0.020f
        )
        MapFeedbackKind.Chest -> MapFeedbackMotionSpec(
            accent = Color(0xFFE8B86D),
            badgeVariant = StatusVariant.Highlight,
            chipRevealAt = 0.04f,
            summaryRevealAt = 0.08f,
            spotlightRevealAt = 0.22f,
            detailRevealAt = 0.38f,
            supportingRevealAt = 0.54f,
            trailingRevealAt = 0.62f,
            cardScaleBoost = 0.024f
        )
        MapFeedbackKind.Replay -> MapFeedbackMotionSpec(
            accent = Color(0xFF7FC2D8),
            badgeVariant = StatusVariant.Highlight,
            chipRevealAt = 0.05f,
            summaryRevealAt = 0.10f,
            spotlightRevealAt = 0.24f,
            detailRevealAt = 0.42f,
            supportingRevealAt = 0.58f,
            trailingRevealAt = 0.66f,
            cardScaleBoost = 0.018f
        )
        MapFeedbackKind.Progress -> MapFeedbackMotionSpec(
            accent = Color(0xFF3D93AF),
            badgeVariant = StatusVariant.Success,
            chipRevealAt = 0.04f,
            summaryRevealAt = 0.07f,
            spotlightRevealAt = 0.20f,
            detailRevealAt = 0.36f,
            supportingRevealAt = 0.50f,
            trailingRevealAt = 0.60f,
            cardScaleBoost = 0.016f
        )
    }

internal fun rewardFeedbackKind(reward: RewardSummary): MapFeedbackKind =
    when {
        reward.timedOut -> MapFeedbackKind.Replay
        reward.newIslandTitle != null -> MapFeedbackKind.NewIsland
        reward.newStickerName != null -> MapFeedbackKind.Chest
        else -> MapFeedbackKind.Progress
    }
