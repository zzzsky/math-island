# Return Feedback Stage Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Upgrade reward, map, and island handoff surfaces to share one stronger return-feedback stage.

**Architecture:** Extend `MapReturnCopy` with detail-stage fields, flow them through `MapFeedbackUiState`, `RewardOverlayUiState`, and `IslandUiState`, then render the same summary/detail pattern in reward, map, and island surfaces. Keep logic behavior-preserving and verify through focused unit and emulator regressions.

**Tech Stack:** Kotlin, Jetpack Compose, Compose UI tests, Android instrumentation via emulator

---

## Chunk 1: Shared Copy + ViewModel Mapping

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapReturnCopy.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapFeedbackMapper.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapProgressFeedback.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/RewardViewModel.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandViewModel.kt`
- Test: `app/src/test/java/com/mathisland/app/feature/map/MapFeedbackMapperTest.kt`
- Test: `app/src/test/java/com/mathisland/app/feature/level/RewardViewModelTest.kt`
- Test: `app/src/test/java/com/mathisland/app/feature/island/IslandViewModelHandoffTest.kt`

- [ ] Add detail-stage fields to `MapReturnCopy`
- [ ] Thread detail-stage fields through feedback/viewmodel state
- [ ] Add or update focused unit tests
- [ ] Run focused unit tests

## Chunk 2: Reward / Map / Island UI

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandHandoffCard.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapProgressFeedback.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/level/RewardOverlayTest.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/island/IslandOverlaySheetTest.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`

- [ ] Render detail-stage cards in reward/map/island surfaces
- [ ] Keep existing stable tags and add detail-card tags only where needed
- [ ] Run focused emulator regressions for reward/map/island tests

## Chunk 3: Milestone Verification

**Files:**
- Modify: `docs/superpowers/specs/2026-03-28-return-feedback-stage-design.md`
- Modify: `docs/superpowers/plans/2026-03-28-return-feedback-stage-implementation.md`

- [ ] Run `./gradlew.bat testDebugUnitTest`
- [ ] Run `./gradlew.bat assembleDebug`
- [ ] Commit the batch
- [ ] Stop `adb / emulator / qemu`
