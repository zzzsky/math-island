# Multi-Step Content Feedback Hints Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add authored `stepFeedbackHints` to the complex `multi-step` lesson banks so retry and timeout review states point learners to the most useful steps.

**Architecture:** Keep the renderer unchanged and treat this batch as a content rollout. Author `stepFeedbackHints` in `CurriculumGameMapping.kt` for `division-steps-05~07`, then verify the configured expand targets and recap copy through focused unit and instrumentation coverage.

**Tech Stack:** Kotlin, Jetpack Compose, Android instrumentation tests, JUnit

---

## File Map

- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`
  Responsibility: add authored `stepFeedbackHints` to `division-steps-05`, `division-steps-06`, and `division-steps-07`
- Modify: `app/src/test/java/com/mathisland/app/feature/level/renderers/MultiStepAnswerStateTest.kt`
  Responsibility: verify content-authored feedback hint resolution still maps to the expected recap states
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`
  Responsibility: verify representative lesson content expands the intended recap steps in retry and timeout review

## Chunk 1: Content Authoring

### Task 1: Add authored hints for `division-steps-05`

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`

- [ ] Write a failing content-oriented assertion in an existing test or add one small test that checks `division-steps-05` has feedback hints on the intended steps.
- [ ] Run the focused test to verify it fails because the lesson still uses fallback hints.
- [ ] Add `stepFeedbackHints` for `division-steps-05`.
- [ ] Ensure step 1 is the retry auto-expand step.
- [ ] Ensure step 3 is the timeout auto-expand step.
- [ ] Run the focused test again and verify it passes.

### Task 2: Add authored hints for `division-steps-06`

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`

- [ ] Add a failing assertion for the intended `division-steps-06` expand targets.
- [ ] Run the focused test to verify it fails.
- [ ] Add `stepFeedbackHints` for `division-steps-06`.
- [ ] Ensure step 2 is the retry auto-expand step.
- [ ] Ensure step 3 is the timeout auto-expand step.
- [ ] Run the focused test again and verify it passes.

### Task 3: Add authored hints for `division-steps-07`

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`

- [ ] Add a failing assertion for the intended `division-steps-07` recap copy and expand targets.
- [ ] Run the focused test to verify it fails.
- [ ] Add `stepFeedbackHints` for `division-steps-07`.
- [ ] Ensure step 2 is the retry auto-expand step.
- [ ] Ensure correct review gives the final summary step stronger authored copy without auto-expanding.
- [ ] Run the focused test again and verify it passes.

## Chunk 2: Behavior Coverage

### Task 4: Add unit coverage for authored hint usage

**Files:**
- Modify: `app/src/test/java/com/mathisland/app/feature/level/renderers/MultiStepAnswerStateTest.kt`

- [ ] Write a failing unit test that checks authored hint labels and expand flags for representative lesson steps.
- [ ] Run `MultiStepAnswerStateTest` and verify the new test fails for the expected reason.
- [ ] Add only the minimal supporting assertions or helpers needed for lesson-bank content access.
- [ ] Run `MultiStepAnswerStateTest` again and verify it passes.

### Task 5: Add instrumentation coverage for authored lesson behavior

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`

- [ ] Write a failing instrumentation test using `division-steps-05`, `division-steps-06`, or `division-steps-07` content directly instead of the synthetic helper question.
- [ ] Verify retry review expands the content-authored step.
- [ ] Verify timeout review expands the content-authored step.
- [ ] Verify correct review shows authored recap copy without forced expansion.
- [ ] Run the focused instrumentation target and confirm it passes.

## Chunk 3: Verification

### Task 6: Concentrated verification

**Files:**
- Verify files above

- [ ] Run `./gradlew.bat testDebugUnitTest --tests com.mathisland.app.feature.level.renderers.MultiStepAnswerStateTest`
- [ ] Run `./gradlew.bat :app:compileDebugAndroidTestKotlin`
- [ ] Run `./gradlew.bat assembleDebug`
- [ ] Run the focused emulator regression script with:
  `com.mathisland.app.feature.level.MultiStepQuestionPaneTest`
  `com.mathisland.app.feature.level.LevelAnswerPaneTest`
  `com.mathisland.app.MathIslandTabletFlowTest`
- [ ] Confirm emulator-related processes are stopped after the regression run.

### Task 7: Batch completion

**Files:**
- Verify git state for current branch

- [ ] Review the diff for content-only scope creep.
- [ ] Commit the batch with a message focused on authored multi-step hints.
- [ ] Push when requested.
