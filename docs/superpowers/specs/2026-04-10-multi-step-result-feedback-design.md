# Multi-Step Result Feedback Design

## Goal

Integrate lesson-level result feedback into `MULTI_STEP` so the recap trail becomes a readable process diagnosis layer after submit.

## Scope

This batch connects existing result states to multi-step recap cards:

- correct
- retry
- timeout

Each recap card receives a result-mode status and optional guidance copy after submission.

This batch does not add:

- per-step scoring in the controller
- true automatic reasoning about which step is wrong from arbitrary answers
- editable step rewind

## Recommended Shape

Keep result diagnostics content-driven and lightweight.

Add optional per-step feedback hints to `Question`:

- correct label/body override
- retry label/body override
- timeout label/body override
- optional flags for which steps should auto-expand in retry or timeout mode

If hints are absent, the renderer falls back to generic copy.

## Renderer Behavior

After result feedback arrives:

- the recap trail enters result mode
- each recap card shows a result status chip
- retry/timeout can auto-expand important steps
- the active stage becomes read-only summary content instead of continuing normal answer entry

To support read-only review when the pane is recreated from feedback alone, the renderer reconstructs a `MultiStepAnswerState` from the submitted encoded answer.

## Data Flow

- controller still only validates the final encoded answer
- `LevelTabletScreen` continues to send `AnswerFeedbackUiState`
- `MultiStepQuestionPane` derives display-only recap diagnosis from `feedback.kind` plus optional `stepFeedbackHints`

This keeps feedback integration entirely inside the renderer.

## Content Strategy

Add optional hints only where worthwhile:

- older lessons can rely on generic fallback copy
- advanced lessons like `division-steps-07` can declare focused retry/timeout steps

## Testing

### Unit

- reconstruct branch-aware state from submitted encoded answer
- fallback and hinted recap feedback states resolve correctly

### Android

- correct feedback shows confirmed recap status
- incorrect feedback auto-expands the hinted recap step
- timeout feedback shows warning recap status and read-only review state

### Flow

- existing multi-step flows still complete unchanged

## Success Criteria

- recap cards switch into result-aware states after submit
- retry and timeout can spotlight a target step without controller changes
- generic fallback works for lessons without explicit hints
- the recap trail and final feedback read as one coherent result experience
