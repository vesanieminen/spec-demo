package com.example.roombook.booking;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.roombook.common.NotFoundException;
import com.example.roombook.room.RoomRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * Bookings of Rooms.
 */
@Service
@Validated
@Transactional(readOnly = true)
public class BookingService {

    private static final Duration MIN_DURATION = Duration.ofMinutes(15);
    private static final Duration MAX_DURATION = Duration.ofHours(4);
    private static final int STEP_MINUTES = 15;
    private static final int HORIZON_DAYS = 90;

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final Clock clock;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, Clock clock) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.clock = clock;
    }

    /**
     * UC-001 step 4, A1, A3: the Bookings of the Room for the day, each with the name of the Employee who made it.
     */
    public List<BookingItem> findByRoomAndDay(long roomId, LocalDate day) {
        return bookingRepository.findByRoomAndDay(roomId, day);
    }

    /**
     * UC-001 steps 6–8: checks the Booking against BR-001 to BR-005 and stores it for the Employee; A2, A3, A5
     * when a rule is violated. Returns the id of the new Booking.
     */
    @Transactional
    public long book(@Valid BookRoom command, long employeeId) {
        LocalDateTime start = command.startTime();
        LocalDateTime end = command.endTime();
        LocalDateTime now = LocalDateTime.now(clock);

        checkTimeSlotGrid(start, end);
        // UC-001 BR-003: A Booking cannot start before the current time.
        if (start.isBefore(now)) {
            throw new BookingInPastException();
        }
        // UC-001 BR-002: A Booking starts at most 90 days (90 × 24 hours) after it is made.
        if (start.isAfter(now.plusDays(HORIZON_DAYS))) {
            throw new BookingHorizonException();
        }
        // UC-001 BR-005: Only an active Room can be booked.
        boolean active = roomRepository.findActiveForShare(command.roomId())
                .orElseThrow(() -> new NotFoundException("The Room does not exist."));
        if (!active) {
            throw new RoomNotActiveException();
        }
        // UC-001 BR-004: A Booking must not overlap another Booking of the same Room.
        if (bookingRepository.existsOverlapping(command.roomId(), start, end)) {
            throw new TimeSlotNotFreeException();
        }
        try {
            return bookingRepository.insert(new NewBooking(command.roomId(), employeeId, start, end, now));
        } catch (DataAccessException e) {
            // UC-001 BR-004: a concurrent Booking passed the check above; the exclusion constraint rejects it
            if (String.valueOf(e.getMostSpecificCause().getMessage()).contains("booking_overlap_ck")) {
                throw new TimeSlotNotFreeException();
            }
            throw e;
        }
    }

    // UC-001 BR-001: A Time Slot lies within one day, on 15-minute steps, 15 minutes to 4 hours.
    private void checkTimeSlotGrid(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        boolean sameDay = start.toLocalDate().equals(end.toLocalDate());
        if (!sameDay || !onStep(start) || !onStep(end)
                || duration.compareTo(MIN_DURATION) < 0 || duration.compareTo(MAX_DURATION) > 0) {
            throw new TimeSlotGridException();
        }
    }

    private static boolean onStep(LocalDateTime time) {
        return time.getMinute() % STEP_MINUTES == 0 && time.getSecond() == 0 && time.getNano() == 0;
    }
}
