# Grouped Matching Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add grouped semantic matching questions without changing the controller answer contract.

**Architecture:** Extend `Question` with optional grouped matching data, upgrade `MatchingAnswerState` and `MatchingQuestionPane` to manage per-group assignments, then wire one grouped lesson into classification island and cover it with focused unit and emulator tests.

**Tech Stack:** Kotlin, Jetpack Compose, Android instrumentation tests, JUnit

---

## Chunk 1: Red Tests

### Task 1: Add grouped matching state tests

**Files:**
- Modify: `app/src/test/java/com/mathisland/app/feature/level/renderers/MatchingAnswerStateTest.kt`

- [ ] Add failing tests for grouped encoding and grouped completeness.
- [ ] Run focused unit tests and confirm failure.

### Task 2: Add grouped pane and flow tests

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/MatchingQuestionPaneTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`

- [ ] Add failing grouped matching renderer tests.
- [ ] Add failing grouped lesson flow test.
- [ ] Run compile/test entry points and confirm failure.

## Chunk 2: Model And Renderer

### Task 3: Add grouped matching domain model

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/domain/model/GameModels.kt`

- [ ] Add `MatchingGroup`.
- [ ] Add `matchingGroups` to `Question`.

### Task 4: Upgrade matching state

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/MatchingAnswerState.kt`

- [ ] Support per-group assignments and encoding.
- [ ] Preserve old single-group helpers for compatibility.

### Task 5: Upgrade matching pane

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/MatchingQuestionPane.kt`

- [ ] Render grouped sections when `matchingGroups` is present.
- [ ] Keep old layout for single-group questions.
- [ ] Submit a single encoded answer string.

## Chunk 3: Content And Verification

### Task 6: Add grouped lesson content

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`
- Modify: `app/src/main/assets/content/islands/classification-island.json`

- [ ] Add `classification-match-05`.
- [ ] Use semantic grouped content only.

### Task 7: Verify and commit

**Files:**
- Verify all touched files above

- [ ] Run focused unit tests.
- [ ] Run `./gradlew.bat assembleDebug`.
- [ ] Run focused emulator regression for grouped matching tests.
- [ ] Commit with `feat: add grouped matching lessons`.
