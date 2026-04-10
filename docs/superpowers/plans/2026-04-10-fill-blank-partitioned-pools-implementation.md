# Fill Blank Partitioned Pools Implementation

1. Extend `FillBlankAnswerState` to track selected slot as well as selected option.
2. Support assignment in either order: option-first or slot-first.
3. Split `FillBlankQuestionPane` option rendering into `number` and `unit` pools.
4. Add pool highlight and slot highlight logic.
5. Add `measure-fill-06` to measurement content.
6. Update unit, pane, answer-pane, and flow tests.
7. Run:
   - `testDebugUnitTest`
   - `assembleDebug`
   - `:app:compileDebugAndroidTestKotlin`
   - focused emulator regression for fill-blank pane, answer pane, and tablet flow
8. Clear `adb / emulator / qemu`.
