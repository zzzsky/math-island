# Island Panel Visual Alignment Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Align the island detail overlay and lesson cards with the map’s existing icon/resource chain and hand-drawn visual language without changing map selection or lesson-entry behavior.

**Architecture:** First add panel-specific tokens and a header component that reuses the canonical island icon resource chain. Then split lesson/story presentation into focused island components and integrate them into `IslandOverlaySheet` while preserving `panel-start-*` contracts. Finally, verify that map selection still updates the panel and that the main flow remains unchanged.

**Tech Stack:** Jetpack Compose, Android drawable resources, Compose UI tests, Gradle

---

## File Structure

- `app/src/main/java/com/mathisland/app/feature/island/IslandPanelTokens.kt`
  Owns colors, strokes, spacing, status colors, and card treatment for the island overlay.
- `app/src/main/java/com/mathisland/app/feature/island/IslandPanelHeader.kt`
  Owns island icon, title, subtitle, and progress summary presentation.
- `app/src/main/java/com/mathisland/app/feature/island/IslandStoryCard.kt`
  Owns the descriptive story/info card container for the selected island.
- `app/src/main/java/com/mathisland/app/feature/island/IslandLessonCard.kt`
  Owns lesson summary, status styling, and the stable `panel-start-<lessonId>` CTA.
- `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`
  Keeps layout orchestration and event wiring only.
- `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
  Screen regressions for panel content switching and contract stability.
- `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`
  End-to-end regression for map -> overlay -> lesson entry.

## Chunk 1: Panel Tokens And Header

### Task 1: Add panel token definitions

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/island/IslandPanelTokens.kt`

- [ ] **Step 1: Write the minimal token file**

Add a focused token object that defines:

- header background / border colors
- story card paper colors
- lesson card idle / enabled / completed / new state colors
- CTA emphasis colors
- radii and elevation-like alpha values

Keep tokens local to island overlay styling. Do not move them into the app-wide theme.

- [ ] **Step 2: Confirm token usage entry points exist**

Run: `rg "IslandPanelTokens|IslandOverlaySheet" app/src/main/java/com/mathisland/app/feature/island`

Expected: `IslandPanelTokens` exists and `IslandOverlaySheet` is still the integration entry.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/island/IslandPanelTokens.kt
git commit -m "feat: add island panel visual tokens"
```

### Task 2: Add island header component reusing canonical icon resources

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/island/IslandPanelHeader.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`

- [ ] **Step 1: Add the header component**

Implement `IslandPanelHeader` so it renders:

- island icon from `MapArtRegistry.resolveIslandArt(islandId)`
- title
- subtitle
- progress summary / progress bar

The component must not own lesson actions or map behavior.

- [ ] **Step 2: Wire it into the overlay**

Replace the inlined title/header styling inside `IslandOverlaySheet` with `IslandPanelHeader`.

- [ ] **Step 3: Preserve contracts**

Make sure no lesson CTA tags or map-selection callbacks move or change as a side effect.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/island/IslandPanelHeader.kt app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt
git commit -m "feat: align island overlay header with map art"
```

## Chunk 2: Story Card And Lesson Cards

### Task 3: Add story card container

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/island/IslandStoryCard.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`

- [ ] **Step 1: Add the story card component**

Create a focused card container that renders the selected island description and any current exploration guidance already available in overlay state.

- [ ] **Step 2: Replace inlined story styling**

Move the existing description presentation in `IslandOverlaySheet` to `IslandStoryCard`.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/island/IslandStoryCard.kt app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt
git commit -m "refactor: split island story card from overlay"
```

### Task 4: Add lesson card component with stable CTA tags

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/island/IslandLessonCard.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`

- [ ] **Step 1: Move lesson card rendering into its own component**

`IslandLessonCard` must render:

- lesson title
- lesson summary
- status label (`未开始 / 可进入 / 已完成 / 新解锁`)
- CTA using the unchanged `panel-start-<lessonId>` tag

- [ ] **Step 2: Keep behavior stable**

The card may style the CTA differently, but clicking it must still call the same `onStartLesson(lessonId)` path.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/island/IslandLessonCard.kt app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt
git commit -m "feat: align island lesson cards with panel visuals"
```

## Chunk 3: Regressions

### Task 5: Lock overlay and map-screen contracts

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

- [ ] **Step 1: Add focused screen assertions**

Cover:

- panel title still updates when the selected island changes
- `panel-start-*` CTA remains visible
- icon-backed header remains visible on the active island

- [ ] **Step 2: Add main-flow assertion**

Cover:

- map -> overlay -> `panel-start-*` -> lesson still works
- unlock feedback flow remains intact after overlay refactor

- [ ] **Step 3: Commit**

```bash
git add app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt
git commit -m "test: preserve island overlay contracts after visual alignment"
```

## Chunk 4: Final Verification

### Task 6: Full verification and cleanup checkpoint

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
git commit -m "feat: align island panel visuals with map art"
```

Then stop emulator-related processes before ending the phase.
