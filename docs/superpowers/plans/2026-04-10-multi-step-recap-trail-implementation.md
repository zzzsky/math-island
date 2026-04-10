# Multi-Step Recap Trail Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add collapsed read-only recap cards for completed `MULTI_STEP` steps and allow tap-to-expand detail review.

**Architecture:** Keep recap state local to `MultiStepQuestionPane`, derive recap content from existing branch-aware metadata, and preserve the current single active stage card as the only editable area.

**Tech Stack:** Kotlin, Jetpack Compose, Android instrumentation tests, JUnit

---

## Chunk 1: Red Tests

### Task 1: Add failing recap trail tests

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/MultiStepQuestionPaneTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`

- [ ] Add a test that completing one step creates a collapsed recap card.
- [ ] Add a test that tapping the recap card expands prompt/support/answer details.
- [ ] Add a test that recap cards stay read-only and do not replace the active stage card.

### Task 2: Reuse flow coverage

**Files:**
- Verify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

- [ ] Keep the existing multi-step flow tests as compatibility coverage.

## Chunk 2: Renderer Helpers And UI

### Task 3: Add branch-aware recap helper lookups

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/MultiStepBranchResolver.kt`

- [ ] Resolve prompt and presentation metadata for arbitrary completed step indexes.
- [ ] Reuse existing branch keys instead of duplicating recap-only data.

### Task 4: Render the recap trail

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/MultiStepQuestionPane.kt`

- [ ] Add local expanded-recap and recent-completion states.
- [ ] Render collapsed recap cards above the active stage card.
- [ ] Toggle expansion on card tap.
- [ ] Keep recap cards read-only.

## Chunk 3: Verification

### Task 5: Concentrated verification

**Files:**
- Verify touched files above

- [ ] Run focused Android test compile.
- [ ] Run focused JVM regression for existing multi-step model tests.
- [ ] Run `./gradlew.bat assembleDebug`.
- [ ] Run focused emulator regression for multi-step pane, level pane, and tablet flow tests.

### Task 6: Batch completion

**Files:**
- Verify git state for current branch

- [ ] Commit the verified batch.
- [ ] Confirm emulator-related processes are stopped.
