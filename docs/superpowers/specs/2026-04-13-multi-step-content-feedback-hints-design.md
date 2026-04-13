# Multi-Step Content Feedback Hints Design

## Goal

Apply content-authored `stepFeedbackHints` to the existing `MULTI_STEP` result-feedback system so complex conditional lessons can guide retry and timeout review more precisely.

## Scope

This batch only targets these lesson banks in [CurriculumGameMapping.kt](D:/Practice/codex/math-island/app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt):

- `division-steps-05`
- `division-steps-06`
- `division-steps-07`

This batch does not:

- change controller logic
- change `MultiStepQuestionPane` behavior rules
- add new question model fields beyond the already-landed `stepFeedbackHints`
- backfill every existing `multi-step` lesson

## Why These Lessons

`division-steps-05~07` already contain the strongest process structure:

- conditional routing
- branch-specific middle steps
- shared final conclusions
- final summary phrasing

They are the best sample set for proving that content-authored recap guidance adds value beyond generic fallback copy.

## Content Strategy

The renderer behavior is already in place. This batch only provides lesson-authored copy and focus signals.

Authoring principles:

- `correct`: explain what this step contributed to the full solution
- `incorrect`: tell the learner which step to revisit first
- `timeout`: explain what is still worth noticing now that the attempt is over

Interaction principles:

- `incorrect` auto-expands only one highest-value diagnosis step
- `timeout` auto-expands only one highest-value review step
- `correct` never auto-expands

## Lesson-Specific Design

### `division-steps-05`

This lesson teaches route selection first, then branch-specific calculation, then bag-count conclusion.

Hint strategy:

- Step 1 is the primary retry diagnosis point.
- Step 2 confirms the chosen route and calculation wording.
- Step 3 is the timeout review point because it shows how the final bag count depends on the prior result.

Expected emphasis:

- retry: "先判断有没有余数"
- timeout: "最后数量要跟着前一步结果走"

### `division-steps-06`

This lesson teaches that different branch paths can converge into one shared final decision.

Hint strategy:

- Step 1 confirms that the learner picked the right route.
- Step 2 is the main retry diagnosis point because the shared final step depends on the branch calculation.
- Step 3 is the timeout review point because it highlights the shared conclusion across branches.

Expected emphasis:

- retry: "先重看分支里的除法结果"
- timeout: "不同路线最后都回到同一个装盒判断"

### `division-steps-07`

This is the fullest `multi-step` sample and should become the reference implementation for content-authored recap guidance.

Hint strategy:

- Step 1 diagnoses route selection.
- Step 2 diagnoses branch calculation and is the primary retry expand step.
- Step 3 diagnoses shared conclusion.
- Step 4 emphasizes complete verbal wrap-up, especially in the correct state.

Expected emphasis:

- correct: "你把最终结论说完整了"
- retry: "先回看分支计算"
- timeout: "先看统一结论，再回想前面的路线"

## Authoring Rules

To keep recap cards readable:

- Use short labels, ideally 4-6 Chinese characters.
- Keep bodies to one sentence.
- Avoid repeating the prompt text verbatim.
- Make each body describe the step's role, not just restate the selected answer.

## Testing Expectations

This batch should be accepted by behavior, not exact prose snapshots.

Primary verification targets:

- `incorrect` expands the expected step for `division-steps-05~07`
- `timeout` expands the expected step for `division-steps-05~07`
- `correct` shows authored recap copy without auto-expanding

Testing can stay focused on representative lessons rather than every string variant.

## Success Criteria

- `division-steps-05~07` all provide non-generic recap guidance through `stepFeedbackHints`
- retry review focuses the most useful diagnosis step
- timeout review focuses the most useful reflection step
- correct review reads as a concise process recap, not repeated generic labels
- existing fallback behavior still covers simpler `multi-step` lessons unchanged
