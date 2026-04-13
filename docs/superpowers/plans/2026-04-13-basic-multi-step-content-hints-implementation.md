# Basic Multi-Step Content Hints Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add lightweight authored `stepFeedbackHints` to the basic `multi-step` lessons and tighten test scope so synthetic tests validate renderer mechanics while real lessons validate content behavior.

**Architecture:** Keep the current review-mode renderer untouched. Add lesson-authored hints for `division-steps-01~04` in curriculum mapping, then move repeated content assertions toward real lesson tests and leave synthetic tests focused on generic renderer behavior.

**Tech Stack:** Kotlin, Jetpack Compose, Android instrumentation tests, JUnit

---

## File Map

- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`
  Responsibility: author `stepFeedbackHints` for `division-steps-01~04`
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`
  Responsibility: verify the new lesson-content hint mapping for `division-steps-01~04`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`
  Responsibility: add real-lesson behavior checks for `division-steps-01~04` and remove redundant synthetic content assertions where appropriate

## Chunk 1: Real Lesson Red Tests

### Task 1: Add failing content-mapping tests for `division-steps-01~04`

**Files:**
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`

- [ ] Add failing assertions that `division-steps-01~04` expose the intended `incorrect` and `timeout` hint metadata.
- [ ] Run the focused controller/curriculum unit tests and verify failure is caused by missing authored hints.

### Task 2: Add failing real-lesson instrumentation checks for `division-steps-01~04`

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`

- [ ] Add failing instrumentation tests using real lesson content for the new foundational lessons.
- [ ] Cover at least one retry expansion and one timeout expansion in the newly-authored lesson set.
- [ ] Run the focused instrumentation target and verify failure is caused by missing lesson hints.

## Chunk 2: Content Authoring

### Task 3: Author hints for `division-steps-01~03`

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`

- [ ] Add lightweight two-step `stepFeedbackHints` for `division-steps-01`
- [ ] Add lightweight two-step `stepFeedbackHints` for `division-steps-02`
- [ ] Add lightweight two-step `stepFeedbackHints` for `division-steps-03`
- [ ] Ensure retry expands step 1 and timeout expands step 2 for each lesson

### Task 4: Author hints for `division-steps-04`

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`

- [ ] Add three-step `stepFeedbackHints` for `division-steps-04`
- [ ] Ensure retry expands step 1
- [ ] Ensure timeout expands step 3
- [ ] Keep the middle calculation step informative but non-expanding

## Chunk 3: Test Scope Cleanup

### Task 5: Remove overlapping synthetic content assertions

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`

- [ ] Review the synthetic multi-step feedback tests added earlier
- [ ] Keep generic renderer-mechanism coverage
- [ ] Remove or compress synthetic assertions that now duplicate real lesson content coverage
- [ ] Make sure at least one synthetic test still proves custom hint auto-expand behavior independently of curriculum content

## Chunk 4: Verification

### Task 6: Focused verification

**Files:**
- Verify files above

- [ ] Run `./gradlew.bat testDebugUnitTest --tests com.mathisland.app.MathIslandGameControllerTest --tests com.mathisland.app.feature.level.renderers.MultiStepAnswerStateTest`
- [ ] Run `./gradlew.bat :app:compileDebugAndroidTestKotlin`
- [ ] Run `./gradlew.bat assembleDebug`
- [ ] Run focused emulator regression for:
  `com.mathisland.app.feature.level.MultiStepQuestionPaneTest`
  `com.mathisland.app.feature.level.LevelAnswerPaneTest`
  `com.mathisland.app.MathIslandTabletFlowTest`
- [ ] Confirm emulator-related processes are stopped afterward

### Task 7: Batch completion

**Files:**
- Verify git state for current branch

- [ ] Review the diff for content/test-only scope
- [ ] Commit when ready
- [ ] Push when requested
