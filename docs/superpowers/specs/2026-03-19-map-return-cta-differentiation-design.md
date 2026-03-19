# Map Return CTA Differentiation Design

## Goal

Make the right-side island overlay respond differently for the three main return flows:

- new island unlock
- chest reward
- replay-first return

The map handoff should not stop at summary copy; it should also influence the overlay's main CTA and highlighted lesson recommendation.

## Design

Extend `IslandUiState` with handoff-aware primary action fields:

- `primaryLessonId`
- `primaryActionLabel`
- `primaryActionMode`

`IslandViewModel` becomes responsible for selecting the preferred primary lesson:

- default flow: first enabled unfinished lesson
- replay flow: prefer a review/replay lesson if present
- chest flow: preserve lesson recommendation, but switch main CTA mode to opening the chest

The overlay keeps its existing structure but routes the main CTA based on `primaryActionMode`.

Lesson cards also reflect the handoff context:

- new island -> mainline recommendation
- replay -> replay-first recommendation
- chest -> continue-after-collection wording

## Constraints

- no reward logic changes
- no lesson progression changes
- existing tags remain stable
- map -> overlay -> lesson/chest flow only becomes more explicit
