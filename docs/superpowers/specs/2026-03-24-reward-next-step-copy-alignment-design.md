# Reward Next-Step Copy Alignment Design

## Goal

Align the reward page's next-step copy with the shared map return copy so the user reads one continuous result language from reward screen back to the map.

## Design

Reuse `MapReturnCopy` inside `RewardViewModel` instead of maintaining a separate set of next-step strings.

Mapping:

- `timedOut` -> `MapFeedbackKind.Replay`
- `newIslandTitle != null` -> `MapFeedbackKind.NewIsland`
- `newStickerName != null` -> `MapFeedbackKind.Chest`
- fallback -> `MapFeedbackKind.Progress`

The reward page should use:

- `continueLabel = summaryLabel`
- `nextStepTitle = summaryTitle`
- `nextStepBody = summaryBody`

## Constraints

- no reward logic changes
- no CTA routing changes
- only wording alignment changes
