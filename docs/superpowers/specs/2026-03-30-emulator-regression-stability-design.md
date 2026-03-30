# Emulator Regression Stability Design

## Goal

Stabilize emulator-based regression runs without changing product behavior.

## Root Cause

Two separate issues were causing unreliable device verification:

1. Multiple Gradle/Kotlin daemon sessions were leaving incremental caches and build outputs locked.
2. `connectedDebugAndroidTest` through UTP was unreliable after failures or long runs, even when the emulator itself remained healthy.

## Design

Introduce a single, sequential emulator regression path:

1. stop Gradle daemons
2. rebuild app and androidTest APKs in-process
3. start a known AVD
4. install fresh APKs
5. run focused instrumentation tests one class at a time through `adb shell am instrument`
6. stop emulator and adb

## Scope

- Add one PowerShell script under `scripts/`
- Document this path in `README.md` and `docs/testing.md`
- Keep existing Gradle-based verification commands as the default path
- Use the script when UTP becomes flaky or when a batch specifically needs a stable focused emulator regression

## Non-Goals

- No product code changes
- No migration away from Gradle instrumentation
- No CI changes in this batch
