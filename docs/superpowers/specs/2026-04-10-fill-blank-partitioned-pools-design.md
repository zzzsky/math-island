# Fill Blank Partitioned Pools Design

## Goal

Upgrade `FILL_BLANK` so mixed-slot questions no longer show one flat option list.

Instead:

- numeric options appear in a number pool
- unit or label options appear in a unit pool
- slots highlight the matching pool
- the renderer still submits one stable encoded answer string

## Scope

This batch only upgrades `FILL_BLANK`.

In scope:

- partitioned option pools
- slot and pool highlighting
- one additional mixed-slot lesson
- focused unit, android, and flow verification

Out of scope:

- new controller state
- free drag interactions
- dynamic option generation
- more than two pool kinds

## Interaction

The renderer will support two-step selection in either order:

- choose an option, then choose a slot
- or choose a slot, then choose an option

Rules:

- selecting a slot highlights the matching pool
- selecting an option highlights matching slots
- assigning an option still replaces prior occupant if needed
- submit remains disabled until all slots are filled

## Pool Kinds

First batch keeps only two pool kinds:

- `number`
- `unit`

Option kind can still be inferred from content:

- all digits => `number`
- everything else => `unit`

## Encoding

No controller changes.

Encoded answer stays:

- slot-order based
- comma-joined string

Example:

`米,300,分米,70`

## Content

Add one more measurement lesson using two pool kinds repeatedly:

- `measure-fill-06`
- mixed slots with both unit and number pools active

## Testing

Unit:

- state supports selecting slot first
- pool kind highlighting helpers

Android:

- `FillBlankQuestionPaneTest` covers partitioned pools and slot-first flow
- `LevelAnswerPaneTest` covers renderer exposure for both pools
- `MathIslandTabletFlowTest` covers the new lesson through reward and return to map
