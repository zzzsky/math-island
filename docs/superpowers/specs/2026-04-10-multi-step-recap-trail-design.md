# Multi-Step Recap Trail Design

## Goal

Upgrade `MULTI_STEP` with a read-only recap trail so completed steps remain visible as compact collapsed cards above the active step.

## Scope

This batch adds a process recap layer inside the existing multi-step pane:

- each completed step becomes a collapsed recap card
- recap cards can expand to show prompt, support text, and answer
- the active step remains a single editable stage card

This batch does not add:

- editing or rewinding completed steps
- controller-side awareness of recap state
- layout changes outside the existing single-column lesson pane

## Recommended Shape

Render completed-step recap cards directly from the existing question metadata and `MultiStepAnswerState`.

Each recap card uses:

- `answerLabel`
- `stageTitle`
- resolved prompt for that step
- resolved support text for that step
- recorded answer

No second content source is introduced. The recap trail is derived from the same branch-aware metadata already used by the active step.

## Interaction Model

- recap cards are read-only
- default state is collapsed
- tapping a recap header toggles expansion for that step
- expansion state is local to the pane

When a step has just been confirmed, its recap card briefly appears highlighted before settling back into the normal collapsed appearance.

## Visual Structure

Place the recap trail between the progress card and the active stage card.

Collapsed recap card:

- status chip
- answer label
- stage title
- short answer summary

Expanded recap card:

- all collapsed content
- prompt text
- support text when present
- final recorded answer

The active stage remains visually dominant so the child still reads top-to-bottom toward the current action.

## Renderer Behavior

`MultiStepQuestionPane` will own two local UI states:

- `expandedRecapStepIndexes`
- `recentlyCompletedStepIndex`

The recap cards read from `MultiStepAnswerState.answers` and branch-aware helper functions for arbitrary step indexes.

## Testing

### Android UI

- completing a step creates a collapsed recap card
- recap cards are collapsed by default
- tapping a recap card reveals prompt, support text, and answer
- current step controls remain active and separate from recap expansion

### Flow

- existing multi-step lesson flows still complete
- recap state does not affect final submission behavior

## Success Criteria

- completed steps remain visible as a compact process trail
- recap cards expand for review without allowing edits
- the active step remains the only editable area
- branch-specific prompts and support copy are preserved inside recap details
