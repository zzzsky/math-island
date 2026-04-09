# Fill Blank Mixed Slot Design

## Goal

Upgrade the existing `FILL_BLANK` renderer so one question can mix number slots and unit/label slots without changing controller flow, lesson flow, or answer API shape.

## Scope

- Add mixed slot metadata to `Question`
- Keep answer submission as one final encoded string
- Add one mixed-slot lesson on measurement island
- Add renderer guidance and slot cues for slot types

## Non-Goals

- No controller interface changes
- No dynamic slot generation
- No drag-and-drop redesign
- No auto-filtered option pools in this batch

## Data Model

Add an optional field to `Question`:

- `blankSlotKinds: List<String> = emptyList()`

Rules:

- Existing fill-blank lessons keep working with an empty list
- When present, `blankSlotKinds.size` must equal the slot count (`blankParts.size - 1`)
- First batch kinds:
  - `number`
  - `unit`

## Encoding

Encoding stays slot-order based:

- Example: `400,分米,90`

This preserves:

- `MathIslandGameController.answer(...)`
- reward flow
- lesson completion logic

## Renderer Behavior

`FillBlankQuestionPane` keeps the current interaction:

1. select option
2. click slot
3. submit when all slots are filled

Enhancements:

- Each slot shows a cue:
  - `填数字`
  - `填单位`
- If an assigned option does not match the slot kind, show a mismatch warning style
- First batch does not block mismatched assignment; correctness still comes from final encoded answer

## Content

Add `measure-fill-05` to measurement island as the first mixed-slot lesson.

Example structure:

- `3 [米] = [300] 厘米，9 [分米] = [90] 厘米。`

This uses:

- mixed number slots
- mixed unit slots
- a single encoded answer string

## Testing

Add:

- unit coverage for mixed lesson resolution
- android renderer coverage for mixed slot cues and submission
- one tablet flow test that reaches reward and returns to map from the new lesson
