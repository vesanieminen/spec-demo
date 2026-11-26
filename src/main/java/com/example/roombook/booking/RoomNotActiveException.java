package com.example.roombook.booking;

import com.example.roombook.common.BusinessRuleException;

/**
 * UC-001 A5, BR-005: the Room was deactivated.
 */
public class RoomNotActiveException extends BusinessRuleException {

    public RoomNotActiveException() {
        super("The Room can no longer be booked.");
    }
}
