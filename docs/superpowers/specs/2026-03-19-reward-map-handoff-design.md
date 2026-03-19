# Reward Map Handoff Design

## Goal

Make the transition from reward overlay back to the map feel like one continuous result flow.

## Scope

Presentation-only:

- keep reward calculation unchanged
- keep map unlock logic unchanged
- keep CTA contracts unchanged

Improve:

- carry the reward page's next-step summary back onto the map
- add a short-lived return summary card on the map
- keep map focus and summary aligned to the same reward outcome

## Approach

Extend `MapFeedbackUiState` with summary copy derived from the reward result. Reuse the existing map feedback handoff and render one shared return-summary card on the map while the feedback is active.

## Validation

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`
