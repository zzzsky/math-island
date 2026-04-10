# Multi-Step Presentations Design

## Goal

Extend `MULTI_STEP` so each step can carry richer presentation metadata, instead of relying on generic "步骤 1/2/3" labels.

## Scope

This batch adds a lightweight presentation layer for multi-step questions:

- each fixed step can define a stage title, support text, and answer label
- each branch step can override that presentation metadata
- the lesson pane uses the metadata for the current step and the completion summary

This batch does not add:

- controller-side understanding of step presentation
- arbitrary branch graphs beyond the existing resolver
- editable backtracking between completed steps

## Recommended Shape

Add a small reusable value object:

- `StepPresentation(stageTitle, supportText, answerLabel)`

Use it in two places:

- `stepPresentations: List<StepPresentation>` for the base path
- `stepBranchPresentations: Map<String, StepPresentation>` for branch-specific overrides

The branch key remains the lookup handle. The renderer already understands branch keys, so no new routing layer is needed.

## Renderer Behavior

For the current step, the renderer resolves:

- current prompt
- current choices
- current presentation metadata

For the completion summary, the renderer resolves the presentation for each answered step and uses its `answerLabel` instead of the generic `步骤 N`.

This makes conditional lessons feel more intentional:

- current card says what the learner is doing right now
- completed summary reads like a structured solution recap

## Content Strategy

Add one more division lesson:

- `division-steps-07`
- 4 steps total
- step 1 chooses exact vs remainder
- step 2 differs by branch
- step 3 and step 4 converge to shared prompts
- all steps use presentation metadata so the lesson demonstrates the new structure immediately

## Testing

### Unit

- resolve branch-aware `StepPresentation`
- use branch-aware `answerLabel` in summaries

### Android

- pane test verifies stage title and support text update with branch choice
- summary after completion uses the configured answer labels

### Flow

- tablet flow completes the new lesson end to end

## Success Criteria

- `MULTI_STEP` supports richer per-step presentation metadata without changing answer encoding
- branch-specific presentation overrides work
- completed summaries use semantic labels, not only step numbers
- `division-steps-07` demonstrates the full capability
