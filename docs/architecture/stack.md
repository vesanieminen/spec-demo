# Stack

**For the agent:** read before writing code. This file is the AI Unified Process record of the technical layers,
independent of how the team documents its architecture. The Layers table says which layer skill builds and tests
each technical layer: `/use-case-implement` runs the Skill column bottom-up, `/use-case-test` runs the Test skill
column from the integration layer up, and `/use-case-coverage` searches for the markers in the last table.

**Last updated:** 2026-11-26

## Layers

| Layer       | Technology              | Skill              | Test skill             | Location                                                        |
|-------------|-------------------------|--------------------|------------------------|-----------------------------------------------------------------|
| Migration   | Flyway                  | `flyway-schema`    | –                      | `src/main/resources/db/migration`                               |
| Persistence | jOOQ                    | `jooq-persistence` | `jooq-persistence-test`| `com.example.roombook.<feature>` (generated code: `com.example.roombook.jooq`) |
| Client      | –                       | –                  | –                      | –                                                               |
| Service     | Spring Boot             | `spring-service`   | `spring-service-test`  | `com.example.roombook.<feature>`                                |
| API         | –                       | –                  | –                      | –                                                               |
| UI          | Vaadin Flow             | `vaadin-flow-ui`   | `vaadin-flow-ui-test`  | `com.example.roombook.<feature>.ui`                             |
| E2E         | Vaadin E2E (Playwright) | –                  | `vaadin-e2e-test`      | `src/test/java/com/example/roombook/e2e`                        |

**Integration layer:**

| Use case offered through | Tested in | Test skill            |
|--------------------------|-----------|-----------------------|
| UI                       | UI        | `vaadin-flow-ui-test` |
| Service                  | Service   | `spring-service-test` |

**E2E scope:** Test cases only

RoomBook has no API layer, because the Vaadin Flow UI calls the service directly and no other client exists, and no
Client layer, because it calls no external system (single sign-on, C-001, is authentication, not a secondary actor of
a use case). A use case offered through the UI is tested in the UI layer, whose tests run in process against the real
database; a use case offered through the service alone (a scheduled job) is
tested in the service layer. Flyway has no test skill of its own: the migrations run under every persistence,
service, and UI test. Browser tests are written for the TC-* journeys only.

## Module layout

Flat single Maven module, root package `com.example.roombook`, packaged by feature (for example `room`, `booking`):

- Service classes, domain records, and the jOOQ repositories of a feature live in `com.example.roombook.<feature>`;
  its Vaadin Flow views in `com.example.roombook.<feature>.ui`.
- jOOQ generated code goes to `com.example.roombook.jooq`, generated from the Flyway migrations at build time.
- Flyway migrations live in `src/main/resources/db/migration`.
- Tests mirror the main packages under `src/test/java`; browser tests live in `com.example.roombook.e2e`.

## Commands

| Purpose            | Command |
|--------------------|---------|
| Build              | `./mvnw verify` |
| Unit tests         | `./mvnw test` |
| Integration tests  | `./mvnw verify` |
| End-to-end tests   | – (recorded with the first browser test) |

## Markers

Each layer skill keeps its own rows here: on every run it checks that the rows for its layer match its marker
reference and corrects them. Do not edit the rows by hand; rerun the layer skill instead.

| Layer | Skill | Kind | Marker to grep for |
|-------|-------|------|--------------------|
| All   | –     | Code | `// UC-XXX BR-YYY:` directly above the method, query condition, or validator that enforces the rule |
| Migration | flyway-schema | Code | `-- UC-XXX BR-YYY:` directly above the `CHECK`, `UNIQUE`, index, or trigger that enforces the rule                    |
| Migration | flyway-schema | Name | `src/main/resources/db/migration/V*__*.sql`, named after the tables they change, never after a use case — search them by table and column |
| Persistence | jooq-persistence | Code | `// UC-XXX BR-YYY:` directly above the jOOQ `.where(...)`/`.and(...)` condition or query that enforces the rule |
| Persistence | jooq-persistence | Name | `<Aggregate>Repository` (`@Repository` with a `DSLContext`, or the adapter implementing that outbound port in a hexagonal layout), named after the aggregate root, never a use case |
| Service | spring-service | Code | `// UC-XXX BR-YYY:` directly above the service method or guard clause that enforces the rule         |
| Service | spring-service | Code | `UC-XXX` in the Javadoc of the public service method that realizes a step or an alternative flow     |
| Service | spring-service | Name | `<Entity>Service` classes annotated `@Service`; domain exceptions `<Reason>Exception`                 |
| UI    | vaadin-flow-ui | Code | `UC-XXX` in the view's class Javadoc (`/** UC-XXX <Use Case Name>. */`)                                 |
| UI    | vaadin-flow-ui | Code | `// UC-XXX BR-YYY:` directly above the `Binder` validator or UI guard that enforces the rule            |
| UI    | vaadin-flow-ui | Name | `@Route` view class `<Name>View` named after the use case (`ManagePersonsView` for "Manage Persons")    |
| UI    | vaadin-flow-ui-test | Test | `@UseCase(id = "UC-XXX", scenario = "…", businessRules = {"BR-YYY"}, requirements = {"NFR-XXX"})` on every Browserless test method     |
| UI    | vaadin-flow-ui-test | Name | `UC<id><Name>Test`, e.g. `UC001ManagePersonsTest`, extending `SpringBrowserlessTest` or the project's subclass of it |
