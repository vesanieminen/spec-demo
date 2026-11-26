package com.example.roombook.booking;

import java.time.LocalDateTime;

/**
 * A Booking as the day view of a Room shows it, with the name of the Employee who made it.
 */
public record BookingItem(long id, LocalDateTime startTime, LocalDateTime endTime, String employeeName) {
}
