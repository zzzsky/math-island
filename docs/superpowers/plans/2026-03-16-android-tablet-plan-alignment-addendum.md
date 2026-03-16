# Android Tablet Plan Alignment Addendum

## Purpose

This addendum covers the remaining file-boundary and naming alignment work after the tablet MVP became functionally complete.

The goal is to make the codebase better match the promised implementation plan structure before more product work is added.

## Parallel Tasks

### Task A: Tablet UI Foundation

Files:

- `app/src/main/java/com/mathisland/app/ui/theme/*`
- `app/src/main/java/com/mathisland/app/ui/components/*`
- `app/src/main/java/com/mathisland/app/MathIslandApp.kt`
- `app/src/main/java/com/mathisland/app/feature/common/TabletUi.kt`

Outcome:

- shared theme tokens and reusable primitives move into `ui/theme` and `ui/components`
- `MathIslandApp.kt` no longer owns visual token definitions

### Task B: Map Component Alignment

Files:

- `app/src/main/java/com/mathisland/app/feature/map/*`
- `app/src/main/java/com/mathisland/app/feature/island/*`
- `app/src/main/java/com/mathisland/app/ui/components/*`

Outcome:

- map and overlay use shared `IslandMapCanvas`, `StoryPanelCard`, and `WoodButton` boundaries
- current interaction and tag contracts remain unchanged

### Task C: Level Surface Alignment

Files:

- `app/src/main/java/com/mathisland/app/feature/lesson/*`
- `app/src/main/java/com/mathisland/app/feature/reward/*`
- `app/src/main/java/com/mathisland/app/feature/level/*`

Outcome:

- lesson and reward surfaces align with the plan's `feature/level` boundary
- reward presentation gains a dedicated overlay boundary
- flow behavior remains unchanged

## Verification

Per branch:

- run focused tests for the touched area

After merge:

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat connectedDebugAndroidTest`
- `./gradlew.bat assembleDebug`
