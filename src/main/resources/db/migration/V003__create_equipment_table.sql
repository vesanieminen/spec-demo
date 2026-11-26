CREATE SEQUENCE equipment_seq START WITH 1 INCREMENT BY 1 CACHE 50;

CREATE TABLE equipment
(
    id   BIGINT DEFAULT nextval('equipment_seq') PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- UC-003 BR-005: No two kinds of Equipment share a name, ignoring case.
CREATE UNIQUE INDEX equipment_name_uk ON equipment (lower(name));
