# Unified Ending Pages Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Redesign the child reward page and the parent summary page into one unified ending-page system with shared visual structure and distinct child/parent voices.

**Architecture:** Keep reward and parent summary as separate product destinations, but align them around a shared four-layer ending-page structure: hero, core summary, secondary details, and next action. Reuse compatible UI primitives where possible and update tests to validate section hierarchy and CTA emphasis.

**Tech Stack:** Kotlin, Jetpack Compose, Android instrumentation tests

---

## File Map

- Modify: `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`
  Responsibility: recompose reward content into the unified ending-page structure
- Modify: `app/src/main/java/com/mathisland/app/feature/level/RewardViewModel.kt`
  Responsibility: align reward copy/state outputs with the new structure if needed
- Modify: `app/src/main/java/com/mathisland/app/feature/parent/*`
  Responsibility: align parent summary screen/state with the same structure
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/RewardOverlayTest.kt`
  Responsibility: verify reward structural hierarchy and CTA presence
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/parent/ParentSummaryTabletScreenTest.kt`
  Responsibility: verify parent summary structural hierarchy and recommendation emphasis
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`
  Responsibility: ensure end-to-end reward and parent summary flows still land on the expected updated sections

## Chunk 1: Reward Page Red Tests

### Task 1: Define the new reward hierarchy in tests

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/RewardOverlayTest.kt`

- [ ] Add failing assertions for hero, core summary, secondary details, and next-action grouping on the reward page
- [ ] Add or rename test tags to reflect structural layers instead of ad hoc cards
- [ ] Run the focused reward instrumentation test and verify it fails for the expected structural mismatch

## Chunk 2: Parent Summary Red Tests

### Task 2: Define the new parent summary hierarchy in tests

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/parent/ParentSummaryTabletScreenTest.kt`

- [ ] Add failing assertions for hero, core summary, secondary details, and recommended next-action grouping
- [ ] Ensure tests check the stronger recommendation area rather than many equal-weight cards
- [ ] Run the focused parent instrumentation test and verify it fails for the expected structural mismatch

## Chunk 3: Reward Implementation

### Task 3: Restructure the reward overlay

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/RewardViewModel.kt`

- [ ] Introduce the shared ending-page section order on the reward page
- [ ] Keep child-facing tone: celebration + next adventure
- [ ] Make the next action the strongest lower-page section
- [ ] Keep reward details readable but secondary
- [ ] Reuse existing components where they fit instead of cloning new ones

## Chunk 4: Parent Summary Implementation

### Task 4: Restructure the parent summary screen

**Files:**
- Modify relevant files under: `app/src/main/java/com/mathisland/app/feature/parent/`

- [ ] Introduce the same shared ending-page section order on the parent page
- [ ] Keep parent-facing tone: learning report + recommended action
- [ ] Make the recommended next step the strongest lower-page section
- [ ] Keep reporting density higher than child but still scannable

## Chunk 5: Flow And Test Alignment

### Task 5: Update end-to-end flow tests

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

- [ ] Update reward flow assertions to the new structural tags
- [ ] Update parent summary flow assertions to the new structural tags
- [ ] Keep flow coverage focused on user-visible structure, not exact copy wording

## Chunk 6: Verification

### Task 6: Concentrated verification

**Files:**
- Verify files above

- [ ] Run `./gradlew.bat :app:compileDebugAndroidTestKotlin`
- [ ] Run `./gradlew.bat assembleDebug`
- [ ] Run focused emulator regression for:
  `com.mathisland.app.feature.level.RewardOverlayTest`
  `com.mathisland.app.feature.parent.ParentSummaryTabletScreenTest`
  `com.mathisland.app.MathIslandTabletFlowTest`
- [ ] Confirm emulator-related processes are stopped afterward

### Task 7: Batch completion

**Files:**
- Verify git state for current branch

- [ ] Review the diff for reward/parent-summary-only scope
- [ ] Commit when ready
- [ ] Push when requested
