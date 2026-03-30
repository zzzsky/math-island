# Unified Return Result Stage Implementation

## Batch Goal

Eliminate duplicated return-result stage structure across reward settlement, map return summary, and island handoff while preserving behavior, copy, and current test contracts.

## Steps

1. Add shared return-result stage model and component under `ui/components`.
2. Migrate `RewardOverlay` to use the shared stage for its spotlight/detail/action stack.
3. Migrate `MapReturnSummaryCard` to use the shared stage.
4. Migrate `IslandHandoffCard` to use the shared stage.
5. Update focused tests only where they assert stage-level tags or structure.
6. Run focused verification:
   - `./gradlew.bat testDebugUnitTest`
   - `./gradlew.bat assembleDebug`
   - focused emulator regression for:
     - `RewardOverlayTest`
     - `IslandOverlaySheetTest`
     - `MapTabletScreenTest`
7. Commit the batch and clear `adb / emulator / qemu`.

## Guardrails

- Keep reward header and CTA row local to `RewardOverlay`.
- Keep map page and island overlay layout unchanged.
- Do not change return-copy wording in this batch.
- Do not weaken existing test tags:
  - `reward-next-action-card`
  - `map-return-action-card`
  - `island-handoff-action-card`

## Expected Result

One reusable return-result stage powers all three surfaces, making future result-stage polish land once instead of three times.
