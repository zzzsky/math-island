package com.mathisland.app

import android.content.Context

private const val PREFS_NAME = "math_island_progress"
private const val KEY_DESTINATION = "destination"
private const val KEY_UNLOCKED_ISLANDS = "unlocked_islands"
private const val KEY_COMPLETED_LESSONS = "completed_lessons"
private const val KEY_TOTAL_STARS = "total_stars"
private const val KEY_STICKERS = "stickers"
private const val KEY_PENDING_REVIEW_FAMILY = "pending_review_family"
private const val KEY_TODAY_LESSONS = "today_lessons"
private const val KEY_STREAK_DAYS = "streak_days"
private const val KEY_LAST_STUDY_DAY = "last_study_day"

@Deprecated(
    message = "Compatibility delegate for legacy SharedPreferences progress only. Use data.progress.ProgressStore implementations directly."
)
internal class MathIslandProgressStore(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(initial: GameProgress): GameProgress {
        val restoredDestination = prefs.getString(KEY_DESTINATION, null)
            ?.let { name -> AppDestination.entries.firstOrNull { destination -> destination.name == name } }
            ?.takeIf { destination ->
                destination == AppDestination.HOME ||
                    destination == AppDestination.MAP ||
                    destination == AppDestination.CHEST
            }
            ?: initial.destination
        val unlockedIslandIds = prefs.getStringSet(KEY_UNLOCKED_ISLANDS, null)?.toSet()
            ?.takeIf(Set<String>::isNotEmpty)
            ?: initial.unlockedIslandIds
        val completedLessonIds = prefs.getStringSet(KEY_COMPLETED_LESSONS, null)?.toSet().orEmpty()
        val stickerNames = prefs.getStringSet(KEY_STICKERS, null)?.toSet().orEmpty()
        val pendingReviewFamily = prefs.getString(KEY_PENDING_REVIEW_FAMILY, null)
        val lastStudyDay = prefs.getString(KEY_LAST_STUDY_DAY, null)?.toLongOrNull()
        val isSameStudyDay = lastStudyDay == java.time.LocalDate.now().toEpochDay()

        return initial.copy(
            destination = restoredDestination,
            unlockedIslandIds = unlockedIslandIds,
            completedLessonIds = completedLessonIds,
            totalStars = prefs.getInt(KEY_TOTAL_STARS, initial.totalStars),
            stickerNames = stickerNames,
            pendingReview = pendingReviewFamily?.let(::ReviewTask),
            todayLessonTitles = if (isSameStudyDay) {
                prefs.getStringSet(KEY_TODAY_LESSONS, null)?.toList().orEmpty()
            } else {
                emptyList()
            },
            streakDays = prefs.getInt(KEY_STREAK_DAYS, initial.streakDays),
            lastStudyDayEpoch = lastStudyDay
        )
    }

    fun save(state: GameProgress) {
        val durableDestination = when (state.destination) {
            AppDestination.HOME,
            AppDestination.MAP,
            AppDestination.CHEST -> state.destination

            AppDestination.LESSON,
            AppDestination.REWARD,
            AppDestination.PARENT_GATE,
            AppDestination.PARENT_SUMMARY -> AppDestination.HOME
        }

        prefs.edit()
            .putString(KEY_DESTINATION, durableDestination.name)
            .putStringSet(KEY_UNLOCKED_ISLANDS, state.unlockedIslandIds.toSet())
            .putStringSet(KEY_COMPLETED_LESSONS, state.completedLessonIds.toSet())
            .putInt(KEY_TOTAL_STARS, state.totalStars)
            .putStringSet(KEY_STICKERS, state.stickerNames.toSet())
            .putString(KEY_PENDING_REVIEW_FAMILY, state.pendingReview?.questionFamily)
            .putStringSet(KEY_TODAY_LESSONS, state.todayLessonTitles.toSet())
            .putInt(KEY_STREAK_DAYS, state.streakDays)
            .putString(KEY_LAST_STUDY_DAY, state.lastStudyDayEpoch?.toString())
            .apply()
    }
}
