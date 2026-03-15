# Math Island Android Tablet Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the first playable Android tablet version of Math Island, optimized for landscape tablets and covering the approved curriculum, map progression, rewards, review recovery, and minimal parent summary.

**Architecture:** Keep the existing native Android direction, but make the UI explicitly tablet-first: a persistent large map canvas, anchored side panels, and split-pane lesson scenes. Use Jetpack Compose with Window Size Class helpers so the app is structured around tablet layouts from the beginning instead of stretching a phone UI later.

**Tech Stack:** Kotlin, Jetpack Compose, Navigation Compose, Material 3, Window Size Class / adaptive layout helpers, Kotlinx Serialization, DataStore, JUnit 4, Compose UI Test, Gradle Kotlin DSL

---

I'm using the writing-plans skill to create the implementation plan.

## Scope Notes

- This plan is based on:
  - `docs/superpowers/specs/2026-03-13-math-island-android-design.md`
  - `docs/superpowers/plans/2026-03-13-math-island-implementation.md`
- This version narrows the target platform to `Android tablet`.
- Assumption for MVP: `landscape-first tablet experience` targeting `1280x800` class devices. Phone support is explicitly out of scope for this plan.

## Planned File Structure

- `settings.gradle.kts`
  Register the app module.
- `build.gradle.kts`
  Shared Gradle conventions.
- `gradle/libs.versions.toml`
  Dependency catalog including Compose, adaptive helpers, serialization, and test libs.
- `app/build.gradle.kts`
  Android app module and test setup.
- `app/src/main/AndroidManifest.xml`
  Manifest with tablet activity setup and orientation policy.
- `app/src/main/res/values/strings.xml`
  Strings for tablet UI and accessibility labels.
- `app/src/main/java/com/mathisland/app/MainActivity.kt`
  Entry point, window setup, and orientation behavior.
- `app/src/main/java/com/mathisland/app/MathIslandTabletApp.kt`
  Root compose app for tablet.
- `app/src/main/java/com/mathisland/app/di/AppContainer.kt`
  Manual dependency graph.
- `app/src/main/java/com/mathisland/app/navigation/TabletNavGraph.kt`
  Top-level navigation for home, map, island, level, chest, parent.
- `app/src/main/java/com/mathisland/app/ui/adaptive/WindowProfile.kt`
  Helpers for tablet layout decisions.
- `app/src/main/java/com/mathisland/app/ui/adaptive/TabletScaffold.kt`
  Shared large-screen scaffold with floating info zones.
- `app/src/main/java/com/mathisland/app/ui/theme/Color.kt`
  Semantic colors for the storybook map UI.
- `app/src/main/java/com/mathisland/app/ui/theme/Type.kt`
  Typography tokens sized for children on tablets.
- `app/src/main/java/com/mathisland/app/ui/theme/Theme.kt`
  Theme wrapper.
- `app/src/main/java/com/mathisland/app/ui/components/WoodButton.kt`
  Primary tablet CTA.
- `app/src/main/java/com/mathisland/app/ui/components/StoryPanelCard.kt`
  Reusable floating paper/wood panel.
- `app/src/main/java/com/mathisland/app/ui/components/IslandMapCanvas.kt`
  Shared map scene surface with island hotspots.
- `app/src/main/java/com/mathisland/app/domain/model/CurriculumModels.kt`
  Curriculum and level models.
- `app/src/main/java/com/mathisland/app/domain/model/QuestionModels.kt`
  Question families.
- `app/src/main/java/com/mathisland/app/data/content/CurriculumRepository.kt`
  Asset loading and parsing.
- `app/src/main/java/com/mathisland/app/data/content/ContentCoverage.kt`
  Coverage audit helpers.
- `app/src/main/java/com/mathisland/app/data/progress/ProgressStore.kt`
  Progress persistence.
- `app/src/main/java/com/mathisland/app/data/progress/ProgressRepository.kt`
  Progress operations.
- `app/src/main/java/com/mathisland/app/domain/usecase/GetHomeStateUseCase.kt`
  Continue-adventure state.
- `app/src/main/java/com/mathisland/app/domain/usecase/GetMapStateUseCase.kt`
  Map progression state.
- `app/src/main/java/com/mathisland/app/domain/usecase/SubmitLessonResultUseCase.kt`
  Rewards and unlock processing.
- `app/src/main/java/com/mathisland/app/domain/usecase/GetPendingReviewUseCase.kt`
  Seagull review scheduling.
- `app/src/main/java/com/mathisland/app/domain/usecase/GetParentSummaryUseCase.kt`
  Parent summary state.
- `app/src/main/java/com/mathisland/app/feature/home/HomeViewModel.kt`
  Tablet home state.
- `app/src/main/java/com/mathisland/app/feature/home/HomeTabletScreen.kt`
  Landscape welcome/home screen.
- `app/src/main/java/com/mathisland/app/feature/map/MapViewModel.kt`
  Map screen state.
- `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
  Full-canvas world map screen.
- `app/src/main/java/com/mathisland/app/feature/island/IslandViewModel.kt`
  Island panel state.
- `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`
  Anchored task panel over the map.
- `app/src/main/java/com/mathisland/app/feature/level/LevelViewModel.kt`
  Lesson state machine.
- `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`
  Split-pane lesson screen.
- `app/src/main/java/com/mathisland/app/feature/level/renderers/ChoiceQuestionPane.kt`
  Choice questions.
- `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt`
  Number entry.
- `app/src/main/java/com/mathisland/app/feature/level/renderers/SortQuestionPane.kt`
  Sort/compare questions.
- `app/src/main/java/com/mathisland/app/feature/level/renderers/GroupQuestionPane.kt`
  Grouping and classification questions.
- `app/src/main/java/com/mathisland/app/feature/level/renderers/RulerQuestionPane.kt`
  Measurement questions.
- `app/src/main/java/com/mathisland/app/feature/reward/RewardOverlay.kt`
  End-of-level reward layer.
- `app/src/main/java/com/mathisland/app/feature/chest/ChestViewModel.kt`
  Chest inventory state.
- `app/src/main/java/com/mathisland/app/feature/chest/ChestTabletScreen.kt`
  Tablet chest scene.
- `app/src/main/java/com/mathisland/app/feature/parent/ParentGateScreen.kt`
  Parent entry gate.
- `app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryTabletScreen.kt`
  Parent summary on tablet.
- `app/src/main/assets/content/catalog.json`
  Island order and level references.
- `app/src/main/assets/content/islands/*.json`
  Curriculum content.
- `app/src/test/java/com/mathisland/app/data/content/ContentCoverageTest.kt`
  Unit coverage tests.
- `app/src/test/java/com/mathisland/app/domain/usecase/SubmitLessonResultUseCaseTest.kt`
  Reward and review logic tests.
- `app/src/androidTest/java/com/mathisland/app/feature/home/HomeTabletScreenTest.kt`
  Home tablet UI tests.
- `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
  Map tablet UI tests.
- `app/src/androidTest/java/com/mathisland/app/feature/level/LevelTabletScreenTest.kt`
  Lesson tablet UI tests.
- `app/src/androidTest/java/com/mathisland/app/feature/chest/ChestTabletScreenTest.kt`
  Chest UI tests.
- `docs/superpowers/plans/2026-03-13-math-island-android-tablet-implementation.md`
  This plan.

## Chunk 1: Tablet Foundation

### Task 1: Bootstrap a landscape-first Android tablet app shell

**Files:**
- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `gradle/libs.versions.toml`
- Create: `app/build.gradle.kts`
- Create: `app/src/main/AndroidManifest.xml`
- Create: `app/src/main/java/com/mathisland/app/MainActivity.kt`
- Test: `app/src/test/java/com/mathisland/app/AppConfigSmokeTest.kt`

- [ ] **Step 1: Write the failing app-config test**

```kotlin
class AppConfigSmokeTest {
    @Test
    fun appTarget_isTabletLandscapeMvp() {
        assertEquals("tablet-landscape", BuildConfig.TARGET_DEVICE_PROFILE)
    }
}
```

- [ ] **Step 2: Run the unit test to verify it fails**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.AppConfigSmokeTest"`

Expected: FAIL because `TARGET_DEVICE_PROFILE` is not defined.

- [ ] **Step 3: Add the Gradle app shell, manifest, and build config for tablet-landscape MVP**

```kotlin
buildConfigField("String", "TARGET_DEVICE_PROFILE", "\"tablet-landscape\"")
```

- [ ] **Step 4: Re-run the unit test**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.AppConfigSmokeTest"`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add settings.gradle.kts build.gradle.kts gradle/libs.versions.toml app/build.gradle.kts app/src/main/AndroidManifest.xml app/src/main/java/com/mathisland/app/MainActivity.kt app/src/test/java/com/mathisland/app/AppConfigSmokeTest.kt
git commit -m "build: bootstrap android tablet app shell"
```

### Task 2: Add adaptive tablet scaffold and window profile helpers

**Files:**
- Create: `app/src/main/java/com/mathisland/app/MathIslandTabletApp.kt`
- Create: `app/src/main/java/com/mathisland/app/navigation/TabletNavGraph.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/adaptive/WindowProfile.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/adaptive/TabletScaffold.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/theme/Color.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/theme/Type.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/theme/Theme.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/navigation/TabletNavGraphTest.kt`

- [ ] **Step 1: Write the failing tablet-shell UI test**

```kotlin
@Test
fun appStarts_withTabletStoryShell() {
    composeRule.setContent { MathIslandTabletApp() }
    composeRule.onNodeWithContentDescription("tablet-world-shell").assertExists()
}
```

- [ ] **Step 2: Run the Android UI test to verify it fails**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.navigation.TabletNavGraphTest`

Expected: FAIL because the tablet app shell does not exist.

- [ ] **Step 3: Implement the root compose app, tablet scaffold, and window profile helpers**

- [ ] **Step 4: Re-run the Android UI test**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.navigation.TabletNavGraphTest`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/MathIslandTabletApp.kt app/src/main/java/com/mathisland/app/navigation/TabletNavGraph.kt app/src/main/java/com/mathisland/app/ui/adaptive/WindowProfile.kt app/src/main/java/com/mathisland/app/ui/adaptive/TabletScaffold.kt app/src/main/java/com/mathisland/app/ui/theme/Color.kt app/src/main/java/com/mathisland/app/ui/theme/Type.kt app/src/main/java/com/mathisland/app/ui/theme/Theme.kt app/src/androidTest/java/com/mathisland/app/navigation/TabletNavGraphTest.kt
git commit -m "feat: add tablet scaffold and adaptive shell"
```

## Chunk 2: Curriculum and Core State

### Task 3: Add curriculum schema and coverage gates

**Files:**
- Create: `app/src/main/java/com/mathisland/app/domain/model/CurriculumModels.kt`
- Create: `app/src/main/java/com/mathisland/app/domain/model/QuestionModels.kt`
- Create: `app/src/main/java/com/mathisland/app/data/content/CurriculumRepository.kt`
- Create: `app/src/main/java/com/mathisland/app/data/content/ContentCoverage.kt`
- Create: `app/src/main/assets/content/catalog.json`
- Create: `app/src/main/assets/content/islands/calculation-island.json`
- Create: `app/src/main/assets/content/islands/measurement-geometry-island.json`
- Create: `app/src/main/assets/content/islands/multiplication-island.json`
- Create: `app/src/main/assets/content/islands/division-island.json`
- Create: `app/src/main/assets/content/islands/big-number-island.json`
- Create: `app/src/main/assets/content/islands/classification-island.json`
- Create: `app/src/main/assets/content/islands/challenge-island.json`
- Test: `app/src/test/java/com/mathisland/app/data/content/ContentCoverageTest.kt`

- [ ] **Step 1: Write the failing coverage test**

```kotlin
@Test
fun allApprovedUnits_areCoveredByIslandContent() {
    assertEquals(
        setOf("二上-1","二上-2","二上-3-4-7","二上-5","二下-1","二下-2-5","二下-3","二下-4","二下-6","二下-7"),
        ContentCoverage.coveredSourceUnits()
    )
}
```

- [ ] **Step 2: Run the unit test to verify it fails**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.data.content.ContentCoverageTest"`

Expected: FAIL because the content models and assets are missing.

- [ ] **Step 3: Add the curriculum schema, repository, and initial asset files**

- [ ] **Step 4: Re-run the unit test**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.data.content.ContentCoverageTest"`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/domain/model/CurriculumModels.kt app/src/main/java/com/mathisland/app/domain/model/QuestionModels.kt app/src/main/java/com/mathisland/app/data/content/CurriculumRepository.kt app/src/main/java/com/mathisland/app/data/content/ContentCoverage.kt app/src/main/assets/content/catalog.json app/src/main/assets/content/islands/*.json app/src/test/java/com/mathisland/app/data/content/ContentCoverageTest.kt
git commit -m "feat: add curriculum schema and coverage gates"
```

### Task 4: Add progress, rewards, and review scheduling

**Files:**
- Create: `app/src/main/java/com/mathisland/app/data/progress/ProgressStore.kt`
- Create: `app/src/main/java/com/mathisland/app/data/progress/ProgressRepository.kt`
- Create: `app/src/main/java/com/mathisland/app/domain/usecase/GetHomeStateUseCase.kt`
- Create: `app/src/main/java/com/mathisland/app/domain/usecase/GetMapStateUseCase.kt`
- Create: `app/src/main/java/com/mathisland/app/domain/usecase/SubmitLessonResultUseCase.kt`
- Create: `app/src/main/java/com/mathisland/app/domain/usecase/GetPendingReviewUseCase.kt`
- Test: `app/src/test/java/com/mathisland/app/domain/usecase/SubmitLessonResultUseCaseTest.kt`

- [ ] **Step 1: Write the failing reward-flow test**

```kotlin
@Test
fun twoWrongAnswers_scheduleReviewAndUnlockReward() = runTest {
    val result = submitLessonResult(
        levelId = "calc-carry-01",
        incorrectQuestionFamilies = listOf("calculation", "calculation")
    )
    assertEquals("calculation", result.pendingReview?.questionFamily)
    assertTrue(result.rewardUnlocked)
}
```

- [ ] **Step 2: Run the unit test to verify it fails**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.domain.usecase.SubmitLessonResultUseCaseTest"`

Expected: FAIL because progress and reward logic are missing.

- [ ] **Step 3: Implement DataStore progress, unlock rules, stars, chest rewards, and seagull review scheduling**

- [ ] **Step 4: Re-run the unit test**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.domain.usecase.SubmitLessonResultUseCaseTest"`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/data/progress/ProgressStore.kt app/src/main/java/com/mathisland/app/data/progress/ProgressRepository.kt app/src/main/java/com/mathisland/app/domain/usecase/GetHomeStateUseCase.kt app/src/main/java/com/mathisland/app/domain/usecase/GetMapStateUseCase.kt app/src/main/java/com/mathisland/app/domain/usecase/SubmitLessonResultUseCase.kt app/src/main/java/com/mathisland/app/domain/usecase/GetPendingReviewUseCase.kt app/src/test/java/com/mathisland/app/domain/usecase/SubmitLessonResultUseCaseTest.kt
git commit -m "feat: add tablet progress rewards and review logic"
```

## Chunk 3: Tablet-First UI Flow

### Task 5: Build the tablet home and full-map experience

**Files:**
- Create: `app/src/main/java/com/mathisland/app/ui/components/WoodButton.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/components/StoryPanelCard.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/components/IslandMapCanvas.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/home/HomeViewModel.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/home/HomeTabletScreen.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/map/MapViewModel.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/home/HomeTabletScreenTest.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`

- [ ] **Step 1: Write the failing home-screen UI test**

```kotlin
@Test
fun homeTabletScreen_showsStartAndMapSurface() {
    composeRule.setContent { HomeTabletScreen(...) }
    composeRule.onNodeWithText("继续冒险").assertExists()
    composeRule.onNodeWithContentDescription("island-map-canvas").assertExists()
}
```

- [ ] **Step 2: Run the home/map UI tests to verify they fail**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.home.HomeTabletScreenTest`

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.map.MapTabletScreenTest`

Expected: FAIL because tablet screens do not exist.

- [ ] **Step 3: Implement the welcome/home tablet screen and map screen with a persistent map canvas and floating story panel**

- [ ] **Step 4: Re-run the home/map UI tests**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.home.HomeTabletScreenTest`

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.map.MapTabletScreenTest`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/ui/components/WoodButton.kt app/src/main/java/com/mathisland/app/ui/components/StoryPanelCard.kt app/src/main/java/com/mathisland/app/ui/components/IslandMapCanvas.kt app/src/main/java/com/mathisland/app/feature/home/HomeViewModel.kt app/src/main/java/com/mathisland/app/feature/home/HomeTabletScreen.kt app/src/main/java/com/mathisland/app/feature/map/MapViewModel.kt app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt app/src/androidTest/java/com/mathisland/app/feature/home/HomeTabletScreenTest.kt app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt
git commit -m "feat: add tablet home and map flow"
```

### Task 6: Add island overlay, lesson split-pane, and reward overlay

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/island/IslandViewModel.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/level/LevelViewModel.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/reward/RewardOverlay.kt`
- Test: `app/src/test/java/com/mathisland/app/feature/level/LevelViewModelTest.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelTabletScreenTest.kt`

- [ ] **Step 1: Write the failing lesson progression test**

```kotlin
@Test
fun lessonCompletes_andShowsRewardOverlay() {
    val viewModel = LevelViewModel(fakeLevel())
    viewModel.submitAnswer("63")
    viewModel.submitAnswer("36")
    viewModel.submitAnswer("47")
    assertTrue(viewModel.state.value is LevelUiState.Reward)
}
```

- [ ] **Step 2: Run the unit test and tablet UI test to verify they fail**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.feature.level.LevelViewModelTest"`

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.level.LevelTabletScreenTest`

Expected: FAIL because lesson flow and reward overlay are missing.

- [ ] **Step 3: Implement the island task overlay, split-pane lesson screen, and reward overlay**

- [ ] **Step 4: Re-run the unit test and tablet UI test**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.feature.level.LevelViewModelTest"`

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.level.LevelTabletScreenTest`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/island/IslandViewModel.kt app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt app/src/main/java/com/mathisland/app/feature/level/LevelViewModel.kt app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt app/src/main/java/com/mathisland/app/feature/reward/RewardOverlay.kt app/src/test/java/com/mathisland/app/feature/level/LevelViewModelTest.kt app/src/androidTest/java/com/mathisland/app/feature/level/LevelTabletScreenTest.kt
git commit -m "feat: add tablet lesson and reward flow"
```

### Task 7: Add tablet question renderers

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/ChoiceQuestionPane.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/SortQuestionPane.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/GroupQuestionPane.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/RulerQuestionPane.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/level/QuestionPaneTest.kt`

- [ ] **Step 1: Write the failing renderer UI test**

```kotlin
@Test
fun rulerPane_showsTabletRulerHandle() {
    composeRule.setContent { RulerQuestionPane(sampleQuestion(), onSubmit = {}) }
    composeRule.onNodeWithTag("tablet-ruler-handle").assertExists()
}
```

- [ ] **Step 2: Run the UI test to verify it fails**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.level.QuestionPaneTest`

Expected: FAIL because the tablet renderers do not exist.

- [ ] **Step 3: Implement the five question panes sized for tablet interaction**

- [ ] **Step 4: Re-run the UI test**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.level.QuestionPaneTest`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/level/renderers/*.kt app/src/androidTest/java/com/mathisland/app/feature/level/QuestionPaneTest.kt
git commit -m "feat: add tablet question panes"
```

## Chunk 4: Content Completion and Release Gates

### Task 8: Fill out the full curriculum for tablet MVP

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
fun everyKnowledgePoint_hasAtLeastOnePlayableTabletLevel() {
    val report = validateCurriculumCoverage(loadCurriculum())
    assertTrue(report.missingKnowledgePoints.isEmpty())
}
```

- [ ] **Step 2: Run the unit test to verify it fails**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.data.content.FullCurriculumValidationTest"`

Expected: FAIL until all approved knowledge points are encoded.

- [ ] **Step 3: Complete the curriculum JSON files for all islands**

- [ ] **Step 4: Re-run the unit test**

Run: `.\gradlew testDebugUnitTest --tests "com.mathisland.app.data.content.FullCurriculumValidationTest"`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/assets/content/islands/*.json app/src/test/java/com/mathisland/app/data/content/FullCurriculumValidationTest.kt
git commit -m "feat: complete curriculum content for tablet mvp"
```

### Task 9: Add chest and parent summary tablet screens

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/chest/ChestViewModel.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/chest/ChestTabletScreen.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/parent/ParentGateScreen.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryTabletScreen.kt`
- Create: `app/src/main/java/com/mathisland/app/domain/usecase/GetParentSummaryUseCase.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/chest/ChestTabletScreenTest.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/parent/ParentSummaryTabletScreenTest.kt`

- [ ] **Step 1: Write the failing chest/parent UI tests**

```kotlin
@Test
fun chestScreen_showsUnlockedSticker() {
    composeRule.setContent { ChestTabletScreen(state = sampleChestState()) }
    composeRule.onNodeWithText("灯塔徽章").assertExists()
}
```

- [ ] **Step 2: Run the Android UI tests to verify they fail**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.chest.ChestTabletScreenTest`

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.parent.ParentSummaryTabletScreenTest`

Expected: FAIL because chest and parent tablet screens are missing.

- [ ] **Step 3: Implement the chest scene and minimal parent summary flow**

- [ ] **Step 4: Re-run the Android UI tests**

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.chest.ChestTabletScreenTest`

Run: `.\gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.parent.ParentSummaryTabletScreenTest`

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/chest/ChestViewModel.kt app/src/main/java/com/mathisland/app/feature/chest/ChestTabletScreen.kt app/src/main/java/com/mathisland/app/feature/parent/ParentGateScreen.kt app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryTabletScreen.kt app/src/main/java/com/mathisland/app/domain/usecase/GetParentSummaryUseCase.kt app/src/androidTest/java/com/mathisland/app/feature/chest/ChestTabletScreenTest.kt app/src/androidTest/java/com/mathisland/app/feature/parent/ParentSummaryTabletScreenTest.kt
git commit -m "feat: add tablet chest and parent summary screens"
```

### Task 10: Add final verification docs and release gates

**Files:**
- Create: `README.md`
- Create: `docs/testing.md`
- Modify: `app/build.gradle.kts`
- Test: `app/src/test/java/com/mathisland/app/SmokeCoverageTest.kt`

- [ ] **Step 1: Write the failing smoke-assembly test**

```kotlin
@Test
fun appContainer_exposesCoreTabletUseCases() {
    val container = AppContainer(fakeContext)
    assertNotNull(container.curriculumRepository)
    assertNotNull(container.getMapStateUseCase)
}
```

- [ ] **Step 2: Run the unit suite to verify it fails**

Run: `.\gradlew testDebugUnitTest`

Expected: FAIL until the remaining wiring gaps are closed.

- [ ] **Step 3: Add any missing app wiring and write setup/testing docs for Android tablets**

- [ ] **Step 4: Run the full verification commands**

Run: `.\gradlew testDebugUnitTest`

Run: `.\gradlew connectedDebugAndroidTest`

Run: `.\gradlew assembleDebug`

Expected: PASS for all three commands.

- [ ] **Step 5: Commit**

```bash
git add README.md docs/testing.md app/build.gradle.kts app/src/test/java/com/mathisland/app/SmokeCoverageTest.kt
git commit -m "docs: add android tablet verification guide"
```

## Manual Review Notes

No plan-document-reviewer subagent is available in this environment, so review each chunk manually:

- Verify every screen file is tablet-specific, not stretched from a phone layout.
- Verify map remains the primary surface on home and map flows.
- Verify lesson UI uses split-pane or anchored overlays, not phone-style full-screen stacks.
- Verify every curriculum unit from `docs/二年级数学.md` is covered by tests.
- Verify final verification commands are run fresh before claiming completion.

Plan complete and saved to `docs/superpowers/plans/2026-03-13-math-island-android-tablet-implementation.md`. Ready to execute?
