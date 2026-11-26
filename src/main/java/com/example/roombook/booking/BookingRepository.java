package com.example.roombook.booking;

import static com.example.roombook.jooq.Tables.BOOKING;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Records;
import org.springframework.stereotype.Repository;

@Repository
public class BookingRepository {

    private final DSLContext ctx;

    public BookingRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /**
     * The Bookings of a Room that start on the given day, ordered by start time.
     */
    public List<BookingItem> findByRoomAndDay(long roomId, LocalDate day) {
        return ctx.select(BOOKING.ID, BOOKING.START_TIME, BOOKING.END_TIME, BOOKING.employee().NAME)
                .from(BOOKING)
                .where(BOOKING.ROOM_ID.eq(roomId))
                .and(BOOKING.START_TIME.ge(day.atStartOfDay()))
                .and(BOOKING.START_TIME.lt(day.plusDays(1).atStartOfDay()))
                .orderBy(BOOKING.START_TIME)
                .fetch(Records.mapping(BookingItem::new));
    }

    /**
     * Whether a Booking of the Room overlaps the Time Slot from start (inclusive) to end (exclusive).
     */
    public boolean existsOverlapping(long roomId, LocalDateTime start, LocalDateTime end) {
        return ctx.fetchExists(BOOKING,
                BOOKING.ROOM_ID.eq(roomId)
                        // UC-001 BR-004: Time Slots that only touch do not overlap.
                        .and(BOOKING.START_TIME.lt(end))
                        .and(BOOKING.END_TIME.gt(start)));
    }

    /**
     * Stores the Booking and returns its id. Throws a DataIntegrityViolationException naming
     * booking_overlap_ck when another Booking of the Room overlaps it.
     */
    public long insert(NewBooking booking) {
        return ctx.insertInto(BOOKING)
                .set(BOOKING.ROOM_ID, booking.roomId())
                .set(BOOKING.EMPLOYEE_ID, booking.employeeId())
                .set(BOOKING.START_TIME, booking.startTime())
                .set(BOOKING.END_TIME, booking.endTime())
                .set(BOOKING.CREATED_AT, booking.createdAt())
                .returning(BOOKING.ID)
                .fetchSingle(BOOKING.ID);
    }
}
