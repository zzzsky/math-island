# Level Feedback Motion Design

## Goal

Polish the final layer of in-lesson interaction feedback so `correct`, `retry`, and `timeout` feel intentional, readable, and consistent across the whole lesson screen without changing lesson progression, correctness checks, or reward routing.

## Scope

Presentation-only work for the active lesson flow:

- lesson left-side status cards
- answer feedback banner
- shared renderer action/header rhythm
- choice-style renderer feedback
- number-pad feedback and temporary lock timing

Keep unchanged:

- answer correctness logic
- question progression and reward entry
- timer expiration rules
- existing renderer test tags and answer contracts

## Problems To Solve

The lesson experience already has better state copy and layout, but the final interaction rhythm is still uneven:

- `correct` feedback is clear, but the confirmation moment is not yet paced consistently across left status, banner, and action area
- `retry` feedback appears, but the recovery path can still feel abrupt rather than guided
- `timeout` pressure is visible, but the transition from warning to expiration still reads too much like generic feedback instead of a dedicated lesson state
- choice renderers and number-pad share the same design language, but not yet the same motion rhythm

## Recommended Approach

Introduce a small motion/timing layer in the lesson presentation tier and let the existing feedback/view-model scaffolding consume it.

The lesson UI should express three distinct rhythms:

- `correct`: short confirm pulse, input locked briefly, then natural progression
- `retry`: supportive retry cue, quick recovery back to input-ready state, keep the last attempt visually anchored
- `timeout`: escalating pressure before expiry, then a clean timeout state that is visually separate from retry

This keeps the work narrowly focused on presentation while reusing the current `LevelTabletScreen`, `LevelStatusCardState`, `RendererFeedbackState`, and `RendererActionState` boundaries.

## UX Model

### Correct

When the submitted answer is correct:

- the right-side feedback banner uses the confirmed tone and remains visible for a short confirmation window
- the left-side lesson status card moves to `已确认`
- the action area resolves to `confirmed` / temporarily locked
- the submitted option or number-pad display remains visually anchored during that confirmation window

### Retry

When the submitted answer is incorrect:

- the right-side feedback banner uses the retry tone
- the left-side lesson status card moves to `正在重试`
- the renderer action area returns to retry-ready quickly after a short lock window
- the just-submitted choice or number-pad value stays visually marked as the prior attempt

### Timeout

Timed lessons should distinguish between warning and expiration:

- pre-expiry warning remains a lesson-pressure state, not an error
- the left-side timer card and right-side guidance use a dedicated timeout warning rhythm
- expiration itself should surface as a distinct timeout state instead of reusing retry semantics

## Architecture

### 1. Shared lesson motion model

Add a compact presentation-layer timing model for lesson feedback. This should live near the level feature and expose named durations/thresholds rather than ad hoc delays in multiple files.

Responsibilities:

- define confirmation window
- define retry relock / unlock timing
- define timeout warning thresholds
- keep motion decisions out of individual renderers

### 2. Level screen orchestration

`LevelTabletScreen` remains the orchestration point for the active lesson page.

It should:

- own the short-lived feedback timing
- map answer results into `correct / retry / timeout-warning / timeout-expired` rhythm
- synchronize the left status cards and right renderer feedback window

It should not:

- change gameplay/controller behavior
- introduce persistence
- own renderer-specific visuals

### 3. Renderer presentation state

The renderer tier should continue to consume shared presentation state rather than invent its own timing.

`RendererFeedbackState` and `RendererActionState` should become the single source for:

- tone
- support copy
- action lock/readiness
- transient “last attempt” emphasis

Choice renderers and number-pad should use the same timing semantics even if their visuals differ.

### 4. Timeout-specific treatment

Timeout should be represented as a dedicated lesson-state branch at the presentation layer.

That means:

- warning copy remains separate from retry copy
- warning/expired visuals use warning tone, not retry tone
- number-pad and choice renderers do not infer timeout from generic disablement alone

## File Boundaries

Primary implementation files:

- `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`
- `app/src/main/java/com/mathisland/app/feature/level/LevelStatusCardState.kt`
- `app/src/main/java/com/mathisland/app/feature/level/renderers/AnswerFeedbackBanner.kt`
- `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererFeedbackState.kt`
- `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererActionState.kt`
- `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererSupport.kt`
- `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt`
- `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererTokens.kt`

Reasonable new files:

- `app/src/main/java/com/mathisland/app/feature/level/LevelMotionTokens.kt`
- or `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererMotionTokens.kt`

Tests to extend:

- `app/src/test/java/com/mathisland/app/feature/level/LevelStatusCardStateTest.kt`
- `app/src/test/java/com/mathisland/app/feature/level/renderers/RendererFeedbackStateTest.kt`
- `app/src/test/java/com/mathisland/app/feature/level/renderers/RendererActionStateTest.kt`
- `app/src/androidTest/java/com/mathisland/app/feature/level/LevelTabletScreenTest.kt`
- `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`

## Detailed Behavior

### Confirmation window

Use one explicit confirmation window for correct answers so all lesson surfaces settle together:

- status card
- feedback banner
- renderer action state
- submitted answer highlight

This should replace scattered delay assumptions where practical.

### Retry recovery window

Retry should keep a short “checking” moment, then return control quickly.

The user experience should read as:

1. submission acknowledged
2. retry guidance shown
3. interaction restored

### Timeout pressure window

Timed lessons should continue to show pressure before expiry, but the copy and visual treatment should become more consistent:

- neutral/steady
- time over half
- final sprint
- expired

The first three are warning-pressure states. Only the last one is terminal for that question flow.

## Copy Guidance

Keep the lesson language short and directive:

- correct: confirmation, then forward motion
- retry: reassurance plus next action
- timeout warning: urgency without sounding like a mistake
- timeout expired: clear closure

Avoid introducing long explanatory paragraphs into the renderer area.

## Testing Strategy

Use TDD for pure state derivation and motion mapping:

- add or update unit tests for feedback/action/timer state transitions first
- verify timeout warning and timeout-expired remain separate states
- verify retry and confirmed states produce different action roles and copy

Then validate UI contracts:

- lesson status card tags remain stable
- feedback banner tags remain stable
- renderer interaction tags remain stable
- number-pad submit/clear behavior remains unchanged except for presentation-state timing

## Non-Goals

This batch does not:

- change question correctness rules
- add combo systems or score multipliers
- add large bespoke animations or particle effects
- redesign reward flow
- redesign map or home surfaces

## Validation

Milestone validation for this batch:

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`

Optional stage-end device validation if the batch remains stable:

- `./gradlew.bat connectedDebugAndroidTest`
