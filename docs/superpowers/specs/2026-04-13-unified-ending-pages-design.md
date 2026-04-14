# Unified Ending Pages Design

## Goal

Unify the lesson-ending experience across the child reward page and the parent summary page so they feel like one visual system with clearly different voices.

## Product Direction

The system should prioritize:

- one shared visual language
- different narrative tones for child and parent

Voice split:

- child: celebration + next adventure
- parent: learning report + recommended action

## Scope

This batch covers the ending surfaces for:

- child reward
- parent summary

Primary implementation targets are the reward overlay and the parent summary screen plus their supporting state/view-model/test files.

This batch does not:

- change question rendering behavior
- add process replay into the child reward page
- redesign map, home, or chest as part of the same batch

## Shared System Shape

Both pages should use the same four-layer structure:

1. Hero
2. Core summary
3. Secondary details
4. Next action

This creates a recognizable ending-page system instead of two unrelated screens.

## Child Reward Page

### Intent

The child page should feel like a lightweight finish line:

- celebrate the result
- make the reward legible
- point toward the next adventure

### Information Hierarchy

- Hero: lesson title, completion state, one short celebratory line
- Core summary: the single most important result of the run
- Secondary details: stars, correctness, unlocks, stickers, challenge-specific status
- Next action: the strongest action area on the page

### Boundaries

- Do not add step-by-step replay here
- Keep result reading fast
- Avoid turning the reward page into a report

## Parent Summary Page

### Intent

The parent page should feel like the same system translated into a report voice:

- summarize what was learned
- call out weak spots
- recommend the next action

### Information Hierarchy

- Hero: today's learning completion snapshot
- Core summary: the main report card for today
- Secondary details: learned topics, weak topics, streak, recommended direction
- Next action: the clearest suggested follow-up

### Boundaries

- Keep it readable and calm
- Do not overload with many equal-weight cards
- Preserve strong scanability for quick adult review

## Visual Language

The two pages should share:

- the same card/surface hierarchy
- the same major spacing rhythm
- the same reveal cadence
- the same CTA treatment family
- the same overall completion-stage feeling

They should differ through:

- headline tone
- supporting copy density
- decorative intensity
- emphasis placement

Child uses:

- brighter emotional emphasis
- shorter copy
- more forward motion in CTA framing

Parent uses:

- steadier composition
- denser but still controlled copy
- clearer reporting emphasis

## Layout Rules

- One primary action block per page
- One dominant summary card per page
- Secondary cards should feel subordinate, not co-primary
- Next action must stay visually obvious even after scrolling

## Testing Expectations

Verification should focus on structure and hierarchy rather than fine visual pixels.

Primary checks:

- reward page exposes hero, summary, detail, and next-action structure
- parent summary exposes the same structural layers
- child CTA remains the strongest lower-page action
- parent recommendation area remains the strongest lower-page action
- instrumentation tests confirm the new key tags and major sections

## Success Criteria

- reward and parent summary clearly belong to one ending-page system
- child feels celebratory and forward-looking
- parent feels informative and action-oriented
- information hierarchy becomes clearer than the current version
- the redesign does not weaken current reward and parent summary flows
