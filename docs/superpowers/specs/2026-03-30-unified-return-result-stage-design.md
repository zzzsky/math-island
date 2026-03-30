# Unified Return Result Stage Design

## Goal

Unify the mid-stage result presentation used by reward settlement, map return summary, and island handoff so those three surfaces read as one system instead of three similar but diverging implementations.

## Scope

This batch only touches the shared result stage structure:

- kind pill
- spotlight card
- detail card
- action card
- staged reveal ordering for those four layers

It does not redesign:

- reward header / stats / CTA row
- map page shell
- island overlay layout
- return-copy semantics

## Current Problem

`RewardOverlay`, `MapReturnSummaryCard`, and `IslandHandoffCard` all implement the same reveal sequence with local `AnimatedVisibility` blocks and local combinations of:

- `SummarySpotlightCard`
- `TabletInfoCard`
- `ReturnActionCard`

The behavior is already conceptually aligned, but the structure is duplicated and will drift if future motion or card-level polish lands in only one surface.

## Design

Introduce a shared stage component in `ui/components` that renders the reusable return-result middle stack.

### Shared state

Create a small immutable UI model that carries:

- `kindLabel`
- `summaryTitle`
- `summaryBody`
- `detailLabel`
- `detailTitle`
- `detailBody`
- `actionLabel`
- `actionTitle`
- `actionBody`

All three feature surfaces map their existing state into that model instead of manually wiring three separate card types.

### Shared component

Create a composable that owns the repeated staged reveal sequence:

1. kind pill
2. spotlight card
3. detail card
4. action card

It receives:

- stage state
- `MapFeedbackMotionSpec`
- `motionProgress`
- per-surface test tags for pill/detail/action
- accent and badge variant from motion spec

It should not know anything about reward CTAs, map shell layout, or island primary actions.

### Surface integration

- `RewardOverlay` keeps its header, stat row, section header, and CTA row.
  - The middle result stack moves to the shared stage.
- `MapReturnSummaryCard` becomes a thin wrapper around the shared stage.
- `IslandHandoffCard` becomes a thin wrapper around the shared stage.

## Testing

Keep the current contract-level tests and update only what is needed to reflect the shared stage abstraction:

- `RewardOverlayTest`
- `IslandOverlaySheetTest`
- `MapTabletScreenTest`
- focused unit coverage for the shared stage state mapping if needed

Verification for the batch:

- `testDebugUnitTest`
- `assembleDebug`
- focused emulator regression for reward/map/island return surfaces

## Non-goals

- new return kinds
- copy rewrite
- stronger animation choreography than the current motion spec
- reward header redesign
