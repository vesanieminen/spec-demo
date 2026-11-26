# Use Case: Book Room

## Overview

**Use Case ID:** UC-001  
**Use Case Name:** Book Room  
**Primary Actor:** Employee  
**Goal:** Book a free Room for a Time Slot so that the meeting has a place  
**Trigger:** An Employee needs a Room for a meeting  
**Status:** Draft  

**Requirements:** [FR-001, FR-002, FR-003, FR-004, NFR-001, NFR-003, NFR-004, NFR-005, C-001](../requirements.md)

## Preconditions

- Employee is signed in
- At least one Room is active

## Main Success Scenario

1. Employee opens the Room list.
2. System displays all active Rooms with their capacity and Equipment.
3. Employee selects a Room and a day.
4. System displays the Bookings of the Room for that day, each with the name of the Employee who made it.
5. Employee chooses the start time and the end time of a free Time Slot.
6. Employee confirms the Booking.
7. System checks the Booking against BR-001 to BR-005.
8. System stores the Booking and displays it in the day view of the Room.

## Alternative Flows

### A1: No Suitable Free Time Slot

**Trigger:** The Room has no free Time Slot that suits the Employee on that day (step 4)  
**Flow:**

1. Employee selects another Room or another day.
2. Use case continues at step 4.

### A2: Time Slot Violates a Booking Rule

**Trigger:** The Time Slot violates BR-001, BR-002, or BR-003 (step 7)  
**Flow:**

1. System rejects the Booking and names the rule that is violated.
2. Use case continues at step 5.

### A3: Double Booking

**Trigger:** The Time Slot overlaps a Booking of the same Room, for example one another Employee made in the meantime (step 7)  
**Flow:**

1. System rejects the Booking, tells the Employee that the Time Slot is no longer free, and displays the current Bookings of the Room for that day.
2. Use case continues at step 5.

### A4: Employee Abandons the Booking

**Trigger:** Employee decides not to book (step 6)  
**Flow:**

1. Employee leaves the Booking without confirming it.
2. Use case ends.

### A5: Room No Longer Active

**Trigger:** An Office Manager deactivated the Room in the meantime, so it violates BR-005 (step 7)  
**Flow:**

1. System rejects the Booking, tells the Employee that the Room can no longer be booked, and displays the Room list.
2. Use case continues at step 3.

## Postconditions

### Success Postconditions

- A Booking of the Room for the Time Slot is stored with the Employee who made it
- Every Employee sees the Time Slot as booked, with the name of the Employee who made the Booking

### Failure Postconditions

- No Booking is stored
- No two Bookings of the same Room overlap

## Business Rules

### BR-001: Time Slot Grid

A Time Slot lies within one day, starts and ends on a 15-minute step (minutes 00, 15, 30, or 45), and lasts at least 15 minutes and at most 4 hours.

### BR-002: Booking Horizon

A Booking starts at most 90 days (90 × 24 hours) after the moment it is made.

### BR-003: No Booking in the Past

A Booking cannot start before the current time.

### BR-004: No Double Booking

A Booking must not overlap another Booking of the same Room. Two Time Slots that only touch (one ends at 10:00, the other starts at 10:00) do not overlap.

### BR-005: Active Rooms Only

Only an active Room can be booked; a deactivated Room cannot.
