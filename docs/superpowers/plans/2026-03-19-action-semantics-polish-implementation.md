# Action Semantics Polish Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Introduce a lightweight shared action-button semantics system and apply it to the main map, island, lesson, and reward flows without changing behavior, labels, or stable tags.

**Architecture:** First add shared action tokens and a single action button entry point in `ui/`. Then migrate the map and island flows to semantic button roles. Finally migrate the lesson and reward flows so the main learning path uses one consistent action language. Keep `WoodButton` as a compatibility wrapper in this batch.

**Tech Stack:** Jetpack Compose, Material 3, Compose UI tests, Gradle

---

## File Structure

- `app/src/main/java/com/mathisland/app/ui/theme/ActionTokens.kt`
  Owns primary, secondary, recommended, completed, and outlined-secondary action colors and shared shape values.
- `app/src/main/java/com/mathisland/app/ui/components/ActionButton.kt`
  Shared semantic action button component.
- `app/src/main/java/com/mathisland/app/ui/components/WoodButton.kt`
  Compatibility wrapper delegating to the new semantic button layer.
- `app/src/main/java/com/mathisland/app/feature/map/MapTopBar.kt`
- `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`
- `app/src/main/java/com/mathisland/app/feature/island/IslandLessonCard.kt`
  Map and island entry points migrated to semantic actions.
- `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`
- `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`
  Level and reward entry points migrated to semantic actions.

## Chunk 1: Action Foundation

### Task 1: Add shared action semantics

**Files:**
- Create: `app/src/main/java/com/mathisland/app/ui/theme/ActionTokens.kt`
- Create: `app/src/main/java/com/mathisland/app/ui/components/ActionButton.kt`
- Modify: `app/src/main/java/com/mathisland/app/ui/components/WoodButton.kt`

- [ ] **Step 1: Add semantic action tokens**

Define `primary`, `secondary`, `recommended`, `completed`, and `outlinedSecondary`.

- [ ] **Step 2: Add `ActionButton`**

Create a small shared button entry that maps semantics to contained vs outlined styles.

- [ ] **Step 3: Wrap `WoodButton`**

Keep current call sites working while delegating to the new action semantics.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/mathisland/app/ui/theme/ActionTokens.kt app/src/main/java/com/mathisland/app/ui/components/ActionButton.kt app/src/main/java/com/mathisland/app/ui/components/WoodButton.kt
git commit -m "feat: add shared action button semantics"
```

## Chunk 2: Map and Island Action Alignment

### Task 2: Migrate map and island entry actions

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapTopBar.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandLessonCard.kt`

- [ ] **Step 1: Migrate top bar actions**

Map `返回首页` to `secondary` and `打开宝箱` to `recommended`.

- [ ] **Step 2: Migrate island primary CTA**

Map island primary action to `recommended` or `completed`.

- [ ] **Step 3: Migrate lesson-card CTA**

Map lesson CTA to `recommended`, `completed`, or `secondary` while preserving `panel-start-*`.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/map/MapTopBar.kt app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt app/src/main/java/com/mathisland/app/feature/island/IslandLessonCard.kt
git commit -m "refactor: align map and island actions"
```

## Chunk 3: Level and Reward Action Alignment

### Task 3: Migrate lesson and reward actions

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`

- [ ] **Step 1: Migrate lesson return action**

Map `返回地图` to `secondary`.

- [ ] **Step 2: Migrate reward actions**

Map main continue CTA to `primary` and `再试冲刺` to `outlined-secondary`.

- [ ] **Step 3: Preserve stable tags**

Do not change:
- `reward-retry-sprint`
- `reward-return-map`

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt
git commit -m "refactor: align lesson and reward actions"
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
git commit -m "feat: polish main path action semantics"
```

Then stop `emulator`, `qemu`, and `adb` before ending the phase.
