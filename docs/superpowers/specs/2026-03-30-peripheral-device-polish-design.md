# Peripheral Device Polish Design

## Goal

Finish the last tablet-facing polish pass for `Home / Chest / Parent` and lock in a stable focused emulator regression surface for those pages.

## Design

- Keep behavior unchanged and focus on device-facing layout polish.
- Add stable section tags for the major surfaces on each page so emulator regressions can assert structure without depending on fragile text-only anchors.
- Make `Chest` resilient to larger sticker sets by switching to a wrapped grid layout.
- Make `ParentSummary` scroll inside its page surface so longer summary content does not push the page actions out of view.
- Make `ParentGate` answers wrap cleanly on device widths.

## Scope

- `feature/home/*`
- `feature/chest/*`
- `feature/parent/*`
- focused android tests for:
  - `HomeTabletScreenTest`
  - `ChestTabletScreenTest`
  - `ParentSummaryTabletScreenTest`

## Non-Goals

- no state-machine changes
- no new business logic
- no navigation changes
