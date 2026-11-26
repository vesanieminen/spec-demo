package com.example.roombook.booking;

import com.example.roombook.common.BusinessRuleException;

/**
 * UC-001 A3, BR-004: the Time Slot overlaps another Booking of the Room.
 */
public class TimeSlotNotFreeException extends BusinessRuleException {

    public TimeSlotNotFreeException() {
        super("The Time Slot is no longer free.");
    }
}
