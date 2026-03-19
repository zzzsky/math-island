# Level Feedback Polish Design

## Goal

Improve lesson-time feedback so answers feel clearer and more guided without changing lesson progression or reward logic.

## Scope

Presentation-only:

- keep answer correctness logic unchanged
- keep renderer tags and answer input contracts unchanged
- keep reward routing and lesson completion unchanged

Add consistent UI feedback for:

- correct answer confirmation
- incorrect answer retry guidance
- temporary submit/lockout state
- clearer timed-lesson warning presentation

## Approach

Introduce a lightweight answer-feedback UI state that sits in the level presentation layer. Pass that state into the shared renderer scaffold and the number-pad renderer, and render one consistent feedback banner near the answer area.

## Validation

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`

