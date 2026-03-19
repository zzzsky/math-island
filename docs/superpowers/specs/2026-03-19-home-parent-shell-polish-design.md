# Home and Parent Shell Polish Design

## Goal

Give the `Home` and `Parent` outer screens one final structure-and-copy polish pass so they read as clearer modules instead of large, page-local compositions.

## Scope

This batch is behavior-preserving:

- keep existing routes and screen ownership
- keep button tags and answer tags unchanged
- keep business logic and view-model inputs unchanged

Focus only on shell-level presentation and composition:

- `Home`
  - clearer hero/recommendation module
  - clearer action-entry module
- `Parent`
  - clearer gate question module
  - clearer summary sections module

## Approach

Split the page-local compositions into small feature-local subcomponents:

- `HomeHeroPanel`
- `HomeActionColumn`
- `ParentGatePanel`
- `ParentSummarySections`

Keep all existing strings unless they are already computed externally. The main value of this batch is modularization, spacing cleanup, and clearer visual grouping, not new content.

## Parallelization

Three isolated lines:

1. `home-shell-polish`
2. `parent-shell-polish`
3. `integration-pass`

`integration-pass` only handles the final light wiring/docs sweep after the two UI lines land.

## Validation

- `./gradlew.bat testDebugUnitTest`
- `./gradlew.bat assembleDebug`

