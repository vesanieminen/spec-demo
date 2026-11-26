CREATE SEQUENCE room_seq START WITH 1 INCREMENT BY 1 CACHE 50;

CREATE TABLE room
(
    id       BIGINT DEFAULT nextval('room_seq') PRIMARY KEY,
    name     VARCHAR(50) NOT NULL,
    capacity INTEGER     NOT NULL,
    active   BOOLEAN     NOT NULL,
    -- UC-003 BR-001: No two Rooms share the same name.
    CONSTRAINT room_name_uk UNIQUE (name),
    -- UC-003 BR-002: The capacity of a Room is a whole number from 1 to 100.
    CONSTRAINT room_capacity_ck CHECK (capacity BETWEEN 1 AND 100)
);
