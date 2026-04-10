# Matching Multi-Round Implementation

1. Extend `Question` with `MatchingRound` and `matchingRounds`.
2. Upgrade `MatchingAnswerState` to track assignments by round and support round advancement.
3. Update `MatchingQuestionPane` to render only the active round and switch CTA from next-round to submit.
4. Add one semantic two-round lesson in `classification-island`.
5. Add focused unit coverage for round encoding and advancement.
6. Add focused android coverage for pane, answer pane, and flow.
7. Run:
   - `testDebugUnitTest`
   - `assembleDebug`
   - focused emulator regression for matching pane, answer pane, and tablet flow
8. Clear `adb / emulator / qemu` after verification.
