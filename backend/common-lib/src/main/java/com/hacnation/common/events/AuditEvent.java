package com.hacnation.common.events;

import java.time.LocalDateTime;

public class AuditEvent {

    private String userId;
    private LocalDateTime timestamp;
    private String service;
    private String action;
    private String oldValue;
    private String newValue;
    private String entityId;
    private String traceId;

    public AuditEvent() {
    }

    public AuditEvent(String userId, LocalDateTime timestamp, String service, String action,
                      String oldValue, String newValue, String entityId, String traceId) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.service = service;
        this.action = action;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.entityId = entityId;
        this.traceId = traceId;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }
    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
}
