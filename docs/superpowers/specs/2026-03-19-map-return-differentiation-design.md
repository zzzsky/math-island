# Map Return Differentiation Design

## Goal

Differentiate the three main map-return flows after reward handoff without changing core game logic:

- new island unlock
- chest reward
- replay-first return after timed challenge

The map summary card and the island overlay should reflect the same return intent.

## Design

Introduce a lightweight `MapFeedbackKind` in the map UI layer:

- `NewIsland`
- `Chest`
- `Replay`
- `Progress`

Move reward-to-map-feedback mapping into a dedicated mapper so the return copy and kind selection are tested independently from route state.

The island overlay handoff card should render when:

- the feedback explicitly highlights the currently focused island, or
- the feedback has no explicit highlighted island and the overlay is showing the current recommended/selected island

This allows chest and replay flows to still surface a handoff card even though they do not unlock a new island.

## UI Effects

- map return summary card uses different accent tones by kind
- top feedback card uses different accent tones by kind
- island handoff card uses the same kind-aware accent

No route behavior, reward progression, or lesson logic changes.
