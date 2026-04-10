# Multi-Step Step Transition Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a short confirmation beat to `MULTI_STEP` so selected options visibly confirm before the next step appears.

**Architecture:** Keep transition state local to `MultiStepQuestionPane`, use a short delayed apply to commit answers, and expose deterministic tags so tests can verify the temporary confirmation mode without depending on animation frames.

**Tech Stack:** Kotlin, Jetpack Compose, JUnit, Android instrumentation tests

---

## Chunk 1: Red Tests

### Task 1: Add failing UI tests for confirmation state

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/MultiStepQuestionPaneTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`

- [ ] Add a test that clicks a step choice and expects a visible confirmation tag before the next prompt appears.
- [ ] Add a test that reset and submit are disabled while the pane is confirming a step.

### Task 2: Keep end-to-end coverage

**Files:**
- Verify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

- [ ] Reuse the existing flow coverage to make sure the delayed transition does not break completion.

## Chunk 2: Pane State And Visuals

### Task 3: Add transient transition state

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/MultiStepQuestionPane.kt`

- [ ] Add a local pending-transition state with selected choice and next branch metadata.
- [ ] Delay application by roughly 320 ms.
- [ ] Block repeat actions while the transition is active.

### Task 4: Add confirmation visuals

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/MultiStepQuestionPane.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererTokens.kt`

- [ ] Highlight the selected choice card and show a confirmation chip.
- [ ] Light up the just-finished progress chip during the transition.
- [ ] Add a small stage-card visual shift for the confirmation beat.

## Chunk 3: Concentrated Verification

### Task 5: Red then green verification

**Files:**
- Verify touched files above

- [ ] Run focused Android test compile or targeted checks to confirm the new tests fail before implementation.
- [ ] Re-run after implementation and confirm green.
- [ ] Run `./gradlew.bat assembleDebug`.
- [ ] Run `./gradlew.bat :app:compileDebugAndroidTestKotlin`.
- [ ] Run focused emulator regression for the multi-step pane and flow tests.
