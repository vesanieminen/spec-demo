package com.example.roombook.room;

import java.util.List;

/**
 * A Room as the Room list shows it: name, capacity, and the names of its Equipment.
 */
public record RoomListItem(long id, String name, int capacity, List<String> equipment) {
}
