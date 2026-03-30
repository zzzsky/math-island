# Return Stage Final Copy Implementation

1. Update shared copy in `MapReturnCopy`.
2. Keep reward, map, island, and list consumers on shared copy only.
3. Refresh exact-string unit tests for reward/island/map view models.
4. Run:
   - `./gradlew.bat testDebugUnitTest`
   - `./gradlew.bat assembleDebug`
   - focused emulator regression for reward/map/island surfaces
5. Commit and stop `adb / emulator / qemu`.
