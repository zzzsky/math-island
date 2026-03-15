# Math Island Android Tablet MVP

Landscape-first Android tablet build of Math Island.

## Current Scope

- 7 playable islands driven from curriculum JSON assets
- Home, map, lesson, reward, chest, parent summary, seagull review
- Challenge island with mixed run, timed sprint, replay, grading, and retry loop
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
./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.MathIslandTabletFlowTest"
./gradlew.bat assembleDebug
```

## Structure

- `app/src/main/assets/content`
  Curriculum catalog and island content
- `app/src/main/java/com/mathisland/app/MathIslandApp.kt`
  Current Compose app shell and screens
- `app/src/main/java/com/mathisland/app/MathIslandGame.kt`
  Game state machine, reward logic, review routing
- `app/src/main/java/com/mathisland/app/di/AppContainer.kt`
  Minimal dependency assembly for curriculum and controller wiring

## Known Gaps

- UI and state are still concentrated in a few files; the planned `feature/*`, `navigation/*`, and `domain/usecase/*` split is not finished yet.
- Progress persistence is still `SharedPreferences`-based, not the planned `DataStore` + repository stack.
