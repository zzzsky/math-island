package com.mathisland.app.feature.map

import com.mathisland.app.domain.model.RewardSummary
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MapFeedbackMapperTest {
    @Test
    fun rewardToMapFeedback_marksNewIslandFlow() {
        val feedback = rewardToMapFeedback(
            RewardSummary(
                lessonTitle = "修桥加减法",
                starsEarned = 3,
                correctAnswers = 3,
                totalQuestions = 3,
                newIslandId = "measurement-island",
                newIslandTitle = "测量与图形岛",
                newStickerName = null
            )
        )

        requireNotNull(feedback)
        assertEquals(MapFeedbackKind.NewIsland, feedback.kind)
        assertEquals("measurement-island", feedback.highlightedIslandId)
        assertEquals("主线继续", feedback.summaryLabel)
    }

    @Test
    fun rewardToMapFeedback_marksChestFlow() {
        val feedback = rewardToMapFeedback(
            RewardSummary(
                lessonTitle = "修桥加减法",
                starsEarned = 3,
                correctAnswers = 3,
                totalQuestions = 3,
                newIslandId = null,
                newIslandTitle = null,
                newStickerName = "Bridge Builder"
            )
        )

        requireNotNull(feedback)
        assertEquals(MapFeedbackKind.Chest, feedback.kind)
        assertNull(feedback.highlightedIslandId)
        assertEquals("先看收藏", feedback.summaryLabel)
    }

    @Test
    fun rewardToMapFeedback_marksReplayFlow() {
        val feedback = rewardToMapFeedback(
            RewardSummary(
                lessonTitle = "海图冲刺赛",
                starsEarned = 0,
                correctAnswers = 1,
                totalQuestions = 3,
                newIslandId = null,
                newIslandTitle = null,
                newStickerName = null,
                timedOut = true,
                gradeLabel = "整备评级",
                gradeDescription = "先回放再发起下一轮冲刺。"
            )
        )

        requireNotNull(feedback)
        assertEquals(MapFeedbackKind.Replay, feedback.kind)
        assertEquals("先做回放", feedback.summaryLabel)
        assertEquals("回放路线已就位", feedback.summaryTitle)
    }
}
