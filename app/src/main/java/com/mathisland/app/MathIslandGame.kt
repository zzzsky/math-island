package com.mathisland.app

import com.mathisland.app.domain.model.AppDestination
import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.Island
import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.domain.model.ParentSummary
import com.mathisland.app.domain.model.Question
import com.mathisland.app.domain.model.ReviewTask
import com.mathisland.app.domain.usecase.GetPendingReviewUseCase
import com.mathisland.app.domain.usecase.LessonResolutionUseCase
import com.mathisland.app.domain.usecase.ProgressionQueryUseCase
import com.mathisland.app.domain.usecase.SubmitLessonResultUseCase

class MathIslandGameController(
    val islands: List<Island>
) {
    private val islandIndex: Map<String, Island> = islands.associateBy { island -> island.id }
    private val lessonResolutionUseCase = LessonResolutionUseCase(islands)
    private val progressionQueryUseCase = ProgressionQueryUseCase(islands)
    private val submitLessonResultUseCase = SubmitLessonResultUseCase(islands)
    private val getPendingReviewUseCase = GetPendingReviewUseCase(this)

    fun initialState(): GameProgress = GameProgress(
        destination = AppDestination.HOME,
        unlockedIslandIds = setOf(islands.first().id),
        completedLessonIds = emptySet(),
        totalStars = 0,
        stickerNames = emptySet(),
        activeLessonId = null,
        activeQuestionIndex = 0,
        correctAnswersInLesson = 0,
        lastWrongFamily = null,
        consecutiveWrongCount = 0,
        scheduledReviewFamily = null,
        pendingReview = null,
        todayLessonTitles = emptyList(),
        streakDays = 0,
        lastStudyDayEpoch = null,
        pendingReward = null
    )

    fun continueAdventure(state: GameProgress): GameProgress {
        val nextLesson = recommendedLesson(state) ?: islands.first().lessons.first()
        return startLesson(state, nextLesson.id)
    }

    fun startLesson(state: GameProgress, lessonId: String): GameProgress {
        val lesson = lessonResolutionUseCase.resolveLesson(state, lessonId)
        require(state.unlockedIslandIds.contains(lesson.islandId)) {
            "Lesson $lessonId is not unlocked."
        }

        return state.copy(
            destination = AppDestination.LESSON,
            activeLessonId = lesson.id,
            activeQuestionIndex = 0,
            correctAnswersInLesson = 0,
            lastWrongFamily = null,
            consecutiveWrongCount = 0,
            scheduledReviewFamily = null,
            pendingReward = null
        )
    }

    fun openMap(state: GameProgress): GameProgress = state.copy(destination = AppDestination.MAP)

    fun openChest(state: GameProgress): GameProgress = state.copy(destination = AppDestination.CHEST)

    fun openParentGate(state: GameProgress): GameProgress = state.copy(destination = AppDestination.PARENT_GATE)

    fun submitParentGateAnswer(state: GameProgress, answer: String): GameProgress =
        if (answer == PARENT_GATE_CORRECT_ANSWER) {
            state.copy(destination = AppDestination.PARENT_SUMMARY)
        } else {
            state
        }

    fun closeParentSummary(state: GameProgress): GameProgress = state.copy(destination = AppDestination.HOME)

    fun goHome(state: GameProgress): GameProgress = state.copy(destination = AppDestination.HOME)

    fun parentSummary(state: GameProgress): ParentSummary =
        lessonResolutionUseCase.parentSummary(state, recommendedLesson(state))

    fun answer(state: GameProgress, choice: String): GameProgress {
        val lesson = currentLesson(state) ?: return state
        val question = lesson.questions[state.activeQuestionIndex]
        val answeredCorrectly = choice == question.correctChoice
        val correctAnswers = state.correctAnswersInLesson + if (answeredCorrectly) 1 else 0
        val lastWrongFamily = if (answeredCorrectly) {
            null
        } else {
            question.family
        }
        val consecutiveWrongCount = when {
            answeredCorrectly -> 0
            state.lastWrongFamily == question.family -> state.consecutiveWrongCount + 1
            else -> 1
        }
        val scheduledReviewFamily = when {
            lesson.isReview -> state.scheduledReviewFamily
            answeredCorrectly -> state.scheduledReviewFamily
            consecutiveWrongCount >= 2 -> state.scheduledReviewFamily ?: question.family
            else -> state.scheduledReviewFamily
        }
        val nextIndex = state.activeQuestionIndex + 1

        return if (nextIndex < lesson.questions.size) {
            state.copy(
                activeQuestionIndex = nextIndex,
                correctAnswersInLesson = correctAnswers,
                lastWrongFamily = lastWrongFamily,
                consecutiveWrongCount = consecutiveWrongCount,
                scheduledReviewFamily = scheduledReviewFamily
            )
        } else {
            completeLesson(state, lesson, correctAnswers)
        }
    }

    fun expireLesson(state: GameProgress): GameProgress {
        val lesson = currentLesson(state) ?: return state
        if (lesson.timeLimitSeconds == null) {
            return state
        }

        val outcome = submitLessonResultUseCase.onTimeout(state, lesson)
        val totalQuestions = lesson.questions.size

        return state.copy(
            destination = AppDestination.REWARD,
            activeLessonId = lesson.id,
            activeQuestionIndex = state.activeQuestionIndex.coerceAtMost(totalQuestions - 1),
            correctAnswersInLesson = state.correctAnswersInLesson,
            lastWrongFamily = null,
            consecutiveWrongCount = 0,
            scheduledReviewFamily = null,
            pendingReview = outcome.pendingReview,
            pendingReward = outcome.reward
        )
    }

    fun claimReward(state: GameProgress): GameProgress = state.copy(
        destination = AppDestination.MAP,
        activeLessonId = null,
        activeQuestionIndex = 0,
        correctAnswersInLesson = 0,
        lastWrongFamily = null,
        consecutiveWrongCount = 0,
        scheduledReviewFamily = null,
        pendingReward = null
    )

    fun currentLesson(state: GameProgress): Lesson? = lessonResolutionUseCase.currentLesson(state)

    fun currentQuestion(state: GameProgress): Question? = lessonResolutionUseCase.currentQuestion(state)

    fun recommendedLesson(state: GameProgress): Lesson? =
        lessonResolutionUseCase.recommendedLesson(state, getPendingReviewUseCase(state) ?: nextPlayableLesson(state))

    fun pendingReviewLesson(state: GameProgress): Lesson? =
        lessonResolutionUseCase.pendingReviewLesson(state)

    fun nextPlayableLesson(state: GameProgress): Lesson? = progressionQueryUseCase.nextPlayableLesson(state)

    fun islandProgress(state: GameProgress, island: Island): Float =
        progressionQueryUseCase.islandProgress(state, island)

    fun lessonCompleted(state: GameProgress, lesson: Lesson): Boolean =
        progressionQueryUseCase.lessonCompleted(state, lesson)

    private fun completeLesson(
        state: GameProgress,
        lesson: Lesson,
        correctAnswers: Int
    ): GameProgress {
        if (lesson.isReview) {
            val outcome = submitLessonResultUseCase.onLessonCompleted(
                state = state,
                lesson = lesson,
                correctAnswers = correctAnswers
            )
            val totalQuestions = lesson.questions.size
            return state.copy(
                destination = AppDestination.REWARD,
                totalStars = state.totalStars + outcome.starsEarned,
                activeLessonId = lesson.id,
                activeQuestionIndex = totalQuestions - 1,
                correctAnswersInLesson = correctAnswers,
                lastWrongFamily = null,
                consecutiveWrongCount = 0,
                scheduledReviewFamily = null,
                pendingReview = outcome.pendingReview,
                pendingReward = outcome.reward
            )
        }

        val totalQuestions = lesson.questions.size
        val completedLessonIds = state.completedLessonIds + lesson.id
        val unlockedIslandIds = progressionQueryUseCase.computeUnlockedIslands(completedLessonIds)
        val island = islandIndex.getValue(lesson.islandId)
        val previousCompletedCount = island.lessons.count { current ->
            state.completedLessonIds.contains(current.id)
        }
        val newCompletedCount = island.lessons.count { current ->
            completedLessonIds.contains(current.id)
        }
        val clearedIsland = previousCompletedCount < island.lessons.size &&
            newCompletedCount == island.lessons.size
        val newStickerName = island.rewardSticker.takeIf { clearedIsland }
        val stickerNames = if (newStickerName != null) {
            state.stickerNames + newStickerName
        } else {
            state.stickerNames
        }
        val newIslandId = unlockedIslandIds
            .minus(state.unlockedIslandIds)
            .firstOrNull()
        val newIslandTitle = newIslandId
            ?.let(islandIndex::get)
            ?.title
        val clearedChallengeReplay = lesson.id == CHALLENGE_REPLAY_LESSON_ID &&
            state.pendingReview != null &&
            correctAnswers == totalQuestions
        val pendingReview = when {
            state.scheduledReviewFamily != null -> ReviewTask(state.scheduledReviewFamily)
            clearedChallengeReplay -> null
            else -> state.pendingReview
        }
        val studySnapshot = updateStudySnapshot(state, lesson.title)
        val outcome = submitLessonResultUseCase.onLessonCompleted(
            state = state.copy(pendingReview = pendingReview),
                lesson = lesson,
                correctAnswers = correctAnswers,
                newIslandId = newIslandId,
                newIslandTitle = newIslandTitle,
                newStickerName = newStickerName
            )

        return state.copy(
            destination = AppDestination.REWARD,
            unlockedIslandIds = unlockedIslandIds,
            completedLessonIds = completedLessonIds,
            totalStars = state.totalStars + outcome.starsEarned,
            stickerNames = stickerNames,
            activeLessonId = lesson.id,
            activeQuestionIndex = totalQuestions - 1,
            correctAnswersInLesson = correctAnswers,
            lastWrongFamily = null,
            consecutiveWrongCount = 0,
            scheduledReviewFamily = null,
            pendingReview = outcome.pendingReview,
            todayLessonTitles = studySnapshot.lessonTitles,
            streakDays = studySnapshot.streakDays,
            lastStudyDayEpoch = studySnapshot.studyDayEpoch,
            pendingReward = outcome.reward
        )
    }

    private fun updateStudySnapshot(state: GameProgress, lessonTitle: String): StudySnapshot {
        val todayEpochDay = currentEpochDay()
        val streakDays = when (state.lastStudyDayEpoch) {
            null -> 1
            todayEpochDay -> maxOf(state.streakDays, 1)
            todayEpochDay - 1 -> state.streakDays + 1
            else -> 1
        }
        val baseTitles = if (state.lastStudyDayEpoch == todayEpochDay) {
            state.todayLessonTitles
        } else {
            emptyList()
        }
        val lessonTitles = if (lessonTitle in baseTitles) {
            baseTitles
        } else {
            baseTitles + lessonTitle
        }

        return StudySnapshot(
            lessonTitles = lessonTitles,
            streakDays = streakDays,
            studyDayEpoch = todayEpochDay
        )
    }
}

private data class StudySnapshot(
    val lessonTitles: List<String>,
    val streakDays: Int,
    val studyDayEpoch: Long
)

private const val PARENT_GATE_CORRECT_ANSWER = "15"
private const val CHALLENGE_REPLAY_LESSON_ID = "challenge-review-01"

private fun currentEpochDay(): Long = java.time.LocalDate.now().toEpochDay()

fun sampleIslands(): List<Island> {
    val calculationLessons = listOf(
        Lesson(
            id = "calc-bridge",
            islandId = "calculation",
            title = "修桥加减法",
            focus = "100 以内进位和退位",
            summary = "帮小海狸把桥板放回去，完成 3 道口算。",
            questions = listOf(
                Question(
                    prompt = "26 + 18 = ?",
                    choices = listOf("34", "44", "54"),
                    correctChoice = "44",
                    hint = "先算 20 + 10，再把 6 和 8 合起来。",
                    family = "calculation"
                ),
                Question(
                    prompt = "72 - 37 = ?",
                    choices = listOf("35", "45", "55"),
                    correctChoice = "35",
                    hint = "个位不够减，先借 1 个十。",
                    family = "calculation"
                ),
                Question(
                    prompt = "43 + 19 = ?",
                    choices = listOf("52", "62", "72"),
                    correctChoice = "62",
                    hint = "先算 43 + 10，再加 9。",
                    family = "calculation"
                )
            )
        ),
        Lesson(
            id = "calc-train",
            islandId = "calculation",
            title = "列车连加站",
            focus = "连加与加减混合",
            summary = "点亮列车车厢，完成 3 道混合运算。",
            questions = listOf(
                Question(
                    prompt = "18 + 27 + 15 = ?",
                    choices = listOf("50", "60", "70"),
                    correctChoice = "60",
                    hint = "先凑整：18 + 27 = 45。",
                    family = "calculation"
                ),
                Question(
                    prompt = "64 - 18 + 9 = ?",
                    choices = listOf("45", "55", "65"),
                    correctChoice = "55",
                    hint = "先算 64 - 18，再把结果加 9。",
                    family = "calculation"
                ),
                Question(
                    prompt = "95 - 27 - 18 = ?",
                    choices = listOf("40", "50", "60"),
                    correctChoice = "50",
                    hint = "先减 20，再减 7，再减 18。",
                    family = "calculation"
                )
            )
        )
    )

    val multiplicationLessons = listOf(
        Lesson(
            id = "multi-fruit",
            islandId = "multiplication",
            title = "果林口诀树",
            focus = "口诀填空",
            summary = "采果子前先念口诀，完成 3 道乘法题。",
            questions = listOf(
                Question(
                    prompt = "4 x 6 = ?",
                    choices = listOf("20", "24", "28"),
                    correctChoice = "24",
                    hint = "四六二十四。",
                    family = "multiplication"
                ),
                Question(
                    prompt = "3 x 7 = ?",
                    choices = listOf("18", "21", "24"),
                    correctChoice = "21",
                    hint = "三七二十一。",
                    family = "multiplication"
                ),
                Question(
                    prompt = "5 x 8 = ?",
                    choices = listOf("35", "40", "45"),
                    correctChoice = "40",
                    hint = "五八四十。",
                    family = "multiplication"
                )
            )
        ),
        Lesson(
            id = "multi-eq",
            islandId = "multiplication",
            title = "连加变乘法",
            focus = "看图列式",
            summary = "把重复加法改成乘法算式。",
            questions = listOf(
                Question(
                    prompt = "3 + 3 + 3 + 3 = ?",
                    choices = listOf("3 x 3", "4 x 3", "4 x 4"),
                    correctChoice = "4 x 3",
                    hint = "有 4 个 3。",
                    family = "multiplication"
                ),
                Question(
                    prompt = "2 + 2 + 2 + 2 + 2 = ?",
                    choices = listOf("2 x 5", "5 x 2", "5 x 5"),
                    correctChoice = "5 x 2",
                    hint = "有 5 组，每组 2。",
                    family = "multiplication"
                ),
                Question(
                    prompt = "6 + 6 + 6 = ?",
                    choices = listOf("3 x 6", "6 x 6", "2 x 6"),
                    correctChoice = "3 x 6",
                    hint = "有 3 个 6。",
                    family = "multiplication"
                )
            )
        )
    )

    val divisionLessons = listOf(
        Lesson(
            id = "div-monkey",
            islandId = "division",
            title = "猴子分水果",
            focus = "平均分",
            summary = "帮猴子把水果平均分给朋友。",
            questions = listOf(
                Question(
                    prompt = "12 个苹果平均分给 3 只猴子，每只几个？",
                    choices = listOf("3", "4", "5"),
                    correctChoice = "4",
                    hint = "12 里面有 3 个 4。",
                    family = "division"
                ),
                Question(
                    prompt = "18 颗葡萄平均放进 6 个盘子，每盘几颗？",
                    choices = listOf("2", "3", "4"),
                    correctChoice = "3",
                    hint = "六三十八。",
                    family = "division"
                ),
                Question(
                    prompt = "20 个贝壳平均分成 5 组，每组几个？",
                    choices = listOf("4", "5", "6"),
                    correctChoice = "4",
                    hint = "五四二十。",
                    family = "division"
                )
            )
        ),
        Lesson(
            id = "div-boat",
            islandId = "division",
            title = "小船运货队",
            focus = "有余数除法",
            summary = "看看最后一条船上还剩几个箱子。",
            questions = listOf(
                Question(
                    prompt = "14 个箱子，每船装 4 个，还剩几个？",
                    choices = listOf("1", "2", "3"),
                    correctChoice = "2",
                    hint = "4 + 4 + 4 = 12，还差 2。",
                    family = "division"
                ),
                Question(
                    prompt = "17 个桃子，每袋装 5 个，还剩几个？",
                    choices = listOf("1", "2", "3"),
                    correctChoice = "2",
                    hint = "5 + 5 + 5 = 15。",
                    family = "division"
                ),
                Question(
                    prompt = "22 本书，每捆装 6 本，还剩几个？",
                    choices = listOf("2", "3", "4"),
                    correctChoice = "4",
                    hint = "6 x 3 = 18。",
                    family = "division"
                )
            )
        )
    )

    return listOf(
        Island(
            id = "calculation",
            title = "计算岛",
            subtitle = "修桥与列车",
            description = "覆盖进位、退位、连加和混合运算。",
            rewardSticker = "Bridge Builder",
            lessons = calculationLessons
        ),
        Island(
            id = "multiplication",
            title = "乘法口诀岛",
            subtitle = "果林采集",
            description = "用口诀和连加转换巩固乘法意义。",
            rewardSticker = "Forest Singer",
            lessons = multiplicationLessons
        ),
        Island(
            id = "division",
            title = "平均分与除法岛",
            subtitle = "猴子和小船",
            description = "从平均分一路练到有余数除法。",
            rewardSticker = "Harbor Captain",
            lessons = divisionLessons
        )
    )
}
