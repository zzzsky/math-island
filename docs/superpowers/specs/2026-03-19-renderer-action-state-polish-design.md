# Renderer Action State Polish Design

## Goal

Make lesson answer buttons communicate clearer state without changing lesson progression, correctness rules, or reward timing.

## Scope

Presentation-only:

- keep answer correctness logic unchanged
- keep reward routing unchanged
- keep existing answer tags unchanged
- keep renderer selection unchanged

Add clearer button semantics for:

- default ready state
- retry after incorrect answers
- confirmed state after correct answers
- short locked/disabled state during answer handoff

## Approach

Introduce a lightweight renderer action state in the level presentation layer. `LevelTabletScreen` will derive a shared `RendererActionState` from the existing answer feedback and input lock, then pass it into the shared renderer scaffold and number-pad renderer.

## Validation

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`
