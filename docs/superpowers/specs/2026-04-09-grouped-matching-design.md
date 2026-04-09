# Grouped Matching Design

## Goal

Upgrade `MATCHING` so one lesson can contain multiple semantic matching groups in a single question, without changing the lesson controller answer API.

## Scope

- Keep `MathIslandGameController.answer(...)` unchanged.
- Preserve existing single-group matching behavior.
- Add grouped matching as an optional richer structure for `MATCHING`.
- Use meaningful semantic content only.

## Model

- Add `MatchingGroup` to the domain model.
- Add optional `matchingGroups` to `Question`.
- If `matchingGroups` is empty, the renderer falls back to the existing `leftItems/rightItems`.

## Encoding

- Each group still encodes matches in left-item order as `left=right`.
- Multiple groups are joined with `||`.
- Example:
  - `平均分苹果=用除法,合并两堆贝壳=用加法||尺子=测长度,秤=测重量`

## Renderer

- `MatchingQuestionPane` renders one section per group.
- Each group owns its own left/right columns and assignments.
- A single submit button remains at the bottom.
- Submission is enabled only when every group is fully matched.

## Content

- Add one grouped matching lesson to `classification-island`.
- Content must be semantic:
  - scene -> operation
  - tool -> purpose

## Testing

- Unit:
  - grouped answer encoding
  - grouped completion
- Android:
  - grouped matching pane
  - grouped matching renderer routing
  - grouped matching lesson flow
