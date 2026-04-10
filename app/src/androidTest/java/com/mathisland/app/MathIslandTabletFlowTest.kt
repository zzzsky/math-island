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
import com.mathisland.app.di.AppContainer
import com.mathisland.app.domain.model.AppDestination
import com.mathisland.app.domain.model.ReviewTask
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

        answerChoiceSequence(listOf("44", "35", "62"))

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        composeRule.onNodeWithText("本关星星").assertIsDisplayed()
        returnToMapFromReward()

        composeRule.onNodeWithTag("map-total-stars").assertIsDisplayed()
        composeRule.onNodeWithTag("panel-lessons-list")
            .performScrollToNode(hasTestTag("panel-start-calc-carry-01"))
        composeRule.onNodeWithTag("panel-start-calc-carry-01")
            .performScrollTo()
            .assertIsDisplayed()
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

        answerChoiceSequence(listOf("34", "45", "62"))

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

        composeRule.onNodeWithTag("parent-summary-today-card").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-weak-card").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-streak-stat").assertIsDisplayed()
        composeRule.onNodeWithTag("parent-summary-recommended-card").assertIsDisplayed()
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

        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithTag("renderer-chant").fetchSemanticsNodes().isNotEmpty()
        }
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
    fun matchingLesson_flowCompletesAndReturnsToMap() {
        unlockClassificationIsland()

        openLessonFromMap("start-classification-match-02")
        answerMatchingSequence(listOf(1, 2, 0))

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        assertReturnedToMap()
    }

    @Test
    fun matchingAdvancedLesson_flowCompletesAndReturnsToMap() {
        unlockClassificationIsland()

        openLessonFromMap("start-classification-match-04")
        answerMatchingSequence(listOf(3, 2, 1, 0))

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        assertReturnedToMap()
    }

    @Test
    fun groupedMatchingLesson_flowCompletesAndReturnsToMap() {
        unlockClassificationIsland()

        openLessonFromMap("start-classification-match-05")
        answerGroupedMatchingSequence(
            listOf(
                Triple(0, 0, 1),
                Triple(0, 1, 0),
                Triple(1, 0, 1),
                Triple(1, 1, 0)
            )
        )

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        assertReturnedToMap()
    }

    @Test
    fun multiRoundMatchingLesson_flowCompletesAndReturnsToMap() {
        unlockClassificationIsland()

        openLessonFromMap("start-classification-match-06")
        answerMultiRoundMatchingSequence(
            listOf(
                listOf(1, 0),
                listOf(1, 0)
            )
        )

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        assertReturnedToMap()
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

        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithTag("renderer-number-pad").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("renderer-number-pad").assertIsDisplayed()
        inputNumberPadAnswer("63")

        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("第 2 / 3 题").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("第 2 / 3 题").assertIsDisplayed()
    }

    @Test
    fun fillBlankLesson_flowCompletesAndReturnsToMap() {
        clearCalculationIsland()

        openLessonFromMap("start-measure-fill-02")
        answerFillBlankSequence(listOf(1 to 0, 0 to 1))

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        assertReturnedToMap()
    }

    @Test
    fun fillBlankAdvancedLesson_flowCompletesAndReturnsToMap() {
        clearCalculationIsland()

        openLessonFromMap("start-measure-fill-04")
        answerFillBlankSequence(listOf(2 to 0, 0 to 1, 1 to 2))

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        assertReturnedToMap()
    }

    @Test
    fun fillBlankMixedLesson_flowCompletesAndReturnsToMap() {
        clearCalculationIsland()

        openLessonFromMap("start-measure-fill-05")
        answerFillBlankSequence(listOf(2 to 0, 0 to 1, 1 to 2))

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        assertReturnedToMap()
    }

    @Test
    fun fillBlankPartitionedLesson_flowCompletesAndReturnsToMap() {
        clearCalculationIsland()

        openLessonFromMap("start-measure-fill-06")
        answerFillBlankSequence(listOf(2 to 0, 3 to 1, 0 to 2, 1 to 3))

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        assertReturnedToMap()
    }

    @Test
    fun multiStepLesson_flowCompletesAndReturnsToMap() {
        unlockBigNumberIsland()

        openLessonFromMap("start-division-steps-02")
        answerMultiStepSequence(listOf(0, 1))

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        assertReturnedToMap()
    }

    @Test
    fun multiStepAdvancedLesson_flowCompletesAndReturnsToMap() {
        unlockBigNumberIsland()

        openLessonFromMap("start-division-steps-04")
        answerMultiStepSequence(listOf(0, 1, 1))

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        assertReturnedToMap()
    }

    @Test
    fun multiStepConditionalLesson_flowCompletesAndReturnsToMap() {
        unlockBigNumberIsland()

        openLessonFromMap("start-division-steps-05")
        answerMultiStepSequence(listOf(0, 0, 1))

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        assertReturnedToMap()
    }

    @Test
    fun multiStepConvergedConditionalLesson_flowCompletesAndReturnsToMap() {
        unlockBigNumberIsland()

        openLessonFromMap("start-division-steps-06")
        answerMultiStepSequence(listOf(1, 0, 0))

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        assertReturnedToMap()
    }

    @Test
    fun multiStepPresentationLesson_flowCompletesAndReturnsToMap() {
        unlockBigNumberIsland()

        openLessonFromMap("start-division-steps-07")
        answerMultiStepSequence(listOf(1, 0, 0, 0))

        composeRule.onNodeWithText("关卡完成").assertIsDisplayed()
        returnToMapFromReward()
        assertReturnedToMap()
    }

    @Test
    fun challengeSprintLesson_usesDistinctQuestionSet() {
        unlockChallengeIsland()

        openLessonFromMap("start-challenge-sprint-01")

        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithTag("renderer-number-pad").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("renderer-number-pad").assertIsDisplayed()
        composeRule.onAllNodesWithText("9 x 9 = ?").assertCountEquals(1)
        inputNumberPadAnswer("81")

        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("第 2 / 3 题").fetchSemanticsNodes().isNotEmpty()
        }
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

        composeRule.onAllNodesWithText("时间到，本次冲刺记为练习").assertCountEquals(1)
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
        answerChoiceSequence(listOf("44", "35", "62"))

        composeRule.onNodeWithTag("reward-retry-sprint").performScrollTo().assertIsDisplayed().performClick()

        composeRule.onNodeWithText("海图冲刺赛").assertIsDisplayed()
        composeRule.onNodeWithTag("lesson-timer").assertIsDisplayed()
    }

    @Test
    fun challengeSprintPerfectRun_showsGoldGrade() {
        unlockChallengeIsland()

        openLessonFromMap("start-challenge-sprint-01")
        inputNumberPadAnswer("81")
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("第 2 / 3 题").fetchSemanticsNodes().isNotEmpty()
        }
        inputNumberPadAnswer("42")
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("第 3 / 3 题").fetchSemanticsNodes().isNotEmpty()
        }
        inputNumberPadAnswer("300")

        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("金帆评级").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodesWithText("金帆评级").assertCountEquals(2)
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
        seedProgressForLessons(
            completedLessonIds = calculationLessonIds + measurementLessonIds,
            unlockedIslandIds = setOf(
                "calculation-island",
                "measurement-geometry-island",
                "multiplication-island"
            ),
            stickerNames = setOf(
                "Bridge Builder",
                "Ruler Ranger"
            )
        )
    }

    private fun unlockBigNumberIsland() {
        seedProgressForLessons(
            completedLessonIds = setOf(
                *calculationLessonIds.toTypedArray(),
                *measurementLessonIds.toTypedArray(),
                *multiplicationLessonIds.toTypedArray(),
                *divisionLessonIds.toTypedArray()
            ),
            unlockedIslandIds = setOf(
                "calculation-island",
                "measurement-geometry-island",
                "multiplication-island",
                "division-island",
                "big-number-island"
            ),
            stickerNames = setOf(
                "Bridge Builder",
                "Ruler Ranger",
                "Forest Singer",
                "Harbor Captain"
            )
        )
    }

    private fun unlockClassificationIsland() {
        seedProgressForLessons(
            completedLessonIds = setOf(
                *calculationLessonIds.toTypedArray(),
                *measurementLessonIds.toTypedArray(),
                *multiplicationLessonIds.toTypedArray(),
                *divisionLessonIds.toTypedArray(),
                *bigNumberLessonIds.toTypedArray()
            ),
            unlockedIslandIds = setOf(
                "calculation-island",
                "measurement-geometry-island",
                "multiplication-island",
                "division-island",
                "big-number-island",
                "classification-island"
            ),
            stickerNames = setOf(
                "Bridge Builder",
                "Ruler Ranger",
                "Forest Singer",
                "Harbor Captain",
                "Lighthouse Keeper"
            )
        )
    }

    private fun unlockChallengeIsland() {
        seedProgressForLessons(
            completedLessonIds = setOf(
                *calculationLessonIds.toTypedArray(),
                *measurementLessonIds.toTypedArray(),
                *multiplicationLessonIds.toTypedArray(),
                *divisionLessonIds.toTypedArray(),
                *bigNumberLessonIds.toTypedArray(),
                *classificationLessonIds.toTypedArray()
            ),
            unlockedIslandIds = setOf(
                "calculation-island",
                "measurement-geometry-island",
                "multiplication-island",
                "division-island",
                "big-number-island",
                "classification-island",
                "challenge-island"
            ),
            stickerNames = setOf(
                "Bridge Builder",
                "Ruler Ranger",
                "Forest Singer",
                "Harbor Captain",
                "Lighthouse Keeper",
                "Shell Sorter"
            )
        )
    }

    private fun unlockChallengeIslandWithPendingCalculationReview() {
        seedProgressForLessons(
            completedLessonIds = setOf(
                *calculationLessonIds.toTypedArray(),
                *measurementLessonIds.toTypedArray(),
                *multiplicationLessonIds.toTypedArray(),
                *divisionLessonIds.toTypedArray(),
                *bigNumberLessonIds.toTypedArray(),
                *classificationLessonIds.toTypedArray()
            ),
            unlockedIslandIds = setOf(
                "calculation-island",
                "measurement-geometry-island",
                "multiplication-island",
                "division-island",
                "big-number-island",
                "classification-island",
                "challenge-island"
            ),
            stickerNames = setOf(
                "Bridge Builder",
                "Ruler Ranger",
                "Forest Singer",
                "Harbor Captain",
                "Lighthouse Keeper",
                "Shell Sorter"
            ),
            pendingReviewFamily = "calculation"
        )
    }

    private fun completeLesson(startTag: String, answers: List<String>) {
        openLessonFromMap(startTag)
        answerChoiceSequence(answers)

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

    private fun assertReturnedToMap() {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag("map-scene-canvas").fetchSemanticsNodes().isNotEmpty() &&
                composeRule.onAllNodesWithTag("map-total-stars").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag("map-islands-list").fetchSemanticsNodes().isNotEmpty() &&
                composeRule.onAllNodesWithTag("island-overlay-sheet").fetchSemanticsNodes().isNotEmpty() &&
                composeRule.onAllNodesWithTag("panel-lessons-list").fetchSemanticsNodes().isNotEmpty()
        }
    }

    private fun inputNumberPadAnswer(answer: String) {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag("number-pad-submit").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithTag("renderer-number-pad")
            .performScrollToNode(hasTestTag("number-pad-submit"))
        answer.forEach { digit ->
            composeRule.onNodeWithTag("number-pad-key-$digit").performClick()
        }
        composeRule.onNodeWithTag("number-pad-submit").performClick()
    }

    private fun answerMatchingSequence(leftToRightAssignments: List<Int>) {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag("renderer-matching").fetchSemanticsNodes().isNotEmpty()
        }
        leftToRightAssignments.forEachIndexed { leftIndex, rightIndex ->
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-left-select-$leftIndex"))
            composeRule.onNodeWithTag("matching-left-select-$leftIndex").performClick()
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-right-assign-$rightIndex"))
            composeRule.onNodeWithTag("matching-right-assign-$rightIndex").performClick()
            composeRule.waitForIdle()
        }
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-submit"))
        composeRule.onNodeWithTag("matching-submit").performClick()
    }

    private fun answerGroupedMatchingSequence(assignments: List<Triple<Int, Int, Int>>) {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag("renderer-matching").fetchSemanticsNodes().isNotEmpty()
        }
        assignments.forEach { (groupIndex, leftIndex, rightIndex) ->
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-left-select-$groupIndex-$leftIndex"))
            composeRule.onNodeWithTag("matching-left-select-$groupIndex-$leftIndex").performClick()
            composeRule.onNodeWithTag("renderer-matching")
                .performScrollToNode(hasTestTag("matching-right-assign-$groupIndex-$rightIndex"))
            composeRule.onNodeWithTag("matching-right-assign-$groupIndex-$rightIndex").performClick()
            composeRule.waitForIdle()
        }
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-submit"))
        composeRule.onNodeWithTag("matching-submit").performClick()
    }

    private fun answerMultiRoundMatchingSequence(roundAssignments: List<List<Int>>) {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag("renderer-matching").fetchSemanticsNodes().isNotEmpty()
        }
        roundAssignments.forEachIndexed { roundIndex, assignments ->
            assignments.forEachIndexed { leftIndex, rightIndex ->
                composeRule.onNodeWithTag("renderer-matching")
                    .performScrollToNode(hasTestTag("matching-left-select-$leftIndex"))
                composeRule.onNodeWithTag("matching-left-select-$leftIndex").performClick()
                composeRule.waitForIdle()
                composeRule.onNodeWithTag("renderer-matching")
                    .performScrollToNode(hasTestTag("matching-right-assign-$rightIndex"))
                composeRule.onNodeWithTag("matching-right-assign-$rightIndex").performClick()
                composeRule.waitForIdle()
            }
            if (roundIndex < roundAssignments.lastIndex) {
                composeRule.onNodeWithTag("renderer-matching")
                    .performScrollToNode(hasTestTag("matching-next-round"))
                composeRule.onNodeWithTag("matching-next-round").performClick()
                composeRule.waitForIdle()
            }
        }
        composeRule.onNodeWithTag("renderer-matching")
            .performScrollToNode(hasTestTag("matching-submit"))
        composeRule.onNodeWithTag("matching-submit").performClick()
    }

    private fun answerFillBlankSequence(optionToSlotAssignments: List<Pair<Int, Int>>) {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag("renderer-fill-blank").fetchSemanticsNodes().isNotEmpty()
        }
        optionToSlotAssignments.forEach { (optionIndex, slotIndex) ->
            composeRule.onNodeWithTag("renderer-fill-blank")
                .performScrollToNode(hasTestTag("fill-blank-option-select-$optionIndex"))
            composeRule.onNodeWithTag("fill-blank-option-select-$optionIndex").performClick()
            composeRule.onNodeWithTag("renderer-fill-blank")
                .performScrollToNode(hasTestTag("fill-blank-slot-action-$slotIndex"))
            composeRule.onNodeWithTag("fill-blank-slot-action-$slotIndex").performClick()
            composeRule.waitForIdle()
        }
        composeRule.onNodeWithTag("renderer-fill-blank")
            .performScrollToNode(hasTestTag("fill-blank-submit"))
        composeRule.onNodeWithTag("fill-blank-submit").performClick()
    }

    private fun answerMultiStepSequence(choiceIndexes: List<Int>) {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithTag("renderer-multi-step").fetchSemanticsNodes().isNotEmpty()
        }
        choiceIndexes.forEach { choiceIndex ->
            composeRule.onNodeWithTag("renderer-multi-step")
                .performScrollToNode(hasTestTag("multi-step-choice-$choiceIndex"))
            composeRule.onNodeWithTag("multi-step-choice-$choiceIndex").performClick()
            composeRule.waitForIdle()
        }
        composeRule.onNodeWithTag("renderer-multi-step")
            .performScrollToNode(hasTestTag("multi-step-submit"))
        composeRule.onNodeWithTag("multi-step-submit").performClick()
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
            runCatching {
                composeRule.onAllNodesWithTag("island-overlay-sheet").fetchSemanticsNodes().isNotEmpty()
            }.getOrDefault(false)
        }
        if (composeRule.onAllNodesWithTag("panel-lessons-list").fetchSemanticsNodes().isNotEmpty()) {
            composeRule.onNodeWithTag("panel-lessons-list")
                .performScrollToNode(hasTestTag(panelStartTagForLesson(startTag)))
        }
        composeRule.onNodeWithTag(panelStartTagForLesson(startTag)).assertIsDisplayed().performClick()
        composeRule.waitUntil(5_000) {
            runCatching {
                composeRule.onAllNodesWithTag("lesson-support-rail").fetchSemanticsNodes().isNotEmpty()
            }.getOrDefault(false)
        }
    }

    private fun answerChoiceSequence(answers: List<String>) {
        answers.forEachIndexed { index, answer ->
            composeRule.waitUntil(5_000) {
                composeRule.onAllNodesWithTag("answer-$answer").fetchSemanticsNodes().isNotEmpty()
            }
            composeRule.onNodeWithTag("answer-$answer").performScrollTo().performClick()
            if (index < answers.lastIndex) {
                val nextQuestionLabel = "第 ${index + 2} / ${answers.size} 题"
                composeRule.waitUntil(10_000) {
                    composeRule.onAllNodesWithText(nextQuestionLabel).fetchSemanticsNodes().isNotEmpty()
                }
            }
        }
    }

    private fun seedProgressForLessons(
        completedLessonIds: Set<String>,
        unlockedIslandIds: Set<String>,
        stickerNames: Set<String>,
        pendingReviewFamily: String? = null
    ) {
        composeRule.activityRule.scenario.onActivity { activity ->
            val container = AppContainer.fromContext(activity)
            val seededState = container.gameController.initialState().copy(
                destination = AppDestination.HOME,
                unlockedIslandIds = unlockedIslandIds,
                completedLessonIds = completedLessonIds,
                totalStars = completedLessonIds.size * 3,
                stickerNames = stickerNames,
                pendingReview = pendingReviewFamily?.let(::ReviewTask)
            )
            DataStoreProgressStore(activity).save(seededState)
        }
        composeRule.activityRule.scenario.recreate()
        composeRule.onNodeWithText("数学岛").assertIsDisplayed()
        composeRule.onNodeWithTag("home-open-map").performClick()
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

    private companion object {
        val calculationLessonIds = setOf(
            "calc-carry-01",
            "calc-big-number-01"
        )

        val measurementLessonIds = setOf(
            "measure-ruler-01",
            "geometry-shape-01",
            "measure-fill-01",
            "measure-fill-02",
            "measure-fill-03",
            "measure-fill-04",
            "measure-fill-05",
            "measure-fill-06"
        )

        val multiplicationLessonIds = setOf(
            "multi-meaning-01",
            "multi-chant-01"
        )

        val divisionLessonIds = setOf(
            "division-share-01",
            "division-remainder-01",
            "division-steps-01",
            "division-steps-02",
            "division-steps-03",
            "division-steps-04",
            "division-steps-05",
            "division-steps-06",
            "division-steps-07"
        )

        val bigNumberLessonIds = setOf(
            "big-number-read-01",
            "big-number-sort-01"
        )

        val classificationLessonIds = setOf(
            "classification-shell-01",
            "classification-match-01",
            "classification-match-02",
            "classification-match-03",
            "classification-match-04",
            "classification-match-05",
            "classification-match-06"
        )
    }
}
