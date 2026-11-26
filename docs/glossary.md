# Glossary

| Term           | Definition                                                                                                                              | Avoid                       |
|----------------|-----------------------------------------------------------------------------------------------------------------------------------------|-----------------------------|
| Employee       | A person working at the office who signs in with the company's single sign-on and books Rooms.                                          | Staff member                |
| Office Manager | An Employee who additionally maintains the Rooms and may cancel any Booking.                                                            | Admin, Administrator        |
| Room           | A meeting room of the office that Employees can book. A Room is either active (bookable) or deactivated (out of service, not bookable). | Space, Resource             |
| Equipment      | A fixed facility of a Room that matters for a meeting, such as a projector or video conferencing.                                       | Amenity, Feature            |
| Time Slot      | A period on one day with a start and an end time in 15-minute steps, lasting 15 minutes to 4 hours.                                     |                             |
| Booking        | An Employee's claim on one Room for one Time Slot. Two Bookings of the same Room never overlap.                                         | Reservation                 |
| Double Booking | Two Bookings of the same Room whose Time Slots overlap; RoomBook rejects the second one.                                                | Overbooking, Clash          |
