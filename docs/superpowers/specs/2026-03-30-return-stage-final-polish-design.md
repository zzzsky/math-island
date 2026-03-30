# Return Stage Final Polish Design

## Goal

Finish the shared result-stage path so reward, map return, island handoff, and map list handoff all derive their CTA emphasis and staged copy from the same source of truth.

## Design

- Keep `MapReturnCopy` as the single copy source for `NewIsland / Chest / Replay / Progress`.
- Extend that source with:
  - a dedicated `spotlightLabel`
  - a shared `continueActionRole`
  - a helper that builds `ReturnResultStageState`
- Let `RewardViewModel`, `MapFeedbackMapper`, and `IslandViewModel` precompute stage state for the active path.
- Keep older UI-state fields as compatibility fallback so focused tests and preview-style constructors do not break.

## Scope

- `ReturnResultStage` and `ReturnActionCard` emphasis polish
- shared stage-state derivation from `MapReturnCopy`
- reward/map/island integration
- focused unit coverage for the shared stage state path

## Non-Goals

- no product-flow changes
- no new destination types
- no new instrumentation surface beyond existing focused regressions
