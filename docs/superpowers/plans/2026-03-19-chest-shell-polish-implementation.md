# Chest Shell Polish Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Refactor the chest screen into clear presentation modules while preserving its current behavior and public UI contracts.

**Architecture:** Extract the chest screen into small, feature-local composables. Keep the top-level screen as a simple orchestration layer that switches between empty and populated states.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, existing action/surface/typography tokens

---

### Task 1: Extract Header

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/chest/ChestHeaderPanel.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/chest/ChestTabletScreen.kt`

- [ ] Move the title, summary text, and top actions into `ChestHeaderPanel`.
- [ ] Keep `chest-open-map` unchanged.

### Task 2: Extract Empty and Collection States

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/chest/ChestEmptyStateCard.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/chest/StickerCollectionGrid.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/chest/StickerCard.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/chest/ChestTabletScreen.kt`

- [ ] Move the empty-state card into `ChestEmptyStateCard`.
- [ ] Move the populated collection layout into `StickerCollectionGrid`.
- [ ] Move each sticker tile into `StickerCard`.
- [ ] Keep existing strings and behavior unchanged.

### Task 3: Verify and Commit

**Files:**
- Modify: `docs/superpowers/specs/2026-03-19-chest-shell-polish-design.md`
- Modify: `docs/superpowers/plans/2026-03-19-chest-shell-polish-addendum.md`
- Modify: `docs/superpowers/plans/2026-03-19-chest-shell-polish-implementation.md`

- [ ] Run `./gradlew.bat testDebugUnitTest`
- [ ] Run `./gradlew.bat assembleDebug`
- [ ] Commit the phase after validation passes.

