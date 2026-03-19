# Chest Shell Polish Design

## Goal

Give the chest screen the same shell-level polish pass already applied to `Home` and `Parent`, without changing collection behavior or navigation.

## Scope

Presentation-only:

- keep `ChestViewModel` and `ChestUiState` unchanged
- keep `chest-open-map` and existing back actions unchanged
- keep sticker content unchanged

Refactor the page into small presentation modules:

- `ChestHeaderPanel`
- `ChestEmptyStateCard`
- `StickerCollectionGrid`
- `StickerCard`

## Validation

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`

