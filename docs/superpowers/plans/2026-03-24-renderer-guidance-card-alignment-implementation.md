# Renderer Guidance Card Alignment Implementation

## Implemented

- Added `RendererGuidanceCard.kt`
- Updated `RendererSupport` helper block to use it
- Updated `RendererSectionHeader` to wrap it
- Updated `NumberPadQuestionPane` helper block and supporting text rhythm
- Added androidTest contract for shared guidance cards in `LevelAnswerPaneTest`

## Verification

- `./gradlew.bat :app:compileDebugAndroidTestKotlin`
- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`
