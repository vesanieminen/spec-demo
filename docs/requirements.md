# Requirements: RoomBook

Requirements catalog derived from [vision.md](vision.md). Domain terms are defined in [glossary.md](glossary.md).

## Functional Requirements

| ID     | Title                  | User Story                                                                                                                                                                       | Priority | Status |
|--------|------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|--------|
| FR-001 | View Rooms             | As an Employee, I want to see all active Rooms with their capacity and Equipment so that I can pick a Room that fits my meeting.                                                  | High     | Open   |
| FR-002 | View Room Availability | As an Employee, I want to see the Bookings of a Room for a chosen day, including the name of the Employee who made each Booking, so that I can find a free Time Slot or ask a colleague to swap. | High     | Open   |
| FR-003 | Book Room              | As an Employee, I want to book a Room for a Time Slot of 15 minutes to 4 hours in 15-minute steps, up to 90 days ahead, so that the Room is reserved for my meeting.             | High     | Open   |
| FR-004 | Prevent Double Booking | As an Employee, I want a Booking that overlaps an existing Booking of the same Room to be rejected so that no two meetings are planned in the same Room at the same time.          | High     | Open   |
| FR-005 | Cancel Own Booking     | As an Employee, I want to cancel a Booking I made so that the Room becomes free for others.                                                                                       | High     | Open   |
| FR-006 | Add Room               | As an Office Manager, I want to add a Room with its name, capacity, and Equipment so that Employees can book it.                                                                 | High     | Open   |
| FR-007 | Edit Room              | As an Office Manager, I want to change the name, capacity, and Equipment of a Room so that Employees see correct information.                                                    | Medium   | Open   |
| FR-008 | Deactivate Room        | As an Office Manager, I want to deactivate a Room so that Employees can no longer book a Room that is out of service.                                                             | Medium   | Open   |
| FR-009 | Cancel Any Booking     | As an Office Manager, I want to cancel any Booking so that I can resolve a dispute about a Room.                                                                                  | High     | Open   |
| FR-010 | Add Equipment Kind     | As an Office Manager, I want to add a new kind of Equipment while adding or editing a Room so that every Room's Equipment can be recorded. | Medium   | Open   |

## Non-Functional Requirements

| ID      | Title              | Requirement                                                                                                                       | Category    | Priority | Status |
|---------|--------------------|-----------------------------------------------------------------------------------------------------------------------------------|-------------|----------|--------|
| NFR-001 | Response Time      | 95% of page loads and Booking confirmations complete within 2 seconds.                                                            | Performance | High     | Open   |
| NFR-002 | Availability       | RoomBook is available 99.9% of business hours (Monday to Friday, 07:00–19:00 local time), measured per calendar month.           | Availability | High    | Open   |
| NFR-003 | Concurrent Access  | RoomBook supports all 150 Employees signed in and using it at the same time while meeting NFR-001.                              | Scalability | Medium   | Open   |
| NFR-004 | Booking Effort     | An Employee completes a Booking in at most 3 steps and within 60 seconds, starting from the Room list.                           | Usability   | High     | Open   |
| NFR-005 | Phone Browser      | All functions are usable in a phone browser at a viewport width of 360 pixels without horizontal scrolling.                      | Usability   | Medium   | Open   |

## Constraints

| ID    | Title            | Constraint                                                                                                          | Category    | Source                         | Priority | Status |
|-------|------------------|---------------------------------------------------------------------------------------------------------------------|-------------|--------------------------------|----------|--------|
| C-001 | Single Sign-On   | Employees sign in with the company's existing single sign-on; RoomBook stores no passwords. The Office Manager role is taken from a single sign-on group. | Technical   |                                | High     | Open   |
| C-002 | Internal Network | RoomBook is reachable from the company's internal network only.                                                    | Operational |                                | High     | Open   |
| C-003 | Data Retention   | A Booking is deleted at the latest 12 months after its Time Slot has ended.                                         | Regulatory  | Company data retention policy  | High     | Open   |
| C-004 | Deadline         | The first release is in production within 3 months of project start.                                                | Schedule    |                                | High     | Open   |
| C-005 | Team Size        | Development is done by a team of two developers.                                                                    | Business    |                                | High     | Open   |
| C-006 | Browser Support  | RoomBook supports the latest two versions of Chrome, Edge, Firefox, and Safari.                                     | Technical   |                                | Medium   | Open   |
