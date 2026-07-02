package com.hacnation.common.events;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BaseEvent {
    protected String eventId = UUID.randomUUID().toString();
    protected String eventType;
    protected LocalDateTime timestamp = LocalDateTime.now();
    protected String traceId = UUID.randomUUID().toString();

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
}
