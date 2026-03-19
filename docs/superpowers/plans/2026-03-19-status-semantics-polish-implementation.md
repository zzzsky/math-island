# Status Semantics Polish Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development when tasks are independent in this session.

**Goal:** Introduce a lightweight shared status semantics layer and migrate the main learning flow plus peripheral screens to it without changing behavior, labels, or stable tags.

**Architecture:** First add shared `StatusTokens` and `StatusChip` in `ui/`. Then migrate the main learning flow (`map / island / level / reward`). Finally migrate peripheral screens (`home / chest / parent`) and remove duplicate chip helpers.

---

## File Structure

- `app/src/main/java/com/mathisland/app/ui/theme/StatusTokens.kt`
- `app/src/main/java/com/mathisland/app/ui/components/StatusChip.kt`
- `app/src/main/java/com/mathisland/app/ui/components/TabletChipLabel.kt`
- `app/src/main/java/com/mathisland/app/feature/map/MapProgressFeedback.kt`
- `app/src/main/java/com/mathisland/app/feature/map/MapIslandListCard.kt`
- `app/src/main/java/com/mathisland/app/feature/island/*`
- `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`
- `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`
- `app/src/main/java/com/mathisland/app/feature/home/HomeTabletScreen.kt`
- `app/src/main/java/com/mathisland/app/feature/chest/ChestTabletScreen.kt`
- `app/src/main/java/com/mathisland/app/feature/parent/ParentGateScreen.kt`
- `app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryTabletScreen.kt`

## Chunk 1: Status Foundation

- [ ] Add `StatusVariant` and shared status colors in `StatusTokens.kt`
- [ ] Add `StatusChip.kt` as the common chip / pill entry point
- [ ] Change `TabletChipLabel` into a compatibility wrapper over `StatusChip`

## Chunk 2: Main Learning Flow

- [ ] Migrate `MapProgressFeedback` pills to shared status semantics
- [ ] Migrate `MapIslandListCard`, `IslandPanelHeader`, `IslandStoryCard`, `IslandLessonCard`
- [ ] Migrate `LevelTabletScreen` and `RewardOverlay`
- [ ] Preserve all existing tags and labels

## Chunk 3: Peripheral Screens

- [ ] Migrate `HomeTabletScreen`
- [ ] Migrate `ChestTabletScreen` and delete `ChestChipLabel`
- [ ] Migrate `ParentGateScreen` / `ParentSummaryTabletScreen` and delete `ParentChipLabel`

## Chunk 4: Final Verification

- [ ] Run `./gradlew.bat testDebugUnitTest`
- [ ] Start emulator and run `./gradlew.bat connectedDebugAndroidTest`
- [ ] Run `./gradlew.bat assembleDebug`
- [ ] Commit milestone and stop `emulator / qemu / adb`
