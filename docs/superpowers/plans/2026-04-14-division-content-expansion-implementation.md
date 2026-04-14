# Division Content Expansion Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Expand the division island with three new pre-`MULTI_STEP` lessons so the curriculum ladder better covers equal sharing, remainders, and short application interpretation.

**Architecture:** Keep the existing division renderer stack intact and grow the curriculum through content assets plus question-bank mappings. Reorder the island's lessons in the asset JSON, add concrete question sets in `CurriculumGameMapping.kt`, and lock the change with controller and tablet flow verification.

**Tech Stack:** Kotlin, Jetpack Compose, JSON curriculum assets, Android instrumentation tests, JVM tests

---

## File Map

- Modify: `app/src/main/assets/content/islands/division-island.json`
  Responsibility: add the new division lesson metadata and reorder the lesson ladder.
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`
  Responsibility: add question banks for `division-share-02`, `division-remainder-02`, and `division-apply-01`.
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`
  Responsibility: verify the division island curriculum exposes the new lessons and maps them to playable content.
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`
  Responsibility: verify at least one new division lesson can be entered and completed through the normal tablet flow.

## Chunk 1: Red Tests For Expanded Division Ladder

### Task 1: Define the expected lesson order in tests

**Files:**
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`

- [ ] **Step 1: Write a failing test for the expanded division lesson order**

Add a test that loads the curriculum/game controller and asserts the division island lesson ids appear in this order:

```kotlin
listOf(
    "division-share-01",
    "division-share-02",
    "division-remainder-01",
    "division-remainder-02",
    "division-apply-01",
    "division-steps-01",
    "division-steps-02",
    "division-steps-03",
    "division-steps-04",
    "division-steps-05",
    "division-steps-06",
    "division-steps-07"
)
```

- [ ] **Step 2: Run the focused unit test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests com.mathisland.app.MathIslandGameControllerTest`

Expected: FAIL because the three new lesson ids do not exist yet.

## Chunk 2: Content Asset Expansion

### Task 2: Add the new lesson metadata into the division island asset

**Files:**
- Modify: `app/src/main/assets/content/islands/division-island.json`

- [ ] **Step 1: Insert `division-share-02` after `division-share-01`**

Use a summary focused on another equal-sharing scenario.

- [ ] **Step 2: Insert `division-remainder-02` after `division-remainder-01`**

Use a summary focused on understanding leftover amounts rather than immediately rounding up.

- [ ] **Step 3: Insert `division-apply-01` before the `division-steps-*` block**

Use a summary focused on short application interpretation.

- [ ] **Step 4: Re-check the final lesson order in the JSON**

The final order must match the spec exactly.

## Chunk 3: Question Bank Implementation

### Task 3: Add playable content for the new lessons

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`

- [ ] **Step 1: Add a question bank for `division-share-02`**

Use the existing `Question` structure with `family = "division"` and standard choice questions.

- [ ] **Step 2: Add a question bank for `division-remainder-02`**

Use questions that distinguish quotient from leftover and do not frame every item as a rounding-up context.

- [ ] **Step 3: Add a question bank for `division-apply-01`**

Use short story-style prompts where the child still answers with a standard choice interaction.

- [ ] **Step 4: Keep the new questions within the existing difficulty band**

Do not introduce new interaction fields or renderer-specific state.

## Chunk 4: Green The Unit Test

### Task 4: Verify the controller can load the new division ladder

**Files:**
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`

- [ ] **Step 1: Extend the test to assert each new lesson has non-empty question content**

Check that `division-share-02`, `division-remainder-02`, and `division-apply-01` all resolve to playable question lists.

- [ ] **Step 2: Run the focused unit test to verify it passes**

Run: `./gradlew.bat testDebugUnitTest --tests com.mathisland.app.MathIslandGameControllerTest`

Expected: PASS

## Chunk 5: Flow Coverage

### Task 5: Add a focused tablet-flow regression for the new division content

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

- [ ] **Step 1: Add one focused flow test for a new division lesson**

Prefer `division-apply-01` because it is the bridge lesson in this batch.

- [ ] **Step 2: Use the existing division lesson helpers**

Open the lesson from the map, answer the sequence, assert reward, and return to the map.

- [ ] **Step 3: Keep the test behavior-focused**

Assert lesson entry and successful completion, not exact copy beyond stable labels already used in flow tests.

## Chunk 6: Verification

### Task 6: Run concentrated verification

**Files:**
- Verify files above

- [ ] **Step 1: Run the focused JVM test**

Run: `./gradlew.bat testDebugUnitTest --tests com.mathisland.app.MathIslandGameControllerTest`

- [ ] **Step 2: Run Android test compilation**

Run: `./gradlew.bat :app:compileDebugAndroidTestKotlin`

- [ ] **Step 3: Run assemble**

Run: `./gradlew.bat assembleDebug`

- [ ] **Step 4: Run focused emulator regression**

Run focused instrumentation for:

- `com.mathisland.app.MathIslandTabletFlowTest`

- [ ] **Step 5: Stop emulator-related processes after verification**

Confirm `emulator.exe` and `qemu-system-x86_64.exe` are not left running. If `adb.exe` auto-respawns from an external tool, note it explicitly rather than treating it as a project failure.

## Chunk 7: Batch Completion

### Task 7: Review scope and prepare for commit

**Files:**
- Verify git state for the batch

- [ ] **Step 1: Review the diff for division-content-only scope**
- [ ] **Step 2: Commit when the batch is ready**
- [ ] **Step 3: Push when requested**
