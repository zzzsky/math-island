# Multi Step Renderer Design

## Goal

Add a `multi-step` renderer that guides learners through ordered decisions, then encodes the completed sequence into the existing string answer contract.

## Shape

- Extend `Question` with `stepPrompts` and `stepChoices`.
- Add `MULTI_STEP` routing.
- Keep controller flow unchanged by submitting a comma-separated answer in step order.

## First Content Slice

- Add `division-steps-01` under `division-island`.
- Prompt learners through two ordered decisions for an average-share problem.

## Verification

- Unit tests for ordered encoding and reset.
- Focused android coverage for renderer visibility and step progression.
- Focused emulator regression for `MultiStepQuestionPaneTest` and `LevelAnswerPaneTest`.
