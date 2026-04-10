# Matching Multi-Round Design

## Goal

Upgrade `MATCHING` from a single matching surface into a multi-round structure where:

- one round must be completed before the next round appears
- each round can still contain either a single matching set or grouped matching sets
- the final answer still collapses into a stable string so the controller interface does not change

## Scope

This batch only upgrades the `MATCHING` renderer and content structure.

In scope:

- multi-round matching data model
- renderer support for round progression
- stable answer encoding across rounds
- one new classification lesson using semantic chains
- focused unit and emulator regression

Out of scope:

- free-form line drawing
- branching rounds
- revisiting previous rounds
- new controller state

## Data Model

Add `MatchingRound`:

- `title`
- `prompt`
- `groups`

Add `matchingRounds` to `Question`.

Compatibility rules:

- if `matchingRounds` is empty, existing `leftItems/rightItems/matchingGroups` behavior stays unchanged
- if `matchingRounds` is present, the renderer treats the question as a multi-round matching question

## Encoding

Each round is encoded exactly like the current matching renderer:

- groups inside a round are joined by `||`
- rounds are joined by `>>>`

Example:

`平均分苹果=用除法,合并两堆贝壳=用加法>>>用除法=求每份有多少,用加法=求合起来一共多少`

This keeps the controller API unchanged because the renderer still submits one final answer string.

## Interaction

Renderer behavior:

- show only the current round
- keep current round prompt in the prompt card
- after current round is complete:
  - if another round exists, primary action becomes `进入下一轮`
  - if it is the last round, primary action becomes `提交配对`
- previously completed rounds are summarized with chips, not reopened

State behavior:

- assignments are stored per round
- current selection never leaks across rounds
- advancing a round clears the current selection

## Content

First content lands in `classification-island` as `classification-match-06`.

Semantic structure:

- round 1: `场景 -> 算法`
- round 2: `算法 -> 作用`

This keeps the task meaningful and avoids low-value number/unit pairing.

## Testing

Unit:

- round encoding
- round completion logic
- advance-to-next-round state transitions

Android:

- `MatchingQuestionPaneTest` covers moving from round 1 to round 2
- `LevelAnswerPaneTest` covers renderer exposure for multi-round matching
- `MathIslandTabletFlowTest` covers a full lesson flow for the new lesson

## Success Criteria

- old matching lessons still behave the same
- grouped matching still works
- multi-round matching works end-to-end
- controller still compares one stable encoded string
