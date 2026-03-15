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
- Challenge mixed run, timed sprint, replay routing, grading, and retry CTA
