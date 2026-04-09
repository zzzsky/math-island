# Multi-Step Conditional Steps Design

## Goal

Upgrade `MULTI_STEP` so earlier answers can change later prompts and choices, while keeping the lesson controller contract unchanged.

## Scope

This batch adds conditional branching inside a single multi-step question:

- Step 1 can choose a branch.
- Later steps can show different prompts and options based on that branch.
- Final submission is still a single encoded answer string.

This batch does not add:

- Free backtracking between branches
- Variable numbers of branch depths beyond the defined step tree
- Controller or reward-flow changes

## Recommended Shape

Use a lightweight branch tree instead of a full workflow engine.

Each step can optionally declare branch-specific overrides for:

- next prompt
- next choices

The renderer owns the branch resolution. The controller still only receives the final encoded answer string.

## Domain Model

Add optional branch metadata to `Question`:

- `stepBranchKeys: List<String>` for the base branch id at each step
- `stepBranchRules: Map<String, StepBranchRule>` for conditional overrides

Add `StepBranchRule`:

- `whenAnswer: String`
- `nextBranchKey: String`

Add optional branch prompt/choice maps:

- `stepBranchPrompts: Map<String, String>`
- `stepBranchChoices: Map<String, List<String>>`

The base path stays compatible:

- Existing multi-step questions continue to use `stepPrompts` and `stepChoices`
- If no branch metadata is present, behavior is unchanged

## Renderer Behavior

`MultiStepQuestionPane` becomes branch-aware:

- At each step, it resolves the current branch key
- It renders that branch's prompt and choices
- Selecting an answer records both the chosen value and the next branch

Rules:

- No back button in this batch
- Reset still clears the whole question
- Submit is enabled only when the resolved branch path reaches its final required step

## Answer Encoding

Keep answer encoding stable and controller-friendly:

- Final answer remains a comma-joined string of selected step answers
- Example:
  - `有余数,商是5余2,需要再加1条船`

The controller never needs to understand branch keys.

## Content Strategy

First content lands in `division-island`, because conditional reasoning naturally fits remainder decisions.

First new lesson:

- `division-steps-05`
- Step 1: decide whether division leaves a remainder
- Step 2 and Step 3 depend on that branch

This creates a real conditional learning path instead of a fixed sequence with renamed text.

## Testing

### Unit

- branch resolution
- conditional prompt/choice lookup
- encoded answer after a branched path

### Android

- `MultiStepQuestionPaneTest` for both branch paths
- `LevelAnswerPaneTest` renderer routing still stable

### Flow

- `MathIslandTabletFlowTest` focused flow for the new conditional lesson

## Success Criteria

- Existing multi-step lessons still work unchanged
- A new branched multi-step lesson works end to end
- Branch choice changes later step content on-device
- Controller and reward flow remain untouched
