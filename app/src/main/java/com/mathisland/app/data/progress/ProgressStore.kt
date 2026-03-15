package com.mathisland.app.data.progress

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.mathisland.app.AppDestination
import com.mathisland.app.GameProgress
import com.mathisland.app.MathIslandProgressStore
import com.mathisland.app.ReviewTask
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap

private const val DEFAULT_PROGRESS_DATASTORE_NAME = "math_island_progress"
private val destinationKey = stringPreferencesKey("destination")
private val unlockedIslandsKey = stringSetPreferencesKey("unlocked_islands")
private val completedLessonsKey = stringSetPreferencesKey("completed_lessons")
private val totalStarsKey = intPreferencesKey("total_stars")
private val stickersKey = stringSetPreferencesKey("stickers")
private val pendingReviewFamilyKey = stringPreferencesKey("pending_review_family")
private val todayLessonsKey = stringSetPreferencesKey("today_lessons")
private val streakDaysKey = intPreferencesKey("streak_days")
private val lastStudyDayKey = longPreferencesKey("last_study_day")

interface ProgressStore {
    fun load(initial: GameProgress): GameProgress
    fun save(state: GameProgress)
}

class DataStoreProgressStore(
    context: Context,
    fileName: String = DEFAULT_PROGRESS_DATASTORE_NAME
) : ProgressStore {
    private val backingFile = context.applicationContext.preferencesDataStoreFile(fileName)
    private val dataStore = dataStoreCache.getOrPut(backingFile.absolutePath) {
        PreferenceDataStoreFactory.create(
            produceFile = { backingFile }
        )
    }

    override fun load(initial: GameProgress): GameProgress = runBlocking {
        val prefs = dataStore.data.first()
        val restoredDestination = prefs[destinationKey]
            ?.let { name -> AppDestination.entries.firstOrNull { destination -> destination.name == name } }
            ?.takeIf { destination ->
                destination == AppDestination.HOME ||
                    destination == AppDestination.MAP ||
                    destination == AppDestination.CHEST
            }
            ?: initial.destination
        val unlockedIslandIds = prefs[unlockedIslandsKey]
            ?.takeIf(Set<String>::isNotEmpty)
            ?: initial.unlockedIslandIds
        val lastStudyDay = prefs[lastStudyDayKey]
        val isSameStudyDay = lastStudyDay == java.time.LocalDate.now().toEpochDay()

        initial.copy(
            destination = restoredDestination,
            unlockedIslandIds = unlockedIslandIds,
            completedLessonIds = prefs[completedLessonsKey].orEmpty(),
            totalStars = prefs[totalStarsKey] ?: initial.totalStars,
            stickerNames = prefs[stickersKey].orEmpty(),
            pendingReview = prefs[pendingReviewFamilyKey]?.let(::ReviewTask),
            todayLessonTitles = if (isSameStudyDay) prefs[todayLessonsKey].orEmpty().toList() else emptyList(),
            streakDays = prefs[streakDaysKey] ?: initial.streakDays,
            lastStudyDayEpoch = lastStudyDay
        )
    }

    override fun save(state: GameProgress) {
        val durableDestination = when (state.destination) {
            AppDestination.HOME,
            AppDestination.MAP,
            AppDestination.CHEST -> state.destination

            AppDestination.LESSON,
            AppDestination.REWARD,
            AppDestination.PARENT_GATE,
            AppDestination.PARENT_SUMMARY -> AppDestination.HOME
        }

        runBlocking {
            dataStore.edit { prefs ->
                prefs[destinationKey] = durableDestination.name
                prefs[unlockedIslandsKey] = state.unlockedIslandIds
                prefs[completedLessonsKey] = state.completedLessonIds
                prefs[totalStarsKey] = state.totalStars
                prefs[stickersKey] = state.stickerNames
                state.pendingReview?.questionFamily?.let { prefs[pendingReviewFamilyKey] = it }
                    ?: prefs.remove(pendingReviewFamilyKey)
                prefs[todayLessonsKey] = state.todayLessonTitles.toSet()
                prefs[streakDaysKey] = state.streakDays
                state.lastStudyDayEpoch?.let { prefs[lastStudyDayKey] = it }
                    ?: prefs.remove(lastStudyDayKey)
            }
        }
    }

    fun clear() {
        runBlocking {
            dataStore.edit { prefs -> prefs.clear() }
        }
    }

    companion object {
        private val dataStoreCache = ConcurrentHashMap<String, DataStore<Preferences>>()
    }
}

class SharedPreferencesProgressStore(
    private val delegate: MathIslandProgressStore
) : ProgressStore {
    override fun load(initial: GameProgress): GameProgress = delegate.load(initial)

    override fun save(state: GameProgress) {
        delegate.save(state)
    }
}

class InMemoryProgressStore(
    private var state: GameProgress? = null
) : ProgressStore {
    override fun load(initial: GameProgress): GameProgress = state ?: initial

    override fun save(state: GameProgress) {
        this.state = state
    }
}
