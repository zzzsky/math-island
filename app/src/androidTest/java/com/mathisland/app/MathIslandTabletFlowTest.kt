package com.mathisland.app

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mathisland.app.data.progress.DataStoreProgressStore
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MathIslandTabletFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun resetProgress() {
        composeRule.activityRule.scenario.onActivity { activity ->
            DataStoreProgressStore(activity).clear()
        }
        composeRule.activityRule.scenario.recreate()
    }

    @Test
    fun homeToLessonToReward_flowWorks() {
        composeRule.onNodeWithText("数学岛").assertIsDisplayed()
        composeRule.onNodeWithTag("home-open-map").performClick()

        composeRule.onAllNodesWithText("计算岛").onFirst().assertIsDisplayed()
        openLessonFromMap("start-calc-carry-01")

        composeRule.onNodeWithText("第 1 / 3 题").assertIsDisplayed()
        composeRule.onNodeWithTag("answer-44").performClick()
        composeRule.onNodeWithTag("answer-35").performClick()
        composeRule.onNodeWithTag("answer-62").performClick()

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        composeRule.onNodeWithText("本关星星").assertIsDisplayed()
        returnToMapFromReward()

        composeRule.onNodeWithTag("map-total-stars").assertIsDisplayed()
        composeRule.onAllNodesWithText("再次练习").onFirst().assertIsDisplayed()
    }

    @Test
    fun clearingCalculationIsland_unlocksMeasurementIsland() {
        clearCalculationIsland()

        composeRule.onAllNodesWithText("测量与图形岛").onFirst().assertIsDisplayed()
        composeRule.onAllNodesWithTag("panel-start-measure-ruler-01").assertCountEquals(1)
    }

    @Test
    fun clearingCalculationIsland_addsStickerToChest() {
        clearCalculationIsland()

        composeRule.onNodeWithTag("map-open-chest").performClick()

        composeRule.onNodeWithText("宝箱收藏").assertIsDisplayed()
        composeRule.onNodeWithText("Bridge Builder").assertIsDisplayed()
        composeRule.onNodeWithText("累计星星 6 · 收集到 1 张岛屿贴纸").assertIsDisplayed()
    }

    @Test
    fun mapFeedback_isShownOnceAfterUnlockThenConsumed() {
        clearCalculationIsland()

        composeRule.onAllNodesWithTag("map-progress-feedback").assertCountEquals(1)
        composeRule.onAllNodesWithText("新岛已解锁").assertCountEquals(1)
        composeRule.onAllNodesWithTag("map-total-stars-pill").assertCountEquals(1)
        composeRule.onAllNodesWithTag("map-route-highlight-measurement-geometry-island").assertCountEquals(1)
        composeRule.onAllNodesWithTag("map-node-highlight-measurement-geometry-island").assertCountEquals(1)
        composeRule.onAllNodesWithTag("panel-start-measure-ruler-01").assertCountEquals(1)
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag("map-open-chest-pulse")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("返回首页").performClick()
        composeRule.onNodeWithTag("home-open-map").performClick()

        composeRule.onAllNodesWithTag("map-progress-feedback").assertCountEquals(0)
    }

    @Test
    fun progressPersistsAfterActivityRecreation() {
        composeRule.onNodeWithText("数学岛").assertIsDisplayed()
        composeRule.onNodeWithTag("home-open-map").performClick()

        completeLesson(
            startTag = "start-calc-carry-01",
            answers = listOf("44", "35", "62")
        )

        composeRule.activityRule.scenario.recreate()

        composeRule.onNodeWithTag("map-total-stars").assertIsDisplayed()
        composeRule.onAllNodesWithText("再次练习").onFirst().assertIsDisplayed()
    }

    @Test
    fun seagullReviewIsPrioritizedAfterRecreation() {
        composeRule.onNodeWithText("数学岛").assertIsDisplayed()
        composeRule.onNodeWithTag("home-open-map").performClick()
        openLessonFromMap("start-calc-carry-01")

        composeRule.onNodeWithTag("answer-34").performClick()
        composeRule.onNodeWithTag("answer-45").performClick()
        composeRule.onNodeWithTag("answer-62").performClick()

        returnToMapFromReward()
        composeRule.onNodeWithText("返回首页").performClick()

        composeRule.activityRule.scenario.recreate()

        composeRule.onNodeWithText("先完成 2 道同类型复习题，再回主线继续推进。").assertIsDisplayed()
        composeRule.onNodeWithTag("home-continue-adventure").performClick()

        composeRule.onNodeWithText("第 1 / 2 题").assertIsDisplayed()
    }

    @Test
    fun parentGateUnlocksSummaryCards() {
        composeRule.onNodeWithText("数学岛").assertIsDisplayed()
        composeRule.onNodeWithTag("home-open-parent").performClick()

        composeRule.onNodeWithText("家长入口").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-answer-15").performClick()

        composeRule.onNodeWithText("今日学习").assertIsDisplayed()
        composeRule.onNodeWithText("薄弱知识点").assertIsDisplayed()
        composeRule.onNodeWithText("连续学习").assertIsDisplayed()
        composeRule.onNodeWithText("建议优先复习").assertIsDisplayed()
    }

    @Test
    fun measurementLesson_usesRulerRenderer() {
        clearCalculationIsland()

        openLessonFromMap("start-measure-ruler-01")

        composeRule.onNodeWithTag("renderer-ruler").assertIsDisplayed()
        composeRule.onNodeWithTag("tablet-ruler-handle").assertIsDisplayed()
    }

    @Test
    fun multiplicationLesson_showsChantBeatStrip() {
        unlockMultiplicationIsland()

        openLessonFromMap("start-multi-meaning-01")

        composeRule.onNodeWithTag("renderer-chant").assertIsDisplayed()
        composeRule.onAllNodesWithTag("chant-beat-strip").assertCountEquals(1)
    }

    @Test
    fun classificationLesson_showsGroupingBaskets() {
        unlockClassificationIsland()

        openLessonFromMap("start-classification-shell-01")

        composeRule.onNodeWithTag("renderer-group").assertIsDisplayed()
        composeRule.onAllNodesWithTag("group-basket-zone").assertCountEquals(1)
    }

    @Test
    fun bigNumberLesson_showsSortSignalLights() {
        unlockBigNumberIsland()

        openLessonFromMap("start-big-number-read-01")

        composeRule.onNodeWithTag("renderer-sort").assertIsDisplayed()
        composeRule.onAllNodesWithTag("sort-signal-lights").assertCountEquals(1)
    }

    @Test
    fun challengeLesson_usesNumberPadRenderer() {
        unlockChallengeIsland()

        openLessonFromMap("start-challenge-mixed-01")

        composeRule.onNodeWithTag("renderer-number-pad").assertIsDisplayed()
        composeRule.onNodeWithTag("number-pad-key-6").assertIsDisplayed().performClick()
        composeRule.onNodeWithTag("number-pad-key-3").assertIsDisplayed().performClick()
        composeRule.onNodeWithTag("number-pad-submit").assertIsDisplayed().performClick()

        composeRule.onNodeWithText("第 2 / 3 题").assertIsDisplayed()
    }

    @Test
    fun challengeSprintLesson_usesDistinctQuestionSet() {
        unlockChallengeIsland()

        openLessonFromMap("start-challenge-sprint-01")

        composeRule.onNodeWithTag("renderer-number-pad").assertIsDisplayed()
        composeRule.onNodeWithText("9 x 9 = ?").assertIsDisplayed()
        composeRule.onNodeWithTag("number-pad-key-8").assertIsDisplayed().performClick()
        composeRule.onNodeWithTag("number-pad-key-1").assertIsDisplayed().performClick()
        composeRule.onNodeWithTag("number-pad-submit").assertIsDisplayed().performClick()

        composeRule.onNodeWithText("第 2 / 3 题").assertIsDisplayed()
    }

    @Test
    fun challengeSprintLesson_timesOutIntoRewardState() {
        unlockChallengeIsland()

        openLessonFromMap("start-challenge-sprint-01")

        composeRule.onNodeWithTag("lesson-timer").assertIsDisplayed()
        composeRule.waitUntil(12_000) {
            composeRule.onAllNodesWithText("时间到，本次冲刺记为练习")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("时间到，本次冲刺记为练习").assertIsDisplayed()
        composeRule.onNodeWithText("0").assertIsDisplayed()
    }

    @Test
    fun timedOutChallengeSprint_routesContinueAdventureToChallengeReplay() {
        unlockChallengeIsland()

        openLessonFromMap("start-challenge-sprint-01")
        composeRule.waitUntil(12_000) {
            composeRule.onAllNodesWithText("时间到，本次冲刺记为练习")
                .fetchSemanticsNodes().isNotEmpty()
        }
        returnToMapFromReward()
        composeRule.onNodeWithText("返回首页").performClick()
        composeRule.onNodeWithTag("home-continue-adventure").performClick()

        composeRule.onNodeWithText("错题回放站").assertIsDisplayed()
        composeRule.onNodeWithTag("renderer-number-pad").assertIsDisplayed()
    }

    @Test
    fun challengeReplayReward_offersRetrySprintAction() {
        unlockChallengeIslandWithPendingCalculationReview()

        openLessonFromMap("start-challenge-review-01")
        composeRule.onNodeWithTag("answer-44").performClick()
        composeRule.onNodeWithTag("answer-35").performClick()
        composeRule.onNodeWithTag("answer-62").performClick()

        composeRule.onNodeWithTag("reward-retry-sprint").assertIsDisplayed().performClick()

        composeRule.onNodeWithText("海图冲刺赛").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-timer").assertIsDisplayed()
    }

    @Test
    fun challengeSprintPerfectRun_showsGoldGrade() {
        unlockChallengeIsland()

        openLessonFromMap("start-challenge-sprint-01")
        inputNumberPadAnswer("81")
        inputNumberPadAnswer("42")
        inputNumberPadAnswer("300")

        composeRule.onNodeWithText("金帆评级").assertIsDisplayed()
    }

    @Test
    fun challengeReplayLesson_usesPendingReviewFamilyQuestions() {
        unlockChallengeIslandWithPendingCalculationReview()

        openLessonFromMap("start-challenge-review-01")

        composeRule.onNodeWithTag("renderer-choice").assertIsDisplayed()
        composeRule.onNodeWithText("26 + 18 = ?").assertIsDisplayed()
        composeRule.onNodeWithTag("answer-44").assertIsDisplayed().performClick()

        composeRule.onNodeWithText("第 2 / 3 题").assertIsDisplayed()
    }

    private fun clearCalculationIsland() {
        composeRule.onNodeWithText("数学岛").assertIsDisplayed()
        composeRule.onNodeWithTag("home-open-map").performClick()

        completeLesson(
            startTag = "start-calc-carry-01",
            answers = listOf("44", "35", "62")
        )
        completeLesson(
            startTag = "start-calc-big-number-01",
            answers = listOf("44", "35", "62")
        )
    }

    private fun unlockMultiplicationIsland() {
        clearCalculationIsland()
        completeLesson(
            startTag = "start-measure-ruler-01",
            answers = listOf("厘米", "100", "平行四边形")
        )
        completeLesson(
            startTag = "start-geometry-shape-01",
            answers = listOf("厘米", "100", "平行四边形")
        )
    }

    private fun unlockBigNumberIsland() {
        unlockMultiplicationIsland()
        completeLesson(
            startTag = "start-multi-meaning-01",
            answers = listOf("4 x 5", "七", "24")
        )
        completeLesson(
            startTag = "start-multi-chant-01",
            answers = listOf("4 x 5", "七", "24")
        )
        completeLesson(
            startTag = "start-division-share-01",
            answers = listOf("4", "6", "6")
        )
        completeLesson(
            startTag = "start-division-remainder-01",
            answers = listOf("4", "6", "6")
        )
    }

    private fun unlockClassificationIsland() {
        unlockBigNumberIsland()
        completeLesson(
            startTag = "start-big-number-read-01",
            answers = listOf("1036", "8848", "2500")
        )
        completeLesson(
            startTag = "start-big-number-sort-01",
            answers = listOf("1036", "8848", "2500")
        )
    }

    private fun unlockChallengeIsland() {
        unlockClassificationIsland()
        completeLesson(
            startTag = "start-classification-shell-01",
            answers = listOf("可以按不同标准分类", "分类统计", "分类统计")
        )
    }

    private fun unlockChallengeIslandWithPendingCalculationReview() {
        composeRule.onNodeWithText("数学岛").assertIsDisplayed()
        composeRule.onNodeWithTag("home-open-map").performClick()

        openLessonFromMap("start-calc-carry-01")
        composeRule.onNodeWithTag("answer-34").performClick()
        composeRule.onNodeWithTag("answer-45").performClick()
        composeRule.onNodeWithTag("answer-62").performClick()
        returnToMapFromReward()

        completeLesson(
            startTag = "start-calc-big-number-01",
            answers = listOf("44", "35", "62")
        )
        completeLesson(
            startTag = "start-measure-ruler-01",
            answers = listOf("厘米", "100", "平行四边形")
        )
        completeLesson(
            startTag = "start-geometry-shape-01",
            answers = listOf("厘米", "100", "平行四边形")
        )
        completeLesson(
            startTag = "start-multi-meaning-01",
            answers = listOf("4 x 5", "七", "24")
        )
        completeLesson(
            startTag = "start-multi-chant-01",
            answers = listOf("4 x 5", "七", "24")
        )
        completeLesson(
            startTag = "start-division-share-01",
            answers = listOf("4", "6", "6")
        )
        completeLesson(
            startTag = "start-division-remainder-01",
            answers = listOf("4", "6", "6")
        )
        completeLesson(
            startTag = "start-big-number-read-01",
            answers = listOf("1036", "8848", "2500")
        )
        completeLesson(
            startTag = "start-big-number-sort-01",
            answers = listOf("1036", "8848", "2500")
        )
        completeLesson(
            startTag = "start-classification-shell-01",
            answers = listOf("可以按不同标准分类", "分类统计", "分类统计")
        )
    }

    private fun completeLesson(startTag: String, answers: List<String>) {
        openLessonFromMap(startTag)
        composeRule.onNodeWithText("第 1 / 3 题").assertIsDisplayed()

        answers.forEach { answer ->
            composeRule.onNodeWithTag("answer-$answer").performClick()
        }

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag("map-scene-canvas")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun returnToMapFromReward() {
        composeRule.onNodeWithTag("reward-return-map").performScrollTo().performClick()
    }

    private fun inputNumberPadAnswer(answer: String) {
        answer.forEach { digit ->
            composeRule.onNodeWithTag("number-pad-key-$digit").performClick()
        }
        composeRule.onNodeWithTag("number-pad-submit").performClick()
    }

    private fun openLessonFromMap(startTag: String) {
        val islandId = islandIdForLessonStart(startTag)
        val mapNodeTag = "map-node-$islandId"
        val mapCardTag = "select-island-$islandId"

        when {
            composeRule.onAllNodesWithTag("map-islands-list").fetchSemanticsNodes().isNotEmpty() -> {
                composeRule.onNodeWithTag("map-islands-list")
                    .performScrollToNode(hasTestTag(mapCardTag))
                composeRule.onNodeWithTag(mapCardTag).performClick()
            }

            composeRule.onAllNodesWithTag(mapNodeTag).fetchSemanticsNodes().isNotEmpty() -> {
                composeRule.onNodeWithTag(mapNodeTag).performClick()
            }

            composeRule.onAllNodesWithTag(mapCardTag).fetchSemanticsNodes().isNotEmpty() -> {
                composeRule.onNodeWithTag(mapCardTag).performClick()
            }
        }

        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText(islandTitleForIslandId(islandId))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag(panelStartTagForLesson(startTag)).assertIsDisplayed().performClick()
    }

    private fun panelStartTagForLesson(startTag: String): String =
        "panel-${startTag}"

    private fun islandTitleForIslandId(islandId: String): String = when (islandId) {
        "calculation-island" -> "计算岛"
        "measurement-geometry-island" -> "测量与图形岛"
        "multiplication-island" -> "乘法口诀岛"
        "division-island" -> "平均分与除法岛"
        "big-number-island" -> "大数岛"
        "classification-island" -> "分类岛"
        "challenge-island" -> "综合挑战岛"
        else -> error("Unknown island id: $islandId")
    }

    private fun islandIdForLessonStart(startTag: String): String {
        val lessonId = startTag.removePrefix("start-")
        return when {
            lessonId.startsWith("calc-") -> "calculation-island"
            lessonId.startsWith("measure-") || lessonId.startsWith("geometry-") -> "measurement-geometry-island"
            lessonId.startsWith("multi-") -> "multiplication-island"
            lessonId.startsWith("division-") -> "division-island"
            lessonId.startsWith("big-number-") -> "big-number-island"
            lessonId.startsWith("classification-") -> "classification-island"
            lessonId.startsWith("challenge-") -> "challenge-island"
            else -> error("Unknown lesson tag: $startTag")
        }
    }
}
