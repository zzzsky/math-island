# Return Feedback Motion Polish Design

## Goal

Strengthen the result-feedback choreography from reward settlement back to the map so the user sees one continuous reveal sequence instead of several unrelated static cards.

## Scope

- Reward overlay staged reveal
- Map return summary staged reveal
- Island handoff staged reveal
- Shared timing/tone rules for `NewIsland / Chest / Replay / Progress`

## Non-Goals

- No gameplay logic changes
- No destination/routing changes
- No new reward or map states

## Design

### Shared motion model

Extend the existing map/reward motion spec so every feedback kind defines:

- accent color and badge variant
- card scale boost
- section reveal thresholds for:
  - hero/summary
  - spotlight
  - detail
  - supporting highlights
  - CTA/trailing actions

This keeps reward, map summary, and island handoff on one timing language.

### Reward overlay

The reward page should reveal in five layers:

1. header summary
2. stat row
3. spotlight result card
4. reward/progress highlight cards
5. next-step cards and CTA row

The page remains scrollable and keeps all existing tags/contracts.

### Map return summary

The map return zone should feel like the reward page has handed off real state:

- kind chip appears first
- summary card follows
- detail card follows
- stars/chest pills remain trailing emphasis

### Island handoff

The right-side island handoff should mirror the same staging:

- kind chip
- spotlight handoff card
- detail card

This keeps the reward page, map summary, and island overlay visually coherent.

## Testing

- Unit: motion spec ordering and kind mapping
- Compose/device:
  - reward overlay contract still visible
  - map feedback contract still visible
  - island handoff contract still visible
