# Renderer Surface Polish Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Align the lesson renderer panes to the same card, tone, and action system already used by the surrounding tablet UI.

**Architecture:** Introduce a renderer-local token file and refactor shared renderer scaffolding first. Then migrate specialized affordance panes one by one without changing renderer tags or answer semantics.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, shared action/surface/typography tokens

---

### Task 1: Add Renderer Tokens

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererTokens.kt`

- [ ] Define helper, option, and affordance surfaces.
- [ ] Define stable accent/text colors for renderer panes.

### Task 2: Migrate Shared Renderer Support

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererSupport.kt`

- [ ] Replace raw cards with shared story-panel surfaces.
- [ ] Replace raw answer buttons with shared action buttons.
- [ ] Keep answer tags and button text unchanged.

### Task 3: Migrate Specialized Renderer Panes

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/GroupQuestionPane.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/SortQuestionPane.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RulerQuestionPane.kt`

- [ ] Align affordance cards and helper copy to the new renderer surfaces.
- [ ] Keep test tags and input behavior intact.

### Task 4: Verify and Commit

**Files:**
- Modify: `docs/superpowers/specs/2026-03-19-renderer-surface-polish-design.md`
- Modify: `docs/superpowers/plans/2026-03-19-renderer-surface-polish-addendum.md`
- Modify: `docs/superpowers/plans/2026-03-19-renderer-surface-polish-implementation.md`

- [ ] Run `./gradlew.bat testDebugUnitTest`
- [ ] Run `./gradlew.bat assembleDebug`
- [ ] Commit the phase after validation passes.

