# Map Return Copy Alignment Design

## Goal

Align the map return language across:

- top return summary
- left island list handoff hints
- right island overlay handoff content

The system should feel like one result language instead of three separate phrasing systems.

## Design

Introduce a shared `MapReturnCopy` model keyed by `MapFeedbackKind`.

For each kind, define:

- summary label
- summary title
- summary body
- list badge
- list body

Initial aligned phrasing:

- `NewIsland`
  - summary label: `主线继续`
  - summary title: `新主线已就位`
- `Chest`
  - summary label: `先看收藏`
  - summary title: `宝箱收藏已更新`
- `Replay`
  - summary label: `先做回放`
  - summary title: `回放路线已就位`
- `Progress`
  - summary label: `继续推进`
  - summary title: `当前推荐已就位`

## Constraints

- no route changes
- no progression changes
- CTA routing rules stay intact
- only copy alignment and UI-state derivation change
