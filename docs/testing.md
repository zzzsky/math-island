# Testing Guide

## Environment

- Use a tablet AVD in landscape.
- Recommended baseline: `1280x800`, API 33 or newer.
- If Gradle fails with `gradle-8.11.1-bin.zip.lck (拒绝访问。)`, rerun outside the sandbox or clear the stale wrapper lock.

## Unit Tests

```powershell
./gradlew.bat testDebugUnitTest
```

Focused examples:

```powershell
./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.SmokeCoverageTest"
./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.MathIslandGameControllerTest"
```

The active lesson renderer implementations now live under `app/src/main/java/com/mathisland/app/feature/level/renderers`.

## Device Tests

Run the tablet end-to-end suite:

```powershell
./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.MathIslandTabletFlowTest"
```

Run one focused flow:

```powershell
./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.MathIslandTabletFlowTest#challengeSprintPerfectRun_showsGoldGrade"
```

## Build Verification

```powershell
./gradlew.bat assembleDebug
```

## What The Main Device Suite Covers

- Home -> map -> lesson -> reward happy path
- Island unlocks and chest rewards
- Parent gate and summary
- Measurement / chant / grouping / sorting renderers
- Active `feature/level/renderers/*` answer-pane split, including challenge number-pad flow
- Challenge mixed run, timed sprint, replay routing, grading, and retry CTA

## Stable Test Contracts

- End-to-end lesson entry should target `panel-start-<lessonId>` from the island overlay.
- Plain `start-<lessonId>` tags are treated as local map/list affordances and should not be the primary flow anchor in instrumentation.
