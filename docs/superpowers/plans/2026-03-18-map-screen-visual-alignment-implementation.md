# Map Screen Visual Alignment Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Align the map screen’s top bar and left-side island list cards with the same icon/resource chain and visual language already used by the map canvas and island overlay, without changing any map behavior.

**Architecture:** First add focused map-screen presentation components for the island list cards and top bar, reusing the existing map art registry and island panel tokens wherever possible. Then integrate those components into `MapTabletScreen` so the screen becomes a scene coordinator instead of a style-heavy file. Finally, lock the stable contracts with screen and flow regressions.

**Tech Stack:** Jetpack Compose, Android drawable resources, Compose UI tests, Gradle

---

## File Structure

- `app/src/main/java/com/mathisland/app/feature/map/MapIslandListCard.kt`
  Owns the left-side island list card presentation and `select-island-<id>` contract.
- `app/src/main/java/com/mathisland/app/feature/map/MapTopBar.kt`
  Owns the back button, chest button, and total-stars pill presentation.
- `app/src/main/java/com/mathisland/app/feature/map/MapScreenTokens.kt`
  Owns map-screen-specific token details that should not live in `IslandPanelTokens`.
- `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
  Keeps state orchestration and event handling only.
- `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
  Locks map list card, top bar, and panel integration contracts.
- `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`
  End-to-end regression for map selection, overlay, and lesson-entry flow.

## Chunk 1: Island List Card Alignment

### Task 1: Add a focused island list card component

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/map/MapIslandListCard.kt`
- Optionally Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandPanelTokens.kt`

- [ ] **Step 1: Implement the list card component**

`MapIslandListCard` must render:

- island icon via `MapArtRegistry.resolveIslandArt(island.id)`
- title and subtitle
- state chip (`当前焦点 / 已完成 / 已解锁 / 等待前岛完成`)
- progress bar

It must keep the existing click contract on `select-island-<id>`.

- [ ] **Step 2: Keep behavior boundaries intact**

Do not add lesson CTA buttons into this card. Clicking still only selects the island.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/map/MapIslandListCard.kt
git commit -m "feat: align map island list cards with panel visuals"
```

## Chunk 2: Top Bar Alignment

### Task 2: Add a focused map top bar component

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/map/MapTopBar.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/map/MapScreenTokens.kt`

- [ ] **Step 1: Implement the top bar component**

`MapTopBar` must render:

- back-home button
- open-chest button
- total-stars pill
- optional chest pulse wrapper

It must preserve these tags:

- `map-open-chest`
- `map-open-chest-pulse`
- `map-total-stars`
- `map-total-stars-pill`

- [ ] **Step 2: Keep event wiring stable**

The component may receive callbacks and emphasis booleans, but must not own feedback state transitions.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/map/MapTopBar.kt app/src/main/java/com/mathisland/app/feature/map/MapScreenTokens.kt
git commit -m "feat: align map top bar with island visuals"
```

## Chunk 3: Screen Integration

### Task 3: Integrate list card and top bar into MapTabletScreen

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`

- [ ] **Step 1: Replace inlined top bar UI**

Move the current top-row button/pill rendering into `MapTopBar`.

- [ ] **Step 2: Replace inlined list card UI**

Move the current `LazyColumn` item card body into `MapIslandListCard`.

- [ ] **Step 3: Preserve screen behavior**

Keep:

- feedback dismissal on selection
- chest pulse behavior
- selected-island switching
- right-panel integration

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt
git commit -m "refactor: compose map screen from aligned visual components"
```

## Chunk 4: Regressions

### Task 4: Lock map-screen contracts after alignment

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

- [ ] **Step 1: Add screen-level assertions**

Cover:

- top bar tags still exist
- island list card tags still exist
- selecting another island still updates the right panel

- [ ] **Step 2: Keep flow regression stable**

Cover:

- map -> overlay -> `panel-start-*` still works
- unlock feedback flow still works with aligned list cards and top bar

- [ ] **Step 3: Commit**

```bash
git add app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt
git commit -m "test: preserve map screen contracts after visual alignment"
```

## Chunk 5: Final Verification

### Task 5: Full verification and cleanup checkpoint

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
git commit -m "feat: align map screen visuals end to end"
```

Then stop emulator-related processes before ending the phase.
