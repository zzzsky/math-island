package com.mathisland.app.feature.map

import com.mathisland.app.domain.model.RewardSummary
import androidx.compose.ui.graphics.Color
import com.mathisland.app.ui.theme.StatusVariant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MapFeedbackMotionTest {
    @Test
    fun motionSpec_distinguishesMapFeedbackKinds() {
        val newIsland = MapFeedbackKind.NewIsland.motionSpec()
        val replay = MapFeedbackKind.Replay.motionSpec()

        assertEquals(StatusVariant.Recommended, newIsland.badgeVariant)
        assertEquals(Color(0xFFF2D48B), newIsland.accent)
        assertEquals(StatusVariant.Highlight, replay.badgeVariant)
        assertEquals(Color(0xFF7FC2D8), replay.accent)
        assertTrue(newIsland.chipRevealAt < newIsland.summaryRevealAt)
        assertTrue(newIsland.summaryRevealAt < newIsland.spotlightRevealAt)
        assertTrue(replay.detailRevealAt > newIsland.detailRevealAt)
        assertTrue(replay.supportingRevealAt > replay.detailRevealAt)
        assertTrue(replay.trailingRevealAt > replay.supportingRevealAt)
    }

    @Test
    fun rewardFeedbackKind_prefersTimeoutBeforeOtherRewardSignals() {
        val reward = RewardSummary(
            lessonTitle = "海图冲刺赛",
            starsEarned = 3,
            correctAnswers = 2,
            totalQuestions = 3,
            newIslandId = "measurement-island",
            newIslandTitle = "测量与图形岛",
            newStickerName = "Bridge Builder",
            timedOut = true
        )

        assertEquals(MapFeedbackKind.Replay, rewardFeedbackKind(reward))
    }
}
