# Renderer Interaction Feedback Design

## Goal

Make the lesson interaction layer feel more responsive without changing lesson progression logic.

Focus areas:

- choice-style renderers should clearly show which option was just submitted
- number-pad renderer should clearly show current input readiness and submitted input feedback

## Design

Add `submittedAnswer` to `AnswerFeedbackUiState`.

Introduce pure resolver helpers:

- `optionFeedbackStateFor(choice, feedback)`
- `numberPadDisplayStateFor(enteredAnswer, feedback)`

These derive presentation-only state:

- neutral / retry / confirmed option tone
- idle / ready / retry / confirmed number-pad display tone

## UI Effects

- selected wrong choice card gets a retry surface and a short supporting line
- selected correct choice card gets a confirmed surface and a short supporting line
- number-pad display shows either:
  - input readiness
  - submitted retry state
  - submitted confirmed state
- clear button is disabled when there is no input to clear

## Constraints

- no scoring changes
- no question progression changes
- existing `answer-*` and `number-pad-*` tags remain intact
