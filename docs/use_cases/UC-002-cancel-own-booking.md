# Use Case: Cancel Own Booking

## Overview

**Use Case ID:** UC-002  
**Use Case Name:** Cancel Own Booking  
**Primary Actor:** Employee  
**Goal:** Cancel a Booking the Employee made so that the Room becomes free for others  
**Trigger:** An Employee no longer needs a Room they booked  
**Status:** Reviewed  

**Requirements:** [FR-005, NFR-001, NFR-005](../requirements.md)

## Preconditions

- Employee is signed in

## Main Success Scenario

1. Employee selects a Room and a day.
2. System displays the Bookings of the Room for that day and marks the Employee's own Bookings that have not ended as cancellable.
3. Employee selects one of their own Bookings and requests its cancellation.
4. System asks the Employee to confirm the cancellation.
5. Employee confirms the cancellation.
6. System deletes the Booking and displays the day view of the Room with the Time Slot free.

## Alternative Flows

### A1: Employee Keeps the Booking

**Trigger:** Employee does not confirm the cancellation (step 5)  
**Flow:**

1. System keeps the Booking unchanged.
2. Use case ends.

### A2: Booking No Longer Cancellable

**Trigger:** The Booking has ended or was deleted in the meantime, for example by an Office Manager (step 6)  
**Flow:**

1. System tells the Employee that the Booking can no longer be cancelled and displays the current Bookings of the Room for that day.
2. Use case ends.

## Postconditions

### Success Postconditions

- The Booking is deleted
- Every Employee sees the Time Slot of the Room as free

### Failure Postconditions

- The Booking stays as it was
- No other Booking is changed

## Business Rules

### BR-001: Own Bookings Only

An Employee can cancel only the Bookings they made themselves.

### BR-002: Cancellable Until the End

A Booking can be cancelled until its Time Slot has ended, also after it has started; a Booking whose Time Slot has ended cannot be cancelled.
