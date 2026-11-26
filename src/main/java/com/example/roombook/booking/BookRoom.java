package com.example.roombook.booking;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

/**
 * UC-001 steps 5–6: the Room and the Time Slot an Employee confirms.
 */
public record BookRoom(long roomId, @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime) {
}
