# Typography System Polish Implementation Plan

**Goal:** Introduce a real typography foundation and semantic heading tokens, then migrate the most visible titles in the main learning flow and peripheral screens without changing labels, behavior, or stable tags.

**Architecture:** First define actual Material 3 typography in `Type.kt` and add semantic heading aliases in `TypographyTokens.kt`. Then migrate the main learning flow headings, followed by peripheral screen headings.

## Chunk 1: Typography Foundation

- [ ] Build real base typography in `app/src/main/java/com/mathisland/app/ui/theme/Type.kt`
- [ ] Add semantic heading tokens in `app/src/main/java/com/mathisland/app/ui/theme/TypographyTokens.kt`

## Chunk 2: Main Flow Heading Alignment

- [ ] Migrate `MapTabletScreen.kt`
- [ ] Migrate `IslandPanelHeader.kt`
- [ ] Migrate `LevelTabletScreen.kt`
- [ ] Migrate `RewardOverlay.kt`

## Chunk 3: Peripheral Heading Alignment

- [ ] Migrate `HomeTabletScreen.kt`
- [ ] Migrate `ChestTabletScreen.kt`
- [ ] Migrate `ParentSummaryTabletScreen.kt`

## Chunk 4: Final Verification

- [ ] Run `./gradlew.bat testDebugUnitTest`
- [ ] Run `./gradlew.bat connectedDebugAndroidTest`
- [ ] Run `./gradlew.bat assembleDebug`
- [ ] Commit milestone and stop `emulator / qemu / adb`
