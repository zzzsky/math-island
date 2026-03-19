# Label and Caption Typography Polish Design

## Goal

Extend the shared typography system one level deeper so small supporting labels, captions, and renderer helper copy stop relying on scattered `labelLarge`, `bodyMedium`, and manual alpha choices.

## Scope

This batch is behavior-preserving and limited to text semantics:

- Add two smaller shared text roles:
  - `Caption`
  - `MicroLabel`
- Apply them to:
  - stat-tile labels
  - island list and island panel progress/supporting lines
  - parent gate helper copy
  - renderer helper and number-pad guidance copy

Out of scope:

- map canvas decorative node text
- painter-only visual labels
- action/button copy
- layout or spacing changes

## Approach

Keep `Type.kt` as the source of raw Material typography values and extend it with smaller text sizes. Keep `TypographyTokens.kt` as the semantic layer used by feature code.

Use:

- `Caption` for compact supporting text inside cards and renderer helper copy
- `MicroLabel` for tiny metadata labels such as stat-tile headings

Reuse existing `TextToneTokens` instead of introducing another tone system.

## Files

- Modify: `app/src/main/java/com/mathisland/app/ui/theme/Type.kt`
- Modify: `app/src/main/java/com/mathisland/app/ui/theme/TypographyTokens.kt`
- Modify: `app/src/main/java/com/mathisland/app/ui/components/TabletStatTile.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/parent/ParentGateScreen.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapIslandListCard.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandPanelHeader.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererSupport.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt`

## Validation

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`

