# Vision: RoomBook

## Mission

Employees at a 150-person office waste time finding a free meeting room: bookings live in a shared spreadsheet that is
often out of date, so rooms are double-booked or sit empty after a reservation. RoomBook replaces the spreadsheet with a
simple web application where employees see which rooms are free and book one in under a minute.

## Target users

- **Employee**: finds a free meeting room for a given time and books it without asking anyone.
- **Office Manager**: maintains the list of meeting rooms and resolves booking conflicts.

## Goals

- Eliminate double bookings: zero overlapping bookings for the same room.
- An employee books a room in at most 3 steps and under 60 seconds.
- 80% of employees use RoomBook instead of the spreadsheet within 2 months of launch.

## Scope

### In scope

- View the meeting rooms with their capacity and equipment (projector, video conferencing).
- See the availability of a room for a day.
- Book a room for a time slot (15-minute steps, at most 4 hours).
- Cancel one's own booking.
- Office Manager: add, edit, and deactivate rooms; cancel any booking.

### Out of scope

- Recurring bookings.
- Integration with Outlook or Google Calendar.
- Booking desks, parking spaces, or equipment.
- Mobile app (the web application must still work on a phone browser).

## Constraints

- Employees sign in with the company's existing single sign-on; RoomBook stores no passwords.
- Runs on the company's internal network only; no public internet access.
- Booking data is kept for at most 12 months (company data retention policy).
- First release within 3 months with a team of two developers.

## Success measures

- The shared booking spreadsheet is retired within 3 months of launch.
- No reported double booking in the first 3 months.
- Office Manager handles fewer than 2 booking conflicts per week (today: about 10).
