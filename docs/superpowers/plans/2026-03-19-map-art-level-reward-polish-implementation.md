# Map Art, Level, and Reward Polish Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Upgrade map placeholder resources toward production-quality illustrated placeholders while bringing the level and reward screens up to the same visual finish level as the map.

**Architecture:** Keep the work behavior-preserving and split it into three isolated tracks: map resource polish in `res/drawable` plus minimal painter support, level-screen surface polish in `feature/level`, and reward-screen finish polish in `feature/level`. Shared contracts such as slot keys, test tags, and controller behavior stay unchanged.

**Tech Stack:** Jetpack Compose, Android vector drawables, Compose UI tests, Gradle

---

## File Structure

- `app/src/main/res/drawable/*`
  Canonical map slot placeholder resources. This batch updates existing vector drawables to look more like illustrated placeholders.
- `app/src/main/java/com/mathisland/app/ui/components/map/*`
  Only minimal support changes if the updated vector assets need painter/layout adjustments.
- `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`
  Main lesson screen information column polish.
- `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`
  Reward screen hierarchy and finish polish.
- `app/src/androidTest/java/com/mathisland/app/ui/components/IslandMapCanvasTest.kt`
- `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
- `app/src/androidTest/java/com/mathisland/app/feature/level/LevelTabletScreenTest.kt`
- `app/src/androidTest/java/com/mathisland/app/feature/level/RewardOverlayTest.kt`
- `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`
  Regression coverage touched by this batch.

## Chunk 1: Map Placeholder Art Polish

### Task 1: Upgrade canonical map drawable placeholders

**Files:**
- Modify: `app/src/main/res/drawable/map_sea_backdrop.xml`
- Modify: `app/src/main/res/drawable/route_segment_default.xml`
- Modify: `app/src/main/res/drawable/route_segment_highlight.xml`
- Modify: `app/src/main/res/drawable/island_*_base.xml`
- Modify: `app/src/main/res/drawable/island_*_icon.xml`
- Modify if needed: `app/src/main/java/com/mathisland/app/ui/components/map/SeaBackdropPainter.kt`
- Modify if needed: `app/src/main/java/com/mathisland/app/ui/components/map/IslandNodePainter.kt`
- Modify if needed: `app/src/main/java/com/mathisland/app/ui/components/map/RoutePainter.kt`

- [ ] **Step 1: Update sea backdrop vector**

Add stronger paper/sea layering while preserving node readability.

- [ ] **Step 2: Update island base vectors**

Give each island a more distinct silhouette, themed terrain blocks, and one or two light decorative motifs.

- [ ] **Step 3: Update icon vectors**

Make each icon more semantically tied to its island theme.

- [ ] **Step 4: Adjust painters only if required**

Keep slot keys and lookup paths unchanged.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/res/drawable app/src/main/java/com/mathisland/app/ui/components/map
git commit -m "feat: polish map placeholder art assets"
```

## Chunk 2: Level Surface Polish

### Task 2: Refine the lesson information surface

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`
- Modify if needed: `app/src/main/java/com/mathisland/app/ui/components/TabletInfoCard.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelTabletScreenTest.kt`

- [ ] **Step 1: Tighten top information hierarchy**

Make timer, title, progress, and flow hint read as a clean information stack.

- [ ] **Step 2: Add light illustrative accents**

Use restrained accent bars, chips, or grouped surfaces without overwhelming the screen.

- [ ] **Step 3: Preserve answer-pane contract**

Do not change answer renderer entry points or behavior.

- [ ] **Step 4: Update focused screen tests if visual structure moved**

Keep stable tags and flow assertions intact.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt app/src/main/java/com/mathisland/app/ui/components/TabletInfoCard.kt app/src/androidTest/java/com/mathisland/app/feature/level/LevelTabletScreenTest.kt
git commit -m "feat: polish lesson screen surfaces"
```

## Chunk 3: Reward Finish Polish

### Task 3: Refine reward overlay hierarchy

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`
- Modify if needed: `app/src/main/java/com/mathisland/app/ui/components/TabletStatTile.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/level/RewardOverlayTest.kt`

- [ ] **Step 1: Rebuild reward layout as a clearer report card**

Separate header, stats, reward/info cards, and action area.

- [ ] **Step 2: Add restrained decorative cues**

Use subtle badges, result emphasis, and card grouping without making the page visually heavy.

- [ ] **Step 3: Preserve reward logic and CTA contracts**

Do not change button tags, challenge follow-up logic, or continue actions.

- [ ] **Step 4: Update focused reward tests if structure changed**

Keep existing entry and action tags stable.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt app/src/main/java/com/mathisland/app/ui/components/TabletStatTile.kt app/src/androidTest/java/com/mathisland/app/feature/level/RewardOverlayTest.kt
git commit -m "feat: polish reward overlay finish"
```

## Chunk 4: Final Verification

### Task 4: Full milestone verification and cleanup

**Files:**
- Verify only: all files touched in previous chunks

- [ ] **Step 1: Run unit tests**

Run: `./gradlew.bat testDebugUnitTest`

Expected: PASS.

- [ ] **Step 2: Start emulator and wait for device**

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

- [ ] **Step 5: Commit integrated milestone and stop emulator-related processes**

```bash
git add .
git commit -m "feat: polish map art and learning flow visuals"
```

Then stop `emulator`, `qemu`, and `adb` before ending the phase.
