# Map Return Differentiation Addendum

## Scope

This addendum refines the previously implemented reward-to-map handoff.

## Tasks

1. Add a tested reward-to-map mapper in the map feature layer.
2. Add `MapFeedbackKind` and carry it through map summary and island handoff UI.
3. Allow overlay handoff content to appear for chest/replay flows when no explicit highlighted island exists.
4. Keep interaction contracts unchanged.

## Acceptance

- new island returns map to a `NewIsland` handoff
- chest returns map to a `Chest` handoff
- timed-out challenge returns map to a `Replay` handoff
- unit tests cover mapper and overlay visibility rules
