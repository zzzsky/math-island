# Reward Next-Step Copy Alignment Addendum

## Scope

This addendum makes the reward screen and map return flow speak the same next-step language.

## Tasks

1. Route `RewardViewModel` next-step fields through `MapReturnCopy`.
2. Lock the new wording with unit tests.

## Acceptance

- reward next-step copy matches the map return copy for the same return kind
- unit tests cover new island, chest, and replay flows
