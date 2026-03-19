# Renderer Action State Polish Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Refine lesson renderer button states so retry, confirmed, and locked moments read clearly without changing lesson logic.

**Architecture:** Keep all behavior in the presentation layer. `LevelTabletScreen` derives a shared `RendererActionState`, and renderer UIs consume that state through shared props instead of creating local semantics independently.

**Tech Stack:** Kotlin, Jetpack Compose, existing level renderer components, Gradle unit/build validation

---

## Chunk 1: State Model And Red Tests

### Task 1: Add failing tests for renderer action state behavior

**Files:**
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelTabletScreenTest.kt`
- Modify: `app/src/androidTest/java/com/mathisland/app/feature/level/LevelAnswerPaneTest.kt`

- [ ] **Step 1: Write failing tests**
- [ ] **Step 2: Run focused tests to verify they fail**
- [ ] **Step 3: Implement minimal action-state model usage**
- [ ] **Step 4: Re-run focused tests**
- [ ] **Step 5: Commit**

## Chunk 2: Shared Renderer Integration

### Task 2: Integrate renderer action state into shared answer panes

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererActionState.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/LevelTabletScreen.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererSupport.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt`

- [ ] **Step 1: Add state model**
- [ ] **Step 2: Wire state derivation from level feedback**
- [ ] **Step 3: Apply state to choice-like renderers and number-pad**
- [ ] **Step 4: Run `./gradlew.bat testDebugUnitTest`**
- [ ] **Step 5: Run `./gradlew.bat assembleDebug`**

## Chunk 3: Docs Sync

### Task 3: Keep current polish docs aligned

**Files:**
- Create: `docs/superpowers/specs/2026-03-19-renderer-action-state-polish-design.md`
- Create: `docs/superpowers/plans/2026-03-19-renderer-action-state-polish-addendum.md`
- Create: `docs/superpowers/plans/2026-03-19-renderer-action-state-polish-implementation.md`

- [ ] **Step 1: Save docs**
- [ ] **Step 2: Include them in milestone commit**
