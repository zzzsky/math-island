# Map List Handoff Polish Design

## Goal

Bring the left-side island list into the same return-flow language already used by:

- map return summary
- right-side island overlay

The list should visually signal whether the current recommendation is:

- mainline progression
- chest-first collection
- replay-first review

## Design

Add lightweight handoff metadata to `MapTabletIslandUiState`:

- `handoffBadge`
- `handoffBody`

`MapViewModel` derives these fields from the active `MapFeedbackUiState` and the recommended/highlighted island target.

Rules:

- `NewIsland` -> only the highlighted island gets `主线推荐`
- `Chest` -> the recommended island gets `宝箱优先`
- `Replay` -> the recommended island gets `回放优先`
- `Progress` -> the recommended island gets `继续推进`

`MapIslandListCard` renders this metadata beneath the progress line using the existing card contract and stable tags.

## Constraints

- no map navigation changes
- no reward logic changes
- no selection contract changes
- device validation can remain deferred; unit + assemble is enough for this batch
