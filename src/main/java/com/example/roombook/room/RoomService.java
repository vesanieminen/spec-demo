package com.example.roombook.room;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Rooms as Employees see them.
 */
@Service
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    /**
     * UC-001 step 2: the active Rooms with their capacity and Equipment.
     */
    public List<RoomListItem> findActive() {
        return repository.findActive();
    }
}
