# Peripheral Actions And Map Motion Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Extend the shared action semantics to peripheral tablet pages while independently polishing map motion feedback into a richer but still interruptible interaction layer.

**Architecture:** Split the batch into two isolated tracks. First, migrate Home/Chest/Parent pages to the existing action semantics foundation. In parallel, deepen map motion within the existing feedback/event model without adding new business state. Merge the peripheral action track first, then the map motion track, and verify the combined result at the end.

**Tech Stack:** Jetpack Compose, Material 3, Compose UI tests, Gradle

---

## File Structure

- `app/src/main/java/com/mathisland/app/feature/home/HomeTabletScreen.kt`
- `app/src/main/java/com/mathisland/app/feature/chest/ChestTabletScreen.kt`
- `app/src/main/java/com/mathisland/app/feature/parent/ParentGateScreen.kt`
- `app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryTabletScreen.kt`
- `app/src/main/java/com/mathisland/app/ui/components/TabletActionCard.kt`
  Peripheral action-semantic alignment track.
- `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
- `app/src/main/java/com/mathisland/app/feature/map/MapProgressFeedback.kt`
- `app/src/main/java/com/mathisland/app/ui/components/IslandMapCanvas.kt`
- `app/src/main/java/com/mathisland/app/ui/components/map/*`
  Map motion polish track.

## Chunk 1: Peripheral Action Alignment

### Task 1: Extend action semantics to Home, Chest, and Parent pages

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/home/HomeTabletScreen.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/chest/ChestTabletScreen.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/parent/ParentGateScreen.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryTabletScreen.kt`
- Modify if needed: `app/src/main/java/com/mathisland/app/ui/components/TabletActionCard.kt`

- [ ] **Step 1: Migrate Home page buttons**

Map continue/map/chest/parent actions onto semantic roles without changing labels or tags.

- [ ] **Step 2: Migrate Chest page buttons**

Map back-home and back-to-map onto semantic roles.

- [ ] **Step 3: Migrate Parent page buttons**

Map answer options and summary return actions onto semantic roles.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/home/HomeTabletScreen.kt app/src/main/java/com/mathisland/app/feature/chest/ChestTabletScreen.kt app/src/main/java/com/mathisland/app/feature/parent/ParentGateScreen.kt app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryTabletScreen.kt app/src/main/java/com/mathisland/app/ui/components/TabletActionCard.kt
git commit -m "refactor: align peripheral page actions"
```

## Chunk 2: Map Motion Polish

### Task 2: Deepen map motion feedback without changing business flow

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapProgressFeedback.kt`
- Modify: `app/src/main/java/com/mathisland/app/ui/components/IslandMapCanvas.kt`
- Modify if needed: `app/src/main/java/com/mathisland/app/ui/components/map/IslandNodePainter.kt`
- Modify if needed: `app/src/main/java/com/mathisland/app/ui/components/map/RoutePainter.kt`
- Test if needed: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
- Test if needed: `app/src/androidTest/java/com/mathisland/app/feature/map/MapProgressFeedbackTest.kt`
- Test if needed: `app/src/androidTest/java/com/mathisland/app/ui/components/IslandMapCanvasTest.kt`

- [ ] **Step 1: Enhance node highlight motion**

Add a more readable ripple/pulse around newly highlighted islands while preserving click targets.

- [ ] **Step 2: Enhance route highlight motion**

Turn static route emphasis into a more directional sweep while keeping the same tags.

- [ ] **Step 3: Coordinate feedback timing**

Keep motion short, interruptible, and consistent with the existing feedback consumption flow.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt app/src/main/java/com/mathisland/app/feature/map/MapProgressFeedback.kt app/src/main/java/com/mathisland/app/ui/components/IslandMapCanvas.kt app/src/main/java/com/mathisland/app/ui/components/map
git commit -m "feat: polish map motion feedback"
```

## Chunk 3: Final Verification

### Task 3: Full milestone verification and cleanup

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
git commit -m "feat: polish peripheral actions and map motion"
```

Then stop `emulator`, `qemu`, and `adb` before ending the phase.
