# Use Case: Manage Rooms

## Overview

**Use Case ID:** UC-003  
**Use Case Name:** Manage Rooms  
**Primary Actor:** Office Manager  
**Goal:** Keep the list of Rooms, their capacity, and their Equipment correct so that Employees book the right Room  
**Trigger:** An Office Manager needs to add a Room, change a Room, or take a Room out of service  
**Status:** Reviewed  

**Requirements:** [FR-006, FR-007, FR-008, FR-010, NFR-001, NFR-005, C-001, C-003](../requirements.md)

## Preconditions

- Office Manager is signed in with the Office Manager role

## Main Success Scenario

1. Office Manager opens the Room administration.
2. System displays all Rooms, active and deactivated, with their capacity and Equipment.
3. Office Manager chooses to add a Room.
4. Office Manager enters the name and the capacity and selects the kinds of Equipment installed in the Room.
5. Office Manager saves the Room.
6. System checks the input against BR-001 and BR-002.
7. System stores the Room according to BR-006 and displays the updated Room list.

## Alternative Flows

### A1: Edit Room

**Trigger:** Office Manager chooses to change an existing Room (step 3)  
**Flow:**

1. Office Manager selects the Room.
2. System displays the name, the capacity, and the Equipment of the Room.
3. Office Manager changes the name, the capacity, or the Equipment.
4. Use case continues at step 5.

### A2: Deactivate Room

**Trigger:** Office Manager chooses to deactivate an active Room (step 3)  
**Flow:**

1. Office Manager selects the Room and requests its deactivation.
2. System displays how many Bookings of the Room have not ended and asks the Office Manager to confirm that they will be deleted.
3. Office Manager confirms the deactivation.
4. System deletes the Bookings of the Room that have not ended and marks the Room as deactivated.
5. System displays the updated Room list.
6. Use case ends.

### A3: Deactivation Not Confirmed

**Trigger:** Office Manager chooses to deactivate an active Room but declines the deletion of its Bookings that A2 asks to confirm (step 3)  
**Flow:**

1. System keeps the Room active and its Bookings unchanged.
2. Use case ends.

### A4: Reactivate Room

**Trigger:** Office Manager chooses to reactivate a deactivated Room (step 3)  
**Flow:**

1. Office Manager selects the Room and requests its reactivation.
2. System marks the Room as active and displays the updated Room list.
3. Use case ends.

### A5: Kind of Equipment Missing

**Trigger:** The kind of Equipment installed in the Room is not in the list (step 4)  
**Flow:**

1. Office Manager enters the name of the new kind of Equipment.
2. System checks the name against BR-005: when a kind of Equipment with that name already exists, System selects the existing kind for the Room; otherwise System adds the new kind to the list and selects it for the Room.
3. Use case continues at step 4.

### A6: Invalid Input

**Trigger:** The name or the capacity violates BR-001 or BR-002 (step 6)  
**Flow:**

1. System rejects the input and names the rule that is violated.
2. Use case continues at step 4.

## Postconditions

### Success Postconditions

- The Room is stored with its name, capacity, Equipment, and status
- Employees can book an active Room and cannot book a deactivated one
- A deactivated Room has no Booking that has not ended

### Failure Postconditions

- No Room is stored with input that violates a business rule
- The Bookings of a Room are deleted only together with its deactivation

## Business Rules

### BR-001: Unique Room Name

No two Rooms share the same name.

### BR-002: Capacity Range

The capacity of a Room is a whole number from 1 to 100.

### BR-003: Deactivate Instead of Delete

A Room is never deleted, only deactivated, so its past Bookings stay assigned to it until they are deleted under C-003.

### BR-004: Deactivation Deletes Open Bookings

Deactivating a Room deletes all of its Bookings that have not ended, after the Office Manager has confirmed it.

### BR-005: Unique Equipment Name

No two kinds of Equipment share the same name, ignoring upper and lower case. A name the Office Manager enters for a kind that already exists selects the existing kind instead of adding a second one.

### BR-006: Status on Saving

A new Room is stored as active. Editing a Room keeps its status: an active Room stays active and a deactivated Room stays deactivated; only A2 and A4 change the status.
