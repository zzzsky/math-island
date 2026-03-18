# Map Drawable Resource Chain Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Land canonical drawable-backed placeholder resources for the map art system while preserving current map interaction behavior, tags, and fallback rendering.

**Architecture:** First add the default vector drawable placeholders for all map art slots. Then teach `MapArtRegistry` to resolve canonical drawable resources before falling back to Compose painters. Finally, lock the behavior with registry, canvas, and screen regressions so map unlock and lesson-entry contracts stay stable.

**Tech Stack:** Android resources, Jetpack Compose, Compose UI tests, JUnit, Gradle

---

## File Structure

- `app/src/main/res/drawable/map_sea_backdrop.xml`
  Default vector placeholder for the sea backdrop slot.
- `app/src/main/res/drawable/route_segment_default.xml`
  Default vector placeholder for the idle route slot.
- `app/src/main/res/drawable/route_segment_highlight.xml`
  Default vector placeholder for the highlighted route slot.
- `app/src/main/res/drawable/island_locked_overlay.xml`
  Shared vector placeholder for locked state overlay.
- `app/src/main/res/drawable/island_unlocked_tint.xml`
  Shared vector placeholder for unlocked tint overlay.
- `app/src/main/res/drawable/island_focused_ring.xml`
  Shared vector placeholder for focused ring overlay.
- `app/src/main/res/drawable/island_completed_badge.xml`
  Shared vector placeholder for completed badge overlay.
- `app/src/main/res/drawable/island_<normalizedIslandId>_base.xml`
  Per-island vector placeholder for island base art.
- `app/src/main/res/drawable/island_<normalizedIslandId>_icon.xml`
  Per-island vector placeholder for island icon art.
- `app/src/main/java/com/mathisland/app/ui/components/map/MapArtRegistry.kt`
  Canonical drawable resolution and fallback orchestration.
- `app/src/test/java/com/mathisland/app/ui/components/map/MapArtRegistryTest.kt`
  Registry contract tests for drawable resolution and fallback behavior.
- `app/src/androidTest/java/com/mathisland/app/ui/components/IslandMapCanvasTest.kt`
  Painter path tests for resource-backed and fallback-backed contracts.
- `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
  Screen regressions for stable map semantics.
- `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`
  End-to-end regression for unlock feedback and panel-start contracts.

## Chunk 1: Drawable Slot Placeholders

### Task 1: Add canonical vector placeholders for shared map slots

**Files:**
- Create: `app/src/main/res/drawable/map_sea_backdrop.xml`
- Create: `app/src/main/res/drawable/route_segment_default.xml`
- Create: `app/src/main/res/drawable/route_segment_highlight.xml`
- Create: `app/src/main/res/drawable/island_locked_overlay.xml`
- Create: `app/src/main/res/drawable/island_unlocked_tint.xml`
- Create: `app/src/main/res/drawable/island_focused_ring.xml`
- Create: `app/src/main/res/drawable/island_completed_badge.xml`

- [ ] **Step 1: Add the vector drawable placeholders**

Create the shared slot resources as simple hand-drawn flavored vectors:

- `map_sea_backdrop.xml`: soft sea gradient and light contour lines
- `route_segment_default.xml`: paper-toned route strip
- `route_segment_highlight.xml`: warm highlighted route strip
- overlays: tint/ring/badge vectors with transparent backgrounds

- [ ] **Step 2: Sanity-check resource names**

Run: `rg "map_sea_backdrop|route_segment_default|route_segment_highlight|island_locked_overlay|island_unlocked_tint|island_focused_ring|island_completed_badge" app/src/main/res/drawable`

Expected: each canonical key appears exactly once as a drawable file name.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/res/drawable/map_sea_backdrop.xml app/src/main/res/drawable/route_segment_default.xml app/src/main/res/drawable/route_segment_highlight.xml app/src/main/res/drawable/island_locked_overlay.xml app/src/main/res/drawable/island_unlocked_tint.xml app/src/main/res/drawable/island_focused_ring.xml app/src/main/res/drawable/island_completed_badge.xml
git commit -m "feat: add shared map drawable placeholders"
```

### Task 2: Add canonical vector placeholders for all island base and icon slots

**Files:**
- Create: `app/src/main/res/drawable/island_calculation_island_base.xml`
- Create: `app/src/main/res/drawable/island_measurement_geometry_island_base.xml`
- Create: `app/src/main/res/drawable/island_multiplication_island_base.xml`
- Create: `app/src/main/res/drawable/island_division_island_base.xml`
- Create: `app/src/main/res/drawable/island_big_number_island_base.xml`
- Create: `app/src/main/res/drawable/island_classification_island_base.xml`
- Create: `app/src/main/res/drawable/island_challenge_island_base.xml`
- Create: `app/src/main/res/drawable/island_calculation_island_icon.xml`
- Create: `app/src/main/res/drawable/island_measurement_geometry_island_icon.xml`
- Create: `app/src/main/res/drawable/island_multiplication_island_icon.xml`
- Create: `app/src/main/res/drawable/island_division_island_icon.xml`
- Create: `app/src/main/res/drawable/island_big_number_island_icon.xml`
- Create: `app/src/main/res/drawable/island_classification_island_icon.xml`
- Create: `app/src/main/res/drawable/island_challenge_island_icon.xml`

- [ ] **Step 1: Add the per-island vector drawables**

Create one base and one icon vector per island, using distinct colors and simple theme symbols so every island is visually distinguishable.

- [ ] **Step 2: Verify the expected file count**

Run: `Get-ChildItem app\\src\\main\\res\\drawable\\island_*_base.xml, app\\src\\main\\res\\drawable\\island_*_icon.xml | Measure-Object`

Expected: `Count = 14`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/res/drawable/island_*_base.xml app/src/main/res/drawable/island_*_icon.xml
git commit -m "feat: add island drawable placeholder set"
```

## Chunk 2: Registry Drawable Resolution

### Task 3: Resolve canonical drawable slots before painter fallback

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/ui/components/map/MapArtRegistry.kt`
- Modify: `app/src/test/java/com/mathisland/app/ui/components/map/MapArtRegistryTest.kt`

- [ ] **Step 1: Write the failing tests**

Add registry tests that assert:

- canonical drawable slot names resolve when the resource exists
- fallback painter still exists when the resource is missing
- island slot lookup still derives names from curriculum `islandId`

- [ ] **Step 2: Run the targeted unit test**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.ui.components.map.MapArtRegistryTest"`

Expected: FAIL until drawable-backed resolution is implemented.

- [ ] **Step 3: Implement minimal registry changes**

Update `MapArtRegistry.kt` so it:

- derives canonical drawable names from existing slot keys
- resolves resource IDs in `drawable`
- returns resource-backed painters when found
- returns existing fallback painters when not found

Do not change `MapArtSource` external behavior.

- [ ] **Step 4: Re-run the targeted unit test**

Run the same command.

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/ui/components/map/MapArtRegistry.kt app/src/test/java/com/mathisland/app/ui/components/map/MapArtRegistryTest.kt
git commit -m "feat: resolve canonical map drawable resources"
```

## Chunk 3: Contract Regressions

### Task 4: Lock painter and screen contracts with resource-backed coverage

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/ui/components/IslandMapCanvasTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

- [ ] **Step 1: Add regression assertions**

Cover both resource-backed and fallback-backed paths:

- map nodes stay clickable
- route/node highlight tags remain visible
- `panel-start-*` contracts remain stable after unlock

- [ ] **Step 2: Run focused instrumentation**

Run:

```powershell
./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.ui.components.IslandMapCanvasTest"
./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.map.MapTabletScreenTest"
./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.MathIslandTabletFlowTest"
```

Expected: PASS.

- [ ] **Step 3: Commit**

```bash
git add app/src/androidTest/java/com/mathisland/app/ui/components/IslandMapCanvasTest.kt app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt
git commit -m "test: lock map drawable resource contracts"
```

## Chunk 4: Final Verification

### Task 5: Full batch verification and cleanup

**Files:**
- Verify only: files from previous tasks

- [ ] **Step 1: Run unit tests**

Run: `./gradlew.bat testDebugUnitTest`

Expected: PASS.

- [ ] **Step 2: Run instrumentation**

Run: `./gradlew.bat connectedDebugAndroidTest`

Expected: PASS.

- [ ] **Step 3: Build debug APK**

Run: `./gradlew.bat assembleDebug`

Expected: PASS.

- [ ] **Step 4: Commit and stop emulator-related processes**

```bash
git add .
git commit -m "feat: add map drawable resource chain"
```

Then stop emulator-related processes before ending the phase.
