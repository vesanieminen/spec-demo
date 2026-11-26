package com.example.roombook.room;

import static com.example.roombook.jooq.Tables.EQUIPMENT;
import static com.example.roombook.jooq.Tables.ROOM;
import static com.example.roombook.jooq.Tables.ROOM_EQUIPMENT;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepository {

    private final DSLContext ctx;

    public RoomRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /**
     * The active Rooms with their Equipment, ordered by name.
     */
    public List<RoomListItem> findActive() {
        return ctx.select(
                        ROOM.ID,
                        ROOM.NAME,
                        ROOM.CAPACITY,
                        multiset(
                                select(EQUIPMENT.NAME)
                                        .from(ROOM_EQUIPMENT)
                                        .join(EQUIPMENT).on(EQUIPMENT.ID.eq(ROOM_EQUIPMENT.EQUIPMENT_ID))
                                        .where(ROOM_EQUIPMENT.ROOM_ID.eq(ROOM.ID))
                                        .orderBy(EQUIPMENT.NAME)
                        ).convertFrom(names -> names.map(Record1::value1)))
                .from(ROOM)
                // UC-001 BR-005: Only an active Room can be booked.
                .where(ROOM.ACTIVE.isTrue())
                .orderBy(ROOM.NAME)
                .fetch(r -> new RoomListItem(r.value1(), r.value2(), r.value3(), r.value4()));
    }

    /**
     * Whether the Room is active, read with a shared row lock, so the Room cannot be deactivated before the
     * surrounding transaction ends. Empty when the Room does not exist.
     */
    public Optional<Boolean> findActiveForShare(long roomId) {
        return ctx.select(ROOM.ACTIVE)
                .from(ROOM)
                .where(ROOM.ID.eq(roomId))
                .forShare()
                .fetchOptional(Record1::value1);
    }
}
