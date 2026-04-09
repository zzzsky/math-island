# Fill Blank Mixed Slot Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add mixed slot support to `FILL_BLANK` so one lesson can combine number and unit slots while preserving the existing lesson and controller flow.

**Architecture:** Extend `Question` with optional slot-kind metadata, keep encoded answers as ordered strings, and teach `FillBlankQuestionPane` to render slot cues and mismatch styling. Add one mixed-slot measurement lesson and verify it through unit, pane, and tablet flow tests.

**Tech Stack:** Kotlin, Jetpack Compose, Android instrumented tests, existing curriculum JSON/content mapping

---

## Chunk 1: Red Tests

### Task 1: Add mixed lesson resolution test

**Files:**
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`

- [ ] Add a test asserting `measure-fill-05` exists and exposes `blankSlotKinds`
- [ ] Run the focused unit test and verify it fails

### Task 2: Add mixed fill-blank renderer test

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/FillBlankQuestionPaneTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`

- [ ] Add a pane test for mixed number/unit slots
- [ ] Add a `LevelAnswerPane` assertion for slot cue tags/text
- [ ] Run Android test compile and verify it fails for missing model/renderer support

### Task 3: Add mixed lesson main-flow test

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

- [ ] Add one flow test for `start-measure-fill-05`
- [ ] Run focused flow compile and verify it fails or remains blocked on missing content

## Chunk 2: Minimal Implementation

### Task 4: Extend the question model

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/domain/model/GameModels.kt`

- [ ] Add `blankSlotKinds`
- [ ] Keep defaults backward compatible

### Task 5: Add mixed lesson content

**Files:**
- Modify: `app/src/main/assets/content/islands/measurement-geometry-island.json`
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`

- [ ] Add `measure-fill-05`
- [ ] Add one mixed-slot question bank entry
- [ ] Keep correct answer encoded in slot order

### Task 6: Teach fill-blank renderer slot cues

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/FillBlankQuestionPane.kt`

- [ ] Derive slot count and kinds safely
- [ ] Show type cues per slot
- [ ] Show mismatch warning state when assignment kind differs from slot kind
- [ ] Preserve existing selection/assignment flow

## Chunk 3: Verification

### Task 7: Turn tests green

**Files:**
- Modify as needed:
  - `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`
  - `app/src/androidTest/java/com/mathisland/app/feature/level/FillBlankQuestionPaneTest.kt`
  - `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`
  - `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

- [ ] Run focused unit tests
- [ ] Run `:app:compileDebugAndroidTestKotlin`
- [ ] Run focused emulator regression for fill-blank flow
- [ ] Run `assembleDebug`

### Task 8: Commit

**Files:**
- Commit all files from this batch

- [ ] `git add` changed files
- [ ] `git commit -m "feat: add mixed fill blank slots"`
