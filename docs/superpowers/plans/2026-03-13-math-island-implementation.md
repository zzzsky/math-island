# Math Island Android App Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the first playable Android version of Math Island, covering every unit in `docs/二年级数学.md` with a child-friendly map, short lesson runs, rewards, review recovery, and a minimal parent summary.

**Architecture:** Use a native Android app with Jetpack Compose, Navigation Compose, and a small manual dependency container. Keep curriculum data in versioned JSON assets, parse it into strongly typed domain models, and drive every island/level from the same generic level runner plus a limited set of question renderers.

**Tech Stack:** Kotlin, Jetpack Compose, Navigation Compose, Kotlinx Serialization, DataStore, JUnit 4, Compose UI Test, Gradle Kotlin DSL

---

I'm using the writing-plans skill to create the implementation plan.

## Planned File Structure

- `settings.gradle.kts`
  Repo-level module registration.
- `build.gradle.kts`
  Shared Gradle configuration.
- `gradle/libs.versions.toml`
  Central dependency versions.
- `app/build.gradle.kts`
  Android app module, Compose, serialization, DataStore, and test setup.
- `app/src/main/AndroidManifest.xml`
  App manifest and parent mode activity flags.
- `app/src/main/res/values/strings.xml`
  App strings and static labels.
- `app/src/main/java/com/mathisland/app/MainActivity.kt`
  Android entry point.
- `app/src/main/java/com/mathisland/app/MathIslandApp.kt`
  Compose root and app container wiring.
- `app/src/main/java/com/mathisland/app/di/AppContainer.kt`
  Manual dependency assembly.
- `app/src/main/java/com/mathisland/app/navigation/MathIslandNavGraph.kt`
  Top-level routes: home, map, island, level, chest, parent.
- `app/src/main/java/com/mathisland/app/ui/theme/Color.kt`
  Color tokens for the island theme.
- `app/src/main/java/com/mathisland/app/ui/theme/Type.kt`
  Typography tokens sized for children.
- `app/src/main/java/com/mathisland/app/ui/theme/Theme.kt`
  Theme wrapper.
- `app/src/main/java/com/mathisland/app/ui/components/PrimaryButton.kt`
  Reusable primary CTA.
- `app/src/main/java/com/mathisland/app/ui/components/IslandCard.kt`
  Island summary card.
- `app/src/main/java/com/mathisland/app/ui/components/RewardBanner.kt`
  Shared reward feedback UI.
- `app/src/main/java/com/mathisland/app/domain/model/CurriculumModels.kt`
  Island, level, lesson, reward, and question models.
- `app/src/main/java/com/mathisland/app/domain/model/QuestionModels.kt`
  Sealed question types shared by all islands.
- `app/src/main/java/com/mathisland/app/data/content/CurriculumRepository.kt`
  Asset loading and parsing.
- `app/src/main/java/com/mathisland/app/data/content/ContentCoverage.kt`
  Source-unit coverage metadata and audit helpers.
- `app/src/main/java/com/mathisland/app/data/progress/ProgressStore.kt`
  DataStore persistence for unlocks, stars, chest rewards, and review tasks.
- `app/src/main/java/com/mathisland/app/data/progress/ProgressRepository.kt`
  Progress read/write operations.
- `app/src/main/java/com/mathisland/app/domain/usecase/GetHomeStateUseCase.kt`
  Continue-adventure summary.
- `app/src/main/java/com/mathisland/app/domain/usecase/GetMapStateUseCase.kt`
  Island unlock state and completion progress.
- `app/src/main/java/com/mathisland/app/domain/usecase/SubmitLessonResultUseCase.kt`
  Scoring, unlock, chest, and review scheduling.
- `app/src/main/java/com/mathisland/app/domain/usecase/GetParentSummaryUseCase.kt`
  Minimal parent dashboard state.
- `app/src/main/java/com/mathisland/app/domain/usecase/GetPendingReviewUseCase.kt`
  Seagull review task selection.
- `app/src/main/java/com/mathisland/app/feature/home/HomeViewModel.kt`
  Home screen state.
- `app/src/main/java/com/mathisland/app/feature/home/HomeScreen.kt`
  Continue adventure / map / chest entry UI.
- `app/src/main/java/com/mathisland/app/feature/map/MapViewModel.kt`
  Map screen state.
- `app/src/main/java/com/mathisland/app/feature/map/MapScreen.kt`
  Island map UI.
- `app/src/main/java/com/mathisland/app/feature/island/IslandViewModel.kt`
  Single-island node list state.
- `app/src/main/java/com/mathisland/app/feature/island/IslandScreen.kt`
  Level list and completion visuals.
- `app/src/main/java/com/mathisland/app/feature/level/LevelViewModel.kt`
  Generic level runner state machine.
- `app/src/main/java/com/mathisland/app/feature/level/LevelScreen.kt`
  Question host, feedback, and result flow.
- `app/src/main/java/com/mathisland/app/feature/level/renderers/ChoiceQuestionCard.kt`
  Choice and judgment questions.
- `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionCard.kt`
  Fill-in and arithmetic entry questions.
- `app/src/main/java/com/mathisland/app/feature/level/renderers/SortQuestionCard.kt`
  Sort and compare questions.
- `app/src/main/java/com/mathisland/app/feature/level/renderers/GroupQuestionCard.kt`
  Average-share and classification drag/drop questions.
- `app/src/main/java/com/mathisland/app/feature/level/renderers/RulerQuestionCard.kt`
  Measurement question UI.
- `app/src/main/java/com/mathisland/app/feature/chest/ChestViewModel.kt`
  Chest inventory state.
- `app/src/main/java/com/mathisland/app/feature/chest/ChestScreen.kt`
  Stickers and cosmetic rewards.
- `app/src/main/java/com/mathisland/app/feature/parent/ParentGateScreen.kt`
  Simple gate before parent summary.
- `app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryScreen.kt`
  Daily learning summary and weak-topic hints.
- `app/src/main/assets/content/catalog.json`
  Island order, source units, and level references.
- `app/src/main/assets/content/islands/calculation-island.json`
  Levels for calculation content.
- `app/src/main/assets/content/islands/measurement-geometry-island.json`
  Levels for measurement and geometry content.
- `app/src/main/assets/content/islands/multiplication-island.json`
  Levels for multiplication and口诀.
- `app/src/main/assets/content/islands/division-island.json`
  Levels for average-share, division, and remainder content.
- `app/src/main/assets/content/islands/big-number-island.json`
  Levels for reading, writing, comparing, and ordering larger numbers.
- `app/src/main/assets/content/islands/classification-island.json`
  Levels for classification content.
- `app/src/main/assets/content/islands/challenge-island.json`
  Mixed review and challenge content.
- `app/src/test/java/com/mathisland/app/data/content/ContentCoverageTest.kt`
  Ensures all source units and example types are represented.
- `app/src/test/java/com/mathisland/app/data/progress/ProgressRepositoryTest.kt`
  Persistence and unlock tests.
- `app/src/test/java/com/mathisland/app/domain/usecase/SubmitLessonResultUseCaseTest.kt`
  Reward, unlock, and seagull scheduling tests.
- `app/src/androidTest/java/com/mathisland/app/feature/home/HomeScreenTest.kt`
  Home screen UI smoke tests.
- `app/src/androidTest/java/com/mathisland/app/feature/map/MapScreenTest.kt`
  Island unlock visibility tests.
- `app/src/androidTest/java/com/mathisland/app/feature/level/LevelScreenTest.kt`
  Generic level runner behavior tests.
- `docs/superpowers/plans/2026-03-13-math-island-implementation.md`
  This plan.

## Chunk 1: Project Foundation

### Task 1: Bootstrap the Android Compose project

**Files:**
- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `gradle/libs.versions.toml`
- Create: `app/build.gradle.kts`
- Create: `app/proguard-rules.pro`
- Create: `app/src/main/AndroidManifest.xml`
- Create: `app/src/main/res/values/strings.xml`
- Create: `app/src/main/java/com/mathisland/app/MainActivity.kt`
- Test: `app/src/test/java/com/mathisland/app/AppConfigSmokeTest.kt`

- [ ] **Step 1: Add the initial Gradle and app module files**

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}
```

- [ ] **Step 2: Write the failing smoke test for app identity**

```kotlin
class AppConfigSmokeTest {
    @Test
    fun appName_isMathIsland() {
        assertEquals("Math Island", BuildConfig.APP_DISPLAY_NAME)
    }
}
```

- [ ] **Step 3: Run the smoke test to verify it fails**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.AppConfigSmokeTest"`

Expected: FAIL because `APP_DISPLAY_NAME` is missing.

- [ ] **Step 4: Define `APP_DISPLAY_NAME`, manifest package, minSdk, targetSdk, and Compose activity setup**

```kotlin
buildConfigField("String", "APP_DISPLAY_NAME", "\"Math Island\"")
```

- [ ] **Step 5: Re-run the smoke test**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.AppConfigSmokeTest"`

Expected: PASS.

- [ ] **Step 6: Commit**

```bash
git add settings.gradle.kts build.gradle.kts gradle/libs.versions.toml app/build.gradle.kts app/src/main/AndroidManifest.xml app/src/main/res/values/strings.xml app/src/main/java/com/mathisland/app/MainActivity.kt app/src/test/java/com/mathisland/app/AppConfigSmokeTest.kt
git commit -m "build: bootstrap android compose app"
```

### Task 2: Create the app shell, theme, and top-level navigation

**Files:**
- Create: `app/src/main/java/com/mathisland/app/MathIslandApp.kt`
- Create: `app/src/main/java/com/mathisland/app/di/AppContainer.kt`
- Create: `app/src/main/java/com/mathisland/app/navigation/MathIslandNavGraph.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/theme/Color.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/theme/Type.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/theme/Theme.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/components/PrimaryButton.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/navigation/MathIslandNavGraphTest.kt`

- [ ] **Step 1: Write the failing navigation smoke test**

```kotlin
@Test
fun startDestination_showsContinueAdventure() {
    composeRule.setContent { MathIslandApp() }
    composeRule.onNodeWithText("继续冒险").assertExists()
}
```

- [ ] **Step 2: Run the Android UI test to verify it fails**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.navigation.MathIslandNavGraphTest`

Expected: FAIL because `MathIslandApp` and navigation graph do not exist.

- [ ] **Step 3: Implement theme tokens, `MathIslandApp`, `PrimaryButton`, and a nav graph with placeholder destinations**

```kotlin
NavHost(navController = navController, startDestination = "home") {
    composable("home") { HomePlaceholder() }
    composable("map") { MapPlaceholder() }
    composable("chest") { ChestPlaceholder() }
    composable("parent") { ParentPlaceholder() }
}
```

- [ ] **Step 4: Re-run the Android UI test**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.navigation.MathIslandNavGraphTest`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/MathIslandApp.kt app/src/main/java/com/mathisland/app/di/AppContainer.kt app/src/main/java/com/mathisland/app/navigation/MathIslandNavGraph.kt app/src/main/java/com/mathisland/app/ui/theme/Color.kt app/src/main/java/com/mathisland/app/ui/theme/Type.kt app/src/main/java/com/mathisland/app/ui/theme/Theme.kt app/src/main/java/com/mathisland/app/ui/components/PrimaryButton.kt app/src/androidTest/java/com/mathisland/app/navigation/MathIslandNavGraphTest.kt
git commit -m "feat: add app shell and navigation"
```

### Task 3: Add the reusable home, map, and chest screen shells

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/home/HomeScreen.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/map/MapScreen.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/chest/ChestScreen.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/components/IslandCard.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/components/RewardBanner.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/home/HomeScreenTest.kt`

- [ ] **Step 1: Write the failing home screen smoke test**

```kotlin
@Test
fun homeScreen_showsThreePrimaryActions() {
    composeRule.setContent { HomeScreen(...) }
    composeRule.onNodeWithText("继续冒险").assertExists()
    composeRule.onNodeWithText("地图").assertExists()
    composeRule.onNodeWithText("宝箱").assertExists()
}
```

- [ ] **Step 2: Run the UI test to verify it fails**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.home.HomeScreenTest`

Expected: FAIL because the screen does not exist.

- [ ] **Step 3: Implement home, map, and chest placeholders using child-friendly cards and large tap targets**

- [ ] **Step 4: Re-run the UI test**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.home.HomeScreenTest`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/home/HomeScreen.kt app/src/main/java/com/mathisland/app/feature/map/MapScreen.kt app/src/main/java/com/mathisland/app/feature/chest/ChestScreen.kt app/src/main/java/com/mathisland/app/ui/components/IslandCard.kt app/src/main/java/com/mathisland/app/ui/components/RewardBanner.kt app/src/androidTest/java/com/mathisland/app/feature/home/HomeScreenTest.kt
git commit -m "feat: add home map and chest shells"
```

## Chunk 2: Curriculum, Progress, and Rewards

### Task 4: Define curriculum models and asset schema

**Files:**
- Create: `app/src/main/java/com/mathisland/app/domain/model/CurriculumModels.kt`
- Create: `app/src/main/java/com/mathisland/app/domain/model/QuestionModels.kt`
- Create: `app/src/main/assets/content/catalog.json`
- Create: `app/src/main/assets/content/islands/calculation-island.json`
- Create: `app/src/main/assets/content/islands/measurement-geometry-island.json`
- Create: `app/src/main/assets/content/islands/multiplication-island.json`
- Create: `app/src/main/assets/content/islands/division-island.json`
- Create: `app/src/main/assets/content/islands/big-number-island.json`
- Create: `app/src/main/assets/content/islands/classification-island.json`
- Create: `app/src/main/assets/content/islands/challenge-island.json`
- Test: `app/src/test/java/com/mathisland/app/data/content/CatalogParsingTest.kt`

- [ ] **Step 1: Write the failing parsing test for the content catalog**

```kotlin
@Test
fun catalog_containsSevenIslands() {
    val catalog = loadCatalog("app/src/main/assets/content/catalog.json")
    assertEquals(7, catalog.islands.size)
}
```

- [ ] **Step 2: Run the parsing test to verify it fails**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.data.content.CatalogParsingTest"`

Expected: FAIL because the models and assets do not exist.

- [ ] **Step 3: Add curriculum models and JSON assets that encode every island and level reference from the approved spec**

```json
{
  "id": "calculation-island",
  "sourceUnits": ["二上-1", "二下-1", "二下-6"],
  "levelIds": ["calc-carry-01", "calc-borrow-01", "calc-mixed-01"]
}
```

- [ ] **Step 4: Re-run the parsing test**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.data.content.CatalogParsingTest"`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/domain/model/CurriculumModels.kt app/src/main/java/com/mathisland/app/domain/model/QuestionModels.kt app/src/main/assets/content/catalog.json app/src/main/assets/content/islands/*.json app/src/test/java/com/mathisland/app/data/content/CatalogParsingTest.kt
git commit -m "feat: define curriculum schema and base assets"
```

### Task 5: Add the curriculum repository and source coverage audit

**Files:**
- Create: `app/src/main/java/com/mathisland/app/data/content/CurriculumRepository.kt`
- Create: `app/src/main/java/com/mathisland/app/data/content/ContentCoverage.kt`
- Test: `app/src/test/java/com/mathisland/app/data/content/ContentCoverageTest.kt`

- [ ] **Step 1: Write the failing coverage audit test**

```kotlin
@Test
fun approvedSourceUnits_areFullyCovered() {
    val coveredUnits = ContentCoverage.coveredSourceUnits()
    assertEquals(
        setOf("二上-1","二上-2","二上-3-4-7","二上-5","二下-1","二下-2-5","二下-3","二下-4","二下-6","二下-7"),
        coveredUnits
    )
}
```

- [ ] **Step 2: Run the coverage audit**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.data.content.ContentCoverageTest"`

Expected: FAIL because coverage helpers are missing or incomplete.

- [ ] **Step 3: Implement asset loading and a coverage helper that also maps example types to question families**

- [ ] **Step 4: Re-run the coverage audit**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.data.content.ContentCoverageTest"`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/data/content/CurriculumRepository.kt app/src/main/java/com/mathisland/app/data/content/ContentCoverage.kt app/src/test/java/com/mathisland/app/data/content/ContentCoverageTest.kt
git commit -m "feat: add curriculum repository and coverage audit"
```

### Task 6: Implement progress persistence and unlock logic

**Files:**
- Create: `app/src/main/java/com/mathisland/app/data/progress/ProgressStore.kt`
- Create: `app/src/main/java/com/mathisland/app/data/progress/ProgressRepository.kt`
- Create: `app/src/main/java/com/mathisland/app/domain/usecase/GetHomeStateUseCase.kt`
- Create: `app/src/main/java/com/mathisland/app/domain/usecase/GetMapStateUseCase.kt`
- Test: `app/src/test/java/com/mathisland/app/data/progress/ProgressRepositoryTest.kt`

- [ ] **Step 1: Write the failing unlock test**

```kotlin
@Test
fun completingLevel_unlocksAdjacentNode() = runTest {
    repository.recordLevelResult("calc-carry-01", stars = 3, reward = null)
    assertTrue(repository.isLevelUnlocked("calc-borrow-01"))
}
```

- [ ] **Step 2: Run the unlock test**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.data.progress.ProgressRepositoryTest"`

Expected: FAIL because the repository and persistence layer do not exist.

- [ ] **Step 3: Implement DataStore-backed progress models for stars, unlocked nodes, island completion, and current continue-adventure pointer**

- [ ] **Step 4: Re-run the unlock test**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.data.progress.ProgressRepositoryTest"`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/data/progress/ProgressStore.kt app/src/main/java/com/mathisland/app/data/progress/ProgressRepository.kt app/src/main/java/com/mathisland/app/domain/usecase/GetHomeStateUseCase.kt app/src/main/java/com/mathisland/app/domain/usecase/GetMapStateUseCase.kt app/src/test/java/com/mathisland/app/data/progress/ProgressRepositoryTest.kt
git commit -m "feat: add progress persistence and unlock logic"
```

### Task 7: Implement rewards, scoring, and seagull review scheduling

**Files:**
- Create: `app/src/main/java/com/mathisland/app/domain/usecase/SubmitLessonResultUseCase.kt`
- Create: `app/src/main/java/com/mathisland/app/domain/usecase/GetPendingReviewUseCase.kt`
- Test: `app/src/test/java/com/mathisland/app/domain/usecase/SubmitLessonResultUseCaseTest.kt`

- [ ] **Step 1: Write the failing reward and review scheduling tests**

```kotlin
@Test
fun twoFailures_inSameQuestionFamily_scheduleSeagullReview() = runTest {
    val result = submitLessonResult(
        levelId = "measure-ruler-01",
        incorrectQuestionFamilies = listOf("measurement", "measurement")
    )
    assertEquals("measurement", result.pendingReview?.questionFamily)
}
```

- [ ] **Step 2: Run the use-case tests**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.domain.usecase.SubmitLessonResultUseCaseTest"`

Expected: FAIL because scoring and review logic are missing.

- [ ] **Step 3: Implement star thresholds, chest unlock rules, island repair progress, and the `小海鸥求助` review scheduler**

- [ ] **Step 4: Re-run the use-case tests**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.domain.usecase.SubmitLessonResultUseCaseTest"`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/domain/usecase/SubmitLessonResultUseCase.kt app/src/main/java/com/mathisland/app/domain/usecase/GetPendingReviewUseCase.kt app/src/test/java/com/mathisland/app/domain/usecase/SubmitLessonResultUseCaseTest.kt
git commit -m "feat: add rewards and review scheduling"
```

## Chunk 3: Generic Gameplay Loop

### Task 8: Build the home, map, and island view models against live data

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/home/HomeViewModel.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/map/MapViewModel.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/island/IslandViewModel.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/island/IslandScreen.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/map/MapScreenTest.kt`

- [ ] **Step 1: Write the failing map visibility test**

```kotlin
@Test
fun lockedIslands_showDisabledState() {
    composeRule.setContent { MapScreen(state = sampleMapState()) }
    composeRule.onNodeWithText("测量与图形岛").assertExists()
    composeRule.onNodeWithContentDescription("locked-island").assertExists()
}
```

- [ ] **Step 2: Run the map UI test**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.map.MapScreenTest`

Expected: FAIL because the screen is still placeholder-only.

- [ ] **Step 3: Implement real view models and update screens to read curriculum plus progress repositories**

- [ ] **Step 4: Re-run the map UI test**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.map.MapScreenTest`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/home/HomeViewModel.kt app/src/main/java/com/mathisland/app/feature/map/MapViewModel.kt app/src/main/java/com/mathisland/app/feature/island/IslandViewModel.kt app/src/main/java/com/mathisland/app/feature/island/IslandScreen.kt app/src/androidTest/java/com/mathisland/app/feature/map/MapScreenTest.kt
git commit -m "feat: connect map flow to curriculum and progress"
```

### Task 9: Implement the generic level runner state machine

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/level/LevelViewModel.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/level/LevelScreen.kt`
- Test: `app/src/test/java/com/mathisland/app/feature/level/LevelViewModelTest.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelScreenTest.kt`

- [ ] **Step 1: Write the failing level progression unit test**

```kotlin
@Test
fun answeringFinalQuestion_movesToResultState() {
    val viewModel = LevelViewModel(fakeLevel())
    viewModel.submitAnswer("A")
    viewModel.submitAnswer("B")
    assertTrue(viewModel.state.value is LevelUiState.Result)
}
```

- [ ] **Step 2: Run the unit test**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.feature.level.LevelViewModelTest"`

Expected: FAIL because the state machine does not exist.

- [ ] **Step 3: Implement a generic level runner with states for intro, question, feedback, reward, and result**

- [ ] **Step 4: Add a UI test that verifies one correct answer advances to the next question and re-run both tests**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.feature.level.LevelViewModelTest"`

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.level.LevelScreenTest`

Expected: PASS for both.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/level/LevelViewModel.kt app/src/main/java/com/mathisland/app/feature/level/LevelScreen.kt app/src/test/java/com/mathisland/app/feature/level/LevelViewModelTest.kt app/src/androidTest/java/com/mathisland/app/feature/level/LevelScreenTest.kt
git commit -m "feat: add generic level runner"
```

### Task 10: Implement the reusable question renderers

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/ChoiceQuestionCard.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionCard.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/SortQuestionCard.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/GroupQuestionCard.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/RulerQuestionCard.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/level/QuestionRendererTest.kt`

- [ ] **Step 1: Write the failing renderer smoke tests for all five question families**

```kotlin
@Test
fun rulerQuestion_allowsDraggingMarker() {
    composeRule.setContent { RulerQuestionCard(sampleRulerQuestion(), onSubmit = {}) }
    composeRule.onNodeWithTag("ruler-marker").assertExists()
}
```

- [ ] **Step 2: Run the renderer UI test**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.level.QuestionRendererTest`

Expected: FAIL because the renderer components do not exist.

- [ ] **Step 3: Implement the five reusable renderers and route `QuestionModels` to them from `LevelScreen`**

- [ ] **Step 4: Re-run the renderer UI test**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.level.QuestionRendererTest`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/level/renderers/*.kt app/src/androidTest/java/com/mathisland/app/feature/level/QuestionRendererTest.kt app/src/main/java/com/mathisland/app/feature/level/LevelScreen.kt
git commit -m "feat: add reusable question renderers"
```

## Chunk 4: Island Content, Parent Mode, and Release Readiness

### Task 11: Fill out the full island content set and validators

**Files:**
- Modify: `app/src/main/assets/content/islands/calculation-island.json`
- Modify: `app/src/main/assets/content/islands/measurement-geometry-island.json`
- Modify: `app/src/main/assets/content/islands/multiplication-island.json`
- Modify: `app/src/main/assets/content/islands/division-island.json`
- Modify: `app/src/main/assets/content/islands/big-number-island.json`
- Modify: `app/src/main/assets/content/islands/classification-island.json`
- Modify: `app/src/main/assets/content/islands/challenge-island.json`
- Test: `app/src/test/java/com/mathisland/app/data/content/FullCurriculumValidationTest.kt`

- [ ] **Step 1: Write the failing full-curriculum validation test**

```kotlin
@Test
fun everyApprovedKnowledgePoint_hasAtLeastOnePlayableLevel() {
    val report = validateCurriculumCoverage(loadCurriculum())
    assertTrue(report.missingKnowledgePoints.isEmpty())
}
```

- [ ] **Step 2: Run the validation test**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.data.content.FullCurriculumValidationTest"`

Expected: FAIL until all source knowledge points and example types are encoded.

- [ ] **Step 3: Complete every island JSON with the final levels needed to cover all approved units, question families, and challenge reviews**

- [ ] **Step 4: Re-run the validation test**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.data.content.FullCurriculumValidationTest"`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/assets/content/islands/*.json app/src/test/java/com/mathisland/app/data/content/FullCurriculumValidationTest.kt
git commit -m "feat: add full playable curriculum content"
```

### Task 12: Implement the parent gate and parent summary flow

**Files:**
- Create: `app/src/main/java/com/mathisland/app/domain/usecase/GetParentSummaryUseCase.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/parent/ParentGateScreen.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryScreen.kt`
- Test: `app/src/test/java/com/mathisland/app/domain/usecase/GetParentSummaryUseCaseTest.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/parent/ParentSummaryScreenTest.kt`

- [ ] **Step 1: Write the failing parent summary use-case test**

```kotlin
@Test
fun parentSummary_returnsWeakTopicsAndDailyLearning() = runTest {
    val summary = useCase()
    assertTrue(summary.weakTopics.isNotEmpty())
    assertTrue(summary.todayLearned.isNotEmpty())
}
```

- [ ] **Step 2: Run the use-case and UI tests**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.domain.usecase.GetParentSummaryUseCaseTest"`

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.parent.ParentSummaryScreenTest`

Expected: FAIL because parent flow does not exist.

- [ ] **Step 3: Implement a simple arithmetic gate plus summary screen with exactly four cards: today learned, weak topics, streak, recommended island**

- [ ] **Step 4: Re-run the use-case and UI tests**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.domain.usecase.GetParentSummaryUseCaseTest"`

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.parent.ParentSummaryScreenTest`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/domain/usecase/GetParentSummaryUseCase.kt app/src/main/java/com/mathisland/app/feature/parent/ParentGateScreen.kt app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryScreen.kt app/src/test/java/com/mathisland/app/domain/usecase/GetParentSummaryUseCaseTest.kt app/src/androidTest/java/com/mathisland/app/feature/parent/ParentSummaryScreenTest.kt
git commit -m "feat: add parent summary flow"
```

### Task 13: Add release checks, build docs, and final verification

**Files:**
- Create: `README.md`
- Create: `docs/testing.md`
- Modify: `app/build.gradle.kts`
- Test: `app/src/test/java/com/mathisland/app/SmokeCoverageTest.kt`

- [ ] **Step 1: Write the failing smoke coverage test**

```kotlin
@Test
fun appContainer_buildsAllCoreUseCases() {
    val container = AppContainer(fakeContext)
    assertNotNull(container.curriculumRepository)
    assertNotNull(container.submitLessonResultUseCase)
}
```

- [ ] **Step 2: Run the full local verification suite**

Run: `.\gradlew testDebugUnitTest`

Expected: FAIL until the smoke coverage test and any remaining wiring gaps are fixed.

- [ ] **Step 3: Implement any missing app container wiring, then document setup, emulator requirements, and test commands in `README.md` and `docs/testing.md`**

- [ ] **Step 4: Run the final verification commands**

Run: `.\gradlew testDebugUnitTest`

Run: `.\gradlew connectedDebugAndroidTest`

Run: `.\gradlew assembleDebug`

Expected: PASS for all three commands.

- [ ] **Step 5: Commit**

```bash
git add README.md docs/testing.md app/build.gradle.kts app/src/test/java/com/mathisland/app/SmokeCoverageTest.kt
git commit -m "docs: add build and verification instructions"
```

## Execution Notes

- Implement in order. Each task builds on the previous task's files and tests.
- Keep question renderer count fixed unless a real coverage gap proves one more renderer is necessary.
- Do not add cloud sync, analytics backends, social features, or teacher tools in this plan.
- If the repository stays documentation-only for a while, keep content JSON and tests committed even before art/audio assets exist.
- Use the coverage tests as the source of truth for “all units included”; do not rely on manual inspection alone.

## Manual Review Notes

No plan-document-reviewer subagent is available in this environment, so review each chunk manually before execution:

- Verify every task points to concrete files.
- Verify every unit from `docs/二年级数学.md` is represented in `ContentCoverageTest` or `FullCurriculumValidationTest`.
- Verify every task ends with a runnable verification command.
- Verify every commit message describes a self-contained increment.

Plan complete and saved to `docs/superpowers/plans/2026-03-13-math-island-implementation.md`. Ready to execute?
