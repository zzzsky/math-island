# Level Status Panel Polish Design

## Goal

Strengthen the left-side lesson panel so it reflects the learner's current solving state instead of acting like a mostly static info column.

## Scope

- Keep lesson progression and answer evaluation unchanged.
- Add two explicit status cards to the left panel:
  - current attempt status
  - timer pressure status for timed lessons
- Reuse existing lesson feedback state and countdown state.

## Status Cards

### Current Attempt

- Default lessons: `准备作答`
- Review lessons: `先看线索`
- Correct answers: `已确认`
- Incorrect answers: `正在重试`
- Timed initial state: `限时进行中`

### Timer Pressure

Timed lessons get a second card that shifts by remaining time:

- early: `保持节奏`
- middle: `时间过半`
- late: `最后冲刺`

## Contracts

- Keep existing lesson tags unchanged.
- Add:
  - `lesson-attempt-status`
  - `lesson-timer-status`

## Verification

- Focused unit tests for the pure mapping logic
- Screen contract compilation
- Milestone:
  - `./gradlew.bat testDebugUnitTest`
  - `./gradlew.bat assembleDebug`
