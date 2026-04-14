# Division Remainder Application Lines Design

## Goal

Strengthen the division island around remainder application by adding three distinct scene lines: leftover-only interpretation, container rounding-up, and transport rounding-up.

## Product Direction

This batch continues the division priority track. It does not introduce a new renderer or a new island. It deepens the meaning of remainder across three scene families that children often confuse:

- remainder means "still left over"
- remainder means "need one more container"
- remainder means "need one more vehicle/trip"

The intent is to separate these meanings clearly before the learner enters the more advanced `MULTI_STEP` lessons.

## Scope

This batch covers:

- three new division lessons in the division island
- three new standard `DIVISION` question banks
- division island lesson ordering updates
- controller and flow validation for the new lessons

Primary implementation targets:

- `app/src/main/assets/content/islands/division-island.json`
- `app/src/main/java/com/mathisland/app/data/content/CurriculumGameMapping.kt`
- `app/src/test/java/com/mathisland/app/MathIslandGameControllerTest.kt`
- `app/src/androidTest/java/com/mathisland/app/MathIslandTabletFlowTest.kt`

This batch does not:

- change `MULTI_STEP`
- change renderer behavior
- rebalance non-division islands

## Learning Problem

Children often collapse all remainder scenes into one rule. The main misunderstanding is:

- "if there is a remainder, always add one"

That is not always true. Sometimes the lesson should stop at "there are some left." Sometimes the scene requires another box, bag, boat, or bus. This batch should explicitly separate those cases.

## Three Lesson Lines

Add three new lessons:

1. `division-leftover-01`
2. `division-container-01`
3. `division-transport-01`

### `division-leftover-01`

Purpose:

- teach remainder as leftover quantity
- avoid any rounding-up requirement

Scene language:

- shells left after grouping
- candies left after packing equal small sets
- books left after bundling equal stacks

Answer meaning:

- how many are left
- not how many more containers are needed

### `division-container-01`

Purpose:

- teach when a remainder means another physical container is required

Scene language:

- boxes
- bags
- trays

Answer meaning:

- after equal filling, the leftovers still need a place
- therefore one more container is needed

### `division-transport-01`

Purpose:

- teach when a remainder means another vehicle/trip is required

Scene language:

- boats
- buses
- carts

Answer meaning:

- after filling full vehicles, the remaining people/items still must be moved
- therefore one more vehicle/trip is needed

## Relation To Existing Division Ladder

The current ladder now includes:

- equal sharing reinforcement
- remainder understanding
- short application interpretation
- `MULTI_STEP`

This batch should sit between remainder understanding and the existing advanced step lessons. It should make the scene meaning of remainder explicit before the learner sees branch-heavy step flows.

## Target Order

Recommended division island order after this batch:

1. `division-share-01`
2. `division-share-02`
3. `division-remainder-01`
4. `division-remainder-02`
5. `division-leftover-01`
6. `division-container-01`
7. `division-transport-01`
8. `division-apply-01`
9. `division-steps-01`
10. `division-steps-02`
11. `division-steps-03`
12. `division-steps-04`
13. `division-steps-05`
14. `division-steps-06`
15. `division-steps-07`

This keeps the ladder readable:

- concept
- remainder
- scene interpretation
- multi-step reasoning

## Content Principles

- keep interactions in the existing `DIVISION` choice format
- keep arithmetic within the same difficulty band as the current division island
- each lesson should emphasize one scene meaning only
- avoid mixing "leftover" and "rounding-up" in the same early lesson
- use concrete nouns that make the scene requirement obvious

## Testing Expectations

Verification should focus on curriculum shape and playability.

Primary checks:

- division island contains the three new lessons in the intended order
- each new lesson resolves to non-empty `division` question banks
- at least one new scene line is covered through the tablet flow
- no regression to existing division entry or reward flow

## Success Criteria

- remainder is no longer taught as one undifferentiated rule
- the learner sees leftover-only, container, and transport scenes as separate ideas
- the division island becomes denser without adding new interaction complexity
- the later `MULTI_STEP` lessons feel better prepared by the simpler scene ladder
