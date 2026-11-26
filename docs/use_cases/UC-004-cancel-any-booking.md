# Use Case: Cancel Any Booking

## Overview

**Use Case ID:** UC-004  
**Use Case Name:** Cancel Any Booking  
**Primary Actor:** Office Manager  
**Goal:** Cancel the Booking of any Employee so that a dispute about a Room is resolved  
**Trigger:** An Office Manager decides to free a Room booked by an Employee  
**Status:** Approved  

**Requirements:** [FR-009, NFR-001, NFR-005, C-001](../requirements.md)

## Preconditions

- Office Manager is signed in with the Office Manager role

## Main Success Scenario

1. Office Manager selects a Room and a day.
2. System displays the Bookings of the Room for that day, each with the name of the Employee who made it, and marks every Booking that has not ended as cancellable.
3. Office Manager selects a Booking and requests its cancellation.
4. System displays the Time Slot and the Employee of the Booking and asks the Office Manager to confirm the cancellation.
5. Office Manager confirms the cancellation.
6. System deletes the Booking and displays the day view of the Room with the Time Slot free.

## Alternative Flows

### A1: Office Manager Keeps the Booking

**Trigger:** Office Manager does not confirm the cancellation (step 5)  
**Flow:**

1. System keeps the Booking unchanged.
2. Use case ends.

### A2: Booking No Longer Cancellable

**Trigger:** The Booking has ended or was deleted in the meantime (step 6)  
**Flow:**

1. System tells the Office Manager that the Booking can no longer be cancelled and displays the current Bookings of the Room for that day.
2. Use case ends.

## Postconditions

### Success Postconditions

- The Booking is deleted
- Every Employee sees the Time Slot of the Room as free

### Failure Postconditions

- The Booking stays as it was
- No other Booking is changed

## Business Rules

### BR-001: Cancellable Until the End

The Office Manager can cancel a Booking under the same time limit as an Employee cancelling their own Booking (UC-002 BR-002).

### BR-002: No Notification

RoomBook does not notify the Employee whose Booking an Office Manager cancels; the Office Manager informs the Employee personally when needed.
