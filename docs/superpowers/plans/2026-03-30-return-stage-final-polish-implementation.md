# Return Stage Final Polish Implementation

## Deliverables

- shared stage-state builder in `MapReturnCopy`
- `ReturnResultStage` support for spotlight label + action role
- reward/map/island consumers switched to shared stage state
- focused unit tests updated for spotlight/action-role derivation

## Verification

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`
- focused emulator regression:
  - `MapTabletScreenTest`
  - `IslandOverlaySheetTest`
  - `RewardOverlayTest`

## Notes

- Keep `RewardOverlayUiState` and `IslandUiState` backward-compatible while the shared stage state rolls through the active path.
