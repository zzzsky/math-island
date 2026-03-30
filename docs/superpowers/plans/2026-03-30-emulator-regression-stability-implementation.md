# Emulator Regression Stability Implementation

## Deliverables

- `scripts/run-focused-emulator-regression.ps1`
- `README.md` update
- `docs/testing.md` update

## Flow

1. Resolve SDK tools from `ANDROID_HOME` / `ANDROID_SDK_ROOT`
2. Stop Gradle daemons
3. Build `app-debug.apk` and `app-debug-androidTest.apk` sequentially
4. Start the configured AVD and wait for boot completion
5. Install both APKs
6. Execute focused instrumentation tests sequentially with per-test timeout
7. Stop emulator and adb unless explicitly kept alive

## Verification

- Run the script against the current focused regression set:
  - `MapTabletScreenTest`
  - `IslandOverlaySheetTest`
  - `RewardOverlayTest`
- Confirm that the script can recover from prior UTP instability by using direct instrumentation
