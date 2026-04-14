# Division Remainder Application Lines Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add three division remainder application lessons that separately teach leftover-only scenes, container rounding-up, and transport rounding-up.

**Architecture:** Keep the renderer unchanged and implement the batch as content growth plus curriculum ordering. Add the three new lesson entries in the division island JSON, author three new `division` question banks in `CurriculumGameMapping.kt`, then extend controller and tablet flow tests to verify the new scene lines are playable and correctly ordered.

**Tech Stack:** Kotlin, JSON curriculum assets, Android instrumentation tests, JVM tests

---

## File Map

- Modify: `app/src/main/assets/content/islands/division-island.json`
  Responsibility: add `division-leftover-01`, `division-container-01`, and `division-transport-01` in the correct place.
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`
  Responsibility: add question banks for the three new remainder application lessons.
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`
  Responsibility: verify the new lesson order and content mapping.
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`
  Responsibility: verify at least one of the new application lessons completes in the real flow.

## Chunk 1: Red Test For The New Scene Lines

### Task 1: Define the expanded remainder application ladder

**Files:**
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`

- [ ] **Step 1: Write a failing test for the new lesson ids and order**

Assert the division island now contains:

```kotlin
listOf(
    "division-share-01",
    "division-share-02",
    "division-remainder-01",
    "division-remainder-02",
    "division-leftover-01",
    "division-container-01",
    "division-transport-01",
    "division-apply-01"
)
```

before the existing `division-steps-*` block.

- [ ] **Step 2: Assert the three new lessons resolve to non-empty `division` questions**

- [ ] **Step 3: Run the focused JVM test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests com.mathisland.app.MathIslandGameControllerTest`

Expected: FAIL because the new lesson ids do not exist yet.

## Chunk 2: Curriculum Asset Update

### Task 2: Add the three lesson entries into the division island asset

**Files:**
- Modify: `app/src/main/assets/content/islands/division-island.json`

- [ ] **Step 1: Add `division-leftover-01` after `division-remainder-02`**

Use a summary that clearly says the learner should identify what remains.

- [ ] **Step 2: Add `division-container-01` after `division-leftover-01`**

Use a summary that clearly says leftovers still need another container.

- [ ] **Step 3: Add `division-transport-01` after `division-container-01`**

Use a summary that clearly says leftovers still need another vehicle/trip.

- [ ] **Step 4: Re-check the final lesson order**

## Chunk 3: Parallel Content Authoring

### Task 3: Add the three question banks

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`

- [ ] **Step 1: Add `division-leftover-01` question bank**

All questions should stop at "how many are left."

- [ ] **Step 2: Add `division-container-01` question bank**

All questions should require one more container when a remainder exists.

- [ ] **Step 3: Add `division-transport-01` question bank**

All questions should require one more vehicle/trip when a remainder exists.

- [ ] **Step 4: Keep all three within the current division difficulty band**

No new renderer fields, no step-based interaction.

## Chunk 4: Green The JVM Test

### Task 4: Verify the controller loads the new scene lines

**Files:**
- Modify: `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`

- [ ] **Step 1: Finalize the test assertions for order and non-empty question content**

- [ ] **Step 2: Run the focused JVM test and verify it passes**

Run: `./gradlew.bat testDebugUnitTest --tests com.mathisland.app.MathIslandGameControllerTest`

Expected: PASS

## Chunk 5: Flow Coverage

### Task 5: Add one focused flow regression for the new lessons

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

- [ ] **Step 1: Add a tablet flow test for one of the three new scene lines**

Prefer `division-container-01` or `division-transport-01` because they verify the strongest scene interpretation.

- [ ] **Step 2: Update helper lesson id sets if needed**

Keep division unlock/seed helpers consistent with the new ladder.

- [ ] **Step 3: Keep the flow test behavior-focused**

Open lesson, answer sequence, assert reward, return to map.

## Chunk 6: Verification

### Task 6: Run concentrated verification

**Files:**
- Verify files above

- [ ] **Step 1: Run the focused JVM test**
- [ ] **Step 2: Run `./gradlew.bat :app:compileDebugAndroidTestKotlin`**
- [ ] **Step 3: Run `./gradlew.bat assembleDebug`**
- [ ] **Step 4: Run focused emulator regression for `com.mathisland.app.MathIslandTabletFlowTest`**
- [ ] **Step 5: Stop emulator-related processes afterward**

## Chunk 7: Batch Completion

### Task 7: Review scope and prepare for integration

**Files:**
- Verify git state for the batch

- [ ] **Step 1: Review the diff for division-remainder-only scope**
- [ ] **Step 2: Commit when ready**
- [ ] **Step 3: Push when requested**
