# Division Content Expansion Design

## Goal

Expand the division island so the main learning path better reflects this term's priority topics: division, remainders, and quotient-based application understanding.

## Product Direction

This batch should strengthen the division island by increasing lesson density before the advanced `MULTI_STEP` block.

The goal is not to invent a new renderer. The goal is to make the division island feel like a fuller curriculum path:

- average sharing first
- remainder understanding second
- application interpretation third
- multi-step reasoning after that

## Scope

This batch covers:

- division island lesson structure
- new division lesson entries in content assets
- new question banks for the added division lessons
- content/controller/flow validation that the new lessons appear in the intended order

Primary implementation targets:

- `app/src/main/assets/content/islands/division-island.json`
- `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`
- related controller and instrumentation tests

This batch does not:

- add a new renderer
- redesign `MULTI_STEP`
- rebalance other islands as primary work

## Current Gap

The current division island already has:

- `division-share-01`
- `division-remainder-01`
- `division-steps-01~07`

This means the island can already teach:

- average sharing
- remainder with rounding-up style context
- multi-step reasoning

But the front half is still too thin. The child reaches advanced step-based lessons before the simpler division ideas have enough repetition and scenario variety.

## Target Learning Ladder

The division island should read as one continuous learning ladder:

1. understand equal sharing
2. stabilize quotient interpretation
3. recognize and describe remainders
4. separate "has remainder" from "must round up"
5. explain division in short application questions
6. enter multi-step reasoning

This structure better matches the stated priority for the term.

## New Lessons

Add three new lessons before the existing `MULTI_STEP` block:

1. `division-share-02`
2. `division-remainder-02`
3. `division-apply-01`

### `division-share-02`

Purpose:

- reinforce "average sharing" as a stable first idea
- provide more plain quotient practice before remainder-heavy contexts

Content shape:

- standard `DIVISION` lesson
- short scenarios such as sharing snacks, pencils, or plates
- answer stays focused on "each group gets how many"

### `division-remainder-02`

Purpose:

- isolate the meaning of remainder
- avoid teaching children that every remainder automatically means "add one more"

Content shape:

- standard `DIVISION` lesson
- questions emphasize quotient plus leftover understanding
- answers should reward identifying how many remain after equal grouping

### `division-apply-01`

Purpose:

- bridge simple division and later `MULTI_STEP`
- teach "first compute, then explain what the result means in the story"

Content shape:

- still standard `DIVISION`
- prompts are more application-like than the earlier drills
- interaction remains one question at a time, not step-by-step

## Lesson Order

The division island should be reordered to:

1. `division-share-01`
2. `division-share-02`
3. `division-remainder-01`
4. `division-remainder-02`
5. `division-apply-01`
6. `division-steps-01`
7. `division-steps-02`
8. `division-steps-03`
9. `division-steps-04`
10. `division-steps-05`
11. `division-steps-06`
12. `division-steps-07`

This keeps the existing high-value `MULTI_STEP` work intact while making it feel earned.

## Content Principles

- keep arithmetic within the current island difficulty band
- use scene language that clearly distinguishes:
  - each group gets how many
  - how many are left
  - whether the context needs another container/vehicle
- avoid mixing too many ideas into the same early lesson
- use `division-apply-01` as the first place where "compute then interpret" becomes explicit

## Testing Expectations

Verification should focus on curriculum behavior, not just string presence.

Primary checks:

- the division island exposes the new lessons in the intended order
- the new lesson ids map to concrete question banks
- the new lessons preserve existing lesson-entry and reward flow behavior
- focused controller/content tests confirm the curriculum expansion without regressing current `MULTI_STEP` coverage

## Success Criteria

- the division island feels materially thicker before the advanced steps block
- remainder is taught in two clearer stages: understanding first, rounding-up context second
- application understanding is introduced before `MULTI_STEP`
- existing advanced division lessons remain intact and still work
