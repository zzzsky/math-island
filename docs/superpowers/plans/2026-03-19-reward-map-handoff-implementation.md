# Reward Map Handoff Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Carry reward-page next-step information back to the map so the return flow feels continuous.

**Architecture:** Extend `MapFeedbackUiState` with summary copy derived from reward results, then render a shared return-summary card on the map while feedback is active.

**Tech Stack:** Kotlin, Jetpack Compose, existing reward and map feedback pipeline, Gradle unit/build validation

---

## Chunk 1: Feedback Model

### Task 1: Extend reward-to-map feedback payload

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/navigation/TabletRouteState.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapProgressFeedback.kt`

- [ ] **Step 1: Add summary fields to map feedback**
- [ ] **Step 2: Derive summary title/body from reward state**

## Chunk 2: Map Presentation

### Task 2: Render map return summary

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`

- [ ] **Step 1: Render return summary card while map feedback is active**
- [ ] **Step 2: Keep it aligned with highlighted island and chest pulse**

## Chunk 3: Docs And Validation

### Task 3: Save docs and run milestone checks

**Files:**
- Create: `docs/superpowers/specs/2026-03-19-reward-map-handoff-design.md`
- Create: `docs/superpowers/plans/2026-03-19-reward-map-handoff-addendum.md`
- Create: `docs/superpowers/plans/2026-03-19-reward-map-handoff-implementation.md`

- [ ] **Step 1: Save docs**
- [ ] **Step 2: Run `./gradlew.bat testDebugUnitTest`**
- [ ] **Step 3: Run `./gradlew.bat assembleDebug`**
