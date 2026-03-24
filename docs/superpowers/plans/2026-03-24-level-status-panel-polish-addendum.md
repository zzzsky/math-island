# Level Status Panel Polish Addendum

## Summary

Add a reactive status layer to the lesson left pane so the lesson screen communicates attempt state and time pressure more clearly.

## Work Items

1. Extract pure card-state mapping for attempt status and timed pressure.
2. Render the new status cards in `LevelTabletScreen`.
3. Preserve all existing lesson and renderer contracts.
4. Add focused tests and keep milestone verification lightweight.
