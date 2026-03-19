# Reward Summary Enhancement Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Strengthen the reward and parent summary screens so they read as one coherent result-report system.

**Architecture:** Add a shared spotlight card, then use it to create a clearer top-level conclusion on the reward page and a stronger hero summary on the parent summary page. Keep behavior and routing unchanged.

**Tech Stack:** Kotlin, Jetpack Compose, existing reward and parent feature screens, Gradle unit/build validation

---

## Chunk 1: Shared Result Card

### Task 1: Add spotlight card

**Files:**
- Create: `app/src/main/java/com/mathisland/app/ui/components/SummarySpotlightCard.kt`

- [ ] **Step 1: Add shared spotlight card**

## Chunk 2: Reward And Parent Summary

### Task 2: Strengthen reward and parent summary screens

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/RewardOverlay.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryViewModel.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryTabletScreen.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/parent/ParentSummarySections.kt`
- Create: `app/src/main/java/com/mathisland/app/feature/parent/ParentSummaryHeroPanel.kt`

- [ ] **Step 1: Add reward spotlight**
- [ ] **Step 2: Add parent summary hero**
- [ ] **Step 3: Group supporting cards more clearly**

## Chunk 3: Docs And Validation

### Task 3: Save docs and run milestone checks

**Files:**
- Create: `docs/superpowers/specs/2026-03-19-reward-summary-enhancement-design.md`
- Create: `docs/superpowers/plans/2026-03-19-reward-summary-enhancement-addendum.md`
- Create: `docs/superpowers/plans/2026-03-19-reward-summary-enhancement-implementation.md`

- [ ] **Step 1: Save docs**
- [ ] **Step 2: Run `./gradlew.bat testDebugUnitTest`**
- [ ] **Step 3: Run `./gradlew.bat assembleDebug`**
