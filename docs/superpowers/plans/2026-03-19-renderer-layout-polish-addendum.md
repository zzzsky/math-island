# Renderer Layout Polish Addendum

## Purpose

Follow renderer feedback and action-state polish with a final layout pass that makes all lesson renderers feel like one system.

## Work Items

- add a shared renderer panel stack
- unify context / feedback / affordance / action ordering
- align number-pad layout with the same section hierarchy
- keep existing contracts and behavior stable

## Acceptance

- helper, feedback, affordance, and action sections appear in a stable order
- number-pad and choice-like renderers feel structurally aligned
- no routing, correctness, or tag regressions
