# Renderer Layout Polish Design

## Goal

Make the lesson answer area read as one consistent panel system across all renderer types.

## Scope

Presentation-only:

- keep answer correctness logic unchanged
- keep lesson progression unchanged
- keep reward routing unchanged
- keep existing renderer tags and answer tags unchanged

Improve:

- consistent section order for helper, feedback, affordance, and actions
- closer visual rhythm between number-pad and choice-like renderers
- clearer panel spacing and grouping in the answer area

## Approach

Introduce a shared renderer panel stack that defines a stable reading order: context, feedback, affordance, then action. Use that shared structure in `RendererSupport` and `NumberPadQuestionPane`, plus a small set of renderer layout tokens.

## Validation

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`
