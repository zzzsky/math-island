# Multi-Step Conditional Steps Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add conditional branching to `MULTI_STEP` questions without changing the controller answer API.

**Architecture:** Extend `Question` with optional branch metadata, upgrade `MultiStepAnswerState` and `MultiStepQuestionPane` to resolve branch-dependent prompts and choices, then add one conditional lesson in division island and cover both branch paths with unit and emulator tests.

**Tech Stack:** Kotlin, Jetpack Compose, Android instrumentation tests, JUnit

---

## Chunk 1: Red Tests

### Task 1: Add failing state tests

**Files:**
- Modify: `app/src/test/java/com/mathisland/app/feature/level/renderers/MultiStepAnswerStateTest.kt`

- [ ] Add tests for conditional branch resolution and encoded answers.
- [ ] Run focused unit tests and confirm failure.

### Task 2: Add failing pane and flow tests

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/MultiStepQuestionPaneTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`

- [ ] Add tests for both conditional paths.
- [ ] Run compile/test entry points and confirm failure.

## Chunk 2: Model And Renderer

### Task 3: Extend question model

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/domain/model/GameModels.kt`

- [ ] Add branch metadata structures for multi-step questions.
- [ ] Keep old multi-step questions fully compatible.

### Task 4: Upgrade multi-step state

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/MultiStepAnswerState.kt`

- [ ] Track selected answers and resolved branch keys.
- [ ] Compute current prompt/choice source from branch state.
- [ ] Preserve final comma-joined answer encoding.

### Task 5: Upgrade multi-step pane

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/MultiStepQuestionPane.kt`

- [ ] Render branch-specific prompts and choices.
- [ ] Keep reset and submit behavior stable.
- [ ] Leave non-conditional lessons unchanged.

## Chunk 3: Content And Verification

### Task 6: Add first conditional lesson

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`
- Modify: `app/src/main/assets/content/islands/division-island.json`

- [ ] Add `division-steps-05`.
- [ ] Ensure one branch represents “有余数” and one represents “整除”.

### Task 7: Verify and commit

**Files:**
- Verify all touched files above

- [ ] Run focused unit tests.
- [ ] Run `./gradlew.bat assembleDebug`.
- [ ] Run focused emulator regression for conditional multi-step tests.
- [ ] Commit with `feat: add conditional multi step lessons`.
