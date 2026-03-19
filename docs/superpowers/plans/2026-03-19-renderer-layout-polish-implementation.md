# Renderer Layout Polish Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Unify lesson renderer panel layout so helper, feedback, affordance, and action sections share one stable reading order.

**Architecture:** Add a shared renderer panel stack and keep renderer-specific content inside clearly separated blocks. `RendererSupport` and `NumberPadQuestionPane` both adopt the same section sequencing and spacing tokens.

**Tech Stack:** Kotlin, Jetpack Compose, existing renderer components, Gradle unit/build validation

---

## Chunk 1: Shared Layout Primitive

### Task 1: Add renderer panel stack and tokens

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererPanelStack.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererTokens.kt`

- [ ] **Step 1: Add shared section stack**
- [ ] **Step 2: Add section spacing tokens**
- [ ] **Step 3: Keep default renderer tags unchanged**

## Chunk 2: Apply To Shared Renderers

### Task 2: Align choice-like renderers and number-pad

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererSupport.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt`

- [ ] **Step 1: Use shared stack in `RendererSupport`**
- [ ] **Step 2: Use shared stack in `NumberPadQuestionPane`**
- [ ] **Step 3: Keep behavior and tags stable**

## Chunk 3: Docs And Validation

### Task 3: Save docs and run milestone checks

**Files:**
- Create: `docs/superpowers/specs/2026-03-19-renderer-layout-polish-design.md`
- Create: `docs/superpowers/plans/2026-03-19-renderer-layout-polish-addendum.md`
- Create: `docs/superpowers/plans/2026-03-19-renderer-layout-polish-implementation.md`

- [ ] **Step 1: Save docs**
- [ ] **Step 2: Run `./gradlew.bat testDebugUnitTest`**
- [ ] **Step 3: Run `./gradlew.bat assembleDebug`**
