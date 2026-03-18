# Island Art Tokenization Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a hand-drawn map rendering foundation with explicit art slots for sea, islands, routes, overlays, decorations, and icons while preserving current map interaction behavior, semantics, and test tags.

**Architecture:** First land a pure art-contract layer under `ui/components/map` with deterministic unit tests for key normalization, lookup order, and fallback behavior. Then move `IslandMapCanvas` drawing into painter-specific files owned by a single worker, and only after that apply the visual-language upgrade plus screen-level regression coverage.

**Tech Stack:** Jetpack Compose, Compose Canvas, Android drawable resources, JUnit, Compose UI tests, Gradle

---

## File Structure

- `app/src/main/java/com/mathisland/app/ui/components/map/MapIllustrationTokens.kt`
  Owns map art keys, size constants, anchors, overlay order, and fallback tokens.
- `app/src/main/java/com/mathisland/app/ui/components/map/MapArtRegistry.kt`
  Owns canonical slot metadata, preferred source-type order, decoration/icon slot names, and fallback painter specs keyed by curriculum `islandId`.
- `app/src/main/java/com/mathisland/app/ui/components/map/MapArtSource.kt`
  Owns the seam that lets tests provide fake art sources without changing production behavior.
- `app/src/main/java/com/mathisland/app/ui/components/map/SeaBackdropPainter.kt`
  Owns sea fallback drawing and optional decoration placement.
- `app/src/main/java/com/mathisland/app/ui/components/map/IslandNodePainter.kt`
  Owns island base art, locked/unlocked/focused/completed visual layers, and icon slot rendering.
- `app/src/main/java/com/mathisland/app/ui/components/map/RoutePainter.kt`
  Owns route base/highlight drawing using art registry contracts.
- `app/src/main/java/com/mathisland/app/ui/components/IslandMapCanvas.kt`
  Keeps semantics, tags, hit targets, and click logic stable while delegating all visuals to painters.
- `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
  May receive minimal glue updates only if painter configuration needs to be passed through.
- `app/src/test/java/com/mathisland/app/ui/components/map/MapIllustrationTokensTest.kt`
  Pure unit tests for key normalization, overlay ordering, and slot constants.
- `app/src/test/java/com/mathisland/app/ui/components/map/MapArtRegistryTest.kt`
  Pure unit tests for lookup precedence, fallback behavior, decoration/icon slot naming, and stable use of curriculum `islandId`.
- `app/src/androidTest/java/com/mathisland/app/ui/components/IslandMapCanvasTest.kt`
  Compose tests for semantics count, route/highlight tags, and click behavior after painter extraction.
- `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
  Compose screen regressions for stable map contracts.
- `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`
  End-to-end regression for unlock feedback + `panel-start-*` contract.

## Ownership And Merge Order

- Worker A: art contracts only
  - `MapIllustrationTokens.kt`
  - `MapArtRegistry.kt`
  - `MapIllustrationTokensTest.kt`
  - `MapArtRegistryTest.kt`
- Worker B: painter extraction only
  - `SeaBackdropPainter.kt`
  - `IslandNodePainter.kt`
  - `RoutePainter.kt`
  - `IslandMapCanvas.kt`
  - `IslandMapCanvasTest.kt`
- Worker C: visual language + screen regression only
  - `MapTabletScreen.kt`
  - `MapTabletScreenTest.kt`
  - `MathIslandTabletFlowTest.kt`

Merge order:

1. Worker A
2. Worker B
3. Worker C

This order is mandatory because `Worker C` depends on stable token/registry contracts and painter boundaries.

## Execution Notes

- Android runtime resource lookup must not rely on same-name file-format precedence. `MapArtRegistry` should keep the canonical approved slot keys from the spec, for example:
  - `island_<normalized>_base`
  - `island_<normalized>_icon`
  - `route_segment_default`
  - `route_segment_highlight`
  - `map_sea_backdrop`
- Format priority belongs in slot metadata, not alternate key names. Registry/spec objects should express “prefer vector, then WebP, then PNG” as source-type order for a canonical slot key.
- Any Compose test that needs prefix matching must add a local helper in the test file, for example a `SemanticsMatcher` over `SemanticsProperties.TestTag`. Do not assume `onAllNodesWithTagPrefix(...)` exists already.

## Chunk 1: Art Contracts

### Task 1: Add token constants and normalization helpers

**Files:**
- Create: `app/src/main/java/com/mathisland/app/ui/components/map/MapIllustrationTokens.kt`
- Create: `app/src/test/java/com/mathisland/app/ui/components/map/MapIllustrationTokensTest.kt`

- [ ] **Step 1: Write the failing test**

Create `MapIllustrationTokensTest.kt` with:

```kotlin
@Test
fun normalizeIslandArtKey_usesCurriculumIslandIdWithoutAliases() {
    assertEquals(
        "measurement_geometry_island",
        normalizeIslandArtKey("measurement-geometry-island"),
    )
}

@Test
fun overlayOrder_matchesApprovedSpec() {
    assertEquals(
        listOf("base", "state", "focus", "badge", "label"),
        MapIllustrationTokens.OverlayOrder,
    )
}

@Test
fun illustrationContracts_matchApprovedSizesAndAnchors() {
    assertEquals(IntSize(1920, 1200), MapIllustrationTokens.SeaBackdropSpecSize)
    assertEquals(IntSize(640, 480), MapIllustrationTokens.IslandBaseSpecSize)
    assertEquals(IntSize(256, 256), MapIllustrationTokens.StateOverlaySpecSize)
    assertEquals(512, MapIllustrationTokens.RouteSegmentLongEdge)
    assertEquals(IntSize(96, 96), MapIllustrationTokens.IslandIconSpecSize)
    assertEquals(ContentScale.Fit, MapIllustrationTokens.ArtContentScale)
    assertEquals(Alignment.Center, MapIllustrationTokens.IslandArtAlignment)
    assertEquals(Alignment.TopEnd, MapIllustrationTokens.BadgeAlignment)
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.ui.components.map.MapIllustrationTokensTest"`

Expected: FAIL because the file and helpers do not exist yet.

- [ ] **Step 3: Write minimal implementation**

Create `MapIllustrationTokens.kt` with:

```kotlin
internal object MapIllustrationTokens {
    val OverlayOrder = listOf("base", "state", "focus", "badge", "label")
    const val SeaBackdropKey = "map_sea_backdrop"
    const val RouteDefaultKey = "route_segment_default"
    const val RouteHighlightKey = "route_segment_highlight"
    const val CloudPrefix = "map_cloud_"
    const val WavePrefix = "map_wave_"
    const val SparkPrefix = "map_spark_"
}

internal fun normalizeIslandArtKey(islandId: String): String =
    islandId.lowercase().replace('-', '_')
```

Also add icon/base/overlay slot builders using the normalized key.

Define the approved spec contracts explicitly in tokens:

- `SeaBackdropSpecSize = IntSize(1920, 1200)`
- `IslandBaseSpecSize = IntSize(640, 480)`
- `StateOverlaySpecSize = IntSize(256, 256)`
- `RouteSegmentLongEdge = 512`
- `IslandIconSpecSize = IntSize(96, 96)`
- `ArtContentScale = ContentScale.Fit`
- `IslandArtAlignment = Alignment.Center`
- `BadgeAlignment = Alignment.TopEnd`

- [ ] **Step 4: Run test to verify it passes**

Run the same unit-test command.

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/ui/components/map/MapIllustrationTokens.kt app/src/test/java/com/mathisland/app/ui/components/map/MapIllustrationTokensTest.kt
git commit -m "feat: add map illustration token contracts"
```

### Task 2: Add registry lookup precedence and optional-slot coverage

**Files:**
- Create: `app/src/main/java/com/mathisland/app/ui/components/map/MapArtRegistry.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/components/map/MapArtSource.kt`
- Create: `app/src/test/java/com/mathisland/app/ui/components/map/MapArtRegistryTest.kt`

- [ ] **Step 1: Write the failing test**

Create `MapArtRegistryTest.kt` with:

```kotlin
@Test
fun resolveIslandArt_usesCurriculumIslandIdAndFallbacks() {
    val spec = MapArtRegistry.resolveIslandArt("measurement-geometry-island")
    assertEquals("measurement_geometry_island", spec.normalizedKey)
    assertNotNull(spec.baseArt)
}

@Test
fun resolveDecorationSlots_exposesCloudWaveAndSparkKeys() {
    val slots = MapArtRegistry.decorationSlots()
    assertTrue(slots.any { it.startsWith("map_cloud_") })
    assertTrue(slots.any { it.startsWith("map_wave_") })
    assertTrue(slots.any { it.startsWith("map_spark_") })
}

@Test
fun resolveIslandArt_prefersVectorThenWebpThenPng() {
    assertEquals(
        listOf(SourceType.Vector, SourceType.WebP, SourceType.Png),
        MapArtRegistry.defaultSourcePriority,
    )
}

@Test
fun resolveSharedArt_exposesSeaBackdropAndRouteKeys() {
    assertEquals("map_sea_backdrop", MapArtRegistry.resolveSeaBackdrop().key)
    assertEquals("route_segment_default", MapArtRegistry.resolveRouteArt(highlighted = false).key)
    assertEquals("route_segment_highlight", MapArtRegistry.resolveRouteArt(highlighted = true).key)
}

@Test
fun resolveIslandArt_singleMissingAssetStillReturnsTestableFallbacks() {
    val missingBase = FakeMissingBaseAssetRegistry().resolveIslandArt("measurement-geometry-island")
    assertNotNull(missingBase.baseArt)

    val missingBadge = FakeMissingBadgeAssetRegistry().resolveIslandArt("measurement-geometry-island")
    assertNotNull(missingBadge.completedBadge)
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.ui.components.map.MapArtRegistryTest"`

Expected: FAIL because the registry does not exist yet.

- [ ] **Step 3: Write minimal implementation**

Create `MapArtRegistry.kt` with:

- pure lookup functions that accept raw curriculum `islandId`
- internal normalization via `normalizeIslandArtKey`
- canonical slot keys that remain equal to the approved spec names
- source-type priority metadata: `Vector -> WebP -> Png`
- slot builders for:
  - `map_sea_backdrop`
  - `route_segment_default`
  - `route_segment_highlight`
  - `island_<normalized>_base`
  - `island_<normalized>_icon`
  - `island_locked_overlay`
  - `island_unlocked_tint`
  - `island_focused_ring`
  - `island_completed_badge`
  - `map_cloud_*`, `map_wave_*`, `map_spark_*`
- fallback spec objects that never return `null`
- fallback behavior for single missing resources, not just all-missing cases

Also create `MapArtSource.kt`:

```kotlin
interface MapArtSource {
    fun resolveSeaBackdrop(): SeaArtSpec
    fun resolveRouteArt(highlighted: Boolean): RouteArtSpec
    fun resolveIslandArt(islandId: String): IslandArtSpec
    fun decorationSlots(): List<String>
}
```

Production code uses `MapArtRegistry : MapArtSource`. Tests may provide fake `MapArtSource` implementations.

- [ ] **Step 4: Run test to verify it passes**

Run the same unit-test command.

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/ui/components/map/MapArtRegistry.kt app/src/test/java/com/mathisland/app/ui/components/map/MapArtRegistryTest.kt
git commit -m "feat: add map art registry contracts"
```

## Chunk 2: Painter Foundation

### Task 3: Extract painter files behind stable canvas semantics

**Files:**
- Create: `app/src/main/java/com/mathisland/app/ui/components/map/SeaBackdropPainter.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/components/map/IslandNodePainter.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/components/map/RoutePainter.kt`
- Modify: `app/src/main/java/com/mathisland/app/ui/components/IslandMapCanvas.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/ui/components/IslandMapCanvasTest.kt`

- [ ] **Step 1: Write the failing tests**

Add deterministic regressions to `IslandMapCanvasTest.kt`:

First add a local helper:

```kotlin
private fun AndroidComposeTestRule<*, *>.onAllNodesWithTagPrefix(prefix: String) =
    onAllNodes(
        SemanticsMatcher("TestTag starts with $prefix") { node ->
            node.config.getOrNull(SemanticsProperties.TestTag)?.startsWith(prefix) == true
        }
    )
```

```kotlin
@Test
fun islandMapCanvas_rendersSameNodeTagsWithFallbackAndAssetBackedRegistries() {
    composeRule.setContent { TestCanvas(artRegistry = FakeFallbackMapArtRegistry()) }
    composeRule.onAllNodesWithTagPrefix("map-node-").assertCountEquals(7)

    composeRule.setContent { TestCanvas(artRegistry = FakeAssetBackedMapArtRegistry()) }
    composeRule.onAllNodesWithTagPrefix("map-node-").assertCountEquals(7)
}

@Test
fun islandMapCanvas_keepsRouteHighlightTagAfterPainterSplit() {
    composeRule.setContent {
        HighlightedCanvas(artRegistry = FakeFallbackMapArtRegistry())
    }
    composeRule.onNodeWithTag("map-route-highlight-measurement-geometry-island").assertExists()
}

@Test
fun islandMapCanvas_clickingIslandStillCallsSelectionHandler() {
    var selected: String? = null
    composeRule.setContent {
        TestCanvas(
            artRegistry = FakeFallbackMapArtRegistry(),
            onIslandSelected = { selected = it },
        )
    }
    composeRule.onNodeWithTag("map-node-measurement-geometry-island").performClick()
    assertEquals("measurement-geometry-island", selected)
}

@Test
fun islandMapCanvas_keepsNodeBoundsWhenHighlightToggles() {
    composeRule.setContent {
        TestCanvas(
            artRegistry = FakeFallbackMapArtRegistry(),
            highlightedIslandId = null,
        )
    }
    val idleBounds =
        composeRule.onNodeWithTag("map-node-measurement-geometry-island")
            .fetchSemanticsNode().boundsInRoot

    composeRule.setContent {
        TestCanvas(
            artRegistry = FakeFallbackMapArtRegistry(),
            highlightedIslandId = "measurement-geometry-island",
        )
    }
    val highlightedBounds =
        composeRule.onNodeWithTag("map-node-measurement-geometry-island")
            .fetchSemanticsNode().boundsInRoot

    assertEquals(idleBounds, highlightedBounds)
}
```

- [ ] **Step 2: Run test to verify it fails**

Start the emulator first if needed:

```powershell
& 'C:\Users\Admin\AppData\Local\Android\Sdk\emulator\emulator.exe' -avd MathIslandTablet_API33 -feature -Vulkan -gpu swiftshader_indirect -no-snapshot-save -no-boot-anim
adb devices
```

Expected: `emulator-5554    device`.

Run: `./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.ui.components.IslandMapCanvasTest"`

Expected: FAIL because the `artRegistry` injection path and fake registry test fixtures do not exist yet.

- [ ] **Step 3: Write minimal implementation**

Create the three painter files and update `IslandMapCanvas.kt` so:

- semantics containers and click handlers stay in `IslandMapCanvas.kt`
- only drawing logic moves into painters
- route/highlight tags remain attached to the same semantic structure
- registry-driven art is read through a `MapArtSource` parameter with default production implementation

- [ ] **Step 4: Run test to verify it passes**

Run the same instrumentation command.

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/ui/components/map/SeaBackdropPainter.kt app/src/main/java/com/mathisland/app/ui/components/map/IslandNodePainter.kt app/src/main/java/com/mathisland/app/ui/components/map/RoutePainter.kt app/src/main/java/com/mathisland/app/ui/components/IslandMapCanvas.kt app/src/androidTest/java/com/mathisland/app/ui/components/IslandMapCanvasTest.kt
git commit -m "refactor: split island map painters"
```

## Chunk 3: Hand-Drawn Fallback Visuals And Screen Regressions

### Task 4: Apply hand-drawn fallback visuals without changing behavior

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/ui/components/map/MapIllustrationTokens.kt`
- Modify: `app/src/main/java/com/mathisland/app/ui/components/map/SeaBackdropPainter.kt`
- Modify: `app/src/main/java/com/mathisland/app/ui/components/map/IslandNodePainter.kt`
- Modify: `app/src/main/java/com/mathisland/app/ui/components/map/RoutePainter.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/ui/components/IslandMapCanvasTest.kt`

- [ ] **Step 1: Write the failing tests**

Add non-regression assertions for geometry and hierarchy-sensitive contracts:

Reuse the `onAllNodesWithTagPrefix` helper added in Task 3.

```kotlin
@Test
fun islandMapCanvas_keepsNodeParityBetweenFallbackAndAssetBackedArt() {
    composeRule.setContent { TestCanvas(artRegistry = FakeFallbackMapArtRegistry()) }
    composeRule.onAllNodesWithTagPrefix("map-node-").assertCountEquals(7)

    composeRule.setContent { TestCanvas(artRegistry = FakeAssetBackedMapArtRegistry()) }
    composeRule.onAllNodesWithTagPrefix("map-node-").assertCountEquals(7)
}

@Test
fun islandMapCanvas_keepsHighlightContainerTagsWithFallbackArt() {
    composeRule.setContent {
        HighlightedCanvas(artRegistry = FakeFallbackMapArtRegistry())
    }
    composeRule.onNodeWithTag("map-node-highlight-measurement-geometry-island").assertExists()
    composeRule.onNodeWithTag("map-route-highlight-measurement-geometry-island").assertExists()
}

@Test
fun islandMapCanvas_keepsHighlightContainerTagsWithAssetBackedArt() {
    composeRule.setContent {
        HighlightedCanvas(artRegistry = FakeAssetBackedMapArtRegistry())
    }
    composeRule.onNodeWithTag("map-node-highlight-measurement-geometry-island").assertExists()
    composeRule.onNodeWithTag("map-route-highlight-measurement-geometry-island").assertExists()
}

@Test
fun islandMapCanvas_keepsNodeBoundsWithSingleMissingAssetFallback() {
    composeRule.setContent {
        TestCanvas(artRegistry = FakeMissingBaseAssetRegistry())
    }
    composeRule.onNodeWithTag("map-node-measurement-geometry-island").assertExists()
    composeRule.onNodeWithTag("map-node-measurement-geometry-island").performClick()
}
```

- [ ] **Step 2: Run test to verify it fails**

Ensure the emulator is running before this step. Reuse the device from Task 3 or start it with the same emulator command if it is not connected.

Run: `./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.ui.components.IslandMapCanvasTest"`

Expected: FAIL because the fake asset-backed registry path and parity assertions do not exist yet.

- [ ] **Step 3: Write minimal implementation**

Update painter fallback visuals to match the approved hand-drawn direction:

- paper-like sea gradient
- light wave/cloud decoration hooks from registry slots
- card-like island silhouettes with gentle shadow
- state tint / focus ring / completion badge layering from token order
- optional island icon slot rendering without affecting title text

- [ ] **Step 4: Run test to verify it passes**

Run the same instrumentation command.

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/ui/components/map/MapIllustrationTokens.kt app/src/main/java/com/mathisland/app/ui/components/map/SeaBackdropPainter.kt app/src/main/java/com/mathisland/app/ui/components/map/IslandNodePainter.kt app/src/main/java/com/mathisland/app/ui/components/map/RoutePainter.kt app/src/androidTest/java/com/mathisland/app/ui/components/IslandMapCanvasTest.kt
git commit -m "feat: add hand-drawn island map fallback visuals"
```

### Task 5: Lock screen-level contracts after art tokenization

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

- [ ] **Step 1: Write the failing tests**

Add regressions for unchanged active-path contracts:

```kotlin
@Test
fun mapScreen_keepsMapNodeAndOverlayContractsAfterArtTokenization() {
    composeRule.onNodeWithTag("map-node-calculation-island").assertExists()
    composeRule.onNodeWithTag("map-node-measurement-geometry-island").assertExists()
    composeRule.onAllNodesWithTag("panel-start-measure-ruler-01").assertCountEquals(1)
}

@Test
fun mapFlow_keepsUnlockFeedbackContractsAfterArtTokenization() {
    composeRule.onNodeWithTag("map-route-highlight-measurement-geometry-island").assertExists()
    composeRule.onNodeWithTag("map-node-highlight-measurement-geometry-island").assertExists()
}
```

Also assert:

- same `panel-start-<lessonId>` count as before
- same visible map-node semantics count on map load
- clicking the focused island still updates overlay content instead of entering lesson directly
- highlighting the selected island does not move the `map-node-*` bounds in root

Use concrete assertions in those tests rather than stubs:

- `onAllNodesWithTagPrefix("map-node-").assertCountEquals(7)`
- `onNodeWithTag("map-node-calculation-island").performClick()`
- `onNodeWithText("计算岛").assertExists()`
- `onAllNodesWithTag("panel-start-calc-carry-01").assertCountEquals(1)`

- [ ] **Step 2: Run test to verify it fails**

Ensure the emulator is running before this step. Reuse the device from Task 3 or start it with the same emulator command if it is not connected.

Run:

```powershell
./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.feature.map.MapTabletScreenTest"
./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.MathIslandTabletFlowTest"
```

Expected: FAIL until screen tests are updated to the new painter-backed rendering path.

- [ ] **Step 3: Write minimal implementation**

Make only minimal `MapTabletScreen.kt` adjustments needed to thread painter-backed dependencies or preserve stable semantics wrappers. Do not add new business state.

- [ ] **Step 4: Run test to verify it passes**

Run the same two commands.

Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt app/src/androidTest/java/com/mathisland/app/feature/map/MapTabletScreenTest.kt app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt
git commit -m "test: preserve map screen contracts after art tokenization"
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
adb devices
```

Expected: `emulator-5554    device`.

- [ ] **Step 3: Run instrumentation**

Run: `./gradlew.bat connectedDebugAndroidTest`

Expected: PASS with no map interaction or unlock feedback regression.

- [ ] **Step 4: Build debug APK**

Run: `./gradlew.bat assembleDebug`

Expected: PASS.

- [ ] **Step 5: Commit and stop emulator processes**

```bash
git add .
git commit -m "feat: add island art tokenization foundation"
```

Then stop emulator-related processes before ending the phase.
