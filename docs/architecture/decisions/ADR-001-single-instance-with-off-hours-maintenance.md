# ADR-001: Run RoomBook as a single instance and do all maintenance outside business hours

**Status:** Proposed
**Date:** 2026-11-26
**Requirements:** NFR-002, C-002, C-005

## Context

- NFR-002 requires RoomBook to be available 99.9% of business hours (Monday to Friday, 07:00–19:00 local time),
  measured per calendar month. With about 260 business hours a month, that allows about 15 minutes of unplanned
  downtime during business hours each month. Evenings, nights, and weekends do not count.
- C-002 makes RoomBook reachable from the company's internal network only, so it runs on internal company
  infrastructure.
- C-005 limits development to two developers, so operating effort has to stay small.
- The Vaadin Flow UI (docs/architecture/stack.md) keeps session state on the server. Running more than one instance
  would need sticky sessions or session replication.

## Decision

- Run RoomBook as one application instance with one database on internal company servers.
- Do deployments, upgrades, database migrations, and other planned maintenance only outside business hours
  (before 07:00, after 19:00, or at weekends).
- Restart the application automatically when it stops or its health check fails.
- Monitor the health check during business hours and alert the team when it fails.
- Back up the database every night.

## Consequences

- Easier: there is no load balancer, no session replication, and no database replication, so two developers can
  build and run the system.
- Easier: planned downtime does not count against NFR-002 because it happens outside business hours.
- Harder: a hardware or host failure during business hours stops RoomBook. Recovery has to fit within about
  15 minutes a month, so restore and restart must be practiced and fast.
- Harder: a release cannot go live during business hours, including urgent fixes, unless downtime is accepted.
- **Alternatives considered:**
  - Two instances behind the company's internal load balancer with sticky sessions and a standby database: allows
    daytime rolling deploys and survives one instance failing, but adds more to operate than a two-person team
    should carry for this system.
  - Container deployment to company Kubernetes with two or more replicas: similar resilience, but depends on the
    company having such a platform, and the docs do not say it does.
