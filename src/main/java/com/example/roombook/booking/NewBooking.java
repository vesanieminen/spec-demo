package com.example.roombook.booking;

import java.time.LocalDateTime;

/**
 * The data of a Booking to store.
 */
public record NewBooking(long roomId, long employeeId, LocalDateTime startTime, LocalDateTime endTime,
        LocalDateTime createdAt) {
}
