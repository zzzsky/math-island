# Multi-Step Branch Convergence Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a converged conditional lesson to `MULTI_STEP` and verify that multiple branches can share a later step without changing answer submission.

**Architecture:** Reuse the existing branch-key resolver, express convergence in lesson content, harden `MultiStepQuestionPane` state reset keys, and cover the new lesson with unit and emulator flow tests.

**Tech Stack:** Kotlin, Jetpack Compose, JUnit, Android instrumentation tests

---

## Chunk 1: Red Tests

### Task 1: Add failing lesson-availability and convergence tests

**Files:**
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`
- Modify: `app/src/test/java/com/mathisland/app/feature/level/renderers/MultiStepAnswerStateTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/MultiStepQuestionPaneTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

- [x] Add controller coverage for `division-steps-06`.
- [x] Add unit coverage for shared final branch resolution.
- [x] Add pane and flow coverage for converged step rendering.
- [x] Run focused JVM tests and confirm failure before implementation.

## Chunk 2: Content Wiring

### Task 2: Add converged division lesson

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`
- Modify: `app/src/main/assets/content/islands/division-island.json`

- [x] Add `division-steps-06`.
- [x] Use branch-specific step 2 prompts and a shared step 3 prompt.
- [x] Keep final answer encoding unchanged.

### Task 3: Harden renderer state reset keys

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/MultiStepQuestionPane.kt`

- [x] Include branch metadata in `remember` keys so question swaps do not leak old local state.

## Chunk 3: Green Verification

### Task 4: Re-run focused tests and compile gates

**Files:**
- Verify all touched files above

- [ ] Re-run focused JVM tests and confirm green.
- [ ] Run `./gradlew.bat assembleDebug`.
- [ ] Run `./gradlew.bat :app:compileDebugAndroidTestKotlin`.
- [ ] Run focused emulator regression for multi-step convergence.

### Task 5: Batch completion

**Files:**
- Verify git state for current branch

- [ ] Summarize changed files and outcomes.
- [ ] Commit after verification.
- [ ] Clean emulator-related processes after the phase.
