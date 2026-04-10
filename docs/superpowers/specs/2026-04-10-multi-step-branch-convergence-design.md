# Multi-Step Branch Convergence Design

## Goal

Extend `MULTI_STEP` so different conditional paths can converge back into a shared later step, while keeping the controller contract unchanged.

## Scope

This batch adds one new shape on top of the existing conditional flow:

- Step 1 can choose between multiple branches.
- Step 2 can still differ by branch.
- Step 3 can reuse one shared prompt and one shared option set.

This batch does not add:

- Arbitrary graph editing or backtracking
- Branch-specific step counts
- Controller-side branch awareness

## Recommended Shape

Keep the existing branch-key resolver and express convergence entirely in content:

- earlier branch rules point to different step-2 keys
- different step-2 keys can both point to the same step-3 key

That keeps renderer logic simple and avoids introducing a new workflow abstraction.

## Renderer Behavior

The renderer remains branch-key driven:

- resolve current branch key from recorded state
- look up branch prompt and choices for the current step
- record the next branch key after each answer

Convergence works when two different step-2 branch rules return the same next branch key.

## Content Strategy

Add one new division lesson:

- `division-steps-06`
- step 1 decides whether division leaves a remainder
- step 2 differs for the remainder and exact branches
- step 3 converges to one shared “how many boxes are needed” prompt

This gives a real child-facing example of “different reasoning, same final decision”.

## State Safety

`MultiStepQuestionPane` should reset local step state when any branch metadata changes, not only when base prompt lists change.

## Testing

### Unit

- shared final branch key is resolved from both step-2 branches
- encoded answers remain stable
- controller content exposes the new lesson

### Android

- pane test covers branch-specific step 2 and shared step 3
- renderer route test still renders the converged prompt
- tablet flow completes the new lesson and returns to map

## Success Criteria

- `division-steps-06` is available in curriculum content
- both step-2 paths can reach the same final prompt
- final submission still uses a single comma-joined answer string
- existing multi-step lessons remain unchanged
