# Map Return Experience Design

## Goal

Make reward handoff feel complete by aligning the map return summary, selected island, and island panel CTA around the same next-step message.

## Scope

Presentation-only:

- keep reward calculation unchanged
- keep map unlock logic unchanged
- keep lesson CTA contracts unchanged

Improve:

- island panel shows the same handoff summary as the map return flow
- highlighted island and overlay content stay aligned
- map return experience reads like one continuous next-step story

## Approach

Extend `IslandUiState` with optional handoff summary fields sourced from `MapFeedbackUiState`. When the highlighted island matches the selected overlay island, show a handoff card above the story and keep the overlay synced to the same focus target.

## Validation

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`
