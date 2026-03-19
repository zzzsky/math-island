# Surface/Card System Polish Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Introduce a shared three-level surface/card system and apply it across the map, overlay, level, and reward screens without changing any product behavior.

**Architecture:** First land shared surface tokens and a minimal `SurfaceCard` wrapper in `ui/`. Then migrate the map and island overlay path to the shared semantics. Finally, align the level and reward surfaces so the main tablet flow reads as one design system instead of several batches of styling.

**Tech Stack:** Jetpack Compose, Android resources, Compose UI tests, Gradle

---

## File Structure

- `app/src/main/java/com/mathisland/app/ui/theme/SurfaceTokens.kt`
  Owns page/primary/secondary surface colors, border colors, radii, and default content padding values.
- `app/src/main/java/com/mathisland/app/ui/components/SurfaceCard.kt`
  Shared card wrapper that applies the semantic surface level.
- `app/src/main/java/com/mathisland/app/ui/components/StoryPanelCard.kt`
  Refactored to use the shared surface system instead of hardcoded shape/color defaults.
- `app/src/main/java/com/mathisland/app/ui/components/TabletInfoCard.kt`
- `app/src/main/java/com/mathisland/app/ui/components/TabletActionCard.kt`
- `app/src/main/java/com/mathisland/app/ui/components/TabletStatTile.kt`
  Shared cards updated to target primary/secondary semantics.
- `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
- `app/src/main/java/com/mathisland/app/feature/map/MapIslandListCard.kt`
- `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`
- `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`
- `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`
  Main feature screens aligned to the new surface hierarchy.

## Chunk 1: Shared Surface Foundation

### Task 1: Add surface tokens and wrapper

**Files:**
- Create: `app/src/main/java/com/mathisland/app/ui/theme/SurfaceTokens.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/components/SurfaceCard.kt`
- Modify: `app/src/main/java/com/mathisland/app/ui/components/StoryPanelCard.kt`

- [ ] **Step 1: Add the shared tokens**

Define:

- `PageSurface`
- `PrimarySurface`
- `SecondarySurface`
- matching border colors
- radii
- default content padding values

- [ ] **Step 2: Add `SurfaceCard`**

Create a small wrapper that applies:

- semantic surface level
- border
- radius
- optional override color/border

- [ ] **Step 3: Refactor `StoryPanelCard`**

Make it use `SurfaceCard` so existing consumers inherit the shared semantics.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/mathisland/app/ui/theme/SurfaceTokens.kt app/src/main/java/com/mathisland/app/ui/components/SurfaceCard.kt app/src/main/java/com/mathisland/app/ui/components/StoryPanelCard.kt
git commit -m "feat: add shared surface card foundation"
```

## Chunk 2: Map And Overlay Alignment

### Task 2: Apply surface semantics to map and island overlay

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapIslandListCard.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapTopBar.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandPanelHeader.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandStoryCard.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandLessonCard.kt`

- [ ] **Step 1: Migrate top-level containers**

Map page containers and the island overlay root should declare page/primary surfaces instead of hardcoded colors.

- [ ] **Step 2: Migrate list cards and overlay cards**

Map list cards, story cards, and lesson cards should declare secondary surfaces or primary surfaces as appropriate.

- [ ] **Step 3: Preserve stable tags**

Do not change:

- `select-island-*`
- `map-open-chest`
- `map-total-stars`
- `panel-start-*`

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt app/src/main/java/com/mathisland/app/feature/map/MapIslandListCard.kt app/src/main/java/com/mathisland/app/feature/map/MapTopBar.kt app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt app/src/main/java/com/mathisland/app/feature/island/IslandPanelHeader.kt app/src/main/java/com/mathisland/app/feature/island/IslandStoryCard.kt app/src/main/java/com/mathisland/app/feature/island/IslandLessonCard.kt
git commit -m "refactor: align map and island surfaces"
```

## Chunk 3: Level And Reward Alignment

### Task 3: Apply surface semantics to level and reward screens

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/ui/components/TabletInfoCard.kt`
- Modify: `app/src/main/java/com/mathisland/app/ui/components/TabletActionCard.kt`
- Modify: `app/src/main/java/com/mathisland/app/ui/components/TabletStatTile.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`

- [ ] **Step 1: Move shared info/stat/action cards to semantic surfaces**

Align these shared cards to primary/secondary semantics.

- [ ] **Step 2: Migrate level and reward containers**

The main lesson and reward containers should use page/primary/secondary surfaces consistently.

- [ ] **Step 3: Preserve feature behavior**

No changes to:

- timer behavior
- reward actions
- challenge-specific logic

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/mathisland/app/ui/components/TabletInfoCard.kt app/src/main/java/com/mathisland/app/ui/components/TabletActionCard.kt app/src/main/java/com/mathisland/app/ui/components/TabletStatTile.kt app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt
git commit -m "refactor: align level and reward surfaces"
```

## Chunk 4: Final Verification

### Task 4: Full verification and cleanup checkpoint

**Files:**
- Verify only: files from previous tasks

- [ ] **Step 1: Run unit tests**

Run: `./gradlew.bat testDebugUnitTest`

Expected: PASS.

- [ ] **Step 2: Start emulator if needed**

Run:

```powershell
& 'C:\Users\Admin\AppData\Local\Android\Sdk\emulator\emulator.exe' -avd MathIslandTablet_API33 -feature -Vulkan -gpu swiftshader_indirect -no-snapshot-save -no-boot-anim
adb wait-for-device
adb devices
```

Expected: `emulator-5554    device`

- [ ] **Step 3: Run instrumentation**

Run: `./gradlew.bat connectedDebugAndroidTest`

Expected: PASS.

- [ ] **Step 4: Build debug APK**

Run: `./gradlew.bat assembleDebug`

Expected: PASS.

- [ ] **Step 5: Commit and stop emulator processes**

```bash
git add .
git commit -m "feat: polish shared surface card system"
```

Then stop emulator-related processes before ending the phase.
