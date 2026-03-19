# Renderer Guidance Polish Design

## Goal

Make the lesson action area clearer by giving every renderer the same short action heading and supporting guidance copy.

## Scope

Presentation-only:

- keep correctness and routing unchanged
- keep renderer tags and answer tags unchanged
- keep existing action-state behavior unchanged

Improve:

- a shared action-area heading for choice-like renderers and number-pad
- clearer short guidance copy for ready, retry, confirmed, and locked states

## Approach

Extend `RendererActionState` with guidance copy and render a shared `RendererSectionHeader` above the action block. Reuse the same heading/body copy in `RendererSupport` and `NumberPadQuestionPane`.

## Validation

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`
