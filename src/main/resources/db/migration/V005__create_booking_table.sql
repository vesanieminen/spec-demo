-- Needed for the exclusion constraint that combines room_id (=) with the Time Slot range (&&).
CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE SEQUENCE booking_seq START WITH 1 INCREMENT BY 1 CACHE 50;

-- Times are local times of the server's default time zone (OQ-013).
CREATE TABLE booking
(
    id          BIGINT DEFAULT nextval('booking_seq') PRIMARY KEY,
    room_id     BIGINT    NOT NULL,
    employee_id BIGINT    NOT NULL,
    start_time  TIMESTAMP NOT NULL,
    end_time    TIMESTAMP NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    CONSTRAINT booking_room_id_fk FOREIGN KEY (room_id) REFERENCES room (id),
    CONSTRAINT booking_employee_id_fk FOREIGN KEY (employee_id) REFERENCES employee (id),
    -- UC-001 BR-001: A Time Slot lies within one day and ends after it starts.
    CONSTRAINT booking_end_time_ck CHECK (end_time > start_time AND end_time::date = start_time::date),
    -- UC-001 BR-001: A Time Slot starts and ends on a 15-minute step.
    CONSTRAINT booking_time_step_ck CHECK (
        date_trunc('minute', start_time) = start_time
            AND date_trunc('minute', end_time) = end_time
            AND extract(MINUTE FROM start_time)::int % 15 = 0
            AND extract(MINUTE FROM end_time)::int % 15 = 0),
    -- UC-001 BR-001: A Time Slot lasts at least 15 minutes and at most 4 hours.
    CONSTRAINT booking_duration_ck CHECK (
        end_time - start_time BETWEEN INTERVAL '15 minutes' AND INTERVAL '4 hours'),
    -- UC-001 BR-002: A Booking starts at most 90 days after it is made.
    CONSTRAINT booking_start_time_ck CHECK (start_time <= created_at + INTERVAL '90 days'),
    -- UC-001 BR-004: Two Bookings of the same Room never overlap; touching is allowed.
    CONSTRAINT booking_overlap_ck EXCLUDE USING gist (
        room_id WITH =,
        tsrange(start_time, end_time, '[)') WITH &&)
);
