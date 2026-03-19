# Renderer Surface Polish Design

## Goal

Bring the lesson renderer panes closer to the rest of the tablet UI system by unifying helper cards, affordance cards, option cards, and answer actions.

## Scope

This batch is presentation-only:

- keep question logic unchanged
- keep renderer tags unchanged
- keep answer contracts unchanged
- unify renderer support surfaces and supporting text

## Approach

Add a small renderer-specific token layer for repeated card and accent colors. Migrate `RendererSupport` to shared surfaces and action semantics, then align the specialized affordance panes (`ruler`, `group`, `sort`, `number-pad`) to the same surface language.

## Files

- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererTokens.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererSupport.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/GroupQuestionPane.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/SortQuestionPane.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RulerQuestionPane.kt`

## Validation

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`

