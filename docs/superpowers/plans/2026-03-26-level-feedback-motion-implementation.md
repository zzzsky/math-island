# Level Feedback Motion Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add the final layer of `correct / retry / timeout` motion and rhythm polish to the lesson screen without changing lesson correctness, progression, or reward routing.

**Architecture:** Keep lifecycle ownership in `LevelTabletScreen`, keep static timing constants in `LevelMotionTokens.kt`, and route all renderer-facing behavior through shared feedback/action mapping. Extend the existing feedback model rather than creating a second lesson state machine.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, existing level renderer architecture, JUnit, Compose UI tests

---

## Chunk 1: Timeout-Capable Feedback Model

### Task 1: Add timeout-expired feedback kind and timing tokens

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/level/LevelMotionTokens.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/AnswerFeedbackBanner.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererFeedbackState.kt`
- Test: `app/src/test/java/com/mathisland/app/feature/level/renderers/RendererFeedbackStateTest.kt`

- [ ] **Step 1: Write the failing timeout feedback-state test**

Add assertions that a new `TimeoutExpired` kind produces a distinct renderer feedback mapping and does not collapse into retry.

- [ ] **Step 2: Run the focused unit test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.renderers.RendererFeedbackStateTest"`

Expected: FAIL because `TimeoutExpired` is not defined or not mapped.

- [ ] **Step 3: Add the timeout feedback kind and motion tokens**

Implement:
- `LevelMotionTokens.kt` with:
  - `CorrectConfirmWindowMillis = 650L`
  - `RetryLockWindowMillis = 450L`
  - `RetryBannerWindowMillis = 1100L`
  - `TimeoutWarningFinalSeconds = 2`
- add `AnswerFeedbackKind.TimeoutExpired`
- update feedback banner tone mapping and shared feedback-state mapping

- [ ] **Step 4: Re-run the focused unit test**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.renderers.RendererFeedbackStateTest"`

Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/level/LevelMotionTokens.kt app/src/main/java/com/mathisland/app/feature/level/renderers/AnswerFeedbackBanner.kt app/src/main/java/com/mathisland/app/feature/level/renderers/RendererFeedbackState.kt app/src/test/java/com/mathisland/app/feature/level/renderers/RendererFeedbackStateTest.kt
git commit -m "feat: add timeout feedback model"
```

### Task 2: Make lesson status cards understand timeout-expired

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/LevelStatusCardState.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/LessonStatusTone.kt`
- Test: `app/src/test/java/com/mathisland/app/feature/level/LevelStatusCardStateTest.kt`

- [ ] **Step 1: Write the failing lesson-status timeout test**

Add assertions that timeout-expired maps to warning tone, subtitle `已超时`, and terminal body copy.

- [ ] **Step 2: Run the focused test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.LevelStatusCardStateTest"`

Expected: FAIL because timeout-expired is not handled.

- [ ] **Step 3: Implement timeout-expired mapping in lesson status**

Update:
- `attemptStatusCardStateFor(...)`
- `lessonStatusToneFor(...)` or equivalent tone mapping path

- [ ] **Step 4: Re-run the focused test**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.LevelStatusCardStateTest"`

Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/level/LevelStatusCardState.kt app/src/main/java/com/mathisland/app/feature/level/LessonStatusTone.kt app/src/test/java/com/mathisland/app/feature/level/LevelStatusCardStateTest.kt
git commit -m "feat: add timeout lesson status mapping"
```

## Chunk 2: Level Screen Lifecycle and Timeout Handoff

### Task 3: Make `LevelTabletScreen` own timeout-expired lifecycle

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/LevelViewModel.kt`
- Test: `app/src/test/java/com/mathisland/app/feature/level/LevelViewModelTest.kt`

- [ ] **Step 1: Write the failing lifecycle/state tests**

Add tests for:
- timed lessons still seed only `TimedWarning` from `LevelViewModel`
- timeout-expired is produced by screen-level orchestration, not the view-model

- [ ] **Step 2: Run the focused unit tests to verify they fail**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.LevelViewModelTest"`

Expected: FAIL because the spec boundary is not yet encoded.

- [ ] **Step 3: Implement timeout-expired lifecycle in `LevelTabletScreen`**

Implement:
- `onExpire` path cancels pending reset job
- force `inputEnabled = false`
- set timeout-expired feedback
- ignore late `onAnswer(...)` once remaining time reaches `0`
- clear transient feedback on question change and lesson change

Keep `LevelViewModel` limited to seeding `TimedWarning` and `flowHint`.

- [ ] **Step 4: Re-run the focused tests**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.LevelViewModelTest"`

Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt app/src/main/java/com/mathisland/app/feature/level/LevelViewModel.kt app/src/test/java/com/mathisland/app/feature/level/LevelViewModelTest.kt
git commit -m "feat: add lesson timeout lifecycle"
```

### Task 4: Tie timer-pressure thresholds to one deterministic rule

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/LevelStatusCardState.kt`
- Test: `app/src/test/java/com/mathisland/app/feature/level/LevelStatusCardStateTest.kt`

- [ ] **Step 1: Write the failing threshold tests**

Add tests covering:
- normal lesson timer transitions
- short timer where `floor(total * 0.5) <= 2` skips `time over half`

- [ ] **Step 2: Run the focused test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.LevelStatusCardStateTest"`

Expected: FAIL because the tie-break rule is not fully encoded.

- [ ] **Step 3: Implement the deterministic threshold rule**

Use:
- `halfThreshold = floor(totalSeconds * 0.5)`
- if `halfThreshold <= 2`, skip `time over half` and go directly to `final sprint`

- [ ] **Step 4: Re-run the focused test**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.LevelStatusCardStateTest"`

Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/level/LevelStatusCardState.kt app/src/test/java/com/mathisland/app/feature/level/LevelStatusCardStateTest.kt
git commit -m "feat: stabilize timeout threshold mapping"
```

## Chunk 3: Renderer Integration and UI Contracts

### Task 5: Update shared renderer glue to pass timeout-expired correctly

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/LevelAnswerPane.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererActionState.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererSupport.kt`
- Test: `app/src/test/java/com/mathisland.app/feature/level/renderers/RendererActionStateTest.kt`

- [ ] **Step 1: Write the failing renderer-action timeout test**

Add assertions that timeout-expired resolves to disabled/terminal action semantics and never reuses retry role/copy.

- [ ] **Step 2: Run the focused test to verify it fails**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.renderers.RendererActionStateTest"`

Expected: FAIL because timeout-expired is not mapped.

- [ ] **Step 3: Implement glue and action-state timeout mapping**

Update:
- `LevelAnswerPane.kt` to forward richer feedback/action state without adding its own timeout logic
- `RendererActionState.kt` to add timeout-expired handling
- `RendererSupport.kt` to render timeout-expired via shared lesson feedback path

- [ ] **Step 4: Re-run the focused test**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.renderers.RendererActionStateTest"`

Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/level/LevelAnswerPane.kt app/src/main/java/com/mathisland/app/feature/level/renderers/RendererActionState.kt app/src/main/java/com/mathisland/app/feature/level/renderers/RendererSupport.kt app/src/test/java/com/mathisland/app/feature/level/renderers/RendererActionStateTest.kt
git commit -m "feat: wire timeout lesson state through renderers"
```

### Task 6: Update number-pad and choice-side presentation for timeout-expired

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererFeedbackState.kt`
- Test: `app/src/test/java/com/mathisland/app/feature/level/renderers/RendererFeedbackStateTest.kt`
- Test: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`

- [ ] **Step 1: Write the failing timeout renderer tests**

Add tests for:
- choice-side timeout-expired falls back to passive context without retry/confirmed emphasis
- number-pad timeout-expired keeps last value visible and shows timeout terminal copy

- [ ] **Step 2: Run the focused tests to verify they fail**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.renderers.RendererFeedbackStateTest"`

Expected: FAIL because timeout-expired renderer behavior is not implemented.

- [ ] **Step 3: Implement timeout-expired renderer presentation**

Update:
- `optionFeedbackStateFor(...)`
- `numberPadDisplayStateFor(...)`
- `NumberPadQuestionPane.kt`

- [ ] **Step 4: Re-run the focused tests**

Run: `./gradlew.bat testDebugUnitTest --tests "com.mathisland.app.feature.level.renderers.RendererFeedbackStateTest"`

Expected: PASS

- [ ] **Step 5: Compile android tests for UI contract safety**

Run: `./gradlew.bat :app:compileDebugAndroidTestKotlin`

Expected: PASS

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt app/src/main/java/com/mathisland/app/feature/level/renderers/RendererFeedbackState.kt app/src/test/java/com/mathisland/app/feature/level/renderers/RendererFeedbackStateTest.kt app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt
git commit -m "feat: add timeout renderer presentation"
```

## Chunk 4: Full Validation and Docs Sync

### Task 7: Run milestone validation and finish the batch

**Files:**
- Modify: `docs/superpowers/specs/2026-03-25-level-feedback-motion-design.md`
- Create: `docs/superpowers/plans/2026-03-26-level-feedback-motion-implementation.md`

- [ ] **Step 1: Run unit tests**

Run: `./gradlew.bat testDebugUnitTest`

Expected: PASS

- [ ] **Step 2: Run build validation**

Run: `./gradlew.bat assembleDebug`

Expected: PASS

- [ ] **Step 3: Optionally run device validation if the environment is stable**

Run: `./gradlew.bat connectedDebugAndroidTest`

Expected: PASS, or document environment instability if device-side execution is unavailable.

- [ ] **Step 4: Stop emulator-related processes after the phase**

Run:

```powershell
Get-Process | Where-Object { $_.ProcessName -in @('adb','emulator','qemu-system-x86_64') } | Stop-Process -Force
```

- [ ] **Step 5: Commit the finished batch**

```bash
git add app docs
git commit -m "feat: polish lesson feedback motion"
```
