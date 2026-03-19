# Label and Caption Typography Polish Implementation Plan

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Extend the shared typography system with compact label/caption roles and migrate supporting copy that still uses ad hoc small text styles.

**Architecture:** Keep `Type.kt` as the raw Material typography source and `TypographyTokens.kt` as the semantic layer. Update only text semantics and tone usage; do not change behavior or contracts.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, existing design-token layer

---

### Task 1: Add Smaller Typography Tokens

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/ui/theme/Type.kt`
- Modify: `app/src/main/java/com/mathisland/app/ui/theme/TypographyTokens.kt`

- [ ] Add compact `bodySmall` and `labelMedium` definitions to `MathIslandTypography`.
- [ ] Expose `Caption` and `MicroLabel` through `TypographyTokens`.
- [ ] Keep existing token names unchanged.

### Task 2: Migrate Card and Panel Support Text

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/ui/components/TabletStatTile.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/parent/ParentGateScreen.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/map/MapIslandListCard.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/island/IslandPanelHeader.kt`

- [ ] Move compact support labels to `MicroLabel` or `Caption`.
- [ ] Replace scattered alpha-based text colors with `TextToneTokens` where appropriate.
- [ ] Keep all strings and tags unchanged.

### Task 3: Migrate Renderer Helper Copy

**Files:**
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/RendererSupport.kt`
- Modify: `app/src/main/java/com/mathisland/app/feature/level/renderers/NumberPadQuestionPane.kt`

- [ ] Move helper copy to `Caption`.
- [ ] Use shared tone tokens instead of inline alpha text colors.
- [ ] Leave answer controls and behavior untouched.

### Task 4: Verify and Commit

**Files:**
- Modify: `docs/superpowers/specs/2026-03-19-label-caption-typography-polish-design.md`
- Modify: `docs/superpowers/plans/2026-03-19-label-caption-typography-polish-addendum.md`
- Modify: `docs/superpowers/plans/2026-03-19-label-caption-typography-polish-implementation.md`

- [ ] Run `./gradlew.bat testDebugUnitTest`
- [ ] Run `./gradlew.bat assembleDebug`
- [ ] Commit the phase once verification passes.

