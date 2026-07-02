package com.hacnation.fileattente.domain.port;

public interface RedisQueuePort {

    void setPosition(String service, String patientId, Integer position);

    Integer getPosition(String service, String patientId);

    void removePosition(String service, String patientId);
}
