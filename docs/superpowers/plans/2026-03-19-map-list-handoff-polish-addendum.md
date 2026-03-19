# Map List Handoff Polish Addendum

## Scope

This addendum extends return-flow differentiation into the left-side island list.

## Tasks

1. Add handoff metadata fields to `MapTabletIslandUiState`.
2. Derive handoff badge/body in `MapViewModel`.
3. Render list-card handoff badge/body in `MapIslandListCard`.
4. Lock the rules with unit tests.

## Acceptance

- new island returns show a mainline recommendation on the highlighted island
- chest returns show a chest-first recommendation on the recommended island
- replay returns show a replay-first recommendation on the recommended island
- neutral islands remain unchanged
