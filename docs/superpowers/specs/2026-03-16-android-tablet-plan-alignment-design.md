# Android Tablet Plan Alignment Design

## Goal

Close the remaining gap between the current Android tablet codebase and the file boundaries promised in the implementation plans, without changing product behavior.

This batch is intentionally architecture-only:

- no new gameplay
- no storage migration
- no UI flow redesign
- no test tag contract changes

## Scope

The current app is functionally stable, but three areas are still misaligned with the plan:

1. Theme tokens and reusable visual primitives are still partially embedded in app/feature files.
2. The lesson and reward surfaces still live under `feature/lesson` and `feature/reward`, while the plan assumes a `feature/level` boundary and a dedicated reward overlay surface.
3. Some map and panel primitives exist as active behavior but are not yet expressed through the shared component names described in the plan.

## Recommended Approach

Use a behavior-preserving alignment batch split into three independent worktrees:

1. `tablet-ui-foundation`
2. `level-surface-alignment`
3. `map-component-alignment`

This keeps merge conflicts low and lets each line move one file-boundary concern at a time.

## Worktree A: Tablet UI Foundation

Target:

- add `ui/theme/*` files for shared color, type, and theme tokens
- add `ui/components/*` for shared tablet primitives
- remove residual visual token ownership from `MathIslandApp.kt`

Acceptance:

- theme and shared primitives move out of app shell / feature-common
- no visual behavior changes
- existing screen tests remain green

## Worktree B: Level Surface Alignment

Target:

- align `feature/lesson/*` toward the planned `feature/level/*` boundary
- introduce a dedicated `RewardOverlay` surface
- keep current lesson state, renderer routing, challenge flow, and reward behavior unchanged

Acceptance:

- lesson and reward presentation code no longer looks like an accidental split across unrelated feature folders
- existing instrumentation flow remains stable
- current tags and callbacks are preserved

## Worktree C: Map Component Alignment

Target:

- align current map primitives with planned shared component names:
  - `IslandMapCanvas`
  - `StoryPanelCard`
  - `WoodButton`
- keep map interactions, island overlay, and feedback behavior unchanged

Acceptance:

- map feature files become thinner wrappers around shared components
- overlay and map tests keep using the same flow contracts

## Merge Order

1. `tablet-ui-foundation`
2. `map-component-alignment`
3. `level-surface-alignment`

Reason:

- theme/components first gives later branches stable imports
- map component alignment is lower-risk than level/reward surface movement
- level alignment lands last because it touches the most test-visible code

## Verification

Branch-level:

- focused unit tests for moved view models or helpers
- focused instrumentation for the touched feature surface

Post-merge on `main`:

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat connectedDebugAndroidTest`
- `./gradlew.bat assembleDebug`

## Stable Contracts

The following contracts must not change in this batch:

- `panel-start-<lessonId>` remains the primary map-to-lesson end-to-end contract
- challenge sprint / replay / grading behavior remains unchanged
- home -> map -> lesson -> reward -> map remains unchanged
- parent and chest flows remain unchanged
