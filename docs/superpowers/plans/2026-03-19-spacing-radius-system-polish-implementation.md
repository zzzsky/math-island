# Spacing & Radius System Polish Implementation Plan

**Goal:** Introduce lightweight spacing/radius theme tokens and migrate the most visible page and card containers in the main flow and peripheral screens without changing labels, behavior, or stable test tags.

**Architecture:** First add shared spacing and radius tokens in `ui/theme/`. Then wire the shared card/chip/info components to them. Finally migrate the main flow pages and peripheral pages to the new tokens.

## Chunk 1: Token Foundation

- [ ] Add `app/src/main/java/com/mathisland/app/ui/theme/SpacingTokens.kt`
- [ ] Add `app/src/main/java/com/mathisland/app/ui/theme/RadiusTokens.kt`
- [ ] Apply tokens to shared components where it reduces repeated hardcoding

## Chunk 2: Main Flow Alignment

- [ ] Migrate `MapTabletScreen.kt`
- [ ] Migrate `IslandOverlaySheet.kt`
- [ ] Migrate `LevelTabletScreen.kt`
- [ ] Migrate `RewardOverlay.kt`

## Chunk 3: Peripheral Alignment

- [ ] Migrate `HomeTabletScreen.kt`
- [ ] Migrate `ChestTabletScreen.kt`
- [ ] Migrate `ParentSummaryTabletScreen.kt`

## Chunk 4: Final Verification

- [ ] Run `./gradlew.bat testDebugUnitTest`
- [ ] Run `./gradlew.bat assembleDebug`
- [ ] Commit milestone and confirm emulator-related processes are not running
