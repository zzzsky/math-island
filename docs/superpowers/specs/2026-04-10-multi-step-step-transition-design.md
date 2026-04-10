# Multi-Step Step Transition Design

## Goal

Improve `MULTI_STEP` so each step selection gives a short confirmation beat before the next step appears.

## Scope

This batch focuses on the in-step transition only:

- tapping an option enters a short confirmed state
- the current card and progress chips reflect the confirmation
- after a short delay, the next step appears

This batch does not add:

- completed-step replay cards
- controller changes
- variable answer timing logic outside the pane

## Recommended Shape

Keep the transition state local to `MultiStepQuestionPane`.

Add a lightweight transient state that records:

- current step index
- selected answer
- selected choice index
- next branch key

When this state is present, the pane is in “confirming step” mode. After a short delay, the state is applied to `MultiStepAnswerState` and then cleared.

## Interaction Rhythm

### Choice tap

- selected option changes into a confirmed visual state immediately
- other options and action buttons become temporarily disabled
- stage card slightly shifts to show the handoff beat

### After delay

- the answer is committed
- the next prompt replaces the current one
- if it was the final step, the completed summary replaces the active stage

Recommended delay:

- around 320 ms

This is long enough to register, short enough not to feel blocked.

## Visual Treatment

- selected choice card uses a success-like surface and border
- selected choice shows a small “已确认” chip
- progress chip for the just-completed step lights up during the transition
- stage card uses a small scale and alpha shift while confirming

## Testing

### Android UI

- after click, confirmed tag/chip is visible before step advancement
- reset and submit are disabled during the transition
- next prompt appears after the confirmation beat

### Flow

- existing multi-step lesson flows still complete
- no behavior change to final encoded answers

## Success Criteria

- each step feels acknowledged before the next one appears
- final submission behavior remains unchanged
- no duplicate taps during the transition window
- the implementation stays local to the renderer
