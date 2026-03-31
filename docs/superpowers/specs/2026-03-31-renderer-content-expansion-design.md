# Renderer Content Expansion Design

## Goal

Expand the three new renderer families from single showcase lessons into reusable lesson slices.

## Scope

- Add 2 more `MATCHING` lessons in `classification-island`
- Add 2 more `FILL_BLANK` lessons in `measurement-geometry-island`
- Add 2 more `MULTI_STEP` lessons in `division-island`

## Constraints

- Reuse the existing renderer and controller contracts
- Keep answer encoding stable
- Focus on content only; no new renderer mechanics in this batch

## Verification

- Unit tests for curriculum lesson availability
- Full `testDebugUnitTest`
- `assembleDebug`
