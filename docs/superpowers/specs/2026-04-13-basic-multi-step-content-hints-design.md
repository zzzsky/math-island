# Basic Multi-Step Content Hints Design

## Goal

Extend content-authored `stepFeedbackHints` from the advanced conditional lessons to the foundational `multi-step` lessons, while simplifying test ownership between synthetic renderer tests and real lesson-content tests.

## Scope

This batch targets:

- `division-steps-01`
- `division-steps-02`
- `division-steps-03`
- `division-steps-04`

This batch also adjusts tests so:

- synthetic tests keep validating renderer mechanics
- real lesson tests validate authored content behavior

This batch does not:

- change renderer logic
- add new model fields
- change controller behavior

## Why This Batch

The current result-feedback system now has:

- renderer-side review behavior
- advanced authored hints on `division-steps-05~07`

The remaining gap is that foundational `multi-step` lessons still fall back to generic recap copy. That makes early lessons feel less intentional than later lessons.

Adding lighter authored hints to `division-steps-01~04` closes that gap without overcomplicating simpler lessons.

## Content Strategy

These lessons are structurally simpler than `division-steps-05~07`, so their hints should also stay simpler.

Authoring principles:

- step 1 usually diagnoses setup or route choice
- final step usually diagnoses the conclusion or quantity decision
- middle steps, when present, explain the calculation role but do not need heavy diagnosis

Interaction principles:

- `incorrect` expands the most important setup step
- `timeout` expands the final conclusion step
- `correct` stays collapsed

## Lesson-Specific Design

### `division-steps-01`

Two-step average-sharing lesson.

- Step 1: diagnose whether the learner first recognized this as an equal-sharing setup
- Step 2: diagnose the final per-person quantity

### `division-steps-02`

Two-step division-with-remainder lesson.

- Step 1: diagnose whether the learner started with division
- Step 2: diagnose the "remainder means one more boat" conclusion

### `division-steps-03`

Two-step average-to-boxes lesson.

- Step 1: diagnose whether the learner recognized the equal-sharing setup
- Step 2: diagnose the final per-box quantity

### `division-steps-04`

Three-step lesson with explicit calculation before final quantity.

- Step 1: diagnose the setup operation
- Step 2: explain the quotient/remainder result
- Step 3: diagnose the final bag-count decision

## Test Responsibility

The test suite should separate mechanism from content.

Synthetic tests should keep covering:

- review-mode renderer behavior
- hint-driven auto-expand behavior
- generic fallback behavior

Real lesson tests should cover:

- authored labels and bodies for `division-steps-01~07`
- expected retry expand targets
- expected timeout expand targets

This reduces repeated assertions across both test types.

## Success Criteria

- `division-steps-01~04` all use content-authored `stepFeedbackHints`
- retry review expands the intended setup step
- timeout review expands the intended conclusion step
- synthetic tests still prove renderer mechanics
- overlapping content assertions are reduced from synthetic tests
