# Home and Parent Shell Polish Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Refactor the `Home` and `Parent` shell screens into smaller presentation modules and give them a final shell-level polish without changing contracts or behavior.

**Architecture:** Split `Home` and `Parent` screens into focused feature-local composables. Keep `Screen` files as orchestration layers that pass through existing state and callbacks unchanged.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, existing shared design tokens/components

---

### Task 1: Home Shell Modules

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/home/HomeHeroPanel.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/home/HomeActionColumn.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/home/HomeTabletScreen.kt`

- [ ] Extract the left-side hero/recommendation block into `HomeHeroPanel`.
- [ ] Extract the right-side actions into `HomeActionColumn`.
- [ ] Keep `home-continue-adventure`, `home-open-map`, `home-open-chest`, and `home-open-parent` unchanged.

### Task 2: Parent Shell Modules

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/parent/ParentGatePanel.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/parent/ParentSummarySections.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/parent/ParentGateScreen.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryTabletScreen.kt`

- [ ] Extract gate question content into `ParentGatePanel`.
- [ ] Extract summary cards/sections into `ParentSummarySections`.
- [ ] Keep `parent-answer-*` tags and completion/return behavior unchanged.

### Task 3: Integration Pass

**Files:**
- Modify: `README.md`
- Modify: `docs/testing.md`
- Modify: `app/src/main/java/com/mathisland/app/MathIslandApp.kt` (only if wiring cleanup is needed)

- [ ] Document the `Home` and `Parent` shell module split if references are now outdated.
- [ ] Keep this task limited to light follow-up cleanup.

### Task 4: Verify and Commit

**Files:**
- Modify: `docs/superpowers/specs/2026-03-19-home-parent-shell-polish-design.md`
- Modify: `docs/superpowers/plans/2026-03-19-home-parent-shell-polish-addendum.md`
- Modify: `docs/superpowers/plans/2026-03-19-home-parent-shell-polish-implementation.md`

- [ ] Run `./gradlew.bat testDebugUnitTest`
- [ ] Run `./gradlew.bat assembleDebug`
- [ ] Commit the phase after validation passes.
