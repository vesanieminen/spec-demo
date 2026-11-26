# Open Questions

Questions about the specification that the specification does not answer yet. A question is `Resolved` only when
the answer is in the specification itself; **Resolved in** names where. Resolved and withdrawn questions stay as
history.

## OQ-001: How many Employees use RoomBook at the same time at peak?

**Element:** NFR-003  
**Raised by:** /requirements, 2026-11-26  
**Blocking:** No  
**Status:** Resolved  
**Assumed:** 50 Employees signed in at the same time.  

The vision names 150 people at the office but no peak load. 50 assumes about a third of the office books in the
same few minutes, for example on Monday morning.

**Answer:** All 150 Employees may use RoomBook at the same time.  
**Answered by:** Vesa, 2026-11-26  
**Resolved in:** NFR-003  

## OQ-002: Which phone screen width must RoomBook support?

**Element:** NFR-005  
**Raised by:** /requirements, 2026-11-26  
**Blocking:** No  
**Status:** Resolved  
**Assumed:** A viewport width of 360 pixels without horizontal scrolling.  

The vision says the web application must work in a phone browser but gives no measurable threshold.

**Answer:** Confirmed.  
**Answered by:** Vesa, 2026-11-26  
**Resolved in:** NFR-005  

## OQ-003: Which document and section of the company data retention policy sets the 12-month limit?

**Element:** C-003  
**Raised by:** /requirements, 2026-11-26  
**Blocking:** No  
**Status:** Resolved  
**Assumed:** Source "Company data retention policy", without a document id or section.  

The vision names the policy but no id, version, or section, so the source of C-003 cannot be traced yet. It is
also open whether the 12 months count from the end of the Time Slot (assumed) or from the creation of the Booking.

**Answer:** Confirmed.  
**Answered by:** Vesa, 2026-11-26  
**Resolved in:** C-003  

## OQ-004: Which browsers must RoomBook support?

**Element:** C-006  
**Raised by:** /requirements, 2026-11-26  
**Blocking:** No  
**Status:** Resolved  
**Assumed:** The latest two versions of Chrome, Edge, Firefox, and Safari.  

The vision names no browsers. The assumption covers the common desktop and phone browsers.

**Answer:** Confirmed.  
**Answered by:** Vesa, 2026-11-26  
**Resolved in:** C-006  

## OQ-005: How does RoomBook know that an Employee is an Office Manager?

**Element:** C-001  
**Raised by:** /requirements, 2026-11-26  
**Blocking:** No  
**Status:** Resolved  
**Assumed:** The Office Manager role is taken from a group of the company's single sign-on.  

Readings: (a) a single sign-on group, so RoomBook keeps no role list; (b) a list of Office Managers maintained in
RoomBook itself, which needs a further function to maintain it.

**Answer:** Confirmed.  
**Answered by:** Vesa, 2026-11-26  
**Resolved in:** C-001  

## OQ-006: What is the largest capacity a Room can have?

**Element:** Entity: ROOM.capacity  
**Raised by:** /entity-model, 2026-11-26  
**Blocking:** No  
**Status:** Resolved  
**Assumed:** Min: 1, Max: 100.  

Neither the vision nor the requirements give a limit. 100 covers any meeting room of a 150-person office and still
catches typing errors such as 1000.

**Answer:** Confirmed.  
**Answered by:** Vesa, 2026-11-26  
**Resolved in:** Entity: ROOM.capacity  

## OQ-007: Does the single sign-on always provide an email address for an Employee?

**Element:** Entity: EMPLOYEE.email  
**Raised by:** /entity-model, 2026-11-26  
**Blocking:** No  
**Status:** Resolved  
**Assumed:** Not Null, Unique, Format: Email — every Employee has a unique company email address.  

The requirements never use the email address; it is kept to identify the Employee to colleagues. Readings: (a)
always present and unique (assumed); (b) optional, for accounts without a mailbox.

**Answer:** Confirmed.  
**Answered by:** Vesa, 2026-11-26  
**Resolved in:** Entity: EMPLOYEE.email  

## OQ-008: May a Booking start before the current time?

**Element:** UC-001 BR-003  
**Raised by:** /use-case-spec, 2026-11-26  
**Blocking:** No  
**Status:** Resolved  
**Assumed:** No. A Booking cannot start before the current time, so a Room that is free right now can be booked only from the next 15-minute step on.  

Readings: (a) no start in the past (assumed); (b) the current 15-minute step may still be booked, so an Employee
who walks into a free Room at 10:07 can book it from 10:00.

**Answer:** Confirmed.  
**Answered by:** Vesa, 2026-11-26  
**Resolved in:** UC-001 BR-003  

## OQ-009: Can an Office Manager reactivate a deactivated Room?

**Element:** UC-003 A4  
**Raised by:** /use-case-spec, 2026-11-26  
**Blocking:** No  
**Status:** Resolved  
**Assumed:** Yes, through A4 Reactivate Room.  

FR-008 covers deactivation only. A Room that comes back into service would otherwise have to be added again under a
new name, because BR-001 keeps the old name taken.

**Answer:** Confirmed.  
**Answered by:** Vesa, 2026-11-26  
**Resolved in:** UC-003 A4  

## OQ-010: Who maintains the list of kinds of Equipment?

**Element:** UC-003 A5  
**Raised by:** /use-case-spec, 2026-11-26  
**Blocking:** No  
**Status:** Resolved  
**Assumed:** The Office Manager adds a missing kind of Equipment while adding or editing a Room (A5). Renaming or removing a kind is not supported.  

No requirement covers the list of kinds of Equipment (Entity: EQUIPMENT). Without A5 the list would stay empty. A
requirement for it is a hand-off to /requirements.

**Answer:** Confirmed.  
**Answered by:** Vesa, 2026-11-26  
**Resolved in:** UC-003 A5, FR-010  

## OQ-011: Is the 90-day booking horizon counted to the minute or by calendar day?

**Element:** UC-001 BR-002  
**Raised by:** /use-case-spec, 2026-11-26  
**Blocking:** No  
**Status:** Resolved  
**Assumed:** To the minute: a Booking starts at most 90 × 24 hours after the moment it is made, as the BOOKING constraint of the entity model states.  

Readings: (a) to the minute (assumed, matches the entity model); (b) by calendar day, so a Booking made at 14:00 may
start at any time on the 90th day after. Reading (b) would also change the BOOKING constraint (/entity-model).

**Answer:** By calendar day.  
**Answered by:** Vesa, 2026-11-26  
**Resolved in:** UC-001 BR-002, Entity: BOOKING  

## OQ-012: What happens when an Office Manager enters a kind of Equipment that already exists?

**Element:** UC-003 BR-005  
**Raised by:** /use-case-spec, 2026-11-26  
**Blocking:** No  
**Status:** Resolved  
**Assumed:** The existing kind is selected for the Room instead of adding a second one, and names are compared ignoring upper and lower case ("projector" selects "Projector").  

Readings: (a) select the existing kind (assumed); (b) reject the name with a message and let the Office Manager
select the kind from the list. Comparing case-sensitively would allow "Projector" and "projector" side by side.

**Answer:** The existing kind is selected.  
**Answered by:** Vesa, 2026-11-26  
**Resolved in:** UC-003 BR-005  

## OQ-013: Which time zone defines the day of a Time Slot and the times shown to Employees?

**Element:** UC-001 BR-001  
**Raised by:** /use-case-implement, 2026-11-26  
**Blocking:** No  
**Status:** Answered  

BR-001 requires a Time Slot to lie within one day, and NFR-002 speaks of local time, but no time zone is named.
Readings: (a) one office time zone configured in RoomBook; (b) the default time zone of the server RoomBook runs on.

**Answer:** (b) The default time zone of the server defines the day, the 15-minute steps, and the times shown.  
**Answered by:** Vesa Nieminen, 2026-11-26  

## OQ-014: Which single sign-on protocol and identity provider does RoomBook use?

**Element:** C-001  
**Raised by:** /use-case-implement, 2026-11-26  
**Blocking:** No  
**Status:** Answered  

C-001 requires the company's existing single sign-on, and the entity model creates the Employee from its data at
each sign-in, but neither the protocol (OpenID Connect, SAML) nor the identity provider is named. Readings: (a)
OpenID Connect; (b) SAML; (c) a development stub that signs every user in as one fixed Employee until the single
sign-on is decided.

**Answer:** (c) For now, a development stub signs every user in as one fixed Employee. The single sign-on protocol and identity provider are decided later; C-001 is not met until then.  
**Answered by:** Vesa Nieminen, 2026-11-26  

## OQ-015: Which message names the violated rule when a Booking is rejected under A2?

**Element:** UC-001 A2  
**Raised by:** /vaadin-flow-ui-test, 2026-11-26  
**Blocking:** No  
**Status:** Answered  

A2 says the system "names the rule that is violated" but words no message, and the tests assert messages word for
word. Readings: (a) one message per rule, restating it (implemented); (b) a message that cites the rule id only.

**Answer:** One message per rule. BR-001: "A Time Slot must lie within one day, start and end on a 15-minute step, and last at least 15 minutes and at most 4 hours." BR-002: "A Booking can start at most 90 days from now." BR-003: "A Booking cannot start before the current time."  
**Answered by:** Vesa Nieminen, 2026-11-26  
