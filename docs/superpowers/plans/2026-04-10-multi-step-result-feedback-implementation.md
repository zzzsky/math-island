# Multi-Step Result Feedback Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Feed lesson result states back into the `MULTI_STEP` recap trail so completed steps become a readable post-submit diagnosis layer.

**Architecture:** Add optional step-level feedback hints to `Question`, reconstruct a display-only multi-step state from `submittedAnswer` when needed, and let `MultiStepQuestionPane` map lesson feedback into recap-card result chips and auto-expanded detail states.

**Tech Stack:** Kotlin, Jetpack Compose, Android instrumentation tests, JUnit

---

## Chunk 1: Red Tests

### Task 1: Add failing feedback-mode tests

**Files:**
- Modify: `app/src/test/java/com/mathisland/app/feature/level/renderers/MultiStepAnswerStateTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`

- [ ] Add a unit test for reconstructing multi-step state from submitted encoded answers.
- [ ] Add Android tests for correct, retry, and timeout recap-card states.
- [ ] Add a test for retry-mode auto expansion of a focused step.

## Chunk 2: Models And Helpers

### Task 2: Add step feedback hint metadata

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/domain/model/GameModels.kt`

- [ ] Add a lightweight `StepFeedbackHint` model.
- [ ] Keep all existing content backward compatible.

### Task 3: Add reconstruction and hint helpers

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/MultiStepBranchResolver.kt`

- [ ] Rebuild branch-aware `MultiStepAnswerState` from encoded answers.
- [ ] Resolve per-step recap feedback labels, bodies, and auto-expand flags with generic fallbacks.

## Chunk 3: Renderer Integration

### Task 4: Connect result mode to recap trail

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/MultiStepQuestionPane.kt`
- Optional: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`

- [ ] Enter recap result mode when feedback is present.
- [ ] Decorate recap cards with correct/retry/timeout statuses.
- [ ] Auto-expand configured recap steps for retry/timeout.
- [ ] Keep existing local solving flow unchanged.

## Chunk 4: Verification

### Task 5: Concentrated verification

**Files:**
- Verify touched files above

- [ ] Run focused unit tests for multi-step state helpers.
- [ ] Run `./gradlew.bat :app:compileDebugAndroidTestKotlin`.
- [ ] Run `./gradlew.bat assembleDebug`.
- [ ] Run focused emulator regression for level pane, multi-step pane, and tablet flow tests.

### Task 6: Batch completion

**Files:**
- Verify git state for current branch

- [ ] Commit the verified batch.
- [ ] Confirm emulator-related processes are stopped.
