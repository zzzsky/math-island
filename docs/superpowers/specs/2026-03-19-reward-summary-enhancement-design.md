# Reward Summary Enhancement Design

## Goal

Make the reward page and parent summary page feel like one coherent result-report system.

## Scope

Presentation-only:

- keep reward logic unchanged
- keep parent summary data unchanged
- keep main CTA contracts unchanged

Improve:

- stronger result spotlight on the reward page
- a clearer hero summary and grouped insights on the parent summary page
- shared result-card language between both screens

## Approach

Introduce a shared spotlight card component for result pages. Use it to give reward and parent summary screens a stronger top-level conclusion, then reorganize supporting information into clearer stat tiles and info cards.

## Validation

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`
