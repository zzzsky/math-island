# Map Return Experience Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Align map return summary, overlay focus, and island CTA so the reward handoff feels like one connected next-step experience.

**Architecture:** Keep reward and map behavior unchanged while extending island overlay state with handoff copy sourced from active map feedback. The overlay renders a shared handoff card when the highlighted island is active.

**Tech Stack:** Kotlin, Jetpack Compose, existing map and island feature screens, Gradle unit/build validation

---

## Chunk 1: Island Handoff State

### Task 1: Extend island view state for handoff content

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapViewModel.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandViewModel.kt`

- [ ] **Step 1: Add optional handoff fields**
- [ ] **Step 2: Populate them from active map feedback**

## Chunk 2: Overlay Experience

### Task 2: Render overlay handoff and align focus

**Files:**
- Create: `app/src/main/java/com/mathisland/app/feature/island/IslandHandoffCard.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandOverlaySheet.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapTabletScreen.kt`

- [ ] **Step 1: Add shared handoff card**
- [ ] **Step 2: Render it in the overlay**
- [ ] **Step 3: Keep overlay focus aligned with active highlighted island**

## Chunk 3: Docs And Validation

### Task 3: Save docs and run milestone checks

**Files:**
- Create: `docs/superpowers/specs/2026-03-19-map-return-experience-design.md`
- Create: `docs/superpowers/plans/2026-03-19-map-return-experience-addendum.md`
- Create: `docs/superpowers/plans/2026-03-19-map-return-experience-implementation.md`

- [ ] **Step 1: Save docs**
- [ ] **Step 2: Run `./gradlew.bat testDebugUnitTest`**
- [ ] **Step 3: Run `./gradlew.bat assembleDebug`**
