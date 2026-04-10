# Multi-Step Presentations Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add richer step presentation metadata to `MULTI_STEP` and ship one new lesson that uses branch-aware stage titles, support copy, and semantic answer labels.

**Architecture:** Introduce a small `StepPresentation` model in `Question`, resolve it through the existing branch-key mechanism, update `MultiStepQuestionPane` to render current-step metadata and labeled summaries, then add a new four-step division lesson plus tests.

**Tech Stack:** Kotlin, Jetpack Compose, JUnit, Android instrumentation tests

---

## Chunk 1: Red Tests

### Task 1: Add failing tests for presentation metadata

**Files:**
- Modify: `app/src/test/java/com/mathisland/app/feature/level/renderers/MultiStepAnswerStateTest.kt`
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/MultiStepQuestionPaneTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

- [ ] Add red tests for branch-aware stage title, support text, and answer label resolution.
- [ ] Add red tests for the new `division-steps-07` lesson.

## Chunk 2: Model And Pane

### Task 2: Add `StepPresentation` metadata to `Question`

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/domain/model/GameModels.kt`

- [ ] Add `StepPresentation`.
- [ ] Add base-step and branch-step presentation collections.
- [ ] Keep old multi-step lessons backward compatible.

### Task 3: Resolve presentation metadata in renderer helpers

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/MultiStepBranchResolver.kt`

- [ ] Resolve branch-aware current-step presentation.
- [ ] Resolve answer labels for completed-step summaries.

### Task 4: Upgrade pane presentation

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/MultiStepQuestionPane.kt`

- [ ] Show current stage title and support copy.
- [ ] Use semantic labels in the completion summary.
- [ ] Preserve existing submission flow.

## Chunk 3: Content And Verification

### Task 5: Add the new demonstration lesson

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`
- Modify: `app/src/main/assets/content/islands/division-island.json`

- [ ] Add `division-steps-07`.
- [ ] Use a 4-step branch-then-converge shape with presentation metadata.

### Task 6: Concentrated verification

**Files:**
- Verify touched files above

- [ ] Run focused JVM tests red then green.
- [ ] Run `./gradlew.bat assembleDebug`.
- [ ] Run `./gradlew.bat :app:compileDebugAndroidTestKotlin`.
- [ ] Run focused emulator regression for multi-step tests.
