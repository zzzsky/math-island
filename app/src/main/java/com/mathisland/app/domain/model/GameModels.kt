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
    val matchingRounds: List<MatchingRound> = emptyList(),
    val blankParts: List<String> = emptyList(),
    val blankOptions: List<String> = emptyList(),
    val blankSlotKinds: List<String> = emptyList(),
    val stepPrompts: List<String> = emptyList(),
    val stepChoices: List<List<String>> = emptyList(),
    val stepBranchKeys: List<String> = emptyList(),
    val stepBranchRules: Map<String, List<StepBranchRule>> = emptyMap(),
    val stepBranchPrompts: Map<String, String> = emptyMap(),
    val stepBranchChoices: Map<String, List<String>> = emptyMap(),
    val stepPresentations: List<StepPresentation> = emptyList(),
    val stepBranchPresentations: Map<String, StepPresentation> = emptyMap(),
    val stepFeedbackHints: List<StepFeedbackHint> = emptyList()
)

data class MatchingGroup(
    val title: String,
    val leftItems: List<String>,
    val rightItems: List<String>
)

data class MatchingRound(
    val title: String,
    val prompt: String,
    val groups: List<MatchingGroup>
)

data class StepBranchRule(
    val whenAnswer: String,
    val nextBranchKey: String
)

data class StepPresentation(
    val stageTitle: String,
    val supportText: String,
    val answerLabel: String
)

data class StepFeedbackHint(
    val correctLabel: String? = null,
    val correctBody: String? = null,
    val incorrectLabel: String? = null,
    val incorrectBody: String? = null,
    val timeoutLabel: String? = null,
    val timeoutBody: String? = null,
    val expandOnIncorrect: Boolean = false,
    val expandOnTimeout: Boolean = false
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
