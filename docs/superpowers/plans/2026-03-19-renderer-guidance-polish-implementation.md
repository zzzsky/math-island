# Renderer Guidance Polish Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Give every lesson action block a shared heading and short guidance copy driven by renderer action state.

**Architecture:** Keep guidance text in `RendererActionState`, then render a shared `RendererSectionHeader` above action controls in `RendererSupport` and `NumberPadQuestionPane`.

**Tech Stack:** Kotlin, Jetpack Compose, existing renderer components, Gradle unit/build validation

---

## Chunk 1: Guidance Copy

### Task 1: Extend renderer action state guidance

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererActionState.kt`
- Modify: `app/src/test/java/com/mathisland/app/feature/level/renderers/RendererActionStateTest.kt`

- [ ] **Step 1: Add failing guidance-copy assertions**
- [ ] **Step 2: Implement state-driven title/body**
- [ ] **Step 3: Run focused test**

## Chunk 2: Shared Action Header

### Task 2: Add shared action-area header

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererSectionHeader.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererSupport.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt`

- [ ] **Step 1: Add shared header component**
- [ ] **Step 2: Reuse it in choice-like renderers**
- [ ] **Step 3: Reuse it in number-pad**

## Chunk 3: Docs And Validation

### Task 3: Save docs and run milestone checks

**Files:**
- Create: `docs/superpowers/specs/2026-03-19-renderer-guidance-polish-design.md`
- Create: `docs/superpowers/plans/2026-03-19-renderer-guidance-polish-addendum.md`
- Create: `docs/superpowers/plans/2026-03-19-renderer-guidance-polish-implementation.md`

- [ ] **Step 1: Save docs**
- [ ] **Step 2: Run `./gradlew.bat testDebugUnitTest`**
- [ ] **Step 3: Run `./gradlew.bat assembleDebug`**
