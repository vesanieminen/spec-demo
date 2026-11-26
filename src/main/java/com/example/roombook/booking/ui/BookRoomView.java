package com.example.roombook.booking.ui;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.example.roombook.booking.BookRoom;
import com.example.roombook.booking.BookingItem;
import com.example.roombook.booking.BookingService;
import com.example.roombook.booking.RoomNotActiveException;
import com.example.roombook.booking.TimeSlotNotFreeException;
import com.example.roombook.common.BusinessRuleException;
import com.example.roombook.employee.ui.CurrentEmployee;
import com.example.roombook.room.RoomListItem;
import com.example.roombook.room.RoomService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * UC-001 Book Room: the Room list, the day view of the selected Room, and the Time Slot to book.
 */
@Route("")
@PageTitle("Book Room")
public class BookRoomView extends VerticalLayout {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");
    private static final int STEP_MINUTES = 15;

    private final transient RoomService roomService;
    private final transient BookingService bookingService;
    private final CurrentEmployee currentEmployee;

    private final Grid<RoomListItem> roomGrid = new Grid<>();
    private final H2 dayHeader = new H2();
    private final DatePicker day = new DatePicker("Day");
    private final Grid<BookingItem> bookingGrid = new Grid<>();
    private final TimePicker startTime = new TimePicker("Start time");
    private final TimePicker endTime = new TimePicker("End time");
    private final Button book = new Button("Book");
    private final VerticalLayout dayView = new VerticalLayout();

    public BookRoomView(RoomService roomService, BookingService bookingService, CurrentEmployee currentEmployee,
            Clock clock) {
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.currentEmployee = currentEmployee;
        setSizeFull();

        // Steps 1–2: the active Rooms with their capacity and Equipment
        roomGrid.setId("room-grid");
        roomGrid.addColumn(RoomListItem::name).setHeader("Room").setKey("name").setAutoWidth(true);
        roomGrid.addColumn(RoomListItem::capacity).setHeader("Capacity").setKey("capacity").setAutoWidth(true);
        roomGrid.addColumn(room -> String.join(", ", room.equipment())).setHeader("Equipment").setKey("equipment");
        roomGrid.setEmptyStateText("No Room can be booked.");
        roomGrid.setAllRowsVisible(true);
        roomGrid.asSingleSelect().addValueChangeListener(e -> showDayView());

        // Steps 3–4: the day and the Bookings of the Room for it
        day.setValue(LocalDate.now(clock));
        day.setRequiredIndicatorVisible(true);
        day.addValueChangeListener(e -> showDayView());

        bookingGrid.setId("booking-grid");
        bookingGrid.addColumn(b -> TIME.format(b.startTime())).setHeader("Start").setKey("start").setAutoWidth(true);
        bookingGrid.addColumn(b -> TIME.format(b.endTime())).setHeader("End").setKey("end").setAutoWidth(true);
        bookingGrid.addColumn(BookingItem::employeeName).setHeader("Booked by").setKey("employee");
        bookingGrid.setEmptyStateText("No Bookings on this day.");
        bookingGrid.setAllRowsVisible(true);

        // Step 5: the start time and end time of the Time Slot
        for (TimePicker picker : new TimePicker[] {startTime, endTime}) {
            // UC-001 BR-001: A Time Slot starts and ends on a 15-minute step.
            picker.setStep(Duration.ofMinutes(STEP_MINUTES));
            picker.setRequiredIndicatorVisible(true);
            picker.addValueChangeListener(e -> updateBookEnabled());
        }

        // Step 6: the Employee confirms the Booking
        book.addThemeVariants(ButtonVariant.PRIMARY);
        book.setEnabled(false);
        book.addClickListener(e -> book());

        FlexLayout slot = new FlexLayout(day, startTime, endTime, book);
        slot.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        slot.setAlignItems(FlexComponent.Alignment.BASELINE);
        slot.getStyle().set("gap", "var(--vaadin-gap-m, 1rem)");

        dayView.setPadding(false);
        dayView.add(dayHeader, slot, bookingGrid);
        dayView.setVisible(false);

        add(new H2("Rooms"), roomGrid, dayView);
        refreshRooms();
    }

    private void refreshRooms() {
        roomGrid.setItems(roomService.findActive());
    }

    private void showDayView() {
        RoomListItem room = roomGrid.asSingleSelect().getValue();
        dayView.setVisible(room != null);
        if (room == null) {
            return;
        }
        dayHeader.setText(room.name());
        refreshBookings();
        updateBookEnabled();
    }

    private void refreshBookings() {
        RoomListItem room = roomGrid.asSingleSelect().getValue();
        if (room != null && day.getValue() != null) {
            bookingGrid.setItems(bookingService.findByRoomAndDay(room.id(), day.getValue()));
        } else {
            bookingGrid.setItems();
        }
    }

    private void updateBookEnabled() {
        book.setEnabled(roomGrid.asSingleSelect().getValue() != null && day.getValue() != null
                && startTime.getValue() != null && endTime.getValue() != null);
    }

    private void book() {
        RoomListItem room = roomGrid.asSingleSelect().getValue();
        LocalDate date = day.getValue();
        LocalTime start = startTime.getValue();
        LocalTime end = endTime.getValue();
        try {
            // Steps 7–8: the service checks BR-001 to BR-005 and stores the Booking
            bookingService.book(new BookRoom(room.id(), date.atTime(start), date.atTime(end)), currentEmployee.id());
            startTime.clear();
            endTime.clear();
            refreshBookings();
        } catch (TimeSlotNotFreeException e) {
            // A3: the Time Slot is no longer free; show the current Bookings of the day
            showError(e.getMessage());
            refreshBookings();
        } catch (RoomNotActiveException e) {
            // A5: the Room can no longer be booked; show the Room list again
            showError(e.getMessage());
            roomGrid.asSingleSelect().clear();
            refreshRooms();
        } catch (BusinessRuleException e) {
            // A2: the Time Slot violates BR-001, BR-002, or BR-003
            showError(e.getMessage());
        }
    }

    private static void showError(String message) {
        Notification notification = Notification.show(message, 5000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.ERROR);
    }
}
