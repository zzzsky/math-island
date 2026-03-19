# Level Feedback Polish Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add clearer in-lesson answer feedback across renderers without changing lesson or reward logic.

**Architecture:** Model feedback as a presentation-layer state and route it through `LevelTabletScreen` plus shared renderer support. Keep the gameplay/controller layer untouched.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, existing level renderer architecture

---

### Task 1: Add Feedback UI Model

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/LevelViewModel.kt`

- [ ] Introduce a lightweight `AnswerFeedbackUiState`.
- [ ] Expose the derived feedback state needed by the level screen.

### Task 2: Add Shared Feedback Surface

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/AnswerFeedbackBanner.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`

- [ ] Render a consistent feedback banner outside the renderer body.
- [ ] Keep timed-lesson warning compatible with the existing timer flow.

### Task 3: Apply Feedback to Renderers

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererSupport.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt`

- [ ] Let shared renderers show feedback-aware affordances.
- [ ] Let number-pad respect the same feedback state.
- [ ] Preserve answer tags and behavior.

### Task 4: Verify and Commit

**Files:**
- Modify: `docs/superpowers/specs/2026-03-19-level-feedback-polish-design.md`
- Modify: `docs/superpowers/plans/2026-03-19-level-feedback-polish-addendum.md`
- Modify: `docs/superpowers/plans/2026-03-19-level-feedback-polish-implementation.md`

- [ ] Run `./gradlew.bat testDebugUnitTest`
- [ ] Run `./gradlew.bat assembleDebug`
- [ ] Commit the phase after validation passes.
