# Stack

**For the agent:** read before writing code. This file is the AI Unified Process record of the technical layers,
independent of how the team documents its architecture. The Layers table says which layer skill builds and tests
each technical layer: `/use-case-implement` runs the Skill column bottom-up, `/use-case-test` runs the Test skill
column from the integration layer up, and `/use-case-coverage` searches for the markers in the last table.

**Last updated:** 2026-11-26

## Layers

| Layer       | Technology               | Skill | Test skill | Location           |
|-------------|--------------------------|-------|------------|--------------------|
| Migration   | Flyway                   | –     | –          | (not yet created)  |
| Persistence | jOOQ                     | –     | –          | (not yet created)  |
| Client      | –                        | –     | –          | –                  |
| Service     | Spring Boot              | –     | –          | (not yet created)  |
| API         | –                        | –     | –          | –                  |
| UI          | Vaadin Flow              | –     | –          | (not yet created)  |
| E2E         | Vaadin E2E (Playwright)  | –     | –          | (not yet created)  |

**Integration layer:**

| Use case offered through | Tested in | Test skill |
|--------------------------|-----------|------------|
| UI                       | UI        | –          |
| Service                  | Service   | –          |

**E2E scope:** Test cases only

The Skill and Test skill columns stay `–` until the layer plugins are installed for the session that builds
RoomBook: `aiup-flyway` (`flyway-schema`), `aiup-jooq` (`jooq-persistence`, `jooq-persistence-test`),
`aiup-spring-boot` (`spring-service`, `spring-service-test`), and `aiup-vaadin` (`vaadin-flow-ui`,
`vaadin-flow-ui-test`, `vaadin-e2e-test`); rerun `/stack` then to fill them in. RoomBook has no API layer, because
the Vaadin Flow UI calls the service directly and no other client exists, and no Client layer, because it calls no
external system (single sign-on, C-001, is authentication, not a secondary actor of a use case). A use case offered
through the UI is tested in the UI layer, whose tests run in process against the real database. Browser tests are
written for the TC-* journeys only.

## Module layout

<!-- Flat single module, hexagonal multi-module, or a workspace with one application per side; the root package or
the application roots; where each layer's code and tests live. -->

## Commands

| Purpose            | Command |
|--------------------|---------|
| Build              |         |
| Unit tests         |         |
| Integration tests  |         |
| End-to-end tests   |         |

## Markers

Each layer skill keeps its own rows here: on every run it checks that the rows for its layer match its marker
reference and corrects them. Do not edit the rows by hand; rerun the layer skill instead.

| Layer | Skill | Kind | Marker to grep for |
|-------|-------|------|--------------------|
| All   | –     | Code | `// UC-XXX BR-YYY:` directly above the method, query condition, or validator that enforces the rule |
