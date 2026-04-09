package com.mathisland.app.domain.model

enum class AppDestination {
    HOME,
    MAP,
    CHEST,
    LESSON,
    REWARD,
    PARENT_GATE,
    PARENT_SUMMARY
}

data class Question(
    val prompt: String,
    val choices: List<String>,
    val correctChoice: String,
    val hint: String,
    val family: String,
    val leftItems: List<String> = emptyList(),
    val rightItems: List<String> = emptyList(),
    val matchingGroups: List<MatchingGroup> = emptyList(),
    val blankParts: List<String> = emptyList(),
    val blankOptions: List<String> = emptyList(),
    val blankSlotKinds: List<String> = emptyList(),
    val stepPrompts: List<String> = emptyList(),
    val stepChoices: List<List<String>> = emptyList()
)

data class MatchingGroup(
    val title: String,
    val leftItems: List<String>,
    val rightItems: List<String>
)

data class Lesson(
    val id: String,
    val islandId: String,
    val title: String,
    val focus: String,
    val summary: String,
    val questions: List<Question>,
    val timeLimitSeconds: Int? = null,
    val isReview: Boolean = false
)

data class Island(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val rewardSticker: String,
    val lessons: List<Lesson>
)

data class RewardSummary(
    val lessonTitle: String,
    val starsEarned: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val newIslandId: String?,
    val newIslandTitle: String?,
    val newStickerName: String?,
    val timedOut: Boolean = false,
    val gradeLabel: String? = null,
    val gradeDescription: String? = null,
    val secondaryActionLabel: String? = null,
    val secondaryActionLessonId: String? = null
)

data class ReviewTask(
    val questionFamily: String
)

data class ParentSummary(
    val todayLearned: List<String>,
    val weakTopics: List<String>,
    val streakDays: Int,
    val recommendedIsland: String
)

data class GameProgress(
    val destination: AppDestination,
    val unlockedIslandIds: Set<String>,
    val completedLessonIds: Set<String>,
    val totalStars: Int,
    val stickerNames: Set<String>,
    val activeLessonId: String?,
    val activeQuestionIndex: Int,
    val correctAnswersInLesson: Int,
    val lastWrongFamily: String?,
    val consecutiveWrongCount: Int,
    val scheduledReviewFamily: String?,
    val pendingReview: ReviewTask?,
    val todayLessonTitles: List<String>,
    val streakDays: Int,
    val lastStudyDayEpoch: Long?,
    val pendingReward: RewardSummary?
)
