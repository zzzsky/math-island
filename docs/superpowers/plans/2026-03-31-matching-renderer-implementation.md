# Matching Renderer Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a new `MATCHING` lesson renderer with drag-to-target pairing while preserving the existing lesson controller and reward flow.

**Architecture:** Extend `Question` with optional matching payload fields, map a new `MATCHING` family into `QuestionRendererType`, and implement a focused `MatchingQuestionPane` that encodes pairings back into the existing string-based answer contract. Keep controller logic unchanged and attach the first content slice through `CurriculumGameMapping`.

**Tech Stack:** Kotlin, Jetpack Compose, Compose UI tests, Android emulator regression, existing lesson/reward flow.

---

## Chunk 1: Model And Routing

### Task 1: Extend `Question` for matching payloads

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/domain/model/GameModels.kt`
- Test: `app/src/test/java/com/mathisland/app/feature/level/renderers/MatchingAnswerStateTest.kt`

- [ ] **Step 1: Write the failing unit test**

Add a unit test that constructs a matching question and expects answer encoding to follow left-item order.

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.renderers.MatchingAnswerStateTest"`

Expected: FAIL because matching state/encoding does not exist yet.

- [ ] **Step 3: Add optional matching fields to `Question`**

Add:
- `leftItems: List<String> = emptyList()`
- `rightItems: List<String> = emptyList()`

- [ ] **Step 4: Add minimal matching answer encoder state**

Create `MatchingAnswerState.kt` with:
- pairing map
- replace/override behavior
- stable `encodedAnswer(leftItems)` output
- `isComplete(leftItems)`

- [ ] **Step 5: Run the focused unit test**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.renderers.MatchingAnswerStateTest"`

Expected: PASS

- [ ] **Step 6: Commit**

```powershell
git add app/src/main/java/com/mathisland/app/domain/model/GameModels.kt app/src/main/java/com/mathisland/app/feature/level/renderers/MatchingAnswerState.kt app/src/test/java/com/mathisland/app/feature/level/renderers/MatchingAnswerStateTest.kt
git commit -m "feat: add matching answer state"
```

### Task 2: Add renderer routing for `MATCHING`

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/QuestionRendererType.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/LevelAnswerPane.kt`
- Test: `app/src/test/java/com/mathisland/app/feature/level/renderers/MatchingAnswerStateTest.kt`

- [ ] **Step 1: Write the failing routing assertion**

Add a focused test that expects `rendererTypeFor("MATCHING")` to return `QuestionRendererType.MATCHING`.

- [ ] **Step 2: Run the focused test**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.renderers.MatchingAnswerStateTest"`

Expected: FAIL on missing renderer type/mapping.

- [ ] **Step 3: Add `MATCHING` to renderer type routing**

Update `QuestionRendererType.kt` and wire `LevelAnswerPane` to dispatch to `MatchingQuestionPane`.

- [ ] **Step 4: Re-run the focused test**

Run the same command.

Expected: PASS

- [ ] **Step 5: Commit**

```powershell
git add app/src/main/java/com/mathisland/app/QuestionRendererType.kt app/src/main/java/com/mathisland/app/feature/level/LevelAnswerPane.kt
git commit -m "feat: route matching renderer"
```

## Chunk 2: Matching Renderer UI

### Task 3: Build `MatchingQuestionPane`

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/MatchingQuestionPane.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererTokens.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/level/MatchingQuestionPaneTest.kt`

- [ ] **Step 1: Write the failing Compose test**

Cover:
- renderer root tag
- left and right columns render
- submission disabled before all pairs are set
- replacing a right-side target updates the encoded answer

- [ ] **Step 2: Run the focused androidTest compile or test**

Run: `./gradlew.bat :app:compileDebugAndroidTestKotlin`

Expected: FAIL because `MatchingQuestionPane` and its test references do not exist yet.

- [ ] **Step 3: Implement minimal renderer**

Requirements:
- root tag `renderer-matching`
- left draggable cards
- right target slots
- visual link state
- submit only after complete matching
- call `onAnswer(encodedAnswer)` on submit

- [ ] **Step 4: Re-run androidTest compile**

Run: `./gradlew.bat :app:compileDebugAndroidTestKotlin`

Expected: PASS

- [ ] **Step 5: Run focused unit/build checks**

Run:
- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`

Expected: PASS

- [ ] **Step 6: Commit**

```powershell
git add app/src/main/java/com/mathisland/app/feature/level/renderers/MatchingQuestionPane.kt app/src/main/java/com/mathisland/app/feature/level/renderers/RendererTokens.kt app/src/androidTest/java/com/mathisland/app/feature/level/MatchingQuestionPaneTest.kt
git commit -m "feat: add matching question pane"
```

## Chunk 3: Content And Regression

### Task 4: Add first matching lesson content

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`

- [ ] **Step 1: Write the failing content test**

Add a test that resolves the new classification lesson and verifies it serves matching questions.

- [ ] **Step 2: Run the focused unit test**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.MathIslandGameControllerTest"`

Expected: FAIL on unknown lesson/family.

- [ ] **Step 3: Add first matching content slice**

In `CurriculumGameMapping.kt`:
- add a matching family
- add one lesson/question bank entry under classification
- use stable encoded `correctChoice`

- [ ] **Step 4: Re-run the focused unit test**

Expected: PASS

- [ ] **Step 5: Commit**

```powershell
git add app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt
git commit -m "feat: add matching lesson content"
```

### Task 5: Focused emulator regression

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/level/MatchingQuestionPaneTest.kt`

- [ ] **Step 1: Add focused instrumentation coverage**

Cover:
- `renderer-matching` appears
- full match enables submit
- lesson can advance after submission

- [ ] **Step 2: Run focused emulator regression**

Run a focused emulator path for:
- `MatchingQuestionPaneTest`
- `LevelAnswerPaneTest`

Expected: PASS

- [ ] **Step 3: Run milestone verification**

Run:
- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`

If environment is stable, also run focused emulator regression again.

- [ ] **Step 4: Commit**

```powershell
git add app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt app/src/androidTest/java/com/mathisland/app/feature/level/MatchingQuestionPaneTest.kt
git commit -m "test: cover matching renderer flow"
```
