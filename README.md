# Math Island Android Tablet MVP

Landscape-first Android tablet build of Math Island.

## Current Scope

- 7 playable islands driven from curriculum JSON assets
- Home, map, lesson, reward, chest, parent summary, seagull review
- Challenge island with mixed run, timed sprint, replay, grading, and retry loop
- Shared UI systems for typography, spacing/radius, surfaces, actions, status chips, and renderer surfaces
- Unit tests plus Compose device smoke coverage

## Requirements

- Android Studio / Android SDK with API 33+ system image
- A working AVD with hardware acceleration enabled
- Java 17

## Run

```powershell
./gradlew.bat installDebug
```

Then launch the app on a tablet-class emulator. The project has been validated on `1280x800` landscape.

## Verify

```powershell
./gradlew.bat testDebugUnitTest
./gradlew.bat assembleDebug
```

For stage or release acceptance, also run:

```powershell
./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.MathIslandTabletFlowTest"
```

## Structure

- `app/src/main/assets/content`
  Curriculum catalog and island content
- `app/src/main/java/com/mathisland/app/MathIslandTabletApp.kt`
  Tablet-first entry point and adaptive scaffold shell
- `app/src/main/java/com/mathisland/app/navigation/TabletNavGraph.kt`
  Tablet navigation boundary that hosts app scenes
- `app/src/main/java/com/mathisland/app/MathIslandApp.kt`
  Current scene host and route coordinator content
- `app/src/main/java/com/mathisland/app/MathIslandGame.kt`
  Game state machine, reward logic, review routing
- `app/src/main/java/com/mathisland/app/di/AppContainer.kt`
  Minimal dependency assembly for curriculum and controller wiring
- `app/src/main/java/com/mathisland/app/feature/home/*`
  Home shell orchestration plus hero/action modules
- `app/src/main/java/com/mathisland/app/feature/chest/*`
  Chest shell orchestration plus header, empty-state, and sticker collection modules
- `app/src/main/java/com/mathisland/app/feature/parent/*`
  Parent gate and summary shells plus extracted presentation modules
- `app/src/main/java/com/mathisland/app/feature/level/*`
  Level screen, reward overlay, and answer-pane entry point for the active lesson flow
- `app/src/main/java/com/mathisland/app/feature/level/renderers/*`
  Active renderer implementations for choice, number-pad, ruler, chant, grouping, and sorting question panes
- `app/src/main/java/com/mathisland/app/ui/theme/*`
  Shared typography, spacing, radius, action, status, and surface tokens
- `app/src/main/java/com/mathisland/app/ui/components/*`
  Shared cards, buttons, chips, and map-aware components

## Known Gaps

- Some stateful gameplay flow still remains concentrated in `MathIslandGame.kt`; future work is more likely to be interaction polish than foundational restructuring.
- Progress persistence now uses `DataStore` directly on the active path.
- Heavier map motion, richer art assets, and deeper lesson feedback are still enhancement work, not MVP blockers.

## Stable UI Contracts

- The stable map-to-lesson entry contract is `panel-start-<lessonId>` from the active island overlay.
- List-level `start-<lessonId>` tags still exist for local map cards, but they are not the primary end-to-end contract.
- Home entry contracts are `home-continue-adventure`, `home-open-map`, `home-open-chest`, and `home-open-parent`.
- Parent verification answers use `parent-answer-<value>`.
- Chest return-to-map uses `chest-open-map`.
