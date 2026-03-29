# Return Feedback Motion Polish Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Strengthen reward-to-map feedback staging so reward overlay, map summary, and island handoff reveal their result surfaces in one shared motion system.

**Architecture:** Extend the existing `MapFeedbackMotionSpec` instead of inventing a second motion model. Apply staged reveal thresholds to reward overlay sections and the map/island feedback cards while preserving current route contracts and tags.

**Tech Stack:** Kotlin, Jetpack Compose, Compose animation, Android instrumented tests, JUnit

---

## Chunk 1: Shared Motion Model

### Task 1: Extend motion spec and unit coverage

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapFeedbackMotion.kt`
- Test: `app/src/test/java/com/mathisland/app/feature/map/MapFeedbackMotionTest.kt`

- [ ] Add explicit reveal thresholds for spotlight/supporting/cta stages.
- [ ] Update unit tests to lock reveal ordering and kind-specific emphasis.
- [ ] Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.map.MapFeedbackMotionTest"`

## Chunk 2: Reward Overlay Staging

### Task 2: Stage reward overlay sections

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/level/RewardOverlayTest.kt`

- [ ] Apply staged reveal to header, stats, spotlight, highlights, next-step cards, and CTA row.
- [ ] Preserve existing tags and contracts.
- [ ] Add one stable tag for the new reward stage chip if needed.
- [ ] Run: `./gradlew.bat :app:compileDebugAndroidTestKotlin`

## Chunk 3: Map + Island Handoff Staging

### Task 3: Stage map return summary and island handoff

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapProgressFeedback.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandHandoffCard.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/island/IslandOverlaySheetTest.kt`

- [ ] Add kind-stage chip and stronger staging without changing existing tags.
- [ ] Keep detail cards and trailing emphasis on the same shared timing language.
- [ ] Run focused emulator regression after compile succeeds.

## Chunk 4: Verification

### Task 4: Fresh verification and checkpoint

**Files:**
- Verify only

- [ ] Run: `./gradlew.bat testDebugUnitTest`
- [ ] Run: `./gradlew.bat assembleDebug`
- [ ] Run focused emulator regression for reward/map/island surfaces
- [ ] Commit the batch
