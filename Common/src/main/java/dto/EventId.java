package dto;

public enum EventId {
    JOIN("join"),
    PLAY("play"),
    WINNER("winner");

    EventId(String winner) {
    }

    public static EventId toEventId(String eventName) {
        for (EventId event : EventId.values()) {
            if(event.name().equalsIgnoreCase(eventName)) {
                return event;
            }
        }
        return null;
    }
}
