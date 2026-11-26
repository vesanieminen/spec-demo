package com.example.roombook.booking;

import com.example.roombook.common.BusinessRuleException;

/**
 * UC-001 A2, BR-001: the Time Slot is off the grid, spans days, or is too short or too long.
 */
public class TimeSlotGridException extends BusinessRuleException {

    public TimeSlotGridException() {
        super("A Time Slot must lie within one day, start and end on a 15-minute step, and last at least 15 minutes and at most 4 hours.");
    }
}
