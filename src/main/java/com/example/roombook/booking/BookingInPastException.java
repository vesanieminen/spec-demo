package com.example.roombook.booking;

import com.example.roombook.common.BusinessRuleException;

/**
 * UC-001 A2, BR-003: the Booking starts before the current time.
 */
public class BookingInPastException extends BusinessRuleException {

    public BookingInPastException() {
        super("A Booking cannot start before the current time.");
    }
}
