# Fill Blank Renderer Design

## Goal

Add a `fill-blank` lesson renderer that lets learners place numeric options into ordered blanks without changing the existing lesson controller contract.

## Shape

- Extend `Question` with `blankParts` and `blankOptions`.
- Add a `FILL_BLANK` renderer route.
- Encode answers as a stable comma-separated string in slot order.
- Keep lesson/reward flow unchanged.

## First Content Slice

- Add `measure-fill-01` under `measurement-geometry-island`.
- Use a two-slot unit-conversion prompt:
  - `1 米 = [ ] 厘米，2 米 = [ ] 厘米。`

## Verification

- Unit coverage for slot assignment and encoding.
- Focused android coverage for renderer visibility and submit gating.
- Focused emulator regression for `FillBlankQuestionPaneTest` and `LevelAnswerPaneTest`.
