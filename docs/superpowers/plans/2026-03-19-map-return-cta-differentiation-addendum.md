# Map Return CTA Differentiation Addendum

## Scope

This addendum extends map return differentiation by making the island overlay action model handoff-aware.

## Tasks

1. Add overlay primary action state to `IslandUiState`.
2. Select replay-focused lessons when replay handoff is active.
3. Route chest handoff through the overlay primary CTA.
4. Adjust lesson card labels/status for handoff context.
5. Cover the new selection rules with unit tests.

## Acceptance

- chest return shows a chest-first main CTA
- replay return prefers the replay/review lesson
- new island return presents a clearer mainline CTA
- existing lesson start tags remain unchanged
