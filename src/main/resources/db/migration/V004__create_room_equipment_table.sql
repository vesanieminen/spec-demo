CREATE TABLE room_equipment
(
    room_id      BIGINT NOT NULL,
    equipment_id BIGINT NOT NULL,
    CONSTRAINT room_equipment_pk PRIMARY KEY (room_id, equipment_id),
    CONSTRAINT room_equipment_room_id_fk FOREIGN KEY (room_id) REFERENCES room (id),
    CONSTRAINT room_equipment_equipment_id_fk FOREIGN KEY (equipment_id) REFERENCES equipment (id)
);
