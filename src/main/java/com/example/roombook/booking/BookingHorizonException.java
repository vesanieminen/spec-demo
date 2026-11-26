package com.example.roombook.booking;

import com.example.roombook.common.BusinessRuleException;

/**
 * UC-001 A2, BR-002: the Booking starts more than 90 days ahead.
 */
public class BookingHorizonException extends BusinessRuleException {

    public BookingHorizonException() {
        super("A Booking can start at most 90 days from now.");
    }
}
