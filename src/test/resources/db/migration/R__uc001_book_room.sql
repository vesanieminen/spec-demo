-- Test data of UC-001 Book Room. Fixed ids far above what the sequences hand out.
INSERT INTO employee (id, sso_subject, name, email)
VALUES (9001, 'test-colleague', 'Alex Colleague', 'alex.colleague@example.com')
ON CONFLICT DO NOTHING;

INSERT INTO room (id, name, capacity, active)
VALUES (9001, 'Test Room Aurora', 6, TRUE),
       (9002, 'Test Room Birch', 4, TRUE),
       (9003, 'Test Room Cedar', 2, FALSE),
       (9004, 'Test Room Dune', 10, TRUE)
ON CONFLICT DO NOTHING;

INSERT INTO equipment (id, name)
VALUES (9001, 'Test Projector'),
       (9002, 'Test Video Conferencing'),
       (9003, 'Test Whiteboard')
ON CONFLICT DO NOTHING;

INSERT INTO room_equipment (room_id, equipment_id)
VALUES (9001, 9001),
       (9001, 9002),
       (9002, 9003)
ON CONFLICT DO NOTHING;

-- Made the day before the fixed test clock (2026-12-01 08:00)
INSERT INTO booking (id, room_id, employee_id, start_time, end_time, created_at)
VALUES (9001, 9001, 9001, '2026-12-02 10:00', '2026-12-02 11:00', '2026-11-30 12:00')
ON CONFLICT DO NOTHING;
