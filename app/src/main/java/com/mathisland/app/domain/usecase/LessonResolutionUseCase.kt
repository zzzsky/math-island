package com.mathisland.app.domain.usecase

import com.mathisland.app.domain.model.GameProgress
import com.mathisland.app.domain.model.Island
import com.mathisland.app.domain.model.Lesson
import com.mathisland.app.domain.model.ParentSummary
import com.mathisland.app.domain.model.Question

private const val CHALLENGE_REPLAY_LESSON_ID = "challenge-review-01"

class LessonResolutionUseCase(
    private val islands: List<Island>
) {
    private val lessonIndex: Map<String, Lesson> = islands
        .flatMap { island -> island.lessons }
        .associateBy { lesson -> lesson.id }
    private val islandIndex: Map<String, Island> = islands.associateBy { island -> island.id }
    private val reviewQuestionBank: Map<String, List<Question>> = islands
        .flatMap { island -> island.lessons }
        .flatMap { lesson -> lesson.questions }
        .groupBy { question -> question.family }
    private val reviewLessonSource: Map<String, Lesson> = islands
        .flatMap { island -> island.lessons }
        .flatMap { lesson -> lesson.questions.map { question -> question.family to lesson } }
        .toMap()

    fun currentLesson(state: GameProgress): Lesson? = state.activeLessonId?.let { lessonId ->
        dynamicLessonForState(state, lessonId)
            ?: lessonIndex[lessonId]
            ?: reviewLessonForState(state)?.takeIf { lesson -> lesson.id == lessonId }
    }

    fun currentQuestion(state: GameProgress): Question? {
        val lesson = currentLesson(state) ?: return null
        return lesson.questions.getOrNull(state.activeQuestionIndex)
    }

    fun resolveLesson(state: GameProgress, lessonId: String): Lesson =
        dynamicLessonForState(state, lessonId)
            ?: lessonIndex[lessonId]
            ?: reviewLessonForState(state)?.takeIf { lesson -> lesson.id == lessonId }
            ?: error("Lesson $lessonId was not found.")

    fun pendingReviewLesson(state: GameProgress): Lesson? =
        challengeReplayRecommendation(state) ?: reviewLessonForState(state)

    fun recommendedLesson(
        state: GameProgress,
        nextPlayableLesson: Lesson?
    ): Lesson? = pendingReviewLesson(state) ?: nextPlayableLesson

    fun parentSummary(
        state: GameProgress,
        recommendedLesson: Lesson?
    ): ParentSummary = ParentSummary(
        todayLearned = state.todayLessonTitles,
        weakTopics = state.pendingReview?.questionFamily?.let { family -> listOf(topicLabelForFamily(family)) }.orEmpty(),
        streakDays = state.streakDays,
        recommendedIsland = recommendedIslandTitle(state, recommendedLesson)
    )

    private fun dynamicLessonForState(state: GameProgress, lessonId: String): Lesson? =
        when (lessonId) {
            CHALLENGE_REPLAY_LESSON_ID -> challengeReplayLessonForState(state)
            else -> null
        }

    private fun reviewLessonForState(state: GameProgress): Lesson? {
        val family = state.pendingReview?.questionFamily ?: return null
        val sourceLesson = reviewLessonSource[family] ?: return null
        val questions = reviewQuestionBank[family]?.take(2).orEmpty()
        if (questions.isEmpty()) {
            return null
        }

        return Lesson(
            id = "review-$family",
            islandId = sourceLesson.islandId,
            title = "小海鸥求助",
            focus = "${sourceLesson.focus} 复习",
            summary = "先帮小海鸥复习 2 道同类型题目，再回到主线冒险。",
            questions = questions,
            isReview = true
        )
    }

    private fun challengeReplayLessonForState(state: GameProgress): Lesson? {
        val baseLesson = lessonIndex[CHALLENGE_REPLAY_LESSON_ID] ?: return null
        val family = state.pendingReview?.questionFamily ?: return baseLesson
        val questions = reviewQuestionBank[family]?.take(3).orEmpty()
        if (questions.isEmpty()) {
            return baseLesson
        }

        return baseLesson.copy(
            focus = "${topicLabelForFamily(family)} 回放",
            summary = "把之前容易出错的 ${topicLabelForFamily(family)} 题再过一遍。",
            questions = questions
        )
    }

    private fun challengeReplayRecommendation(state: GameProgress): Lesson? {
        if (state.pendingReview?.questionFamily != "challenge") {
            return null
        }
        val lesson = challengeReplayLessonForState(state) ?: return null
        return lesson.takeIf { replay ->
            state.unlockedIslandIds.contains(replay.islandId)
        }
    }

    private fun recommendedIslandTitle(
        state: GameProgress,
        recommendedLesson: Lesson?
    ): String = state.pendingReview?.questionFamily?.let(::islandTitleForFamily)
        ?: recommendedLesson?.let { lesson ->
            islandIndex[lesson.islandId]?.title ?: islands.first().title
        }
        ?: islands.last().title

    private fun topicLabelForFamily(family: String): String = when (family) {
        "calculation" -> "计算"
        "measurement" -> "测量与图形"
        "multiplication" -> "乘法口诀"
        "division" -> "平均分与除法"
        "big-number" -> "大数"
        "classification" -> "分类"
        "challenge" -> "综合挑战"
        else -> family
    }

    private fun islandTitleForFamily(family: String): String = when (family) {
        "calculation" -> "计算岛"
        "measurement" -> "测量与图形岛"
        "multiplication" -> "乘法口诀岛"
        "division" -> "平均分与除法岛"
        "big-number" -> "大数岛"
        "classification" -> "分类岛"
        "challenge" -> "综合挑战岛"
        else -> islands.first().title
    }
}
