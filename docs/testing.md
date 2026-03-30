# Testing Guide

## Environment

- Use a tablet AVD in landscape.
- Recommended baseline: `1280x800`, API 33 or newer.
- If Gradle fails with `gradle-8.11.1-bin.zip.lck (拒绝访问。)`, rerun outside the sandbox or clear the stale wrapper lock.

## Unit Tests

```powershell
./gradlew.bat testDebugUnitTest
```

This is the default milestone check during iterative development.

Focused examples:

```powershell
./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.SmokeCoverageTest"
./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.MathIslandGameControllerTest"
```

The active lesson renderer implementations now live under `app/src/main/java/com/mathisland/app/feature/level/renderers`.

## Device Tests

Use device tests for milestone acceptance, route-contract changes, or before pushing a larger UX batch.

Run the tablet end-to-end suite:

```powershell
./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.MathIslandTabletFlowTest"
```

Run one focused flow:

```powershell
./gradlew.bat connectedDebugAndroidTest "-Pandroid.testInstrumentationRunnerArguments.class=com.mathisland.app.MathIslandTabletFlowTest#challengeSprintPerfectRun_showsGoldGrade"
```

If UTP or Gradle instrumentation becomes flaky after failures, use the sequential emulator regression script instead:

```powershell
./scripts/run-focused-emulator-regression.ps1
```

This path:

- stops Gradle daemons first
- rebuilds app and androidTest APKs sequentially
- installs fresh APKs onto the emulator
- runs focused instrumentation one class at a time through `adb shell am instrument`
- shuts down emulator and adb at the end by default

## Build Verification

```powershell
./gradlew.bat assembleDebug
```

This is the default build check paired with `testDebugUnitTest` during day-to-day development.

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
- Home shell contracts are `home-continue-adventure`, `home-open-map`, `home-open-chest`, and `home-open-parent`.
- Parent gate answers are `parent-answer-<value>`.
- Chest navigation back to map uses `chest-open-map`.

## Current UI Structure

- `feature/home/*` now uses extracted hero/action modules.
- `feature/chest/*` now uses extracted header, empty-state, and sticker collection modules.
- `feature/parent/*` now uses extracted gate and summary presentation modules.
- `feature/level/renderers/*` holds the active lesson answer panes and renderer support surfaces.
