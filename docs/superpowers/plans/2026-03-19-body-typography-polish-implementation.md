# Body Typography Polish Implementation Plan

**Goal:** Extend the shared typography layer to cover body and supporting copy, then migrate card copy first and page supporting copy second without changing labels, behavior, or stable tags.

**Architecture:** First expand `TypographyTokens` and add `TextToneTokens`. Then migrate reusable card components and reward/island copy. Finally migrate supporting copy on the main pages.

## Chunk 1: Foundation

- [ ] Extend `app/src/main/java/com/mathisland/app/ui/theme/TypographyTokens.kt`
- [ ] Add `app/src/main/java/com/mathisland/app/ui/theme/TextToneTokens.kt`

## Chunk 2: Card Copy Alignment

- [ ] Migrate `app/src/main/java/com/mathisland/app/ui/components/TabletInfoCard.kt`
- [ ] Migrate `app/src/main/java/com/mathisland/app/ui/components/TabletActionCard.kt`
- [ ] Migrate `app/src/main/java/com/mathisland/app/feature/island/IslandStoryCard.kt`
- [ ] Migrate relevant copy in `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`

## Chunk 3: Page Supporting Copy Alignment

- [ ] Migrate `HomeTabletScreen.kt`
- [ ] Migrate `LevelTabletScreen.kt`
- [ ] Migrate `ChestTabletScreen.kt`
- [ ] Migrate `MapProgressFeedback.kt`

## Chunk 4: Final Verification

- [ ] Run `./gradlew.bat testDebugUnitTest`
- [ ] Run `./gradlew.bat assembleDebug`
- [ ] Commit milestone and confirm emulator-related processes are not running
