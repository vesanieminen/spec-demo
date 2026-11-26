package com.example.roombook.booking.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import com.example.roombook.TestcontainersConfiguration;
import com.example.roombook.booking.BookingItem;
import com.example.roombook.room.RoomListItem;
import com.example.roombook.usecase.UseCase;
import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.timepicker.TimePicker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.bean.override.convention.TestBean;

/**
 * Use case test for UC-001 "Book Room", the integration layer of the stack: the view runs against the real services
 * and a PostgreSQL database. The shared test data is {@code R__uc001_book_room.sql}; the clock is fixed at
 * 2026-12-01 08:00 in the server's default time zone (OQ-013).
 */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class UC001BookRoomTest extends SpringBrowserlessTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 12, 1, 8, 0);
    private static final LocalDate TODAY = NOW.toLocalDate();
    private static final LocalDate TOMORROW = TODAY.plusDays(1);

    private static final String AURORA = "Test Room Aurora";
    private static final String BIRCH = "Test Room Birch";
    private static final String CEDAR = "Test Room Cedar";
    private static final String DUNE = "Test Room Dune";
    private static final long BIRCH_ID = 9002;
    private static final long DUNE_ID = 9004;
    private static final long COLLEAGUE_ID = 9001;
    private static final String COLLEAGUE = "Alex Colleague";
    // The development sign-in stub (OQ-014) signs every session in as this Employee
    private static final String DEV_SSO_SUBJECT = "dev-employee";
    private static final String DEV_NAME = "Development Employee";

    private static final String MSG_BR001 = "A Time Slot must lie within one day, start and end on a 15-minute step, "
            + "and last at least 15 minutes and at most 4 hours.";
    private static final String MSG_BR002 = "A Booking can start at most 90 days from now.";
    private static final String MSG_BR003 = "A Booking cannot start before the current time.";
    private static final String MSG_A3 = "The Time Slot is no longer free.";
    private static final String MSG_A5 = "The Room can no longer be booked.";

    @TestBean
    private Clock clock;

    static Clock clock() {
        ZoneId zone = ZoneId.systemDefault();
        return Clock.fixed(NOW.atZone(zone).toInstant(), zone);
    }

    @Autowired
    private JdbcClient jdbcClient;

    @AfterEach
    void removeCreatedData() {
        jdbcClient.sql("delete from booking where employee_id in (select id from employee where sso_subject = ?)")
                .param(DEV_SSO_SUBJECT).update();
        jdbcClient.sql("delete from booking where employee_id = ? and id <> 9001").param(COLLEAGUE_ID).update();
        jdbcClient.sql("update room set active = true where id = ?").param(DUNE_ID).update();
    }

    // --- Main success scenario ---

    @Test
    @UseCase(id = "UC-001", businessRules = {"BR-005"})
    void room_list_shows_the_active_rooms_with_capacity_and_equipment() {
        navigate(BookRoomView.class);

        Grid<RoomListItem> rooms = roomGrid();
        int aurora = rowOf(rooms, AURORA);
        assertThat(aurora).isNotNegative();
        assertThat(test(rooms).getCellText(aurora, 1)).isEqualTo("6");
        assertThat(test(rooms).getCellText(aurora, 2)).isEqualTo("Test Projector, Test Video Conferencing");
        int birch = rowOf(rooms, BIRCH);
        assertThat(test(rooms).getCellText(birch, 1)).isEqualTo("4");
        assertThat(test(rooms).getCellText(birch, 2)).isEqualTo("Test Whiteboard");
        // BR-005: a deactivated Room is not offered
        assertThat(rowOf(rooms, CEDAR)).isNegative();
    }

    @Test
    @UseCase(id = "UC-001")
    void day_view_shows_the_bookings_of_the_room_with_the_name_of_the_employee() {
        navigate(BookRoomView.class);

        openDayView(AURORA, TOMORROW);

        Grid<BookingItem> bookings = bookingGrid();
        assertThat(test(bookings).size()).isEqualTo(1);
        assertThat(test(bookings).getCellText(0, 0)).isEqualTo("10:00");
        assertThat(test(bookings).getCellText(0, 1)).isEqualTo("11:00");
        assertThat(test(bookings).getCellText(0, 2)).isEqualTo(COLLEAGUE);
    }

    @Test
    @UseCase(id = "UC-001")
    void booking_a_free_time_slot_stores_it_and_shows_it_in_the_day_view() {
        navigate(BookRoomView.class);
        openDayView(AURORA, TOMORROW);

        book(LocalTime.of(13, 0), LocalTime.of(14, 0));

        // Postcondition: the Booking is stored with the Employee who made it
        assertThat(jdbcClient.sql("""
                        select b.start_time from booking b join employee e on e.id = b.employee_id
                        where b.room_id = 9001 and e.sso_subject = ?""")
                .param(DEV_SSO_SUBJECT).query(LocalDateTime.class).list())
                .containsExactly(TOMORROW.atTime(13, 0));
        assertThat(jdbcClient.sql("""
                        select b.end_time from booking b join employee e on e.id = b.employee_id
                        where b.room_id = 9001 and e.sso_subject = ?""")
                .param(DEV_SSO_SUBJECT).query(LocalDateTime.class).list())
                .containsExactly(TOMORROW.atTime(14, 0));
        // Step 8 and postcondition: shown in the day view as booked, with the Employee's name
        Grid<BookingItem> bookings = bookingGrid();
        int row = rowStartingAt(bookings, "13:00");
        assertThat(row).isNotNegative();
        assertThat(test(bookings).getCellText(row, 1)).isEqualTo("14:00");
        assertThat(test(bookings).getCellText(row, 2)).isEqualTo(DEV_NAME);
        assertThat(find(Notification.class).exists()).isFalse();
    }

    // --- A1: No Suitable Free Time Slot ---

    @Test
    @UseCase(id = "UC-001", scenario = "A1: No Suitable Free Time Slot")
    void selecting_another_day_and_room_shows_their_bookings() {
        navigate(BookRoomView.class);
        openDayView(AURORA, TODAY);
        assertThat(test(bookingGrid()).size()).isZero();

        selectDay(TOMORROW);
        assertThat(test(bookingGrid()).size()).isEqualTo(1);
        assertThat(test(bookingGrid()).getCellText(0, 2)).isEqualTo(COLLEAGUE);

        selectRoom(BIRCH);
        assertThat(test(bookingGrid()).size()).isZero();
    }

    // --- A2: Time Slot Violates a Booking Rule ---

    @Test
    @UseCase(id = "UC-001", scenario = "A2: Time Slot Violates a Booking Rule", businessRules = {"BR-001"})
    void time_slot_off_the_15_minute_grid_is_rejected() {
        navigate(BookRoomView.class);
        openDayView(BIRCH, TOMORROW);

        book(LocalTime.of(9, 10), LocalTime.of(10, 0));

        assertRejected(MSG_BR001);
    }

    @Test
    @UseCase(id = "UC-001", scenario = "A2: Time Slot Violates a Booking Rule", businessRules = {"BR-001"})
    void time_slot_shorter_than_15_minutes_is_rejected() {
        navigate(BookRoomView.class);
        openDayView(BIRCH, TOMORROW);

        book(LocalTime.of(9, 0), LocalTime.of(9, 0));

        assertRejected(MSG_BR001);
    }

    @Test
    @UseCase(id = "UC-001", scenario = "A2: Time Slot Violates a Booking Rule", businessRules = {"BR-001"})
    void time_slot_ending_before_it_starts_is_rejected() {
        navigate(BookRoomView.class);
        openDayView(BIRCH, TOMORROW);

        book(LocalTime.of(10, 0), LocalTime.of(9, 0));

        assertRejected(MSG_BR001);
    }

    @Test
    @UseCase(id = "UC-001", scenario = "A2: Time Slot Violates a Booking Rule", businessRules = {"BR-001"})
    void time_slot_longer_than_4_hours_is_rejected() {
        navigate(BookRoomView.class);
        openDayView(BIRCH, TOMORROW);

        book(LocalTime.of(8, 0), LocalTime.of(12, 15));

        assertRejected(MSG_BR001);
    }

    @Test
    @UseCase(id = "UC-001", businessRules = {"BR-001"})
    void time_slot_of_exactly_15_minutes_is_accepted() {
        navigate(BookRoomView.class);
        openDayView(BIRCH, TOMORROW);

        book(LocalTime.of(14, 0), LocalTime.of(14, 15));

        assertThat(devBookings()).isEqualTo(1);
    }

    @Test
    @UseCase(id = "UC-001", businessRules = {"BR-001"})
    void time_slot_of_exactly_4_hours_is_accepted() {
        navigate(BookRoomView.class);
        openDayView(BIRCH, TOMORROW);

        book(LocalTime.of(8, 0), LocalTime.of(12, 0));

        assertThat(devBookings()).isEqualTo(1);
    }

    @Test
    @UseCase(id = "UC-001", scenario = "A2: Time Slot Violates a Booking Rule", businessRules = {"BR-002"})
    void booking_starting_more_than_90_days_ahead_is_rejected() {
        navigate(BookRoomView.class);
        // 90 × 24 hours after 2026-12-01 08:00 is 2027-03-01 08:00
        openDayView(BIRCH, LocalDate.of(2027, 3, 1));

        book(LocalTime.of(8, 15), LocalTime.of(8, 30));

        assertRejected(MSG_BR002);
    }

    @Test
    @UseCase(id = "UC-001", businessRules = {"BR-002"})
    void booking_starting_exactly_90_days_ahead_is_accepted() {
        navigate(BookRoomView.class);
        openDayView(BIRCH, LocalDate.of(2027, 3, 1));

        book(LocalTime.of(8, 0), LocalTime.of(8, 15));

        assertThat(devBookings()).isEqualTo(1);
    }

    @Test
    @UseCase(id = "UC-001", scenario = "A2: Time Slot Violates a Booking Rule", businessRules = {"BR-003"})
    void booking_starting_before_the_current_time_is_rejected() {
        navigate(BookRoomView.class);
        openDayView(BIRCH, TODAY);

        book(LocalTime.of(7, 45), LocalTime.of(8, 15));

        assertRejected(MSG_BR003);
    }

    @Test
    @UseCase(id = "UC-001", businessRules = {"BR-003"})
    void booking_starting_at_the_current_time_is_accepted() {
        navigate(BookRoomView.class);
        openDayView(BIRCH, TODAY);

        book(LocalTime.of(8, 0), LocalTime.of(8, 15));

        assertThat(devBookings()).isEqualTo(1);
    }

    // --- A3: Double Booking ---

    @Test
    @UseCase(id = "UC-001", scenario = "A3: Double Booking", businessRules = {"BR-004"})
    void time_slot_overlapping_an_existing_booking_is_rejected() {
        navigate(BookRoomView.class);
        openDayView(AURORA, TOMORROW);

        book(LocalTime.of(10, 30), LocalTime.of(11, 30));

        assertRejected(MSG_A3);
        assertThat(test(bookingGrid()).size()).isEqualTo(1);
        assertThat(test(bookingGrid()).getCellText(0, 2)).isEqualTo(COLLEAGUE);
    }

    @Test
    @UseCase(id = "UC-001", scenario = "A3: Double Booking", businessRules = {"BR-004"})
    void time_slot_booked_by_another_employee_in_the_meantime_is_rejected_and_the_current_bookings_shown() {
        navigate(BookRoomView.class);
        openDayView(BIRCH, TOMORROW);
        assertThat(test(bookingGrid()).size()).isZero();

        // Competing change: another Employee books 09:00–10:00 after the day view was shown
        jdbcClient.sql("""
                        insert into booking (room_id, employee_id, start_time, end_time, created_at)
                        values (?, ?, ?, ?, ?)""")
                .params(BIRCH_ID, COLLEAGUE_ID, TOMORROW.atTime(9, 0), TOMORROW.atTime(10, 0), NOW)
                .update();

        book(LocalTime.of(9, 30), LocalTime.of(10, 30));

        assertRejected(MSG_A3);
        // A3: the current Bookings of the Room for that day are displayed
        assertThat(test(bookingGrid()).size()).isEqualTo(1);
        assertThat(test(bookingGrid()).getCellText(0, 0)).isEqualTo("09:00");
        assertThat(test(bookingGrid()).getCellText(0, 2)).isEqualTo(COLLEAGUE);
    }

    @Test
    @UseCase(id = "UC-001", businessRules = {"BR-004"})
    void time_slot_that_only_touches_an_existing_booking_is_accepted() {
        navigate(BookRoomView.class);
        openDayView(AURORA, TOMORROW);

        book(LocalTime.of(11, 0), LocalTime.of(11, 15));

        assertThat(devBookings()).isEqualTo(1);
        assertThat(test(bookingGrid()).size()).isEqualTo(2);
    }

    // --- A4: Employee Abandons the Booking ---

    @Test
    @UseCase(id = "UC-001", scenario = "A4: Employee Abandons the Booking")
    void leaving_without_confirming_stores_no_booking() {
        navigate(BookRoomView.class);
        openDayView(BIRCH, TOMORROW);
        test(timePicker("Start time")).setValue(LocalTime.of(9, 0));
        test(timePicker("End time")).setValue(LocalTime.of(10, 0));

        navigate(BookRoomView.class);

        assertThat(devBookings()).isZero();
    }

    // --- A5: Room No Longer Active ---

    @Test
    @UseCase(id = "UC-001", scenario = "A5: Room No Longer Active", businessRules = {"BR-005"})
    void booking_a_room_deactivated_in_the_meantime_is_rejected_and_the_room_list_shown() {
        navigate(BookRoomView.class);
        openDayView(DUNE, TOMORROW);

        // Competing change: an Office Manager deactivates the Room after it was selected
        jdbcClient.sql("update room set active = false where id = ?").param(DUNE_ID).update();

        book(LocalTime.of(9, 0), LocalTime.of(10, 0));

        assertRejected(MSG_A5);
        // A5: the Room list is displayed again, without the deactivated Room
        assertThat(rowOf(roomGrid(), DUNE)).isNegative();
        assertThat(roomGrid().asSingleSelect().getValue()).isNull();
        assertThat(find(Grid.class).withId("booking-grid").exists()).isFalse();
    }

    // --- helpers ---

    @SuppressWarnings("unchecked")
    private Grid<RoomListItem> roomGrid() {
        return find(Grid.class).id("room-grid");
    }

    @SuppressWarnings("unchecked")
    private Grid<BookingItem> bookingGrid() {
        return find(Grid.class).id("booking-grid");
    }

    private TimePicker timePicker(String caption) {
        return find(TimePicker.class).withCaption(caption).single();
    }

    private void selectRoom(String name) {
        Grid<RoomListItem> rooms = roomGrid();
        int row = rowOf(rooms, name);
        assertThat(row).as("Room %s in the Room list", name).isNotNegative();
        test(rooms).select(row);
    }

    private void selectDay(LocalDate day) {
        test(find(DatePicker.class).withCaption("Day").single()).setValue(day);
    }

    private void openDayView(String room, LocalDate day) {
        selectRoom(room);
        selectDay(day);
    }

    private void book(LocalTime start, LocalTime end) {
        test(timePicker("Start time")).setValue(start);
        test(timePicker("End time")).setValue(end);
        test(find(Button.class).withText("Book").single()).click();
    }

    private void assertRejected(String message) {
        Notification notification = find(Notification.class).last();
        assertThat(test(notification).getText()).isEqualTo(message);
        // Failure postcondition: no Booking is stored
        assertThat(devBookings()).isZero();
    }

    private int devBookings() {
        return jdbcClient.sql("""
                        select count(*) from booking b join employee e on e.id = b.employee_id
                        where e.sso_subject = ?""")
                .param(DEV_SSO_SUBJECT).query(Integer.class).single();
    }

    private static int rowOf(Grid<RoomListItem> grid, String name) {
        List<RoomListItem> items = grid.getListDataView().getItems().toList();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).name().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    private int rowStartingAt(Grid<BookingItem> grid, String start) {
        for (int i = 0; i < test(grid).size(); i++) {
            if (test(grid).getCellText(i, 0).equals(start)) {
                return i;
            }
        }
        return -1;
    }
}
